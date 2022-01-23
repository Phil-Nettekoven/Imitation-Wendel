import java.io.*;

public class dataManager {
    public static void savePattern(Pattern save, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./patterns/" + fileName + ".ptrn");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(save);
            out.close();
            fileOut.close();
            System.out.println("Pattern saved!");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static Pattern readPattern(String fileName) {
        Pattern p;
        try {
            FileInputStream fileIn = new FileInputStream("./patterns/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            p = (Pattern) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Pattern loaded successfully.");
            return p;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Pattern '" + fileName + "' not found.");
            c.printStackTrace();
        }
        return null;
    }

    public static void saveKit(File[] save, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream("./kits/" + fileName + ".kit");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(save);
            out.close();
            fileOut.close();
            System.out.println("Kit Saved!");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static File[] readKit(String fileName) {

        try {
            FileInputStream fileIn = new FileInputStream("./kits/" + fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            File[] p = (File[]) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Kit loaded successfully.");
            return p;
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Kit '" + fileName + "' not found.");
            c.printStackTrace();
        }
        return null;
    }

}
