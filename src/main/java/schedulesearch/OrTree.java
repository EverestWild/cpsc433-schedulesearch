package schedulesearch;

import java.util.ArrayList;
import java.util.List;

public class OrTree {

    public Problem pr;
    public Solution sol;
    public List<OrTree> children = new ArrayList<OrTree>();

    public OrTree(Problem pr, Solution sol) {
        this.pr = pr;
        this.sol = sol;
    }

    public OrTree(Problem pr, Solution sol, List<OrTree> children) {
        this.pr = pr;
        this.sol = sol;
        for (OrTree child : children) {
            this.children.add(child);
        }
    }

}
