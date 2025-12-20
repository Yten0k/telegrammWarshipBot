package tutorial;

public enum GameLanguage {
    RU("ru",
            new String[]{"Играем от атаки", "ВЕРНИ ЕГО В КАМЕННЫЙ ВЕК", "Заряжай",
            "Стреляй где погуще, своих потом разберём", "Залп", "Покажи ему Кузькину мать"},
            new String[] {"Играем от защиты", "Вас атакуют милорд",
                    "Он конечно селён, но не сильнее наших подлодок"},
            new String[] {"ПОПАЛ!", "Без 3 минут он не с нами", "Радар не подвёл",
                    "Одной клеткой в могиле", "Получай!"},
            new String[] {"На корм акулам", "Ко дну его придави", "Победа всё ближе"},
            new String[] {"Наводчика расстрелять!", "Орудие барахлит", "Он был там, ну был же!"},
            "Поставь 4х палубный корабль",
            "Поставь 3х палубный корабль",
            "Поставь 2х палубный корабль",
            "Поставь 1о палубный корабль",
            "Корабли закончились, ждём второго игрока");
    //EN();

    public final String NAME_LANGUAGE;
    public final String[] AttackText;
    public final String[] DefenceText;
    public final String[] HitText;
    public final String[] KillText;
    public final String[] MissText;
    public final String BattleShipMessage;
    public final String CruiserMessage;
    public final String DestroyerMessage;
    public final String BoatMessage;
    public final String EndMessage;

    GameLanguage(String NAME_LANGUAGE, String[] AttackText, String[] DefenceText, String[] HitText, String[] KillText, String[] MissText, String battleShip, String cruiser, String destroyer, String boat, String end){
        this.NAME_LANGUAGE= NAME_LANGUAGE;
        this.AttackText =AttackText;
        this.DefenceText =DefenceText;
        this.HitText = HitText;
        this.KillText = KillText;
        this.MissText = MissText;
        this.BattleShipMessage = battleShip;
        this.CruiserMessage = cruiser;
        this.DestroyerMessage = destroyer;
        this.BoatMessage = boat;
        this.EndMessage = end;
    }

    public String GetTargetMessage(String[] TargetMessage){
        int RandomPhraseNumber = (int)(Math.random() * TargetMessage.length);
        return TargetMessage[RandomPhraseNumber];
    }
    public String GetShipMessage(Ships ship){
        switch (ship){
            case Battleship -> { return BattleShipMessage; }
            case Cruiser -> { return CruiserMessage; }
            case Distroyer -> { return DestroyerMessage; }
            case Boat -> { return BoatMessage; }
            default -> { return EndMessage; }
        }
    }
}
