package schedulesearch;

import java.util.ArrayList;
import java.util.Collection;

public class OrTree {

    public Problem pr;
    public Solution sol;
    public Collection<OrTree> children = new ArrayList<OrTree>();
    public Collection<Course> possible_courses = new ArrayList<Course>();

    public OrTree(Problem pr, Solution sol, Collection<Course> possible_courses) {
        this.pr = pr;
        this.sol = sol;
        this.possible_courses.addAll(possible_courses);
    }

}
