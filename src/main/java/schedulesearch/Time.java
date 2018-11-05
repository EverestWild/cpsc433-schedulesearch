package schedulesearch;

public class Time {

    public enum Day {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
    }

    public Day day;
    public int hour;
    public int minute;

    public Time(Day day, int hour, int minute) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public static Time parse(String day, String time) throws IllegalArgumentException {
        // Parse the day
        Day parsed_day;
        if (day.equals("MO")) {
            parsed_day = Day.Monday;
        }
        else if (day.equals("TU")) {
            parsed_day = Day.Tuesday;
        }
        else if (day.equals("FR")) {
            parsed_day = Day.Friday;
        }
        else {
            throw new IllegalArgumentException("Unknown day");
        }

        // Parse the time
        String[] split_time = time.split(":");
        int hour;
        int minute;
        if (split_time.length == 2) {
            try {
                hour = Integer.parseInt(split_time[0]);
                minute = Integer.parseInt(split_time[1]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ill-formed time");
            }
        }
        else {
            throw new IllegalArgumentException("Ill-formed time");
        }

        return new Time(parsed_day, hour, minute);
    }

}
