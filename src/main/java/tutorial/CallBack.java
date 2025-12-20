package tutorial;


public enum CallBack {
    Final(false,""),
    Continue(false, ""),
    Pass(false, ""),
    ExceptionMutex(true, "Denial of access"),
    ExceptionInvalidPosition(true, "Incorrect ship construction"),
    ExceptionUncorrectedPosition(true, "Incorrect ship construction"),
    ExceptionMove(true, "Incorrect move"),
    UnknownError(true, "Unknown error");

    public final boolean ExceptionCallBack;
    public final String ExceptionMessage;

    CallBack(boolean exceptionCallBack, String ExceptionMessage){
        this.ExceptionCallBack = exceptionCallBack;
        this.ExceptionMessage = ExceptionMessage;
    }

    public CallBack PassState(CallBack NewCall){
        if(NewCall.ExceptionCallBack || !this.ExceptionCallBack)
            return NewCall;
        return this;
    }
}
