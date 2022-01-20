import java.util.TimerTask;

public class patternTask extends TimerTask{
    patternTask(){
        
    }
    public void run(){
        synchronized(wendel.signalMain){
        wendel.signalMain.notify();
        //System.out.println("Signal sent at " +System.currentTimeMillis());
        }
    }
}
