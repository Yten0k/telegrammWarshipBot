package tutorial;


public class Cruiser extends ShipsAncestor{

    public CallBack BuildShip(boolean[][] field, int[] coordinate) {
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        callBack=CallBack.Continue;
        if(ship==null || ship.lenghtOfShip()==0){
            if(spacePlace(field, coordinateX,coordinateY) &&
                    (correctLocation(field,coordinate,2)
                            || (spacePlace(field,coordinateX + 1,coordinateY) && spacePlace(field,coordinateX-1,coordinateY))
                            || (spacePlace(field, coordinateX,coordinateY+1) && spacePlace(field,coordinateX,coordinateY-1))))
                ship = new ShipBuilder(3,coordinateX,coordinateY);
            else {
                callBack = callBack.PassState(CallBack.ExceptionUncorrectedPosition);
            }
        }
        else {
            if (spacePlace(field, coordinateX, coordinateY))
                callBack = ship.buildShip(coordinateX, coordinateY) ? callBack.PassState(CallBack.Continue)
                        : callBack.PassState(CallBack.ExceptionInvalidPosition);
            else {
                callBack = callBack.PassState(CallBack.ExceptionUncorrectedPosition);
            }
            if (ship.lenghtOfShip() != 3)
                callBack = callBack.PassState(CallBack.Continue);
            else callBack = callBack.PassState(CallBack.Final);

        }
        return callBack;
    }
    public boolean isShipBuilt() {
        if (ship==null) return false;
        return ship.lenghtOfShip()==3;
    }

}
