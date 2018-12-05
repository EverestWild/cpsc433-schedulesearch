package schedulesearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrTreeSearch {

    public Problem problem;
    public List<Course> possible_courses = new ArrayList<Course>();
    public List<Slot> possible_slots = new ArrayList<Slot>();

    public OrTreeSearch(ProblemSet problem_set) {
        problem = new Problem(problem_set);
        // Grab a list of all courses
        possible_courses.addAll(problem_set.courses);
        possible_courses.addAll(problem_set.labs);
        // Grab a list of all slots
        possible_slots.addAll(problem_set.course_slots);
        possible_slots.addAll(problem_set.lab_slots);
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
            // Insert the course and remove it and its slot from the candidate lists
            problem.assignments.add(new Assignment(slot, course));
            possible_courses.remove(course);
            possible_slots.remove(slot);
        }
    }

    public Problem run() {
        List<Course> courses = new ArrayList<Course>(possible_courses);
        List<Slot> slots = new ArrayList<Slot>(possible_slots);
        return searchNode(new Problem(problem), courses, slots);
    }

    private Problem searchNode(Problem problem, List<Course> possible_courses, List<Slot> possible_slots) {
        Collections.shuffle(possible_courses);
        for (Course course : possible_courses) {
            Collections.shuffle(possible_slots);
            for (Slot slot : possible_slots) {
                // Check if this combination would break constraints
                // TODO

                // Enter this node
                Problem new_problem = new Problem(problem);
                List<Course> courses = new ArrayList<Course>(possible_courses);
                List<Slot> slots = new ArrayList<Slot>(possible_slots);
                new_problem.assignments.add(new Assignment(slot, course));
                courses.remove(course);
                slots.remove(slot);

                Problem result = searchNode(new_problem, courses, slots);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

}
