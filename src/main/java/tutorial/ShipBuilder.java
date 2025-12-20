package tutorial;

public class ShipBuilder {
    private int[][] shipBuffer;                                         //Массив, хранящий координаты точек, пренадлежащих Определённому кораблю
    private int localLenght;                                        //Количество точек, пренадлежищих Определённому кораблю в данный момент

    private int patternX;                                               //Тенденсия роста по X (рост вдоль столбца) \
    private int patternY;                                               //Тенденция роста по Y (росто вдоль строки) / Только одна из двух переменных равна 0

    ShipBuilder(){ localLenght=0; }                                                            //Конструктор по умолчанию

    ShipBuilder(ShipBuilder CopyShip){
        this.shipBuffer = CopyShip.shipBuffer;
        this.localLenght = CopyShip.localLenght;
        this.patternX = CopyShip.patternX;
        this.patternY = CopyShip.patternY;
    }
    //Каждый корабль начинается с "точки-строителя" - первая точка, которая пораждает новый корабль.
    //У карабля есть тенденция роста только по одной оси. Тенденция не даёт создавать угловые корабли.
    //Тенденция роста задаётся второй точкой.
    ShipBuilder(int lenght, int buildCoordinateX, int buildCoordinateY){       //Конструктор с точкой-строителем
        shipBuffer = new int[lenght][2];
        for(int i = 0;i<lenght;i++)
            shipBuffer[i] = new int[]{-1,-1};
        localLenght = 0;
        shipBuffer[0][0] = buildCoordinateX;
        shipBuffer[0][1] = buildCoordinateY;
        ++localLenght;
    }
    public int lenghtOfShip(){ return localLenght; }
    public boolean buildShip(int coordinateX, int coordinateY){
        if(localLenght==1){                                             //Если точка вторая, то она задаёт тенденцию
            patternX = Math.abs(shipBuffer[0][0] - coordinateX);        // |
            patternY = Math.abs(shipBuffer[0][1] - coordinateY);        // |
            if(((patternY ==0) ^ (patternX ==0))                        // | Условие существования только одной тенденции
                    && (patternX < shipBuffer.length)               // | Условия, что вторая точка задана корректно, точка находится в пределах длинны корабля
                    && (patternY < shipBuffer.length)){
                if(patternX !=0)                                        // | "Нормализация" тенденции, приводим в вид длинны корабля - 1,
                    patternX = shipBuffer.length - 1;                   // |       для проверки корректного расстояния и правильной тенденции новой точки
                else
                    patternY = shipBuffer.length - 1;

            }
            else{
                return false;
            }
        }

        if(localLenght > shipBuffer.length)//      #
            return true;//                                #
        if(Include(coordinateX,coordinateY))
            return false;
        for(int i = 0;i<localLenght;i++){                               //Расстояние между всеми точками корабля не должно превышать нормализированного патерна
            if(Math.abs(shipBuffer[i][0] - coordinateX) > patternX
                    || Math.abs(shipBuffer[i][1] - coordinateY) > patternY) {
                return false;
            }

        }
        shipBuffer[localLenght][0]=coordinateX;
        shipBuffer[localLenght][1]=coordinateY;
        ++localLenght;
        return true;
    }

    //Размещение корабля на игровом поле
    public void postShip(boolean[][] field){
        for(int i = 0; i< shipBuffer.length;i++){
            int coordinateX = shipBuffer[i][0];
            int coordinateY = shipBuffer[i][1];
            field[coordinateX][coordinateY] = true;
        }
    }
    public PrimitiveShip MorfToPrimitive(){
        PrimitiveShip primitiveShip = new PrimitiveShip(lenghtOfShip());
        for(int i = 0; i<localLenght; i++){
            int coordinateX = shipBuffer[i][0];
            int coordinateY = shipBuffer[i][1];
            primitiveShip.coordinates.add(new Coordinates(coordinateX,coordinateY));
        }
        return primitiveShip;
    }
    public boolean Include(int coordinateX, int coordinateY){
        for (int[] ints : shipBuffer) {
            if (ints[0] == coordinateX && ints[1] == coordinateY)
                return true;
        }
        return false;
    }
}
