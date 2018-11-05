package schedulesearch;

public class Slot {

    public Time time;
    public int slot_min;
    public int slot_max;

    public Slot(Time time, int slot_min, int slot_max) {
        this.time = time;
        this.slot_min = slot_min;
        this.slot_max = slot_max;
    }
}
