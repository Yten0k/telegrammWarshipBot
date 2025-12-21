package tutorial;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.util.ArrayList;

public class GamePlayScenario implements Scenario{

    private final String path = System.getProperty("user.dir") + "\\src\\";

    private GameField fieldFirstPlayer; //Поле игрока 2, где играет игрок 1
    private GameField fieldSecondPlayer; //Поле игрока 1, где играет игрок 2

    private CallBack callBack = CallBack.Continue;
    //private PlayerMutex mutex = PlayerMutex.PLAYER_ONE_MUTEX;
    private final PlayerOrder Order = new PlayerOrder(PlayerMutex.PLAYER_ONE_MUTEX, true);

    private boolean EndGame = false;

    private boolean Marge(PlayerData playerData){
        if(playerData.GetMutex()==PlayerMutex.PLAYER_ONE_MUTEX){
            if(fieldFirstPlayer == null) {
                fieldFirstPlayer = new GameField(playerData.PlayerFleet);
            }
            else return true;
        }
        else {
            if(fieldSecondPlayer == null) {
                fieldSecondPlayer = new GameField(playerData.PlayerFleet);
            }
            else return true;
        }
        return true;
    }

    @Override
    public CallBack GetCall() {
        return callBack;
    }

    @Override
    public State Process(DrawField drawer, PlayerData playerData, int[] coordinate) {
        callBack = CallBack.Pass;
        if(!this.Order.MyOrder(playerData.GetMutex())){
            callBack = callBack.PassState(CallBack.ExceptionMutex);
            return State.Playing;
        }

        if(!Marge(playerData)){
            callBack = callBack.PassState(CallBack.UnknownError);
            return State.Playing;
        }

        if(playerData.gridPlayer[coordinate[0]][coordinate[1]]!=0){
            callBack=callBack.PassState(CallBack.ExceptionMove);
            return State.Playing;
        }

        int NumberOfHittenShip;

        if(Order.MyOrder(PlayerMutex.PLAYER_ONE_MUTEX)){
            callBack = fieldFirstPlayer.Hit(coordinate);
            NumberOfHittenShip = fieldFirstPlayer.Include(coordinate);
        }
        else{
            callBack = fieldSecondPlayer.Hit(coordinate);
            NumberOfHittenShip = fieldSecondPlayer.Include(coordinate);
        }

        if(callBack == CallBack.Continue){
            drawer.DrawPlayerChangedField(Order.GetOrder().SWAP(), coordinate, false);
            playerData.gridPlayer[coordinate[0]][coordinate[1]]=1;
        }
        if(callBack == CallBack.Final){
            PrimitiveShip TargetShip;
            if(Order.MyOrder(PlayerMutex.PLAYER_ONE_MUTEX))
                TargetShip = fieldFirstPlayer.GameFieldPlayer.get(NumberOfHittenShip);
            else
                TargetShip = fieldSecondPlayer.GameFieldPlayer.get(NumberOfHittenShip);

            for(Coordinates coordinates : TargetShip.coordinates) {
                drawer.DrawPlayerChangedField(Order.GetOrder().SWAP(), coordinates.CoordinatesToArray(), true);
                playerData.gridPlayer[coordinates.GetX()][coordinates.GetY()]=2;
            }

            if (fieldFirstPlayer.HasNoFleet() || fieldSecondPlayer.HasNoFleet()){
                EndGame = true;
            }
        }
        if(callBack == CallBack.Pass)
            playerData.gridPlayer[coordinate[0]][coordinate[1]]=-1;

        Order.SwapOrder(callBack);
        return State.Playing;
    }

    @Override
    public boolean IsFieldDraw() {
        return EndGame;
    }

    @Override
    public EditMessageText EditText(PlayerData playerData) {
        return null;
    }

    @Override
    public SendPhoto PhotoMessage(PlayerMutex mutex) {
        SendPhoto message = new SendPhoto();
        File picture;
        if(mutex == PlayerMutex.PLAYER_ONE_MUTEX){
            picture = new File(path + "2_field.png");
        }
        else
            picture = new File(path + "1_field.png");

        message.setPhoto(new InputFile(picture));
        return message;
    }
}
