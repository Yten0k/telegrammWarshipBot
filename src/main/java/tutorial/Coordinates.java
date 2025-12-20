package tutorial;

public class Coordinates {
    private int coordinateX;
    private int coordinateY;
    private boolean hitted = false;

    public Coordinates(int x, int y){
        this.coordinateX = x;
        this.coordinateY = y;
    }

    public int GetX(){return coordinateX;}
    public int GetY(){return coordinateY;}

    public boolean equal(int x, int y) {
        return x == coordinateX && y == coordinateY;
    }

    public boolean isHitted(){return hitted;}
    public void Hit(){hitted = true;}
    public int[] CoordinatesToArray(){ return new int[]{coordinateX, coordinateY}; }
}
