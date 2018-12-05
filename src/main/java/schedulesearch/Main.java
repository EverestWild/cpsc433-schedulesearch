package schedulesearch;

import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import schedulesearch.Time.Day;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
    	String input = args[0];
    	int pref1 = Integer.parseInt(args[1]);
    	int pref2 = Integer.parseInt(args[2]);
    	int pref3 = Integer.parseInt(args[3]);
    	int pref4 = Integer.parseInt(args[4]);
    	Collection<Assignment> solution = new ArrayList<Assignment>();
        ProblemSet problem = new ProblemSet(input);
        SetBasedSearch search = new SetBasedSearch(problem);
	int timer = 2000;
        while (search.step()) {
            timer -= 1;
	    if(timer = 0){
		break;
	    }
        }

        System.out.print(search.problems.eval());
        Problem solution_set = search.getSolution();
        solution.addAll(solution_set.assignments);
        Iterator<Assignment> iter = solution.iterator();
        String day = null;
        
        while(iter.hasNext()) {
        	Assignment currentassign = iter.next();
        	if(currentassign.slot.time.day == Day.Monday) {
        		day = "MO";
        	}
        	if(currentassign.slot.time.day == Day.Tuesday) {
        		day = "TU";
        	}
        	if(currentassign.slot.time.day == Day.Friday) {
        		day = "FR";
        	}
        	if(currentassign.course.lab == 0){
        		System.out.print(currentassign.course.name + " " +currentassign.course.course + "LEC " + currentassign.course.section + "\t\t: " + day + ", " + currentassign.slot.time.hour + ":" + currentassign.slot.time.minute);
        	}
        	if(currentassign.course.section == 0) {
        		System.out.print(currentassign.course.name + " " +currentassign.course.course + "TUT " + currentassign.course.lab + "\t\t: " + day + ", " + currentassign.slot.time.hour + ":" + currentassign.slot.time.minute);
        	}
        	else {
        		System.out.print(currentassign.course.name + " " +currentassign.course.course + " LEC " + currentassign.course.section + " TUT " + currentassign.course.lab + "\t: " + day + ", " + currentassign.slot.time.hour + ":" + currentassign.slot.time.minute);
        	}
        }                       
    }
}
