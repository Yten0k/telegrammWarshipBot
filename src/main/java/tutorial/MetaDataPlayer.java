package tutorial;

import java.util.HashMap;
import java.util.Map;

public class MetaDataPlayer {
    private Long SessionID;
    private Integer MessageID;
    private Long OpponentID;
    public InterfaceLanguage language = InterfaceLanguage.RU;
    public boolean InGame = false;

    MetaDataPlayer(Long SessionID, boolean inGame) {
        this.SessionID = SessionID;
        this.InGame = inGame;
    }
    MetaDataPlayer(Long SessionID, Integer MessageID, Long OpponentID) {
        this.SessionID = SessionID;
        this.MessageID = MessageID;
        this.OpponentID = OpponentID;
        this.InGame = true;
    }
    public Long GetOpponent(){ return OpponentID; }

    public Long GetSessionID(){ return SessionID; }
    public Integer GetMessageID() { return MessageID; }

}
