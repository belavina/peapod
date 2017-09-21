import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;

public class Main {
	
	static List<Node> nodes    = new ArrayList<Node>();

	public static void printSurvey () {
		for (int i = 0; i < nodes.size(); i++ ) {
			System.out.println(">> Node Numer: " + (i + 1) + "  >>");
			System.out.println(nodes.get(i));
		}
	}
	
	public static void main(String[] args) {

		// Make Window
		JFrame window = new JFrame("Peapod");

		// Make buttons
		JButton saveNode = new JButton();
		JButton findLocation = new JButton();
		JButton createCSV  = new JButton();
		JButton clearNodes = new JButton();	
		
		// JPane to hold the buttons on the window
		JPanel pane = new JPanel();

		// Setup window
		window.setSize(275, 300);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		// setup buttons
		saveNode.setText("Create New Node");
		saveNode.setPreferredSize(new Dimension(200, 40));

		clearNodes.setText("Clear All Nodes");
		clearNodes.setPreferredSize(new Dimension(200, 40));
	
		findLocation.setText("Find Location");
		findLocation.setPreferredSize(new Dimension(200, 40));

		createCSV.setText("Create CSV");
		createCSV.setPreferredSize(new Dimension(200, 40));
		
		
		// add buttons to pane
		pane.add(saveNode);
		pane.add(findLocation);
		pane.add(createCSV);
		pane.add(clearNodes);
		

		// add pane to window
		window.add(pane);
		
		
		// Create New Node action listener
		clearNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("======================");
				System.out.println("Dropping all the Nodes");
				System.out.println("======================");

				// Check for duplicates
				nodes.clear();
			}
		});
		
		// Create New Node action listener
		saveNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("===========================");
				System.out.println("Initiated Node Registration");
				System.out.println("===========================");
				// Gets node and places it in ReadSerial
				List<ESPDevice> foundDevices = SerialReader.read(false);
				
				// Check for duplicates
				nodes.add(new Node(foundDevices));
				System.out.println("\n++ ++ ++ ++ ++ ++ ++ ++ ++ ++");
				System.out.println("++ All Registered Devices ++");
				printSurvey();
				System.out.println("++ ++ ++ ++ ++ ++ ++ ++ ++");
			}
		});
		
	
		// Find nearest node actionlistener
		findLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Fuzzy logic comparison
				System.out.println("============================");
				System.out.println("Searching for Node Locations");
				System.out.println("============================");

				// Gets a "Node" of the current location
				List<ESPDevice> UPos = SerialReader.read(false);
				List<ESPDevice> nodeDist = new ArrayList<ESPDevice>();
				
				System.out.println("\n++ ++ ++ ++ ++ ++ ++ ++ +++");
				System.out.println("++ Retrieved new Signals ++");
				System.out.println(UPos);
				System.out.println("++ ++ ++ ++ ++ ++ ++ ++ ++ ++");

				// Vars needed
				int closestNode = 0;
				double bestScore = 10000;
				int weight = 5;
				
				Node curNode;

				// Fuzzy logic comparison
				System.out.println("=======================");
				System.out.println("Calculating Fuzzy Logic");
				System.out.println("=======================");
				for (int i = 0; i < nodes.size(); i++) {  // for each node-survey 
					
					List<Double> curScores = new ArrayList<>();
					
					System.out.println("++ ++ ++ ++ ++");
					System.out.println("++ Node : " + (i+1));
					
					for (int j = 0; j < UPos.size(); j++) {   // for each device 
						curNode = nodes.get(i);

						if (curNode.esp.contains(UPos.get(j))){
	
							ESPDevice device = curNode.esp.get(curNode.esp.indexOf(UPos.get(j)));
							int diff = Math.abs(device.signalStrength - UPos.get(j).signalStrength);

							System.out.println("Calc " + j + " = " + diff + " ESSID of " + UPos.get(j).SSID);

							curScores.add((double)diff + weight);
						} else {
							curScores.add((double)weight);
						}
					}

					System.out.println("Finished Calc of Node " + (i+1));

					double avgScore = curScores.stream().mapToDouble(d -> d).average().orElse(999);

					System.out.println("Calculated score: " + avgScore + "\n");
					if (avgScore < bestScore) {
						bestScore = avgScore;
						closestNode = i + 1;
					}
				}
				System.out.println("Closest Node Is " + closestNode);

				// LocationCSV locationCSV = new LocationCSV(UPos, closestNode, scores);
				// locationCSV.writeFile();

			}
		});

	}
}