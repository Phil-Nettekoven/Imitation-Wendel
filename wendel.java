import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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


        File[] samples = { new File("sounds/kick_16bit.wav"), new File("sounds/snare_16bit.wav"),
                new File("sounds/CH_16bit.wav"), new File("sounds/OH_16bit.wav") };

        //byte[] pattern = readPattern("default.ser");

        byte[] pattern = {
            0,2,1,2,0,2,1,3,
            0,2,1,2,0,2,1,3,
            0,2,1,2,0,2,1,3,
            0,2,1,2,0,2,1,3,
            0,2,1,2,0,2,1,3,
        };

        double bpm;

        System.out.println("Enter a BPM: ");
        bpm = scanner.nextDouble();

        playPattern(bpm, samples, pattern);
        

        System.out.println("type anything");
        bpm = scanner.nextDouble();
        //scanner.close();

    }

    // savePattern(pattern, "array");

    public static void playPattern(double bpm, File[] samples, byte[] pattern)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        Timer clock = new Timer();
        patternTask task = new patternTask();
        bpm = (((1 / ((double)bpm / 60)) * 1000) / 2);
        System.out.println((long)bpm);

        clock.schedule(task, 0, (long)bpm);

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
                default:
                    System.out.println("Invalid sample");
            }
            synchronized (signalMain) {  
                signalMain.wait();
                System.out.println("Signal received at " +System.currentTimeMillis());
                clip.start();
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

    public static void savePattern(byte[] save, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./patterns/" + fileName + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(save);
            out.close();
            fileOut.close();
            System.out.println("Pattern saved!");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static byte[] readPattern(String fileName) {
        byte[] p;
        try {
            FileInputStream fileIn = new FileInputStream("./patterns/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            p = (byte[]) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Pattern loaded successfully.");
            return p;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Pattern not found!");
            c.printStackTrace();
        }
        return null;
    }
}
