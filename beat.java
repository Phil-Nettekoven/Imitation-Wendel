import java.io.Serializable;

public class beat implements Serializable {
    private boolean[] beats = { false, false, false, false, false, false }; // KICK, SNARE, CH, OH, EXTRA1, EXTRA2
    private long offset = 0; // no offset

    public boolean[] getBeatArray() {
        return beats;
    }

    public void toggleChannel(int channel) {
        if(channel < 0 || channel > 5){
            System.out.println("Invalid channel. ");
            return;
        } else{
            beats[channel] = !beats[channel];
        }
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

}
