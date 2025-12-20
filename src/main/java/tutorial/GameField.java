package tutorial;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    public ArrayList<PrimitiveShip> GameFieldPlayer = new ArrayList<>(); //Корабли второго игрока, на котором играет первый игрок

    CallBack callBack = CallBack.Pass;

    GameField(ArrayList<PrimitiveShip> field){
        GameFieldPlayer.addAll(field);
    }

    public boolean HasNoFleet(){
        boolean bFlag = true;
        for(PrimitiveShip ship : GameFieldPlayer){
            bFlag = bFlag && ship.KillShip();
        }
        return bFlag;
    }

    public CallBack Hit(int[] coordinate){
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        callBack = CallBack.Pass;

        for(PrimitiveShip ship : GameFieldPlayer){
            if(ship.Hit(coordinateX,coordinateY)){
                callBack = callBack.PassState(CallBack.Continue);
                if(ship.KillShip()){
                    callBack=callBack.PassState(CallBack.Final);
                }
                break;
            } else if (ship.Include(coordinateX, coordinateY)!=-1) {
                callBack=callBack.PassState(CallBack.ExceptionMove);
            }
        }
        return callBack;
    }
    public int Include(int[] coordinate){
        for(int i = 0; i<GameFieldPlayer.size(); i++){
            if(GameFieldPlayer.get(i).Include(coordinate[0],coordinate[1])!=-1)
                return i;
        }
        return -1;
    }
}
