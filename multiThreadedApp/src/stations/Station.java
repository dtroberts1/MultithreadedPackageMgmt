package stations;

/*  Name: Dylan Roberts 
 * Course: CNT 4714 Spring 2020  
 * Assignment title: Project 2 � Multi-threaded programming in Java  
 * Date:  February 1, 2020 
 * Class:  CNT4714
 */ 

import conveyors.SynchronizedConveyor;

public class Station implements Runnable {

	private int workload; // workload remaining
	public SynchronizedConveyor inConveyor; // current output conveyer
	public SynchronizedConveyor outConveyor; // current input conveyer
	private int stationID;
	public Station(int id, int workload, SynchronizedConveyor in, SynchronizedConveyor out) {
		this.workload = workload;
		this.stationID = id;
		this.inConveyor = in;
		this.outConveyor = out;
	}
	public void setConnection(int conveyor) {
		if (conveyor == 0) {
			this.inConveyor.initializeConnection(this, true);
		}else {
			this.outConveyor.initializeConnection(this, false);
		}
	}
	@Override
    public void run() {
		// Set Connections for this station
		for (int i = 0; i < 2; i++) {
			try {
			//	Thread.sleep(randomGenerator.nextInt(30));
				setConnection(i);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		// After Both conveyer have been set, Print Number of Packages to move
		System.out.print("Routing Station " + this.stationID + ": Workload set" + 
				". Station " + this.stationID + " has " + this.workload + " package groups to move.\n");
				
		// While workloads remain,
		while (this.workload > 0) {
			try {
				// Attempt to get lock
				if (this.inConveyor.lockConveyor(this, true)) {
					try {
						// If can get lock, attempt to get lock on the other one conveyer as well
						if (this.outConveyor.lockConveyor(this, false)) {
							workload--;
							// If it can also get lock on the other one, move package across and release both locks
							// Print Successfully Moved package on __
							System.out.print("Routing Station " + this.stationID + 
									" sucessfully moves packages on conveyor C" + this.inConveyor.getConveyorID() + "\n");
							System.out.print("Routing Station " + this.stationID + 
									" sucessfully moves packages on conveyor C" + this.outConveyor.getConveyorID() + "\n");						// X packages left to move
							System.out.print("Routing Station " + this.stationID +
									" has " + this.workload + " package " + ((workload > 1) ? "groups" : "group") + " left to move.\n");
							
							// Release both locks
							this.inConveyor.getLockObj().unlock();
							System.out.print("Routing Station " + this.stationID + ": unlocks (released access) to conveyor C" + this.inConveyor.getConveyorID() + "\n");
							this.outConveyor.getLockObj().unlock();
							System.out.print("Routing Station " + this.stationID + ": unlocks (released access) to conveyor C" + this.outConveyor.getConveyorID() + "\n");
							
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		}
		System.out.print("* * Station " + this.getID() + ": workload sucessfully completed. * *\n");
	}
	public int getID() {
		return this.stationID;
	}
	public int getWorkload() {
		return this.workload;
	}
	public void setWorkload(int workload) {
		this.workload = workload;
	}
}
