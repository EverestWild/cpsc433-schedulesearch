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

        String swap1 = randomClass.assigned;

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

        String swap2 = randomClass.assigned;

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

        //Don't move classes in the partial assignment
        Collection<Assignment> partialAssign = selectedProblem.problem_set.partial_assignments;

        for(Assignment partial : partialAssign){
            if(currentAssign.assigned == partial.assigned){
                return false;
            }
        }

        String cpsc813 = "813";
        String cpsc913 = "913";

        //Don't touch CPSC 813 and CPSC 913
        if((currentAssign.assigned.contains(cpsc813)) || (currentAssign.assigned.contains(cpsc913))){
            return false;
        }

        return true;
    }

    public boolean secondValidTest(Assignment currentAssign, Problem selectedProblem, String swap1){
        String current = currentAssign.assigned;
        String[] swapSplit = swap1.split("\\s+");

        //Don't move classes in the partial assignment
        Collection<Assignment> partialAssign = selectedProblem.problem_set.partial_assignments;

        for(Assignment partial : partialAssign){
            if(current == partial.assigned){
                return false;
            }
        }

        //Don't touch CPSC 813 and CPSC 913
        String cpsc813 = "813";
        String cpsc913 = "913";

        if((current.contains(cpsc813)) || (current.contains(cpsc913))){
            return false;
        }

        //Make sure classes aren't being moved into an unwanted slot
        Collection<Assignment> unwantedAssign = selectedProblem.problem_set.unwanted;

        for(Assignment unwant : unwantedAssign){
            if(swap1 == unwant.assigned){
                return false;
            }
        }

        //If swap1 is a course, select another course. If it's a lab, select another lab.
        //Make sure a course does not move into a slot with a lab for that course ?????????
        if((!swap1.contains("TUT")) || (!swap1.contains("LAB"))){
            if((current.contains("TUT")) || (current.contains("LAB"))){
                return false;
            }

            String swapNum = swapSplit[1];

            Time currentTime = currentAssign.time;

            for(Assignment comp : selectedProblem.assignments){
                if((comp.time.day == currentTime.day) && (comp.assigned.contains(swapNum))){
                    if((comp.assigned.contains("TUT")) || (comp.assigned.contains("LAB"))){
                        return false;
                    }
                }
            }
        }

        //Make sure a lab does not move into a slot with a course for that lab ??????????
        else if((swap1.contains("TUT")) || (swap1.contains("LAB"))){
            if((!current.contains("TUT")) || (!current.contains("LAB"))){
                return false;
            }

            String swapNum = swapSplit[1];

            Time currentTime = currentAssign.time;

            for(Assignment comp : selectedProblem.assignments){
                if((comp.time.day == currentTime.day) && (comp.assigned.contains(swapNum))){
                    if((!comp.assigned.contains("TUT")) || (!comp.assigned.contains("LAB"))){
                        return false;
                    }
                }
            }
        }

        //If swap1 is a LEC 9 course, swap2 must be an evening course
        if((swapSplit[2] == "LEC") && (swapSplit[3].contains("9"))){
            if(currentAssign.time.hour < 18){
                return false;
            }
        }

        //Make sure a 500 level course does not move into a slot with another 500 level course
        if(swapSplit[1].charAt(0) == '5'){
            for(Assignment comp : selectedProblem.assignments){
                if((comp.time.day == currentAssign.time.day)){

                    String[] currentSplit = currentAssign.assigned.split("\\s+");

                    if(currentSplit[1].charAt(0) == '5'){
                        return false;
                    }
                }
            }
        }

        //If slot1 is CPSC 313 or CPSC 413, don't swap them with 18:00 courses
        if((swap1.contains("313")) || (swap1.contains("413"))){
            if(currentAssign.time.hour == 18){
                return false;
            }
        }

        return true;
    }

}
