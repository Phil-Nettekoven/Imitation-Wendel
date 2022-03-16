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

    public static void playPattern(File[] samples, Pattern pattern, double bpm) // needs rework to support new
                                                                                // structure.
            throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {

        cls();

        AudioInputStream stream = null;
        Clip clip = null;
        Timer clock = new Timer();
        System.out.println("BPM :" + bpm);

        long bpmLong = (long) (((1 / ((double) bpm / 60)) * 1000) / 2); // converts bpm into milliseconds between beats
                                                                        // in 4/
        System.out.println(bpmLong);

        clock.scheduleAtFixedRate(new patternTask(), 0, bpmLong);
        File blank = new File("BLANK");
        boolean[] beat;
        for (int i = -1; i < pattern.getSize(); i++) {
            if (i < 0) {
                clip = queueSound(stream, clip, blank);
                clipStart(clip);
            } else {

                beat = pattern.getBeat(i).getBeatArray();
                if (!beat[0]) {
                    synchronized (signalMain) {
                        signalMain.wait();
                    }
                } else {
                    clip = queueSound(stream, clip, blank); // TEMP TEMP TEMP TEMP TEMP temp TEMP
                    clipStart(clip);
                }
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
        int index = 1;
        if (index > 0) {
            displayBar(curPattern, index);
        } else {
            System.out.println("Error, index must be 1 or greater, but is " + index);
        }

        String test = scanner.nextLine();
        System.out.println("test" + test);
        return curPattern;
    }

    public static void displayBar(Pattern curPattern, int i) {
        cls();
        int barIndex = ((i / 8) + 1);
        char[][] barChars = getBarChars(curPattern, barIndex);

        System.out.print("           _______________BAR " + barIndex + "________________\n");
        System.out.print("          |   1    |   2    |   3    |   4    |\n");
        System.out.print("CUSTOM 2  |" + barChars[0][5] + "   " + barChars[1][5] + "   |" + barChars[2][5] + "   "
                + barChars[3][5] + "   |" + barChars[4][5] + "   " + barChars[5][5] + "   |" + barChars[6][5] + "   "
                + barChars[7][5] + "   |\n");
        System.out.print("CUSTOM 1  |" + barChars[0][4] + "   " + barChars[1][4] + "   |" + barChars[2][4] + "   "
                + barChars[3][4] + "   |" + barChars[4][4] + "   " + barChars[5][4] + "   |" + barChars[6][4] + "   "
                + barChars[7][4] + "   |\n");
        System.out.print("OPEN HAT  |" + barChars[0][3] + "   " + barChars[1][3] + "   |" + barChars[2][3] + "   "
                + barChars[3][3] + "   |" + barChars[4][3] + "   " + barChars[5][3] + "   |" + barChars[6][3] + "   "
                + barChars[7][3] + "   |\n");
        System.out.print("CLOSED HAT|" + barChars[0][2] + "   " + barChars[1][2] + "   |" + barChars[2][2] + "   "
                + barChars[3][2] + "   |" + barChars[4][2] + "   " + barChars[5][2] + "   |" + barChars[6][2] + "   "
                + barChars[7][2] + "   |\n");
        System.out.print("SNARE     |" + barChars[0][1] + "   " + barChars[1][1] + "   |" + barChars[2][1] + "   "
                + barChars[3][1] + "   |" + barChars[4][1] + "   " + barChars[5][1] + "   |" + barChars[6][1] + "   "
                + barChars[7][1] + "   |\n");
        System.out.print("KICK      |" + barChars[0][0] + "   " + barChars[1][0] + "   |" + barChars[2][0] + "   "
                + barChars[3][0] + "   |" + barChars[4][0] + "   " + barChars[5][0] + "   |" + barChars[6][0] + "   "
                + barChars[7][0] + "   |\n");
        // System.out.print(" |___________________________________|\n");

    }

    private static char[][] getBarChars(Pattern curPattern, int curBar) {
        char[][] barChars = new char[8][curPattern.getBeat(1).getBeatArray().length];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < curPattern.getBeat(i).getBeatArray().length; j++) {
                barChars[i][j] = beatDisplay(curPattern.getBeat(i).getBeatArray()[j]);
            }
        }
        return barChars;
    }

    private static char beatDisplay(boolean beat) {
        if (beat) {
            return 'x';
        } else {
            return ' ';
        }
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static Pattern initPattern() {

        Pattern defaultPattern = new Pattern(2);
        defaultPattern.getBeat(0).toggleChannel(0);
        defaultPattern.getBeat(1).toggleChannel(2);
        defaultPattern.getBeat(2).toggleChannel(1);
        defaultPattern.getBeat(3).toggleChannel(2);
        defaultPattern.getBeat(4).toggleChannel(0);
        defaultPattern.getBeat(5).toggleChannel(2);
        defaultPattern.getBeat(6).toggleChannel(1);
        defaultPattern.getBeat(7).toggleChannel(3);
        defaultPattern.getBeat(8).toggleChannel(0);
        defaultPattern.getBeat(9).toggleChannel(2);
        defaultPattern.getBeat(10).toggleChannel(1);
        defaultPattern.getBeat(11).toggleChannel(2);
        defaultPattern.getBeat(12).toggleChannel(0);
        defaultPattern.getBeat(13).toggleChannel(2);
        defaultPattern.getBeat(14).toggleChannel(1);
        defaultPattern.getBeat(15).toggleChannel(3);
        return defaultPattern;
    }

    public static File[] initKit() {
        File[] defaultKit = { new File("sounds/kick_16bit.wav"), new File("sounds/snare_16bit.wav"),
                new File("sounds/CH_16bit.wav"), new File("sounds/OH_16bit.wav"), new File("BLANK"), new File("BLANK"),
                new File("BLANK"), new File("BLANK"), new File("BLANK"), new File("sounds/silence.wav") };

        return defaultKit;
    }
}
