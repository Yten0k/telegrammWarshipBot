package tutorial;

import java.util.ArrayList;

public class PrimitiveShip {
    public ArrayList<Coordinates> coordinates = new ArrayList<>();
    private int SizeShip;

    public int GetSizeShip(){return SizeShip;}

    PrimitiveShip(int SizeShip){ this.SizeShip = SizeShip; }

    private boolean dead = false;

    public int Include(int x, int y){
        for(int i = 0; i<SizeShip;i++){
            if(coordinates.get(i).equal(x,y))
                return i;
        }
        return -1;
    }

    public boolean KillShip(){
        boolean flag = true;
        for(int i = 0; i<SizeShip; i++)
            flag = flag && coordinates.get(i).isHitted();
        if(flag)
            dead = true;
        return dead;
    }

    public void PutCoordinate(int[] coordinate){
        coordinates.add(new Coordinates(coordinate[0], coordinate[1]));
    }
    public boolean Hit(int coordinateX, int coordinateY){
        int NumberOfCoordinate = Include(coordinateX,coordinateY);

        if(NumberOfCoordinate!=-1 && !coordinates.get(NumberOfCoordinate).isHitted()){
            coordinates.get(NumberOfCoordinate).Hit();
            return true;
        }
        return false;
    }
}
