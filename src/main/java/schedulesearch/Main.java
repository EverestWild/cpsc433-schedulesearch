package schedulesearch;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        ProblemSet problem = new ProblemSet("input.txt"); // TODO - read filename(s) from args
        SetBasedSearch search = new SetBasedSearch(problem);
        while (search.step()) {
            // TODO - break early if we run out of time
        }
        // TODO - print results
    }

}
