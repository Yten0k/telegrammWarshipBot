package tutorial;

public enum State {
    FillingShips(new FillShipsScenario()),
    Playing(new GamePlayScenario());
    //Finish,
    //Waiting;

    public final Scenario scenario;

    State(Scenario scenario) {
        this.scenario = scenario;
    }

    public State Next(){
        switch (this){
            case FillingShips -> { return Playing; }
            default -> { return Playing; }
        }
    }
}