// =============================================================================
//  ProblemSet
// -----------------------------------------------------------------------------
//  A ProblemSet is a simple container for all data related to a problem set.
// -----------------------------------------------------------------------------
//  A. Problem set data
//  B. Constructors
//  C. Input parsing
//  D. Section handlers
// =============================================================================

package schedulesearch;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ProblemSet {

    // =========================================================================
    //  A. Problem set data
    // =========================================================================

    public String name = null;
    public Collection<Slot> course_slots = new ArrayList<Slot>();
    public Collection<Slot> lab_slots = new ArrayList<Slot>();
    public Collection<String> courses = new ArrayList<String>();
    public Collection<String> labs = new ArrayList<String>();
    public Collection<Pair<String, String>> not_compatible = new ArrayList<Pair<String, String>>();
    public Collection<Assignment> unwanted = new ArrayList<Assignment>();
    public Collection<Pair<Assignment, Integer>> preferences = new ArrayList<Pair<Assignment, Integer>>();
    public Collection<Pair<String, String>> pairs = new ArrayList<Pair<String, String>>();
    public Collection<Assignment> partial_assignments = new ArrayList<Assignment>();

    // =========================================================================
    //  B. Constructors
    // =========================================================================

    public ProblemSet(String filename) throws IOException, ParseException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(filename);
            parse(in);
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }

    // =========================================================================
    //  C. Input parsing
    // =========================================================================

    private int line_no;

    private void parse(InputStream in) throws ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        line_no = 0;
        try {
            while ((line = reader.readLine()) != null) {
                line_no += 1;
                if (current_section == null) {
                    if (line.length() > 0) {
                        if (line.charAt(line.length() - 1) == ':') {
                            current_section = makeHandler(line.substring(0, line.length() - 1));
                        }
                        else {
                            throw new ParseException("Expected section name", line_no);
                        }
                    }
                }
                else {
                    if (line.length() == 0) {
                        current_section = null;
                    }
                    else {
                        current_section.parseLine(line.trim().split("\\s*,\\s*"));
                    }
                }
            }
        }
        catch (IOException e) {
            throw new ParseException(e, line_no);
        }
    }

    private interface SectionHandler {
        void parseLine(String[] elements) throws ParseException;
    }

    private SectionHandler current_section = null;

    private SectionHandler makeHandler(String section_name) throws ParseException {
        if (section_name.equalsIgnoreCase("Name")) {
            return (line) -> parseName(line);
        }
        else if (section_name.equalsIgnoreCase("Course slots")) {
            return (line) -> parseCourseSlots(line);
        }
        else if (section_name.equalsIgnoreCase("Lab slots")) {
            return (line) -> parseLabSlots(line);
        }
        else if (section_name.equalsIgnoreCase("Courses")) {
            return (line) -> parseCourses(line);
        }
        else if (section_name.equalsIgnoreCase("Labs")) {
            return (line) -> parseLabs(line);
        }
        else if (section_name.equalsIgnoreCase("Not compatible")) {
            return (line) -> parseNotCompatible(line);
        }
        else if (section_name.equalsIgnoreCase("Unwanted")) {
            return (line) -> parseUnwanted(line);
        }
        else if (section_name.equalsIgnoreCase("Preferences")) {
            return (line) -> parsePreferences(line);
        }
        else if (section_name.equalsIgnoreCase("Pair")) {
            return (line) -> parsePair(line);
        }
        else if (section_name.equalsIgnoreCase("Partial assignments")) {
            return (line) -> parsePartialAssignments(line);
        }
        else {
            throw new ParseException("Unknown section \"" + section_name + "\"", line_no);
        }
    }

    // =========================================================================
    //  D. Section handlers
    // =========================================================================

    private void parseName(String[] elements) throws ParseException {
        if (elements.length == 1) {
            if (name == null) {
                name = elements[0];
            }
            else {
                throw new ParseException("More than one name provided for the schedule", line_no);
            }
        }
        else {
            throw new ParseException("Wrong number of elements for the schedule name", line_no);
        }
    }

    private void parseCourseSlots(String[] elements) throws ParseException {
        if (elements.length == 4) {
            try {
                Time time = Time.parse(elements[0], elements[1]);
                int course_max = Integer.parseInt(elements[2]);
                int course_min = Integer.parseInt(elements[3]);
                course_slots.add(new Slot(time, course_max, course_min));
            }
            catch (NumberFormatException e) {
                throw new ParseException("Invalid arguments given for course slot", e, line_no);
            }
        }
        else {
            throw new ParseException("Wrong number of elements for a course slot", line_no);
        }
    }

    private void parseLabSlots(String[] elements) throws ParseException {
        if (elements.length == 4) {
            try {
                Time time = Time.parse(elements[0], elements[1]);
                int lab_max = Integer.parseInt(elements[2]);
                int lab_min = Integer.parseInt(elements[3]);
                lab_slots.add(new Slot(time, lab_max, lab_min));
            }
            catch (NumberFormatException e) {
                throw new ParseException("Invalid arguments given for lab slot", e, line_no);
            }
        }
        else {
            throw new ParseException("Wrong number of elements for a lab slot", line_no);
        }
    }

    private void parseCourses(String[] elements) throws ParseException {
        if (elements.length == 1) {
            courses.add(elements[0]);
        }
        else {
            throw new ParseException("Wrong number of elements for a course", line_no);
        }
    }

    private void parseLabs(String[] elements) throws ParseException {
        if (elements.length == 1) {
            labs.add(elements[0]);
        }
        else {
            throw new ParseException("Wrong number of elements for a lab", line_no);
        }
    }

    private void parseNotCompatible(String[] elements) throws ParseException {
        if (elements.length == 2) {
            // TODO - parse these into identifiers instead of leaving them as strings
            not_compatible.add(new Pair<String, String>(elements[0], elements[1]));
        }
        else {
            throw new ParseException("Wrong number of elements for a 'not compatible' statement", line_no);
        }
    }

    private void parseUnwanted(String[] elements) throws ParseException {
        if (elements.length == 3) {
            Time time = Time.parse(elements[1], elements[2]);
            unwanted.add(new Assignment(time, elements[0]));
        }
        else {
            throw new ParseException("Wrong number of elements for an 'unwanted' statement", line_no);
        }
    }

    private void parsePreferences(String[] elements) throws ParseException {
        if (elements.length == 4) {
            Time time = Time.parse(elements[0], elements[1]);
            int value = Integer.parseInt(elements[3]);
            preferences.add(new Pair<Assignment, Integer>(new Assignment(time, elements[2]), value));
        }
        else {
            throw new ParseException("Wrong number of elements for a 'preference' statement", line_no);
        }
    }

    private void parsePair(String[] elements) throws ParseException {
        if (elements.length == 2) {
            // TODO - parse these into identifiers instead of leaving them as strings
            pairs.add(new Pair<String, String>(elements[0], elements[1]));
        }
        else {
            throw new ParseException("Wrong number of elements for a 'pair' statement", line_no);
        }
    }

    private void parsePartialAssignments(String[] elements) throws ParseException {
        if (elements.length == 3) {
            Time time = Time.parse(elements[1], elements[2]);
            partial_assignments.add(new Assignment(time, elements[0]));
        }
        else {
            throw new ParseException("Wrong number of elements for a 'partial assignments' statement", line_no);
        }
    }

}
