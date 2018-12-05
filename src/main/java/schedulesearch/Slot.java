package schedulesearch;

import java.util.Collection;

public class Slot {

    public Time time;
    public int slot_min;
    public int slot_max;
    public int duration;

    public Slot(Time time, int slot_min, int slot_max, int duration) {

        this.time = time;
        this.slot_min = slot_min;
        this.slot_max = slot_max;
        this.duration = duration;
    }

    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    public int getSlotMin() {
        return slot_min;
    }
    public void setSlotMin(int slot_min) {
        this.slot_min = slot_min;
    }
    public int getSlotMax() {
        return slot_max;
    }
    public void setSlotMax(int slot_max) {
        this.slot_max = slot_max;
    }

    public boolean overlap(Slot other) {
        int slot_start = 0;
        int slot_end = 0;
        int other_slot_start = 0;
        int other_slot_end = 0;

        if((time.day.equals(Time.Day.Monday)||time.day.equals(Time.Day.Friday)) && other.time.day.equals(Time.Day.Tuesday)) {
            return false;
        }
        if(time.day.equals(Time.Day.Tuesday)&&(other.time.day.equals(Time.Day.Monday)||other.time.day.equals(Time.Day.Friday))) {
            return false;
        }

        slot_start = (time.hour * 60) + time.minute;
        slot_end = (time.hour *60) + time.minute + duration;

        other_slot_start = (other.time.hour *60) + other.time.minute;
        other_slot_end = (other.time.hour *60) + other.time.minute + other.duration;

        if(slot_start < other_slot_start && slot_end > other_slot_start) {
            return true;
        }
        if(slot_start < other_slot_end && slot_end > other_slot_end) {
            return true;
        }

        if(slot_start == other_slot_start && slot_end == other_slot_end) {
            return true;
        }

        return false;
    }

}
