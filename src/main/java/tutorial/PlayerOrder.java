package tutorial;

import org.glassfish.jersey.model.internal.RankedComparator;

public class PlayerOrder {
    private PlayerMutex order;
    private boolean Rule; //false - меняется очередь каждый ход, true - очередь меняется после промаха

    PlayerOrder(PlayerMutex playerMutex, boolean Rule){
        this.order = playerMutex;
        this.Rule = Rule;
    }
    PlayerOrder(PlayerMutex playerMutex){ this.order = playerMutex; }

    public boolean SwapOrder(CallBack callBack){
        if(Rule && callBack == CallBack.Pass){
            order = order.SWAP();
            return false;
        } else if (!Rule) {
            order = order.SWAP();
            return false;
        }
        return true;
    }
    public PlayerMutex GetOrder(){ return order; }

    public boolean MyOrder(PlayerMutex playerMutex){
        if(order == PlayerMutex.BOTH_MUTEX) return true;
        return playerMutex==order;
    }
    public void PassMutex(PlayerMutex mutex){ this.order = mutex; }
}
