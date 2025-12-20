package tutorial;

public enum Ships {
    Battleship(new BattelShip(),1),
    Cruiser(new Cruiser(), 1),
    Distroyer(new Distroyer(),2),
    Boat(new Boat(),3),
    End(new End(),-1);
    public final int attribute;
    public final ShipsAncestor shipsAncestor;

    Ships (ShipsAncestor shipsAncestor, int attribute) {
        this.shipsAncestor = shipsAncestor;
        this.attribute = attribute;
    }
    Ships Next(){
        switch (this){
            case Battleship -> { return Cruiser; }
            case Cruiser -> { return Distroyer; }
            case Distroyer -> { return Boat; }
            default -> { return End; }
        }
    }
}
