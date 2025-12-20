package tutorial;

import java.util.List;

public class ShipsAncestor {
    protected ShipBuilder ship;
    protected final int SizeField = 8;

    protected CallBack callBack = CallBack.Continue;

    protected CallBack BuildShip(boolean[][] field, int[] coordinate)
    {return CallBack.Continue;}
    public boolean isShipBuilt() {
        if (ship==null) return false;
        return true;
    }
    public void DeleteShip() { ship = null; }

    protected boolean spacePlace(boolean[][] field, int coordinateX, int coordinateY){
        if(coordinateX<0 || coordinateY<0 || coordinateY>7 || coordinateX>7) return false;
        int[][] cell = new int[SizeField][2];
        for (int i = 0; i<SizeField;i++){
            cell[i][0] = coordinateX;
            cell[i][1] = coordinateY;
            if(i == 0 || i == 1 || i ==2)
                cell[i][0]-=1;
            if(i==5 || i==6 || i ==7)
                cell[i][0]+=1;
            if(i==0 || i==3 || i==5)
                cell[i][1]-=1;
            if(i==2 || i==4 || i==7)
                cell[i][1]+=1;
        }

        for(int i = 0; i<SizeField; i++){
            if(cell[i][0]<0 || cell[i][1]<0 || cell[i][0]>7 || cell[i][1]>7){
                cell[i][0]=-1;
                cell[i][1]=-1;
            }
        }
        for(int i = 0; i<SizeField; i++){
            if(cell[i][0]!=-1)
                if(field[cell[i][0]][cell[i][1]])
                    return false;
        }
        return true;
    }

    protected boolean correctLocation(boolean[][] field, int[] coordinate, int radius){
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        return (spacePlace(field, coordinateX+radius, coordinateY) //Kill ur self
                || spacePlace(field, coordinateX-radius,coordinateY)
                || spacePlace(field, coordinateX,coordinateY+radius)
                || spacePlace(field, coordinateX, coordinateY-radius));
    }

    public boolean PostShip(List<ShipBuilder> fleet, boolean[][] field){
        if(isShipBuilt()){
            fleet.add(new ShipBuilder(ship));
            ship.postShip(field);
            return true;
        }
        return false;
    }

}
