package schedulesearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class OrTreeSearch{

    public OrTree tree;
    public OrTree node;

    public OrTreeSearch(ProblemSet problem_set) {
        // Create a new problem for the root of the tree
        Problem problem = new Problem(problem_set);
        // Grab a list of all courses
        Collection<Course> possible_courses = new ArrayList<Course>();
        possible_courses.addAll(problem_set.courses);
        possible_courses.addAll(problem_set.labs);
        // Insert the partial assignments into the schedule
        for (Pair<Time, Course> partial_assignment : problem_set.partial_assignments) {
            Time time = partial_assignment.getKey();
            Course course = partial_assignment.getValue();
            Slot slot;
            if (course.lab == 0) {
                slot = problem_set.getCourseSlotByTime(time);
            }
            else {
                slot = problem_set.getLabSlotByTime(time);
            }
            if (slot == null) {
                throw new RuntimeException("");
            }
            // Insert the course and remove it from the candidates list
            problem.assignments.add(new Assignment(slot, course));
            possible_courses.remove(course);
        }
        // Initialize the tree
        tree = new OrTree(problem, Solution.Unknown, possible_courses);
        node = tree;
    }

    //Assigns one random course or lab to one random slot and returns whether the search
    //is complete or not
    public boolean step(){
        // TODO
        return true;
    }

}
