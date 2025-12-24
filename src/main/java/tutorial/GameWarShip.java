package tutorial;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class GameWarShip {
    private DrawField DrawField = new DrawField();

    public EditMessageText EditTextNMarkup;
    public SendPhoto PhotoMessage;
    public EditMessageCaption EditCaptionNMarkup;

    private GameLanguage gameLanguage = GameLanguage.RU;

    private final int[] coordinate = new int[2];

    private State state = State.FillingShips;
    public State getState(){ return state; }
    public void NextState() { state=state.Next(); }

    private final int SizeField = 8;

    private PlayerData PlayerOne;
    private PlayerData PlayerTwo;

    private PlayerData TempPlayer;

    public Long SessionNumber;
    public boolean gamePrepare = false;

    public String GetHittenMessage(CallBack TypeMessage ) {
        switch (TypeMessage){
            case Continue -> {return gameLanguage.GetTargetMessage(gameLanguage.HitText);}
            case Final -> {return gameLanguage.GetTargetMessage(gameLanguage.KillText);}
            case Pass -> {return gameLanguage.GetTargetMessage(gameLanguage.MissText);}
            default -> {return "";}
        }
    }

    public Long GetFirstID() { return PlayerOne.GetID(); }
    public Long GetSecondID() { return PlayerTwo.GetID(); }

    public PlayerData GiveNegativePlayer(Long targetID) {
        if (targetID.equals(PlayerOne.GetID())) return PlayerTwo;
        else return PlayerOne;
    }
    public PlayerData GivePlayer(Long targetID){
        if(targetID.equals(PlayerOne.GetID())) return PlayerOne;
        return PlayerTwo;
    }

    private PlayerMutex mutex = PlayerMutex.BOTH_MUTEX;

//    private final String[] RuAttackText = {"Играем от атаки", "ВЕРНИ ЕГО В КАМЕННЫЙ ВЕК", "Заряжай",
//            "Стреляй где погуще, своих потом разберём", "Залп", "Покажи ему Кузькину мать"};
//    private final String[] RuDefenceText = {"Играем от защиты", "Вас атакуют милорд",
//            "Он конечно селён, но не сильнее наших подлодок"};
//    public final String[] RuHitText = {"ПОПАЛ!", "Без 3 минут он не с нами", "Радар не подвёл",
//            "Одной клеткой в могиле", "Получай!"};
//    public final String[] RuKillText = {"На корм акулам", "Ко дну его придави", "Победа всё ближе"};
//    public final String[] RuMissText = {"Наводчика расстрелять!", "Орудие барахлит",
//            "Он был там, ну был же!"};



    public void ClearField(){
        PlayerOne.ClearMarkupField();
        PlayerTwo.ClearMarkupField();
    }

    public CallBack Process(Long ChatUserID, String command, InterfaceLanguage interfaceLanguage){

        if(!gameLanguage.NAME_LANGUAGE.equalsIgnoreCase(interfaceLanguage.NAME_LANGUAGE))
            switch (interfaceLanguage){
                case RU -> gameLanguage = GameLanguage.RU;
                default -> gameLanguage = GameLanguage.RU;
            }

        TempPlayer = GivePlayer(ChatUserID);
        mutex = TempPlayer.GetMutex();

        String[] parsCommand = command.split(" ");
        coordinate[0] = Integer.parseInt(parsCommand[0]);
        coordinate[1] = Integer.parseInt(parsCommand[1]);

        //if(TempPlayer.gridPlayer[coordinate[0]][coordinate[1]] == 1)
            //return CallBack.ExceptionUncorrectedPosition;


        state.scenario.Process(DrawField, TempPlayer, coordinate);

        PhotoMessage = state.scenario.PhotoMessage(mutex);
        EditTextNMarkup = state.scenario.EditText(TempPlayer);


        if(!state.scenario.GetCall().ExceptionCallBack) {


            switch (state){
                case FillingShips -> {
                    if(EditTextNMarkup!=null) {
                        EditTextNMarkup.setReplyMarkup(buildKeyboard(ChatUserID));
                        EditTextNMarkup.setChatId(ChatUserID.toString());
                    }
                    else return CallBack.UnknownError;
                    if(state.scenario.IsFieldDraw()){
                        PlayerOne.Swopping(PlayerTwo);
                    }
                }
                case Playing -> {
                    if(PhotoMessage!=null){
                        if(TempPlayer.SaveOrder){
                            PhotoMessage.setCaption(gameLanguage.GetTargetMessage(gameLanguage.DefenceText));
                            PhotoMessage.setChatId(GiveNegativePlayer(ChatUserID).GetID().toString());
                            //PhotoMessage.setReplyMarkup(buildKeyboard(GiveNegativePlayer(ChatUserID).GetID()));

                            EditCaptionNMarkup = new EditMessageCaption();
                            EditCaptionNMarkup.setReplyMarkup(buildKeyboard(ChatUserID));
                            EditCaptionNMarkup.setCaption(gameLanguage.GetTargetMessage(gameLanguage.AttackText));
                            EditCaptionNMarkup.setChatId(ChatUserID.toString());
                        }
                        else {
                            PhotoMessage.setCaption(gameLanguage.GetTargetMessage(gameLanguage.AttackText));
                            PhotoMessage.setChatId(GiveNegativePlayer(ChatUserID).GetID().toString());
                            PhotoMessage.setReplyMarkup(buildKeyboard(GiveNegativePlayer(ChatUserID).GetID()));

                            EditCaptionNMarkup = new EditMessageCaption();
                            buildKeyboard(ChatUserID);
                            //EditCaptionNMarkup.setReplyMarkup(buildKeyboard(ChatUserID));
                            EditCaptionNMarkup.setCaption(gameLanguage.GetTargetMessage(gameLanguage.DefenceText));
                            EditCaptionNMarkup.setChatId(ChatUserID.toString());
                        }
                    }
                    else return CallBack.UnknownError;
                }
            }
        }

        return state.scenario.GetCall();

    }

    public boolean TryPushUID(Long ID){
        if(PlayerOne == null){
            PlayerOne = new PlayerData(ID, PlayerMutex.PLAYER_ONE_MUTEX, gameLanguage);
            SessionNumber = ID;
            System.out.println(SessionNumber);
            return true;
        }
        if(PlayerTwo == null){
            PlayerTwo = new PlayerData(ID, PlayerMutex.PLAYER_TWO_MUTEX, gameLanguage);
            gamePrepare=true;
            System.out.println(SessionNumber);
            return true;
        }
        return false;
    }

    public InlineKeyboardMarkup buildKeyboard(Long ChatID) {

        TempPlayer = GivePlayer(ChatID);

        String EnptyCell = "⬜\uFE0F";
        String TargetCell = "🟩";
        String MissCell = "\uD83D\uDD18";
        String DeadCell = "\uD83D\uDFE5";

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                String text;
                switch (TempPlayer.gridPlayer[i][j]){
                    case 0 -> { text = EnptyCell; }
                    case 1 -> { text = TargetCell; }
                    case -1 -> { text = MissCell; }
                    case 2 -> { text = DeadCell; }
                    default -> { text = EnptyCell; }
                }
                button.setText(text);
                button.setCallbackData(i + " " + j);
                row.add(button);
            }
            rows.add(row);
        }

        markup.setKeyboard(rows);
        return markup;
    }

}

