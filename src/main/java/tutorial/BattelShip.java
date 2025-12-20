package tutorial;

public class BattelShip extends ShipsAncestor {

    public CallBack BuildShip(boolean[][] field, int[] coordinate) {
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        callBack = CallBack.Continue;
        if(ship==null){
            ship = new ShipBuilder(4, coordinateX, coordinateY);
            callBack = callBack.PassState(CallBack.Continue);
        }
        else {
            callBack = ship.buildShip(coordinateX, coordinateY) ? callBack.PassState(CallBack.Continue)
                    : callBack.PassState(CallBack.ExceptionInvalidPosition);

            if(ship.lenghtOfShip() != 4)
                callBack = callBack.PassState(CallBack.Continue);
            else callBack = callBack.PassState(CallBack.Final);
        }

        return callBack;
    }

    public boolean isShipBuilt() {
        if (ship==null) return false;
        return ship.lenghtOfShip()==4;
    }

}
