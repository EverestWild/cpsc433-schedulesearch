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

}
