package schedulesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.util.Collections;

public class OrTreeSearch{

    public OrTree tree;
    public OrTree node;
    public Iterator<String> iter;

    //ArrayList that holds all of the courses and labs
    public ArrayList<String> coursesAndLabs = new ArrayList<String>();

    public OrTreeSearch(ProblemSet pr){
        tree = new OrTree(pr, Solution.Unknown);
        node = tree;
        coursesAndLabs.addAll(pr.courses);
        coursesAndLabs.addAll(pr.labs);
        Collections.shuffle(coursesAndLabs);
        iter = coursesAndLabs.iterator();
    }

    //Assigns one random course or lab to one random slot and returns whether the search
    //is complete or not
    public boolean step(){
        Random rand = new Random();
        int randIndex;
        int courseSize = pr.problem_set.courses.size();
        int labSize = pr.problem_set.labs.size();
        String currentCourse = null;
        String currentLab = null;
        Iterator<String> courseIter = pr.problem_set.courses.iterator();
        Iterator<String> labIter = pr.problem_set.labs.iterator();

        //While loop that won't exit unless a random viable course or lab has been selected
        while((currentCourse == null) && (currentLab == null)){

            //Decides whether it will pick a random course or random lab
            randIndex = rand.nextInt(2);

            //sol = Yes if there are no more courses to schedule
            if((courseSize == 0) && (labSize == 0)){
                tree.sol = Solution.Yes;
                break;
            }

            //Selects a random viable course
            if((randIndex == 0) && (courseSize != 0)){
                randIndex = rand.nextInt(courseSize);
                for(int i = 0; i <= randIndex; i++){
                    currentCourse = courseIter.next();
                }

                //Checks to make sure randomly selected course has not already been used
                if (used.contains(currentCourse)){
                    currentCourse = null;
                }

                //Removes course from potential drawpool so it cannot be randomly picked again
                else{
                    pr.problem_set.courses.remove(randIndex);
                }
            }

            //Failsafe in case there are no more courses to pick
            else{
                randIndex = 1;
            }

            //Selects a random viable lab
            if((randIndex == 1) && (labSize != 0)){
                randIndex = rand.nextInt(labSize);
                for(int i = 0; i <= randIndex; i++){
                    currentLab = labIter.next();
                }

                //Checks to make sure randomly selected lab has not already been used
                if(used.contains(currentLab)){
                    currentLab = null;
                }

                //Removes lab from potential drawpool so it cannot be randomly picked again
                else{
                    pr.problem_set.labs.remove(randIndex);
                }
            }

            //Failsafe in case there are no more labs to pick
            else{
                randIndex = 0;
            }
        }

        Slot currentCourseSlot = null;
        Slot currentLabSlot = null;
        Assignment currentAssign = null;
        Iterator<Slot> courseSlotIter = pr.problem_set.course_slots.iterator();
        Iterator<Slot> labSlotIter = pr.problem_set.lab_slots.iterator();

        //While loop that won't exit unless a random viable slot has been selected
        while((currentCourseSlot == null) && (currentLabSlot == null)){
            int full = 0;

            //If sol == Yes there is no need to find a random slot
            if(tree.sol == Solution.Yes){
                break;
            }

            //Counts the number of full slots
            else{
                for(Slot s : pr.problem_set.course_slots){
                    if(s.slot_max == 0){
                        full += 1;
                    }
                }
            }

            //If every slot is full, then the course/lab has nowhere to go and sol = No
            if(full == pr.problem_set.course_slots.size()){
                tree.sol = Solution.No;
                break;
            }

            //Finds a random slot for the previously randomly selected course
            if(currentLab == null){
                randIndex = rand.nextInt(pr.problem_set.course_slots.size());
                for(int i = 0; i <= randIndex; i++){
                    currentCourseSlot = courseSlotIter.next();
                }

                //Only assigns the course to the slot if the min is 0
                if(currentCourseSlot.slot_min > 0){
                    currentAssign = new Assignment(currentCourseSlot.time, currentCourse);
                    pr.assignments.add(currentAssign);

                    //After assigning the course, reduces the min and max by 1, keeps them at 0
                    if(currentCourseSlot.slot_min == 0){
                        currentCourseSlot.slot_max -= 1;
                    }
                    else{
                        currentCourseSlot.slot_min -= 1;
                        currentCourseSlot.slot_max -= 1;
                    }
                }
                else{
                    currentCourseSlot = null;
                }
            }

            //Finds a random slot for the previously randomly selected lab
            else{
                randIndex = rand.nextInt(pr.problem_set.lab_slots.size());
                for(int i = 0; i <= randIndex; i++){
                    currentLabSlot = labSlotIter.next();
                }

                //Only assigns the lab to the slot if the min is 0
                if(currentLabSlot.slot_min > 0){
                    currentAssign = new Assignment(currentLabSlot.time, currentLab);
                    pr.assignments.add(currentAssign);

                    //After assigning the lab, reduces the min and max by 1, keeps them at 0
                    if(currentLabSlot.slot_min == 0){
                        currentLabSlot.slot_max -= 1;
                    }
                    else{
                        currentLabSlot.slot_min -= 1;
                        currentLabSlot.slot_max -= 1;
                    }
                }
                else{
                    currentLabSlot = null;
                }
            }
        }

        if(tree.sol == Solution.Unknown){
            return true;
        }
        else if(tree.sol == Solution.No){
            return true;
        }
        else{
            return false;
        }
    }
}
