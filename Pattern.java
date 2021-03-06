import java.io.Serializable;
import java.util.Arrays;

public class Pattern implements Serializable {
    private int size;
    private beat[] p;
    private double bpm = 120;

    Pattern(int numBars) {
        this.size = (numBars*8) + 1;
        p = new beat[this.size];
        for (int i = 0; i < this.size; i++){
            p[i] = new beat();
        }
    }

    public beat getBeat(int i) {
        return p[i];
    }

    public int getSize(){
        return size;
    }

    public double getBpm(){
        return bpm;
    }

    public void setBpm(double newBpm){
        if (newBpm < 1){
        System.out.println("BPM must be non-zero, positive double. ");
        return;
        } else{
        bpm = newBpm;
        }
    }

    public void growPattern(int num) {
        if (num < 1) {
            System.out.println("Please enter a non-zero, positive integer.");
            return;
        }
        size += num;
        p = Arrays.copyOf(p, size);
    }

    public void shrinkPattern(int num) {
        if (num < 1) {
            System.out.println("Please enter a non-zero, positive integer.");
            return;
        } else if (size <= 1) {
            System.out.println("Pattern is already empty.");
            return;
        } else {
            size -= num;
            p = Arrays.copyOf(p, size);
        }
    }

}
