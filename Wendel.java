import java.io.*;
import java.util.Scanner;
import java.util.Timer;
//import java.util.concurrent.TimeUnit;
import javax.sound.sampled.*;

class Wendel extends Thread {
    static Wendel signalMain = null;
    private static Scanner scanner = null;
    private static String version = "1.0";

    public static void main(String[] args)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        signalMain = new Wendel();
        scanner = new Scanner(System.in);
        String input = null;

        Pattern curPattern = initPattern();
        dataManager.savePattern(curPattern, "default");
        File[] currentKit = dataManager.readKit("SecondArrangement.kit");

        boolean invalidInput = false;
        boolean isRunning = true;

        while (isRunning) {
            mainMenu();
            if (invalidInput) {
                System.out.print("Invalid Input!\n");
                System.out.print("Enter an option: ");
            } else {
                System.out.print("\nEnter an option: ");
            }

            input = scanner.nextLine().toUpperCase();
            switch (input) {
                case ("A"):
                    playPattern(currentKit, curPattern, curPattern.getBpm());
                    invalidInput = false;
                    break;
                case ("B"):
                    curPattern = editPattern(curPattern);
                    invalidInput = false;
                    break;
                case ("C"):
                    savePattern(curPattern);
                    invalidInput = false;
                    break;
                case ("D"):
                    loadPattern();
                    invalidInput = false;
                    break;
                case ("F"):
                    invalidInput = false;
                    break;
                case ("G"):
                    invalidInput = false;
                    break;
                case ("E"):
                    isRunning = false;
                    break;
                case ("S"):
                    setBPM(curPattern);
                    invalidInput = false;
                    break;
                default:
                    invalidInput = true;

            }

        }

    }

    public static void mainMenu() throws InterruptedException {
        cls();
        System.out.println("philn's \033[3m'Imitation Wendel'\033[0m, v" + version + "!");
        System.out.print("A) Play current pattern\n");
        System.out.print("B) Edit current pattern\n");
        System.out.print("C) Save current pattern\n");
        System.out.print("D) Load new pattern\n");
        System.out.print("F) Load new kit\n");
        System.out.print("G) Save current kit\n");
        System.out.print("S) Set BPM\n");
        System.out.print("\nE) Exit\n\n");

    }

    public static void playPattern(File[] samples, Pattern pattern, double bpm)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        cls();

        AudioInputStream stream = null;
        Clip clip = null;
        Timer clock = new Timer();
        System.out.println("BPM :"+bpm);

        long bpmLong = (long) (((1 / ((double) bpm / 60)) * 1000) / 2); // converts bpm into milliseconds between beats
                                                                        // in 4/
        System.out.println(bpmLong);

        clock.scheduleAtFixedRate(new patternTask(), 0, bpmLong);
        File blank = new File("BLANK");
        byte beat;
        for (int i = 0; i < pattern.getSize(); i++) {
            beat = pattern.getBeat(i).getBeatArray()[0];
            if (samples[beat].equals(blank)){
                synchronized (signalMain) {
                    signalMain.wait();
                }
            } else{
            clip = queueSound(stream, clip, samples[beat]);
            clipStart(clip);
            }
        }

        clock.cancel();
        clip.close();
    }

    public static Pattern loadPattern() {
        cls();
        System.out.println("Enter a filename: ");
        String fileName = scanner.nextLine();
        return dataManager.readPattern(fileName);
    }

    public static void savePattern(Pattern curPattern) {
        cls();
        System.out.println("Enter a filename: ");
        String fileName = scanner.nextLine();
        dataManager.savePattern(curPattern, fileName);
    }

    public static Pattern editPattern(Pattern curPattern) {

        return curPattern;
    }

    public static void displayBar(Pattern curPattern){
        
    }

    public static double setBPM(Pattern curPattern) {
        double bpm = 120.0;
        cls();

        System.out.print("Enter BPM: ");
        curPattern.setBpm(scanner.nextDouble());

        return bpm;
    }

    public static void clipStart(Clip clip) throws InterruptedException {
        synchronized (signalMain) {
            signalMain.wait();
            // System.out.println("Signal received at " + System.currentTimeMillis());
            clip.start();
        }
    }

    public static Clip queueSound(AudioInputStream stream, Clip clip, File sound)
            throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        stream = AudioSystem.getAudioInputStream(sound);
        clip = AudioSystem.getClip();
        clip.open(stream);
        return clip;
    }

    public static void cls() { // clears terminal
        // System.out.print("\033[H\033[2J");
        // System.out.flush();
    }

    public static Pattern initPattern(){

        Pattern defaultPattern = new Pattern(16);
        defaultPattern.getBeat(0).setBeats(0, 9);
        defaultPattern.getBeat(1).setBeats(0, 0);
        defaultPattern.getBeat(2).setBeats(0, 2);
        defaultPattern.getBeat(3).setBeats(0, 1);
        defaultPattern.getBeat(4).setBeats(0, 2);
        defaultPattern.getBeat(5).setBeats(0, 0);
        defaultPattern.getBeat(6).setBeats(0, 2);
        defaultPattern.getBeat(7).setBeats(0, 1);
        defaultPattern.getBeat(8).setBeats(0, 3);
        defaultPattern.getBeat(9).setBeats(0, 0);
        defaultPattern.getBeat(10).setBeats(0, 2);
        defaultPattern.getBeat(11).setBeats(0, 1);
        defaultPattern.getBeat(12).setBeats(0, 2);
        defaultPattern.getBeat(13).setBeats(0, 0);
        defaultPattern.getBeat(14).setBeats(0, 2);
        defaultPattern.getBeat(15).setBeats(0, 1);
        defaultPattern.getBeat(16).setBeats(0, 3);
        return defaultPattern;
    }

    public static File[] initKit(){
        File[] defaultKit = { new File("sounds/kick_16bit.wav"), new
        File("sounds/snare_16bit.wav"),
        new File("sounds/CH_16bit.wav"), new File("sounds/OH_16bit.wav"), new
        File("BLANK"), new File("BLANK"),
        new File("BLANK"), new File("BLANK"), new File("BLANK"), new
        File("sounds/silence.wav") };

        return defaultKit;
    }
}
