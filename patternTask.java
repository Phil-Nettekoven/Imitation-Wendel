import java.util.TimerTask;

public class patternTask extends TimerTask{
    patternTask(){
        
    }
    public void run(){
        synchronized(Wendel.signalMain){
        Wendel.signalMain.notify();
        //System.out.println("Signal sent at " +System.currentTimeMillis());
        }
    }
}
