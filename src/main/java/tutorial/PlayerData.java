package tutorial;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private final int SizeField = 8;
    public final int[][] gridPlayer = new int[SizeField][SizeField];
    private final PlayerMutex mutex;
    private final Long UserID;
    public ArrayList<PrimitiveShip> PlayerFleet = new ArrayList<>();
    public GameLanguage language;

    PlayerData(Long UserID, PlayerMutex mutex, GameLanguage language){
        this.UserID = UserID;
        this.mutex = mutex;
        this.language = language;
    }

    public PlayerMutex GetMutex() {return mutex; }
    public Long GetID() { return UserID; }

    public void PushNewShip(List<ShipBuilder> fleet){
        if(fleet!=null)
            PlayerFleet.add(fleet.get(fleet.size()-1).MorfToPrimitive());
    }
    public void ClearMarkupField(){
        for(int i = 0; i<SizeField; i++){
            for(int j = 0; j< SizeField; j++)
                gridPlayer[i][j] = 0;
        }
    }

    public void Swopping(PlayerData OtherPlayerData){
        ArrayList<PrimitiveShip> FleetBuffer = OtherPlayerData.PlayerFleet;
        OtherPlayerData.PlayerFleet = this.PlayerFleet;
        this.PlayerFleet = FleetBuffer;
    }
}
