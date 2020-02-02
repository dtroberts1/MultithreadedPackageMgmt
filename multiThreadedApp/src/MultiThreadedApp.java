/*  Name: Dylan Roberts 
 * Course: CNT 4714 Spring 2020  
 * Assignment title: Project 2 � Multi-threaded programming in Java  
 * Date:  February 1, 2020 
 * Class:  CNT4714
 */ 

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import stations.Station;
import conveyors.SynchronizedConveyor;


public class MultiThreadedApp {
	public static void main(String[] args) {
		// IMPORTANT: It reads from the following directory:
		 File file = new File("src/resources/config.txt");
		 int nbrRoutingStations;
		 int []allWork;
		 int []conveyorIDs;
		 
		 // Create Instance of Executer Service for the number of stations
		 ExecutorService executerService = Executors.newCachedThreadPool();
		 
		 SynchronizedConveyor [] conveyors;
	 
		 try (Scanner scanner = new Scanner(file)){
			 nbrRoutingStations = Integer.parseInt(scanner.nextLine());
			 allWork = new int[nbrRoutingStations];
			 conveyors = new SynchronizedConveyor[nbrRoutingStations];
			 conveyorIDs = new int[nbrRoutingStations];
			 for (int i = 0 ; i < nbrRoutingStations; i++)
				 conveyors[i] = new SynchronizedConveyor(i);
			 scanner.reset();
			  // for (int i = nbrRoutingStations - 1;i >= 0; i--) {
			 for (int i = 0;i < nbrRoutingStations; i++) {
				   	allWork[i] = Integer.parseInt(scanner.nextLine());
				   	conveyorIDs = getConveyorsPerStation(i, nbrRoutingStations);
					executerService.execute(new Station(i, allWork[i], conveyors[conveyorIDs[0]], conveyors[conveyorIDs[1]]));
			   }
			   
			   scanner.close();
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	}
	public static int[] getConveyorsPerStation(int stationNbr, int nbrStations) {
		nbrStations -= 1;
		int []returnArray = {stationNbr,((stationNbr - 1 < 0) ? (nbrStations): (stationNbr - 1))};
		
		return returnArray;
	}
}
