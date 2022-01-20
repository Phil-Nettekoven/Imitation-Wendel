import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import javax.sound.sampled.*;

class wendel extends Thread {
    public static wendel signalMain = null;
    public static AudioInputStream stream = null;
    public static Clip clip = null;
    public static Scanner scanner = null;

    public static void main(String[] args)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        signalMain = new wendel();
        scanner = new Scanner(System.in);


        // File[] samples = { new File("sounds/kick_16bit.wav"), new File("sounds/snare_16bit.wav"),
        //         new File("sounds/CH_16bit.wav"), new File("sounds/OH_16bit.wav"), new File("sounds/silence.wav") };

        //saveData.saveKit(samples, "SecondArrangement");

        byte[] pattern = saveData.readData("default.ptrn");
        File[] samples = saveData.readKit("SecondArrangement.kit");

        // byte[] pattern = { 6,
        //     0,2,1,2,0,2,1,3,
        //     0,2,1,2,0,2,1,3,
        //     0,2,1,2,0,2,1,3,
        //     0,2,1,2,0,2,1,3,
        //     0,2,1,2,0,2,1,3,
        // };

        //saveData.savePattern(pattern, "default");

        double bpm;

        System.out.println("Enter a BPM: ");
        bpm = scanner.nextDouble();

        playPattern(bpm, samples, pattern);
        playPattern(bpm, samples, pattern);
        playPattern(bpm, samples, pattern);
        playPattern(bpm, samples, pattern);
        playPattern(bpm, samples, pattern);

    }

    // savePattern(pattern, "array");

    public static void playPattern(double bpm, File[] samples, byte[] pattern)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        Timer clock = new Timer();
        patternTask task = new patternTask();
        bpm = (((1 / ((double)bpm / 60)) * 1000) / 2);
        //bpm -= 10;
        System.out.println((long)bpm);
        
        //clip = queueSound(stream, clip, samples[pattern[0]]);
        clock.scheduleAtFixedRate(task, 0, (long)bpm);
        
        // clip.start();
        // synchronized (signalMain) {  
        //     signalMain.wait();
        //     // System.out.println("Signal received at " +System.currentTimeMillis());
        //     clip.start();
        // }

        for (int i = 0; i < pattern.length; i++) {
            
            switch (pattern[i]){ 
                case (0):
                    clip = queueSound(stream, clip, samples[0]);
                    break;
                case (1):
                    clip = queueSound(stream, clip, samples[1]);
                    break;
                case (2):
                    clip = queueSound(stream, clip, samples[2]);
                    break;
                case (3):
                    clip = queueSound(stream, clip, samples[3]);
                    break;
                    case(6):
                    clip = queueSound(stream, clip, samples[4]);
                    break;
                default:
                    System.out.println("Invalid sample");
            }
            synchronized (signalMain) {  
                signalMain.wait();
                //System.out.println("Signal received at " +System.currentTimeMillis());
                clip.start();
                //System.out.println("Sample played at " +System.currentTimeMillis());
            }
            
            //clock.schedule(task, 0, (long)bpm);
        }
        clock.cancel();

    }

    public static Clip queueSound(AudioInputStream stream, Clip clip, File sound)
            throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        stream = AudioSystem.getAudioInputStream(sound);
        clip = AudioSystem.getClip();
        clip.open(stream);
        return clip;
    }

    
}
