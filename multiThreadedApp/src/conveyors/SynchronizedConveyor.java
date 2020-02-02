package conveyors;

/*  Name: Dylan Roberts 
 * Course: CNT 4714 Spring 2020  
 * Assignment title: Project 2 – Multi-threaded programming in Java  
 * Date:  February 1, 2020 
 * Class:  CNT4714
 */ 
import java.util.concurrent.locks.ReentrantLock; 
import stations.Station;

public class SynchronizedConveyor{
	
	private int conveyorID;
	private ReentrantLock accessLock = new ReentrantLock();
	
	public SynchronizedConveyor(int id) {
		this.conveyorID = id;
	}
	public int getConveyorID() {
		return this.conveyorID;
	}
	public void setConveyorID(int id) {
		this.conveyorID = id;
	}
	public ReentrantLock getLockObj() {
		return this.accessLock;
	}
	public void initializeConnection(Station s, boolean input) {
		System.out.print("Routing Station " + s.getID() + (input ? ": in-connection" : ": out-connection") + " is " +
		"set to conveyor number C" + this.getConveyorID() + "\n");
	}
	// input = true -> input
	// input = false -> output
	// Attempt to lock this conveyer. If it cannot, unlock the other one (if the other one is locked)
	// to resolve deadlocks/race conditions
	public boolean lockConveyor(Station s, boolean input) {
		if (input) { // If this conveyer is the input conveyer, attempt to lock it
			if (this.accessLock.tryLock()) {
				try {
					System.out.print("Routing Station " + s.getID() + ": holds lock on " + "input" +
					" conveyor C" + this.getConveyorID() + "\n");
				}catch (Exception e) {
					//e.printStackTrace();
				}
				return true;
			}
			else {
				
				try {
					// Since input conveyer lock failed, unlock the other conveyer (if it's locked)
					if (s.outConveyor.accessLock.isLocked())
					{
						System.out.print("Routing Station " + s.getID() + ". Unable to lock input conveyor." +
								" Releasing lock on output conveyor C" + s.outConveyor.getConveyorID() + "\n");
						s.outConveyor.accessLock.unlock();
					}
				}
				catch(Exception e) {
					//e.printStackTrace();
				}
				
				return false;
			}		
		}
		else { // If attempting to lock output conveyer
			if (this.accessLock.tryLock()) {
				try {
					System.out.print("Routing Station " + s.getID() + ": holds lock on " + "output" +
					" conveyor C" + this.getConveyorID() + "\n");
				}catch (Exception e) {
					//e.printStackTrace();
				}

				return true;
			}
			else {
				try {
					// If failed to lock the output conveyer, unlock the input conveyer (if it's locked)
					if (s.inConveyor.accessLock.isLocked())
					{
						System.out.print("Routing Station " + s.getID() + ". Unable to lock output conveyor." +
								" Releasing lock on input conveyor C" + s.inConveyor.getConveyorID() + "\n");
						s.inConveyor.accessLock.unlock();
					}
				}catch(Exception e) {
					//e.printStackTrace();
				}

				return false;
			}		
		}
	}	
	public void unlockConveyor() {
		this.accessLock.unlock();
	}

}
