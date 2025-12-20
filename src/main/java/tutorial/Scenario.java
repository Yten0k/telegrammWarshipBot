package tutorial;

import org.telegram.telegrambots.meta.api.interfaces.Validable;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.ArrayList;

interface Scenario{
    CallBack GetCall();
    State Process(DrawField drawer, PlayerData playerData, int[] command);
    boolean IsFieldDraw();

    EditMessageText EditText(PlayerData playerData);
    SendPhoto PhotoMessage(PlayerMutex mutex);
}