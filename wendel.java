import java.io.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.*;

class wendel extends Thread {
    static wendel signalMain = null;
    private static AudioInputStream stream = null;
    private static Clip clip = null;
    private static Scanner scanner = null;
    private static String version = "1.0";

    public static void main(String[] args)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        signalMain = new wendel();
        scanner = new Scanner(System.in);
        String input = null;
        double bpm = 120.0;
        Pattern curPattern = new Pattern(16);

        // File[] samples = { new File("sounds/kick_16bit.wav"), new
        // File("sounds/snare_16bit.wav"),
        // new File("sounds/CH_16bit.wav"), new File("sounds/OH_16bit.wav"), new
        // File("BLANK"), new File("BLANK"),
        // new File("BLANK"), new File("BLANK"), new File("BLANK"), new
        // File("sounds/silence.wav") };

        // dataManager.saveKit(samples, "SecondArrangement");

        File[] currentKit = dataManager.readKit("SecondArrangement.kit");

        System.out.println("");

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
                    playPattern(currentKit, curPattern, bpm);
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
                    bpm = setBPM();
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
        System.out.print("\nAre you ready to sequence?: \n\n");
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
        Timer clock = new Timer();

        long bpmLong = (long) (((1 / ((double) bpm / 60)) * 1000) / 2); // converts bpm into milliseconds between beats
                                                                        // in 4/
        // System.out.println(bpmLong);

        clock.scheduleAtFixedRate(new patternTask(), 0, bpmLong);

        for (int i = 0; i < pattern.getSize(); i++) {
            byte beat = pattern.getBeat(i).getBeatArray()[0]; // temp
            clip = queueSound(stream, clip, samples[beat]);
            clipStart();
        }

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

        for (int i = 1; i < curPattern.getSize(); i++) {
            cls();
            System.out.println("ENTER BEAT NUM: ");
            byte s = scanner.nextByte();
            curPattern.getBeat(i).setBeats(0, s);
        }

        return curPattern;
    }

    public static double setBPM() {
        double bpm = 120.0;
        cls();

        System.out.print("Enter BPM: ");
        bpm = scanner.nextDouble();

        return bpm;
    }

    public static void clipStart() throws InterruptedException {
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}
