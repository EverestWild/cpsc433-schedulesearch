package schedulesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;
import java.util.Collection;

public class SetBasedSearch {

    public ProblemSet problem_set;
    public List<Problem> problems = new ArrayList<Problem>();

    public SetBasedSearch(ProblemSet problem_set) {
        this.problem_set = problem_set;
        // TODO - create or-tree and populate the problem collection
    }

    public boolean step() {
        //Select the problem with the smallest eval value
        Problem currentProblem = null;
        Problem selectedProblem = null;
        int problemSize = problems.size();
        int eval;
        int minEval = 10000;

        for(int i = 0; i < problemSize; i++){
            currentProblem = problems.get(i);

            eval = currentProblem.eval();

            if(eval == 0){
                return false;
            }

            if(eval <= minEval){
                minEval = eval;
                selectedProblem = currentProblem;
            }
        }

        //Create an ArrayList of valid assignments to select from
        ArrayList<Assignment> validClasses = new ArrayList<Assignment>();
        Iterator<Assignment> iter = selectedProblem.assignments.iterator();
        Assignment currentAssign = null;
        int assignSize = selectedProblem.assignments.size();

        for(int i = 0; i < assignSize; i++){
            currentAssign = iter.next();

            //test constraints
            if(firstValidTest(currentAssign, selectedProblem)){
                validClasses.add(currentAssign);
            }
        }

        if(validClasses.size() == 0){
            return false;
        }

        //Select a random valid class from the ArrayList to swap
        Collections.shuffle(validClasses);

        Assignment randomClass = validClasses.get(0);

        Course swap1 = randomClass.assigned;

        //Create an ArrayList of assignments that are compatible with swap1
        validClasses.clear();
        iter = selectedProblem.assignments.iterator();

        for(int i = 0; i < assignSize; i++){
            currentAssign = iter.next();

            //test constraints again
            if(secondValidTest(currentAssign, selectedProblem, swap1)){
                validClasses.add(currentAssign);
            }
        }

        if(validClasses.size() == 0){
            return true;
        }

        //Select random compatible class from the ArrayList to swap
        Collections.shuffle(validClasses);

        randomClass = validClasses.get(0);

        Course swap2 = randomClass.assigned;

        //Swap the two classes with eachother
        iter = selectedProblem.assignments.iterator();

        for(int i = 0; i < assignSize; i++){
            currentAssign = iter.next();

            if(currentAssign.assigned == swap1){
                currentAssign.assigned = swap2;
            }
            else if(currentAssign.assigned == swap2){
                currentAssign.assigned = swap1;
            }
        }

        //Add new problem with swapped classes to problems
        problems.add(selectedProblem);

        //Delete problem with highest eval value
        problemSize = problems.size();

        int maxProblem = 0;
        int maxEval = 0;

        for(int i = 0; i < problemSize; i++){
            currentProblem = problems.get(i);

            eval = currentProblem.eval();

            if(eval == 0){
                return false;
            }

            if(eval >= maxEval){
                maxEval = eval;
                maxProblem = i;
            }
        }
        problems.remove(maxProblem);

        return true;
    }

    public Problem getSolution() {
        //Returns the problem with the lowest eval value
        Problem currentProblem = null;
        int problemSize = problems.size();
        int minProblem = 0;
        int eval;
        int minEval = 10000;

        for(int i = 0; i < problemSize; i++){
            currentProblem = problems.get(i);

            eval = currentProblem.eval();

            if(eval == 0){
                return currentProblem;
            }

            if(eval <= minEval){
                minEval = eval;
                minProblem = i;
            }
        }

        return problems.get(minProblem);
    }

    public boolean firstValidTest(Assignment currentAssign, Problem selectedProblem){
        Course current = currentAssign.assigned;

        //Don't move classes in the partial assignment
        Collection<Pair<Time, Course>> partialAssign = selectedProblem.problem_set.partial_assignments;

        for(Pair<Time, Course> partial : partialAssign){
            Course pairCourse = partial.getValue();

            if(current == pairCourse){
                return false;
            }
        }

        //Don't touch CPSC 813 and CPSC 913
        if((current.course == 813) || (current.course == 913)){
            return false;
        }

        return true;
    }

    public boolean secondValidTest(Assignment currentAssign, Problem selectedProblem, Course swap1){
        Course current = currentAssign.assigned;

        //Don't move classes in the partial assignment
        Collection<Pair<Time, Course>> partialAssign = selectedProblem.problem_set.partial_assignments;

        for(Pair<Time, Course> partial : partialAssign){
            Course pairCourse = partial.getValue();

            if(current == pairCourse){
                return false;
            }
        }

        //Don't touch CPSC 813 and CPSC 913
        if((current.course == 813) || (current.course == 913)){
            return false;
        }

        //Make sure classes aren't being moved into an unwanted slot
        Collection<Pair<Time, Course>> unwantedAssign = selectedProblem.problem_set.unwanted;

        for(Pair<Time, Course> unwant : unwantedAssign){
            Course pairCourse = unwant.getValue();

            if(swap1 == pairCourse){
                return false;
            }
        }

        //If swap1 is a course, select another course. If it's a lab, select another lab.
        //Make sure a course does not move into a slot with a lab for that course ?????????
        if(swap1.lab == 0){
            if(current.lab >= 1){
                return false;
            }

            Time currentTime = currentAssign.slot.time;

            for(Assignment comp : selectedProblem.assignments){
                if((currentAssign.slot.overlap(comp.slot)) && (comp.assigned.course == swap1.course)){
                    if(comp.assigned.lab >= 1){
                        return false;
                    }
                }
            }
        }

        //Make sure a lab does not move into a slot with a course for that lab ??????????
        else if(swap1.lab >= 1){
            if(current.lab == 0){
                return false;
            }

            Time currentTime = currentAssign.slot.time;

            for(Assignment comp : selectedProblem.assignments){
                if((currentAssign.slot.overlap(comp.slot)) && (comp.assigned.course == swap1.course)){
                    if(comp.assigned.lab == 0){
                        return false;
                    }
                }
            }
        }

        //If swap1 is a LEC 9 course, swap2 must be an evening course
        if(swap1.section == 9){
            if(currentAssign.slot.time.hour < 18){
                return false;
            }
        }

        //Make sure a 500 level course does not move into a slot with another 500 level course
        if((swap1.course >= 500) && (swap1.lab == 0)){
            for(Assignment comp : selectedProblem.assignments){
                if((comp.slot.time.hour == currentAssign.slot.time.hour)){

                    if((current.course >= 500) && (current.lab == 0)){
                        return false;
                    }
                }
            }
        }

        //If slot1 is CPSC 313 or CPSC 413, don't swap them with 18:00 courses
        if((swap1.course == 313) || (swap1.course == 413)){
            if(currentAssign.slot.time.hour == 18){
                return false;
            }
        }

        return true;
    }

}
