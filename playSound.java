
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author PN
 */
public class playSound implements Runnable{
    Clip clip = null;
    File file = null;
    playSound(File filePath){
        file = filePath;
    }

    public void run(){ //queue clip
        try
        {
            //File audioFile = new File(file);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            
            //JOptionPane.showMessageDialog(null,"Press OK to stop playing");
            
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void play(){
        clip.start(); 
    }
    
}
