package tutorial;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.io.*;

public class Bot extends TelegramLongPollingBot {

    private final String path = System.getProperty("user.dir") + "\\src\\";

    private GameWarShip WarShip;
    private InterfaceLanguage language = InterfaceLanguage.RU;

    private String[] PullFirst = new String[]{
            "0 0", "0 1", "0 2", "0 3", "0 5", "0 6", "0 7", "2 0", "2 1", "4 0", "4 1", "4 3", "5 6", "7 0"};
    private String[] PullSecond = new String[]{
            "0 0", "1 0", "2 0", "3 0", "0 3", "1 3", "2 3", "0 5", "1 5", "0 7", "1 7", "7 7", "5 5", "7 0"};

    private int LastMessage;

    private Map<Long, Long> UsersInGame = new HashMap<>(); //UserID->GameSession
    private Map<Long, MetaDataPlayer> UserMeta = new HashMap<>();
    private Map<Long, GameWarShip> GameSessions = new HashMap<>(); //GameSession->Game


    @Override
    public String getBotUsername() {
        return "WarShip";
    }

    @Override
    public String getBotToken() {
        return "8430233063:AAFsz_JZtA_ejKJx10evAuRuFWct_1Ta8qU";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        Long ChatUserID;
        if(msg!=null)
            ChatUserID = update.getMessage().getChatId();
        else
            ChatUserID = update.getCallbackQuery().getMessage().getChatId();

        //if(UsersInGame.containsKey(ChatUserID))
        //    WarShip = GameSessions.get(UsersInGame.get(ChatUserID));

        // 1️⃣ Обработка команды /start

        if(!UserMeta.containsKey(ChatUserID))
            UserMeta.put(ChatUserID, new MetaDataPlayer(ChatUserID, false));

        if (update.hasMessage() && msg.hasText()) {
            String text = msg.getText();
            String[] command = text.split(" ");
            if (text.equals("/start")) {
                //sendGrid(ChatUserID);
                System.out.println("ParseMode");
            }
            if (text.equals("/rules")){
                SendMessage(ChatUserID, language.RULES);
            }
            if(command[0].equals("/join")){
                if(UserMeta.containsKey(Long.parseLong(command[1])) && UserMeta.get(Long.parseLong(command[1])).InGame) {
                    WarShip.TryPushUID(ChatUserID);
                    UserMeta.put(ChatUserID, new MetaDataPlayer(WarShip.SessionNumber, true));
                    if(WarShip.gamePrepare)
                        sendGrid();
                    else
                        return;
                }
                else
                    SendMessage(ChatUserID, language.GAME_NO_EXIST_EXCEPTION, 3500L);
            }
            if(text.equals("/newgame")){
                //WarShip = GameSessions.put(ChatUserID, new GameWarShip());
                if (WarShip == null){
                    WarShip = new GameWarShip();
                    WarShip.TryPushUID(ChatUserID);
                    SendMessage(ChatUserID, language.INVITE + WarShip.SessionNumber);
                    UserMeta.put(ChatUserID, new MetaDataPlayer(WarShip.SessionNumber, true));
                }
            }
            return;
        }

        if(!UserMeta.containsKey(ChatUserID) || !UserMeta.get(ChatUserID).InGame){
            SendMessage(ChatUserID, language.NON_PLAYER_EXCEPTION);
            return;
        }



        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            String[] Pull = WarShip.GetFirstID().equals(chatId) ? PullFirst : PullSecond; //Use DeBug Mode

            if(data.equals("7 7") && WarShip.getState().equals(State.FillingShips)){//DeBud Mode
                for(int l = 0; l < 14; l++) {


                    String[] parts = Pull[l].split(" "); //data.split(" ");
                    int i = Integer.parseInt(parts[0]);
                    int j = Integer.parseInt(parts[1]);

                    System.out.println("Нажата кнопка с координатами: " + i + " " + j);

                    CallBack action = WarShip.Process(chatId, Pull[l], language);

                    if(WarShip.getState() == State.FillingShips) {
                            if (!WarShip.getState().scenario.IsFieldDraw()) {
                                if (!action.ExceptionCallBack && WarShip.EditTextNMarkup != null) {
                                    WarShip.EditTextNMarkup.setMessageId(messageId);
                                    SendMarkup(WarShip.EditTextNMarkup);
                                } else
                                    System.out.println(action);
                            } else {
                                BothDeleteMessage();
                                //BothSendMessage(language.GAME_PREPARE, true);
                                Long ChatOpponent = UserMeta.get(ChatUserID).GetOpponent();
                                SendMessage(ChatUserID, UserMeta.get(ChatUserID).language.GAME_PREPARE,3500L);
                                SendMessage(ChatOpponent, UserMeta.get(ChatOpponent).language.GAME_PREPARE, 3500L);

                                WarShip.ClearField();
                                SendField(WarShip.GetSecondID());
                                SendField(WarShip.GetFirstID());
                                WarShip.NextState();
                            }
                    }
                }

            }//Use DeBug Mode

            else {//Main Mode
                String[] parts = data.split(" ");
                int i = Integer.parseInt(parts[0]);
                int j = Integer.parseInt(parts[1]);

                System.out.println("Press coordinates: " + i + " " + j);

                CallBack action = WarShip.Process(chatId, data, language);
                if(action.ExceptionCallBack){
                    SendMessage(chatId, language.ExceptionMessage(action), 3500L);
                    return;
                }
                switch (WarShip.getState()) {
                    case FillingShips -> {
                        if (!WarShip.getState().scenario.IsFieldDraw()) {
                            if (WarShip.EditTextNMarkup != null) {
                                WarShip.EditTextNMarkup.setMessageId(messageId);
                                SendMarkup(WarShip.EditTextNMarkup);
                            } else
                                System.out.println(action);
                        } else {
                            BothDeleteMessage();
                            //BothSendMessage(language.GAME_PREPARE, true);
                            Long ChatOpponent = UserMeta.get(ChatUserID).GetOpponent();
                            SendMessage(ChatUserID, UserMeta.get(ChatUserID).language.GAME_PREPARE,3500L);
                            SendMessage(ChatOpponent, UserMeta.get(ChatOpponent).language.GAME_PREPARE, 3500L);

                            WarShip.ClearField();
                            SendField(WarShip.GetSecondID());
                            SendField(WarShip.GetFirstID());
                            WarShip.NextState();
                        }

                    }
                    case Playing -> {
                        if (WarShip.EditCaptionNMarkup != null) {
                            SendMessagePlayingState(messageId, chatId, action);
                        }
                        if (WarShip.getState().scenario.IsFieldDraw()) {
                            WarShip.NextState();
                        }
                    }
                }
            }

            LastMessage = messageId;
        }
    }

    private void SendMessagePlayingState(Integer messageId, Long chatId, CallBack action){
        WarShip.EditCaptionNMarkup.setMessageId(messageId);
        SendMarkup(WarShip.EditCaptionNMarkup);

        final Long time = 3000L;
        SendMessage(chatId, WarShip.GetHittenMessage(action), time);

        DeleteMessage deleteMessage = GenerateDelete(WarShip.GiveNegativePlayer(chatId).GetID());
        deleteMessage.setMessageId(LastMessage);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        SendMarkup(WarShip.PhotoMessage);
    }

    private void SendMarkup(EditMessageText messageText){
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void SendMarkup(EditMessageCaption messageText){
        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    private void SendMarkup(SendPhoto message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    private void BothDeleteMessage(){

        Long first = WarShip.GetFirstID();
        Long second = WarShip.GetSecondID();

        DeleteMessage message1 = GenerateDelete(first);
        DeleteMessage message2 = GenerateDelete(second);

        try {
            execute(message1);
            execute(message2);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private DeleteMessage GenerateDelete(Long ChatID){
        DeleteMessage message = new DeleteMessage();
        message.setChatId(ChatID.toString());
        message.setMessageId(UserMeta.get(ChatID).GetMessageID());

        return message;
    }

    private void sendGrid() {

        Long first = WarShip.GetFirstID();
        Long second = WarShip.GetSecondID();

        SendMessage message1 = GenerateMessage(first);
        SendMessage message2 = GenerateMessage(second);

        Integer MessageID1=null;
        Integer MessageID2=null;

        try {
            MessageID1 = execute(message1).getMessageId();
            MessageID2 = execute(message2).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if(MessageID1 != null && MessageID2 != null){
            UserMeta.put(first, new MetaDataPlayer(first, MessageID1, second));
            UserMeta.put(second, new MetaDataPlayer(first, MessageID2, first));
        }
    }

    private SendMessage GenerateMessage(Long ChatID){

        InlineKeyboardMarkup markup;
        markup = WarShip.buildKeyboard(ChatID);

        SendMessage message = new SendMessage();
        message.setChatId(ChatID.toString());
        message.setText(UserMeta.get(ChatID).language.START_GAME);
        message.setReplyMarkup(markup);

        return message;
    }

    private void SendField(Long ChatID){
        SendPhoto message = new SendPhoto();
        message.setChatId(String.valueOf(ChatID));
        File picture;
        if(WarShip.GetFirstID().equals(ChatID)) {
            picture = new File(path + "1_field.png");
            message.setCaption(language.ATTACK_CONDITION);
        }
        else {
            picture = new File(path + "2_field.png");
            message.setCaption(language.DEFENCE_CONDITION);
        }
        

        message.setPhoto(new InputFile(picture));
        if(WarShip.GetFirstID().equals(ChatID))
            message.setReplyMarkup(WarShip.buildKeyboard(ChatID));

        try {
            execute(message);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    public void SendMessage(Long ChatID, String putMessage){
        SendMessage message = new SendMessage();
        message.setChatId(ChatID.toString());
        message.setText(putMessage);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void SendMessage(Long ChatID, String putMessage, Long time){
        SendMessage message = new SendMessage();
        message.setChatId(ChatID.toString());
        message.setText(putMessage);

        Integer MsgID;
        try {
            MsgID = execute(message).getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        if(MsgID!=null){
            DeleteMessage deleteMessage = new DeleteMessage(ChatID.toString(),MsgID);
            try {
                Thread.sleep(time);
                execute(deleteMessage);
            } catch (InterruptedException | TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void BothSendMessage(String putMessage, boolean deletable){

        final Long Time = 3000L;
        if(deletable){
            SendMessage(WarShip.GetFirstID(), putMessage, Time);
            SendMessage(WarShip.GetSecondID(), putMessage, Time);
        }
        else {
            SendMessage(WarShip.GetFirstID(), putMessage);
            SendMessage(WarShip.GetSecondID(), putMessage);
        }
    }

}
