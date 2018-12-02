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
                if (assign.time == slot.time) {
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
                if (assign.time == slot.time) {
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
                if (pref.key.time == assign.time && pref.key.assigned != assign.assigned) {
                    eval += pref.value;
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
        for (Pair<String, String> pair : problem_set.pairs) {
            Assignment assign_a = null;
            Assignment assign_b = null;
            for (Assignment assign : assignments) {
                if (assign.assigned == pair.key) {
                    assign_a = assign;
                    if (assign_b != null) {
                        break;
                    }
                }
                else if (assign.assigned == pair.value) {
                    assign_b = assign;
                    if (assign_a != null) {
                        break;
                    }
                }
            }
            if (assign_a.time ~= assign_b.time) {
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
