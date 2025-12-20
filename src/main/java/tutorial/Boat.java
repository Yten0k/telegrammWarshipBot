package tutorial;


public class Boat extends ShipsAncestor{
    public CallBack BuildShip(boolean[][] field, int[] coordinate) {
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        callBack=CallBack.Continue;
        if(spacePlace(field, coordinateX,coordinateY)) {
            ship = new ShipBuilder(1, coordinateX, coordinateY);
            callBack=callBack.PassState(CallBack.Final);
        }
        else
            callBack = callBack.PassState(CallBack.ExceptionUncorrectedPosition);
        return callBack;
    }

    public boolean isShipBuilt() {
        if (ship==null) return false;
        return ship.lenghtOfShip()==1;
    }
}
