package tutorial;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.ArrayList;

public class FillShipsScenario implements Scenario {

    private Field fieldFirstPlayer = new Field();
    private Field fieldSecondPlayer = new Field();

    private CallBack callBack=CallBack.Continue;

    //private PlayerMutex mutex = PlayerMutex.BOTH_MUTEX;
    private PlayerOrder Order = new PlayerOrder(PlayerMutex.BOTH_MUTEX);

    private boolean FieldOnPicture = false;

    @Override
    public CallBack GetCall() {
        return callBack;
    }

    @Override
    public State Process(DrawField drawer, PlayerData playerData, int[] coordinate) {

        if(!Order.MyOrder(playerData.GetMutex()))
            callBack = callBack.PassState(CallBack.ExceptionMutex);
        else {
            if(playerData.GetMutex() == PlayerMutex.PLAYER_ONE_MUTEX)
                callBack = fieldFirstPlayer.fillField(coordinate);
            else if (playerData.GetMutex() == PlayerMutex.PLAYER_TWO_MUTEX)
                callBack = fieldSecondPlayer.fillField(coordinate);

            if (callBack == CallBack.Final) {
                //drawer.DrawPlayerField(mutex, field.field);

                if(playerData.GetMutex() == PlayerMutex.PLAYER_ONE_MUTEX)
                    playerData.PushNewShip(fieldFirstPlayer.fleet);
                else if (playerData.GetMutex() == PlayerMutex.PLAYER_TWO_MUTEX)
                    playerData.PushNewShip(fieldSecondPlayer.fleet);

                if (fieldFirstPlayer.ShipState == Ships.End){
                    if(fieldSecondPlayer.ShipState == Ships.End){
                        drawer.DrawPlayerField(PlayerMutex.PLAYER_ONE_MUTEX, fieldFirstPlayer.field);
                        drawer.DrawPlayerField(PlayerMutex.PLAYER_TWO_MUTEX, fieldSecondPlayer.field);
                        FieldOnPicture = true;
                        return State.Playing; //Изменение на другое состояние
                    }

                    else
                        Order.PassMutex(PlayerMutex.PLAYER_TWO_MUTEX);
                }
                else if(fieldSecondPlayer.ShipState == Ships.End)
                    Order.PassMutex(PlayerMutex.PLAYER_ONE_MUTEX);
            }
        }
        if(!callBack.ExceptionCallBack)
            playerData.gridPlayer[coordinate[0]][coordinate[1]] = 1;
        return State.FillingShips;
    }

    @Override
    public boolean IsFieldDraw(){ return FieldOnPicture; }

    @Override
    public EditMessageText EditText(PlayerData playerData) {
        EditMessageText messageText = new EditMessageText();
        String outPutString;
        if(playerData.GetMutex() == PlayerMutex.PLAYER_ONE_MUTEX)
            outPutString = playerData.language.GetShipMessage(fieldFirstPlayer.ShipState);
        else
            outPutString = playerData.language.GetShipMessage(fieldSecondPlayer.ShipState);
        messageText.setText(outPutString);
        return messageText;
    }

    @Override
    public SendPhoto PhotoMessage(PlayerMutex mutex) {
        return null;
    }
}
