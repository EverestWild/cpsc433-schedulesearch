package schedulesearch;

import java.util.ArrayList;
import java.util.Collection;

public class Problem {

    public ProblemSet problem_set;
    public Collection<Assignment> assignments = new ArrayList<Assignment>();

    public Problem(ProblemSet problem_set) {
        this.problem_set = problem_set;
    }

    public Problem(ProblemSet problem_set, Collection<Assignment> assignments) {
        this.problem_set = problem_set;
        this.assignments.addAll(assignments);
    }

    public Problem(Problem other) {
        problem_set = other.problem_set;
        assignments.addAll(other.assignments);
    }

    public int eval() {
        // TODO - multipliers
        return evalMinFilled() + evalPref() + evalPair() + evalSecDiff();
    }

    public int evalMinFilled() {
        int eval = 0;
        for (Slot slot : problem_set.course_slots) {
            int filled = 0;
            for (Assignment assign : assignments) {
                // TODO - also test that this assignment is for a course
                if (assign.slot.time == slot.time) {
                    ++filled;
                }
            }
            if (filled < slot.slot_min) {
                ++eval;
            }
        }
        for (Slot slot : problem_set.lab_slots) {
            int filled = 0;
            for (Assignment assign : assignments) {
                // TODO - also test that this assignment is for a lab
                if (assign.slot.time == slot.time) {
                    ++filled;
                }
            }
            if (filled < slot.slot_min) {
                ++eval;
            }
        }
        return eval;
    }

    public int evalPref() {
        int eval = 0;
        for (Assignment assign : assignments) {
            for (Pair<Assignment, Integer> pref : problem_set.preferences) {
                if (pref.getKey().slot.time == assign.slot.time && pref.getKey().assigned != assign.assigned) {
                    eval += pref.getValue();
                }
            }
        }
        return eval;
    }

    public int evalPair() {
        int eval = 0;
        // TODO - this probably needs to be fixed, as the strings in the pair
        // are class names, not lecture/lab section names like the string in
        // Assignment
        for (Pair<Course, Course> pair : problem_set.pairs) {
            Assignment assign_a = null;
            Assignment assign_b = null;
            for (Assignment assign : assignments) {
                if (assign.assigned == pair.getKey()) {
                    assign_a = assign;
                    if (assign_b != null) {
                        break;
                    }
                }
                else if (assign.assigned == pair.getValue()) {
                    assign_b = assign;
                    if (assign_a != null) {
                        break;
                    }
                }
            }
            if (assign_a.slot.time != assign_b.slot.time) {
                ++eval;
            }
        }
        return eval;
    }

    public int evalSecDiff() {
        return 0; // TODO - this, and the other fixes, will require section
                  // name strings to be parsed into a more detailed structure
    }

}
