import java.util.*;
import java.io.*;


class Task {
	int id, time, staff;
	String name;
	int earliestStart, latestStart;
	List<Task> outEdges;
	int cntPredecessors;
	List<Task> predecessors;
	ArrayList<Integer> idPredecessors;

	Task(int id, String name, int time, int staff, ArrayList<Integer> idPredecessors) {
		this(id, name, time, staff, idPredecessors, new LinkedList<Task>(), new LinkedList<Task>());
	}

	Task(int id, String name, int time, int staff, ArrayList<Integer> idPredecessors, List<Task> predecessors, List<Task> outEdges) {
		this.id = id;
		this.name = name;
		this.time = time;
		this.staff = staff;
		this.idPredecessors = idPredecessors;
		this.predecessors = predecessors;
		this.outEdges = outEdges;
	}

	void setEarliestStart(int s) {
		this.earliestStart = s;
	}

	void setLatestStart(int s) {
		this.latestStart = s;
	}
	
	void addPredecessor(Task task) {
		predecessors.add(task);
	}

	void addOutEdge(Task task) {
		outEdges.add(task);
	}

	static List<Task> readProject(String proj) throws FileNotFoundException {
		File file = new File(proj);
		Scanner input = new Scanner(file);
		int num_tasks = Integer.parseInt(input.nextLine());
		input.nextLine();
		List<Task> project = new ArrayList<Task>();
		List<Task> outEdges = new ArrayList<Task>();
		for (int i = 0; i < num_tasks; i++) {
			int id = input.nextInt();
			String name = input.next();
			int time = input.nextInt();
			int staff = input.nextInt();
			ArrayList<Integer> idPredecessors = new ArrayList<Integer>();
			String restOfLine = input.nextLine();
			String[] restOfLineTrim = restOfLine.trim().replaceAll(" +", " ").split(" ");
			List<String> cnt = new ArrayList<String>();
			cnt = Arrays.asList(restOfLineTrim);
			
			int j = 0;
				while (Integer.parseInt(cnt.get(j)) != 0) {
					idPredecessors.add(Integer.parseInt(cnt.get(j)));
					j++;
				}

			Task task = new Task(id, name, time, staff, idPredecessors);
			project.add(task);
		}

		for (Task n : project) { //legger til predecessors
				for (int i : n.idPredecessors) {
					n.addPredecessor(project.get(i - 1));
					project.set(n.id - 1, n);
				}
			}

		for (Task t : project) { //Hvis t er en predecessor til u blir u lagt til i outedges til t
			for (Task u : project) {
				if (t.id != u.id) {
					if ((u.predecessors).contains(t)) {
						t.addOutEdge(u);
						project.set(t.id - 1, t);
					}
				}
			}

		}
		return project;
	}
}

class ProjectPlanner {

  	static List<Task> topologicalSort(List<Task> project) { 
   		int total_time = 0;
   		int[] indegree = new int[project.size()];
       	for (Task task : project) { //Putter inngraden til hver task i en array
       		indegree[(task.id - 1)] = task.idPredecessors.size();
		}
       	LinkedList<Task> stack = new LinkedList<Task>(); 
  		for (int i = 0; i < project.size(); i++) { //Legger til hver task med inngrad 0 i stacken
  			if (indegree[i] == 0) {
  				stack.add(project.get(i));
  			}
  		}
  		
  		LinkedList<Task> result = new LinkedList<Task>(); 
  	    while (stack.size() != 0) { //Fortsetter til stacken er tom
  	  		Task n = stack.getFirst();  //første element i stacken blir tatt ut og lagt til i result;
  	  		result.add(n);
  	  		stack.remove(n);
       	    
       	    for (Task edge : n.outEdges) {	 		  //Inngraden til etterfølgerne til n blir redusert med 1,
       	   		if (--indegree[(edge.id - 1)] == 0) { //og hvis noen ender opp med inngrad 0, blir de lagt til i stacken
       	   			stack.add(edge);
       	   		}
       	   	}	
   	 	}
   	 	if (result.size() != project.size()) {
   	 		System.out.println("Project is unrealizable");
   	 		System.exit(0);
   	 	}
   	 	return result;
   	}

