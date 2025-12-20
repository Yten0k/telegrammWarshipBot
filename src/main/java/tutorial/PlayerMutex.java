package tutorial;

public enum PlayerMutex {
    PLAYER_ONE_MUTEX(1),
    PLAYER_TWO_MUTEX(2),
    BOTH_MUTEX(0);

    private final int PlayerID;
    //private final int UserID;
    PlayerMutex(int PlayerID) {this.PlayerID = PlayerID;}
    public int GetID(){return PlayerID;}
    public PlayerMutex SWAP() {
        if (this!=BOTH_MUTEX)
            return this==PLAYER_ONE_MUTEX ? PLAYER_TWO_MUTEX : PLAYER_ONE_MUTEX;
        return this;
    }
}
