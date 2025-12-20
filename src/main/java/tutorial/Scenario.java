package tutorial;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;


interface Scenario{
    CallBack GetCall();
    State Process(DrawField drawer, PlayerData playerData, int[] command);
    boolean IsFieldDraw();

    EditMessageText EditText(PlayerData playerData);
    SendPhoto PhotoMessage(PlayerMutex mutex);
}