public class beat {
    private byte[] beats = { 9, 9, 9, 9 };
    private long offset = 0; // no offset
    private boolean[] status = { false, false, false, false };

    public byte[] getBeat() {
        return beats;
    }

    public void setBeats(int channel, int sample) {
        if (channel < 0 || channel >= beats.length) {
            System.out.println("Invalid channel");
            return;
        } else if ((sample < 0) || sample > 9) {
            System.out.println("Please enter an integer between 0 and 9.");
            return;
        }

        else {
            beats[channel] = (byte) sample;
            if (sample == 9) {
                status[channel] = false;
            } else {
                status[channel] = true;
            }
        }
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

}
