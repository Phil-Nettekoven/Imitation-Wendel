import java.io.*;
import java.util.Scanner;
import javax.sound.sampled.*;
import java.util.Timer;
import java.util.TimerTask;

public class patternTask extends TimerTask{
    private int i = 0;
    patternTask(){
        
    }
    public void run(){
        synchronized(wendel.signalMain){
        //System.out.println("task task");
        wendel.signalMain.notify();
        }
    }
}
