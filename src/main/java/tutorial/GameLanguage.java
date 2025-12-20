package tutorial;

public enum Language {
    RU(new String[]{"Играем от атаки", "ВЕРНИ ЕГО В КАМЕННЫЙ ВЕК", "Заряжай",
            "Стреляй где погуще, своих потом разберём", "Залп", "Покажи ему Кузькину мать"},
            new String[] {"Играем от защиты", "Вас атакуют милорд",
                    "Он конечно селён, но не сильнее наших подлодок"},
            new String[] {"ПОПАЛ!", "Без 3 минут он не с нами", "Радар не подвёл",
                    "Одной клеткой в могиле", "Получай!"},
            new String[] {"На корм акулам", "Ко дну его придави", "Победа всё ближе"},
            new String[] {"Наводчика расстрелять!", "Орудие барахлит", "Он был там, ну был же!"});
    //EN();

    public final String[] AttackText;
    public final String[] DefenceText;
    public final String[] HitText;
    public final String[] KillText;
    public final String[] MissText;

    Language(String[] AttackText, String[] DefenceText, String[] HitText, String[] KillText, String[] MissText){
        this.AttackText =AttackText;
        this.DefenceText =DefenceText;
        this.HitText = HitText;
        this.KillText = KillText;
        this.MissText = MissText;
    }

    public String GetTargetMessage(String[] TargetMessage){
        int RandomPhraseNumber = (int)(Math.random() * TargetMessage.length);
        return TargetMessage[RandomPhraseNumber];
    }
}