   	static void earliestStart(List<Task> project) {
   		project = topologicalSort(project);
   		for (Task t : project) {
   			if (t.predecessors.size() == 0) { //Earliest start til første task er alltid 0
   				t.setEarliestStart(0); 
   			} else {
   				int earliestStartMax = 0; 
   				for (Task p : t.predecessors) { //Earliest start til t blir når den seneste forgjengeren er ferdig
   					int earliestStartnew = p.earliestStart + p.time;
   					if (earliestStartnew > earliestStartMax) {
   						t.setEarliestStart(earliestStartnew);
   						earliestStartMax = earliestStartnew;
   					}
   				}
   			}
   		}
   	}

   	static void latestStart(List<Task> project) {
   		earliestStart(project); //Henter earliest start-verdiene 
   		project = topologicalSort(project);
   		int shortestTime = 0;
   		for (Task t : project) { 
   			if (t.earliestStart + t.time > shortestTime) { //Korteste tid prosjekte kan fullføres på
   				shortestTime = t.earliestStart + t.time;
   			}
   		}
   		for (int i = project.size() - 1; i > 0; i--) { 
   			int latestStartMax = shortestTime;
   			Task t = project.get(i);	
   			if (t.outEdges.size() == 0) { //Starter bakerst i prosjektet
   				t.setLatestStart(shortestTime - t.time);
   			} 
   			for (Task o : t.outEdges) { //t får latest start lik den tidligste latest start i t.outedges - t.time
   				if (o.earliestStart < latestStartMax) {
   					t.setLatestStart(o.latestStart - t.time);
   					latestStartMax = o.latestStart;
   				}
   			}
   		}
   	}

   	static void optimalTime(List<Task> project) {
   		System.out.println("Starting project");
   		earliestStart(project);
   		int time = 0;
   		int staff = 0;
   		List<Task> finished = new LinkedList<Task>();
   		while (finished.size() < project.size()) {
   			for (Task t : project) {
   				if (t.earliestStart == time) { //Task starter når t.earliestStart = time
   					System.out.println("Time: " + time);
   					System.out.println("\t\t Starting: " + t.name);
   					staff += t.staff;
   					System.out.println("\t\t Current staff: " + staff);
   					System.out.println();
   				}
   				if (t.earliestStart + t.time == time) { //Task fullført når t.earliestStart + t.time = t0ime
   					finished.add(t);
   					System.out.println("Time: " + time);
   					System.out.println("\t\t Finished: " + t.name + "\t Time taken: " + t.time);
   					staff -= t.staff;
   					System.out.println("\t\t Current staff: " + staff);
   					System.out.println();
   				}
   				if (finished.size() == project.size()) { //Task fullført når alle tasks er fullført
   					System.out.println("Finished!");
   					System.out.println("**** Shortest possible project execution is " + time + " ****");

   				}
   			}
   			time++;
   		}
   	}
}

class Oblig2 {


	public static void main (String[] args) throws FileNotFoundException { 
   	 	List<Task> project = Task.readProject(args[0]);  
		ProjectPlanner.earliestStart(project);
		ProjectPlanner.latestStart(project);
		for (Task t : project) {
			boolean critical = true;
			int slack = t.latestStart - t.earliestStart;
			if (slack != 0) {
				critical = false;
			}
			System.out.println("Task ID: " + t.id + "\tName: " + t.name + "\tTime: " + t.time + "\tStaff: "+ t.staff);
			System.out.println("\t\tEarliest start: " + t.earliestStart +"\tSlack: " + slack + "\tCritical: " + critical);
			System.out.print("\t\tPredecessor IDs: ");
			for (Task p : t.predecessors) {
				System.out.print(" " + p.id + " ");
			}
			System.out.println();
			System.out.println();
		}
		ProjectPlanner.optimalTime(project);
		
	}
}
