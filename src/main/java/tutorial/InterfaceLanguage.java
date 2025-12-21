package tutorial;

public enum InterfaceLanguage {
    RU("ru","", "Поделись кодом игры со своим другом: ", "Все игроки готовы. Колдуем поле",
            "Начни строить корабль", "Ты атакуешь",
            "Ты защищаешь", "Ты не в игре",
            "Такая игра уже существует", "Ещё не пришла твоя очередь",
            "Здесь нельзя разместить часть корабля",
            "Здесь нельзя разместить корабль", "Неправильный ход", "Такой игры нет", "Неизвестная ошибка"),

    EN("en","", "Share code game with your friend","All players ready. Create the field",
               "Build first ship", "You are attacking", "You are defending", "You are not in the game",
               "Such a game already exists", "It's not your turn yet", "You can't put a part of the ship here",
               "You can't put a ship here", "Wrong move", "There is no such game", "Unknown error");

    public final String NAME_LANGUAGE;
    public final String RULES;
    public final String INVITE;
    public final String GAME_PREPARE; //Игровое поле готово, после того, как его заполнили
    public final String START_GAME;
    public final String ATTACK_CONDITION;
    public final String DEFENCE_CONDITION;
    public final String NON_PLAYER_EXCEPTION;
    public final String GAME_ALREADY_EXIST_EXCEPTION;
    public final String MUTEX_EXCEPTION;
    public final String INVALID_POSITION_EXCEPTION;
    public final String UNCORRECTED_POSITION_EXCEPTION;
    public final String MOVE_EXCEPTION;
    public final String GAME_NO_EXIST_EXCEPTION;
    public final String UNKNOWN_EXCEPTION;

    InterfaceLanguage(String  NAME_LANGUAGE,
                      String rules, String invite, String gamePrepare, String startGame, String attackCondition,
                      String defenceCondition, String nonPlayerException, String gameAlreadyExistException,
                      String mutexException, String invalidPositionException, String uncorrectPositionException,
                      String moveException, String gameNoExistException, String unknownException) {
        this.NAME_LANGUAGE = NAME_LANGUAGE;
        RULES = rules;
        INVITE = invite;
        GAME_PREPARE = gamePrepare;
        START_GAME = startGame;
        ATTACK_CONDITION = attackCondition;
        DEFENCE_CONDITION = defenceCondition;
        NON_PLAYER_EXCEPTION = nonPlayerException;
        GAME_ALREADY_EXIST_EXCEPTION = gameAlreadyExistException;
        MUTEX_EXCEPTION = mutexException;
        INVALID_POSITION_EXCEPTION = invalidPositionException;
        UNCORRECTED_POSITION_EXCEPTION = uncorrectPositionException;
        MOVE_EXCEPTION = moveException;
        GAME_NO_EXIST_EXCEPTION = gameNoExistException;
        UNKNOWN_EXCEPTION = unknownException;
    }
    public String ExceptionMessage(CallBack callBack){
        switch (callBack){
            case ExceptionUncorrectedPosition -> { return UNCORRECTED_POSITION_EXCEPTION; }
            case ExceptionInvalidPosition -> { return INVALID_POSITION_EXCEPTION; }
            case ExceptionMutex -> { return MUTEX_EXCEPTION; }
            case ExceptionMove -> { return MOVE_EXCEPTION; }
            default -> { return UNKNOWN_EXCEPTION; }
        }
    }
}
