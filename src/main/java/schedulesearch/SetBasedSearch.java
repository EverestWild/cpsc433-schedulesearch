package schedulesearch;

import java.util.ArrayList;
import java.util.List;

public class SetBasedSearch {

    public ProblemSet problem_set;
    public List<Problem> problems = new ArrayList<Problem>();

    public SetBasedSearch(ProblemSet problem_set) {
        this.problem_set = problem_set;
        // TODO - create or-tree and populate the problem collection
    }

    public boolean step() {
        return false; // TODO
    }

    public Problem getSolution() {
        return problems.get(0); // TODO
    }

}
