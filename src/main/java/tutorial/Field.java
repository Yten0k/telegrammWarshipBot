package tutorial;

import java.util.ArrayList;
import java.util.List;

public class Field {
    public boolean[][] field = new boolean[8][8];
    public List<ShipBuilder> fleet = new ArrayList<>();

    private int attributeCopy = Ships.Battleship.attribute;

    public Ships ShipState = Ships.Battleship;
    //private int numberOfShip = 0;
    //private ShipBuilder tempShip;
/*
    private boolean spacePlace(int coordinateX, int coordinateY){
        if(coordinateX<0 || coordinateY<0 || coordinateY>9 || coordinateX>9) return false;
        int[][] cell = new int[8][2];
        for (int i = 0; i<8;i++){
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

        for(int i = 0; i<8; i++){
            if(cell[i][0]<0 || cell[i][1]<0 || cell[i][0]>9 || cell[i][1]>9){
                cell[i][0]=-1;
                cell[i][1]=-1;
            }
        }
        for(int i = 0; i<8; i++){
            if(cell[i][0]!=-1)
                if(field[cell[i][0]][cell[i][1]])
                    return false;
        }
        return true;
    }

    private boolean fillBattelship(String callBack, int[] coordinate){
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        if(tempShip.lenghtOfShip()==0){
            tempShip = new ShipBuilder(4, coordinateX, coordinateY);
            return false;
        }
        callBack = tempShip.buildShip(coordinateX, coordinateY) ? "" : "invalid position";
        if(tempShip.lenghtOfShip()!=4){
            return false;
        }
        return true;
    }

    private boolean fillCruiser(String callBack, int[] coordinate) {
        int coordinateX = coordinate[0];
        int coordinateY = coordinate[1];
        if(tempShip.lenghtOfShip()==0){
            if(spacePlace(coordinateX,coordinateY) &&
                    (spacePlace(coordinateX+2, coordinateY) //Kill ur self
                    || spacePlace(coordinateX-2,coordinateY)
                    || spacePlace(coordinateX,coordinateY+2)
                    || spacePlace(coordinateX, coordinateY-2)
                    || (spacePlace(coordinateX + 1,coordinateY) && spacePlace(coordinateX-1,coordinateY))
                    || (spacePlace(coordinateX,coordinateY+1) && spacePlace(coordinateX,coordinateY-1))))
                tempShip = new ShipBuilder(3,coordinateX,coordinateY);
            else {
                System.out.println("invalid ship");
                callBack = "invalid ship";
            }
            return false;
        }
        if(spacePlace(coordinateX,coordinateY))
            callBack = tempShip.buildShip(coordinateX, coordinateY) ? "" : "invalid position";
        else {
            System.out.println("invalid ship");
            callBack = "invalid ship";
        }
        if(tempShip.lenghtOfShip()!=3)
            return false;
        return true;
//        tempShip.postShip(field);
//        //fleet[numberOfShip] = new ShipBuilder(tempShip);
//        ++numberOfShip;
//        tempShip = new ShipBuilder();
//        outFild();
//        return Ships.Distroyer;
    }


    private boolean fillDistroyer(String callBack, int[] coordinate){
        return true;
    }
    private boolean fillBoat(String callBack, int[] coordinate){
        return true;
    }
*/


    public void outFild() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (field[i][j]) System.out.printf(" 00 ");
                else System.out.printf(" [] ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public CallBack fillField(int[] coordinate) {

        CallBack StatusOfShip = ShipState.shipsAncestor.BuildShip(field, coordinate);

        if (ShipState == Ships.End) return CallBack.Final;
        if (StatusOfShip==CallBack.Final) {
            ShipState.shipsAncestor.PostShip(fleet, field);
            outFild();
            ShipState.shipsAncestor.DeleteShip();
            if (attributeCopy == 1){
                ShipState = ShipState.Next();
                attributeCopy = ShipState.attribute;
            } else
                attributeCopy--;
        }
        return StatusOfShip;
    }
}
