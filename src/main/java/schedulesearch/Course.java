package schedulesearch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Course{

    public String name;
    public int course;
    public int section;
    public int lab;

    static private final Pattern pattern = Pattern.compile("(?<name>\\w+) (?<course>\\d+)(?: LEC (?<section>\\d+))?(?: (?:LAB|TUT) (?<lab>\\d+))?", Pattern.CASE_INSENSITIVE);

    public Course(String str) throws IllegalArgumentException {
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
	    name = matcher.group("name");
            course = Integer.parseInt(matcher.group("course"));
            if (matcher.group("section") != null) {
                section = Integer.parseInt(matcher.group("section"));
            }
            else {
                section = 0;
            }
            if (matcher.group("lab") != null) {
                lab = Integer.parseInt(matcher.group("lab"));
            }
            else {
                lab = 0;
            }
        }
        else {
            throw new IllegalArgumentException("Invalid course or section name");
        }
    }

    public Course(Course other) {
	name = other.name;
        course = other.course;
        section = other.section;
        lab = other.lab;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        else if (o != null && o instanceof Course) {
            Course c = (Course) o;
            return name == c.name && course == c.course && section == c.section && lab == c.lab;
        }
        else {
            return false;
        }
    }

}
