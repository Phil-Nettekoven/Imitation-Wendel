import java.io.Serializable;
import java.util.Arrays;

public class Pattern implements Serializable {
    private int size;
    private beat[] p;

    Pattern(int size) {
        this.size = size + 1;
        p = new beat[this.size];
    }

    public beat getBeat(int i) {
        return p[i];
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
