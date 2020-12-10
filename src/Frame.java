import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.util.*;


public class Frame extends JFrame {

	private ButtonGroup appendGroup;
	private JButton newSet, appendData, deleteData, updateBound, displayGraph, viewLog, updateData, dataDistribution;
	private JRadioButton appendFile, appendKeyboard;
	private JTextArea displayData, analysis,distribution, errorMessage;
	private JTextField addValue, deleteValue, lowBound, highBound;
	private JLabel boundLabel;
	private JScrollPane scroll;
	
	private JFileChooser fc;
	private File errorLog;
	private ActionListener bt = new ClickListener();
	private FocusListener fl = new focus();
	private OutputStream os = null;
	
	private Scanner scan;
	
	private int[] array = new int[500];
	private int count, arrayMaxSize = 500;
	private int mean, median, mode, high, low;

	private boolean boundUpdated = false;
	private boolean setCreated = false;
	
	private String history = "";
	
	public Frame(JFileChooser mainFC, File mainErrorLog, int approved) {
		setSize(1300, 800);
		count = 0;
		mean = 0;
		median = 0;
		mode = 0;
		high = 100;
		low = 0;
		
		fc = mainFC;
		errorLog = mainErrorLog;
		if(approved == JFileChooser.APPROVE_OPTION) {
			count = 0;
			arrayMaxSize = 500;
			array = new int[arrayMaxSize];
			
			System.out.println("Approved: " + fc.getSelectedFile().getAbsolutePath());				
			inputData();
			boundUpdated = false;
			setCreated = true;
			
			System.out.println("Error log: " + fc.getCurrentDirectory() + "\\errorLog.txt");
			history += "New data set create\n";
			history += "\tNew data set added from " + fc.getSelectedFile().getAbsolutePath() + " successfully with " + count + " data records @ default boundary\n";
		}
			
			
		
		try {
			os = new FileOutputStream(errorLog);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		createPanel();
	}
	
	private void createPanel() {
		JPanel main = new JPanel(new GridBagLayout());
		JPanel buttonGroup = buttonGroup();
		JPanel displayGroup = displayGroup();
		JPanel boundaryGroup = boundaryGroup();
		JPanel errorGroup = errorGroup();
		
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel topGroup = new JPanel(new GridBagLayout());
		//topGroup.setLayout(new GridLayout(1,2));
		c.gridx = 0;
		c.gridy = 0;
		topGroup.add(buttonGroup,c);
		c.gridx = 400;
		topGroup.add(displayGroup,c);
		//topGroup.setPreferredSize(new Dimension(1200, 500));
		//buttonGroup.setSize(new Dimension(1000, 500));
		//topGroup.add(buttonGroup);
		//displayGroup.setSize(new Dimension(200,500));
		//topGroup.add(displayGroup);
		
		//main.setLayout(new GridLayout(3,1));
		c.gridx = 0;
		c.gridy = 0;
		//main.setBounds(0, 0, 1200, 500);
		main.add(topGroup,c);
		c.gridy = 500;
		main.add(boundaryGroup,c);
		c.gridy = 700;
		main.add(errorGroup,c);
		
		add(main);
	}
	
	private JPanel buttonGroup() {		
		newSet = new JButton("Create new set");
		newSet.addActionListener(bt);
		
		appendData = new JButton("Append data");
		appendData.addActionListener(bt);
		
		deleteData = new JButton("Delete data");
		deleteData.addActionListener(bt);
		
		appendFile = new JRadioButton("Append from file");
		appendFile.addActionListener(bt);
		
		appendKeyboard = new JRadioButton("Append from input");
		appendKeyboard.addActionListener(bt);
		
		appendGroup = new ButtonGroup();
		appendGroup.add(appendFile);
		appendGroup.add(appendKeyboard);
		
		addValue = new JTextField("Adding value");
		addValue.addFocusListener(fl);
		deleteValue = new JTextField("Deleting value");
		deleteValue.addFocusListener(fl);
		
		JPanel appendGroup = new JPanel();
		appendGroup.setLayout(new BorderLayout());
		appendGroup.add(appendFile, BorderLayout.WEST);
		appendGroup.add(appendKeyboard, BorderLayout.EAST);
		
		JPanel group = new JPanel();
		group.setLayout(new GridLayout(6, 1));
		group.add(newSet);
		group.add(appendData);
		group.add(appendGroup);
		group.add(addValue);
		group.add(deleteData);
		group.add(deleteValue);
		
		return group;
		
	}
	
	private JPanel displayGroup() {
		displayData = new JTextArea();
		displayData.setEditable(false);
		displayData.setLineWrap(true);
		displayData.setWrapStyleWord(true);
		displayData.setFont(new Font("Serif", 0, 22));
		//displayData.setPreferredSize(new Dimension(300, 500));
		displayData.setTabSize(5);
		scroll = new JScrollPane(displayData);
		scroll.setViewportView(displayData);
		scroll.setPreferredSize(new Dimension(310, 500));
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		JPanel display = new JPanel();
		display.setBorder(new TitledBorder("Data Display"));
		//display.add(displayData);
		display.add(scroll);
		
		
		analysis = new JTextArea();
		analysis.setEditable(false);
		analysis.setLineWrap(true);
		analysis.setWrapStyleWord(true);
		analysis.setFont(new Font("Serif", 0, 22));
		analysis.setPreferredSize(new Dimension(300,500));
		JPanel ana = new JPanel();
		ana.setBorder(new TitledBorder("Data Analysis"));
		ana.add(analysis);
		
		distribution = new JTextArea();
		distribution.setEditable(false);
		distribution.setLineWrap(false);
		//distribution.setWrapStyleWord(true);
		distribution.setFont(new Font("Serif", 0, 15));
		distribution.setTabSize(5);
		//distribution.setPreferredSize(new Dimension(300, 500));
		JScrollPane distScroll = new JScrollPane(distribution, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		distScroll.setViewportView(distribution);
		distScroll.setPreferredSize(new Dimension(300, 500));
		JPanel dist = new JPanel();
		dist.setBorder(new TitledBorder("Report Summary"));
		dist.add(distScroll);
		//dist.add(distribution);

		
		JPanel group = new JPanel();
		group.setLayout(new GridLayout(1,3));  // row, col
		group.add(display);
		group.add(ana);
		group.add(dist);
		
		return group;
	}
	
	private JPanel boundaryGroup() {
		JPanel dummy = new JPanel();
		
		boundLabel = new JLabel();
		boundLabel.setText("Set boundary: ");
		lowBound = new JTextField("0");
		lowBound.setPreferredSize(new Dimension(50,25));
		lowBound.addFocusListener(fl);
		highBound = new JTextField("100");
		highBound.setPreferredSize(new Dimension(50,25));
		highBound.addFocusListener(fl);
		
		updateBound = new JButton("Update Bounds");
		updateBound.addActionListener(bt);
		
		updateData = new JButton("Update Analysis");
		updateData.addActionListener(bt);
		
		displayGraph = new JButton("Display Graph");
		//displayGraph.setPreferredSize(new Dimension(60,20));
		displayGraph.addActionListener(bt);
		
		dataDistribution = new JButton("Data Distribution");
		dataDistribution.addActionListener(bt);
		
		JPanel displayFix = new JPanel();
		//displayFix.setLayout(new GridLayout(3,1));
		displayFix.add(dummy);
		displayFix.add(displayGraph);
		displayFix.add(dataDistribution);
		
		JPanel textGroup = new JPanel();
		textGroup.setLayout(new GridLayout(2,1));
		textGroup.add(lowBound);
		textGroup.add(highBound);
		
		JPanel boundGroup = new JPanel();
		boundGroup.add(boundLabel);
		boundGroup.add(textGroup);
		boundGroup.add(updateBound);
		boundGroup.add(updateData);
		
		/*JPanel boundGroup2 = new JPanel();
		boundGroup2.setLayout(new GridLayout(3,1));
		boundGroup2.add(dummy);
		boundGroup2.add(boundGroup);*/
		
		GridBagConstraints c = new GridBagConstraints();
		JPanel group = new JPanel(new GridBagLayout());
		//group.setLayout(new BorderLayout());
		
		c.gridx = 0;
		c.gridy = 500;
		group.add(boundGroup, c);
		c.gridx = 600;
		group.add(displayFix, c);
		
		return group;
		
	}
	
	private JPanel errorGroup() {
		errorMessage = new JTextArea();
		errorMessage.setEditable(false);
		errorMessage.setLineWrap(false);
		errorMessage.setWrapStyleWord(true);
		errorMessage.setFont(new Font("Serif", 0, 18));
		errorMessage.setPreferredSize(new Dimension(1000, 50));
		JPanel errorPanel = new JPanel();
		errorPanel.setBorder(new TitledBorder("Error Log"));
		errorPanel.add(errorMessage);
		
		viewLog = new JButton("View log and report");
		viewLog.addActionListener(bt);
		
		JPanel group = new JPanel();
		//group.setLayout(new GridLayout(2,1));
		group.add(viewLog);
		group.add(errorPanel);
		
		return group;
	}
	
	private class ClickListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			errorMessage.setText("");
			errorMessage.setBackground(Color.WHITE);
			
			if(e.getSource() == newSet) {
				history += "Creating new data set\n";
				int approved = fc.showOpenDialog(null);
				
				if(approved == JFileChooser.APPROVE_OPTION) {
					count = 0;
					arrayMaxSize = 500;
					array = new int[arrayMaxSize];
					
					System.out.println("Approved: " + fc.getSelectedFile().getAbsolutePath());				
					inputData();
					boundUpdated = false;
					setCreated = true;
					
					System.out.println("Error log: " + fc.getCurrentDirectory() + "\\errorLog.txt");
					history += "\tNew data set added from " + fc.getSelectedFile().getAbsolutePath() + " successfully with " + count + " data records @ default boundary\n";
										
				} else {
					errorMessage.setText("Could not get file");
					errorMessage.setBackground(Color.RED);
					writeError("Could not get file\n");
					history += "\tFailed to create new data set\n";
				}
			}
			
			
			
			else if(e.getSource() == appendData) {
				boundUpdated = true;
				history += "Append new data\n";
				
				if(setCreated) {
				if(appendKeyboard.isSelected()) {  // append from keyboard
					history += "\tAppend data from keyboard input\n";
					if(!(addValue.getText().isEmpty())){
						int tempData = 0;
						boolean error = false;
						try {
							tempData = Integer.parseInt(addValue.getText().trim());
						} catch(NumberFormatException nfe) {
							errorMessage.setText("Adding value error");
							writeError("Adding value error\n");
							error = true;
							history += "\tFailed to append data due to invalid number\n";
						}
						
						if(!error) {
							if(tempData >= low && tempData <= high) {
								count ++;
								if(count >= arrayMaxSize)
									doubleArraySize();
								
								array[count-1] = tempData;
								//Arrays.sort(array);
								updateAnalysis();
								history += "\tAppended " + tempData + " successfully\n";
							} else {
								errorMessage.setText("Adding value out of boundary");
								errorMessage.setBackground(Color.RED);
								writeError("Adding value out of boundary\n");
								history += "\tFailed to append data due to out-of-bound value\n";
							}
						}
					} else {
						errorMessage.setText("Adding value empty");
						errorMessage.setBackground(Color.RED);
						writeError("Adding value empty\n");
						history += "\tFailed to append data due to empty adding value\n";
					}
					
					
				} else if(appendFile.isSelected()) {  // append from file
					System.out.println("Append from file");
					history += "\tAppending data from file\n";
					int approved = fc.showOpenDialog(null);
					
					if(approved == JFileChooser.APPROVE_OPTION) {
						System.out.println("Approved: " + fc.getSelectedFile().getAbsolutePath());
						int tempCount = count;
						appendFile();
						history += "\tData appended from " + fc.getSelectedFile().getAbsolutePath() + " successfully with " + (count-tempCount) + " records added\n";
					} else {
						errorMessage.setText("Could not get file");
						errorMessage.setBackground(Color.RED);
						writeError("Could not get file\n");
						history += "\tCould not get file. Failed to append data\n";
					}
					
				} else {
					errorMessage.setText("Append data option not chosen");
					errorMessage.setBackground(Color.RED);
					writeError("Append data option not chosen\n");
					history += "\tFailed to identify appending option\n";
					}
				} else {
					errorMessage.setText("Create new set before appending data.");
					errorMessage.setBackground(Color.RED);
					writeError("Create new set before appending data\n");
					history += "\tData set has not been created. Failed to append data\n";
				}
			}
			
			else if(e.getSource() == deleteData) {
				boundUpdated = true;
				System.out.println("Delete data");
				history += "Delete data\n";
				
				if(setCreated) {
				if(!(deleteValue.getText().isEmpty())) {
					try {
						int tempValue = Integer.parseInt(deleteValue.getText().trim());
						deleteArray(tempValue);					
					} catch (NumberFormatException nfe){
						errorMessage.setText("Deleting value invalid");
						errorMessage.setBackground(Color.RED);
						writeError("Deleting value invalid\n");
						history += "\tDeleting value invalid. Failed to delete data\n";
					}
				} else {
					errorMessage.setText("Deleting value empty");
					errorMessage.setBackground(Color.RED);
					writeError("Deleting value empty\n");
					history += "\tDeleting value empty. Failed to delete data\n";
				}
				} else {
					errorMessage.setText("Create new set before deleting data.");
					errorMessage.setBackground(Color.RED);
					writeError("Create new set before deleting data\n");
					history += "\tData set has not been created. Failed to delete data\n";
				}
			}
			
			
			else if(e.getSource() == updateBound) {
				history += "Update boundary\n";
				if(boundUpdated) {
					errorMessage.setText("Boundary can only be updated once before update analysis. \nCreate new set then update new boundary");
					errorMessage.setBackground(Color.RED);
					writeError("Boundary can only be updated once before update analysis.\nCreate new set then update new boundary\n");
					history += "\tFailed to update boundary. Boundary can only be updated once after creating new data set\n";
				}
				else
				
				if(!(lowBound.getText().isEmpty()) && !(highBound.getText().isEmpty())) {
					int tempLow = 0;
					int tempHigh = 0;
					boolean error = false;
					try {
						tempLow = Integer.parseInt(lowBound.getText().trim());
						tempHigh = Integer.parseInt(highBound.getText().trim());
					} catch(NumberFormatException nfe){
						errorMessage.setText("Bound number error");
						errorMessage.setBackground(Color.RED);
						writeError("Bound number error\n");
						history += "\tInvalid boundary numbers. Failed to update boundaries\n";
						error = true;
					}
					
					if(!error)
					if(tempLow > tempHigh) {
						errorMessage.setText("Low boundary value is higher than high boundary value");
						errorMessage.setBackground(Color.RED);
						writeError("Low boundary value is higher than high boundary value\n");
						history += "\tLower boundary is entered higher than upper boundary. Update boundaries failed\n";
					}
					else {  // execute
						low = tempLow;
						high = tempHigh;
						boundUpdated = true;
						history += "\tBoundaries updated successfully with " + tempLow + " and " + tempHigh + " boundaries\n";
						
						if(setCreated)
							inputData();
						//setCreated = false;
					}					
					
				} else {
					errorMessage.setText("Boundaries values empty");
					errorMessage.setBackground(Color.RED);
					writeError("Boundaries values empty\n");
					history += "Boundary values empty. Failed to update boundaries\n";
				}
			}
			
			
			else if(e.getSource() == displayGraph) {
				boundUpdated = true;
				System.out.println("Display graph");
				GraphFrame graphFrame = new GraphFrame(array, count);
				graphFrame.setTitle("Data Graph");
				graphFrame.setVisible(true);
			}
			
			else if(e.getSource() == dataDistribution) {
				boundUpdated = true;
				DistributionFrame distFrame = new DistributionFrame(array, count, high, low);
				distFrame.setTitle("Data Distribution");
				distFrame.setVisible(true);
			}
			
			
			else if(e.getSource() == viewLog) {
				boundUpdated = true;
				System.out.println("View log");
				errorMessage.setText("Error log saved to " + errorLog.getAbsolutePath());
				File report = new File(fc.getCurrentDirectory() + "\\ProgramReport.txt");
				OutputStream reportOS = null;
				try {
					reportOS = new FileOutputStream(report);
					reportOS.write("Grade Analysis Report Log\n\n".getBytes());
					reportOS.write(history.getBytes());
					reportOS.write("\n\n==================End of report===================\n".getBytes());
					reportOS.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				errorMessage.append("\nReport saved to " + report.getAbsolutePath());				
			}
			
			
			else if(e.getSource() == updateData) {
				boundUpdated = true;
				if(count > 0)
					updateAnalysis();
				else {
					errorMessage.setText("No data set added.\nCreate new data set before analysis");
					errorMessage.setBackground(Color.RED);
					writeError("No data set added.\nCreate new data set before analysis\n");
				}					
			}
			
			distribution.setText(history);
			
		}
	}
	
	private class focus implements FocusListener{
		public void focusGained(FocusEvent e) {
			if(e.getSource() == lowBound)
				lowBound.setText("");
			if(e.getSource() == highBound)
				highBound.setText("");
			if(e.getSource() == addValue)
				addValue.setText("");
			if(e.getSource() == deleteValue)
				deleteValue.setText("");
		}

		
		public void focusLost(FocusEvent e) {}
	}
	
	private void inputData() {
			
		try {
			scan = new Scanner(new BufferedReader(new FileReader(fc.getSelectedFile())));
			if(fc.getSelectedFile().getName().contains(".csv")) {
				System.out.println("CSV FILE DETECTED!");
				String inline = scan.nextLine();
				String [] temp = inline.split("¿");
				inline = temp[1]; // first line
				int index = 0;
				count = 0;
				
				String[] inToken = inline.split(",");
				for(int i=0; i<inToken.length; i++) {
					try {
						int tempIn = Integer.parseInt(inToken[i]);
						if(tempIn >= low && tempIn <= high) {
							array[index] = tempIn;
							index++;
							count++;
						}
					} catch (NumberFormatException nfe) {}
				}
				
				while(scan.hasNext()) {
					inline = scan.nextLine();					
					inToken = inline.split(",");

					if(count + inline.length() >= arrayMaxSize)
						doubleArraySize();
					
					for(int i=0; i<inToken.length; i++) {
						try {
							int tempIn = Integer.parseInt(inToken[i]);
							if(tempIn >= low && tempIn <= high) {
								array[index] = tempIn;
								index++;
								count++;
							}
						} catch (NumberFormatException nfe) {}
					}
					
				}
				
			}
			
			else if(fc.getSelectedFile().getName().contains(".txt")){
				int index = 0;
				count = 0;
				while(scan.hasNext()) {
					if(count >= arrayMaxSize)
						doubleArraySize();
					
					try {
						int temp = scan.nextInt();
						if(temp >= low && temp <= high) {
							array[index] = temp;
							index ++;
							count ++;
						}
					} catch (InputMismatchException e) {}
				}
			} else {
				errorMessage.setText("Invalid file format");
				errorMessage.setBackground(Color.RED);
				writeError("Invalid file format\n");
			}
			
		} catch (FileNotFoundException e1) {
			errorMessage.setText("File not found");
			errorMessage.setBackground(Color.RED);
			writeError("File not found\n");
		}
	}
	
	private void appendFile() {
		try {
			scan = new Scanner(new BufferedReader(new FileReader(fc.getSelectedFile())));
			
			if(fc.getSelectedFile().getName().contains(".csv")) {
				System.out.println("CSV FILE DETECTED!");
				String inline = null;
				if(scan.hasNext()) {
					inline = scan.nextLine();
					String [] temp = inline.split("¿");
					inline = temp[1]; // first line	
					
					// first line input
					String[] inToken = inline.split(",");
					for(int i=0; i<inToken.length; i++) {
						try {
							int tempIn = Integer.parseInt(inToken[i]);
							if(tempIn >= low && tempIn <= high) {
								array[count] = tempIn;
								count++;
							}
						} catch (NumberFormatException nfe) {}
					}
				} else {
					errorMessage.setText("Empty file");
					errorMessage.setBackground(Color.RED);
					writeError("Empty file input\n");
					return;
				}
				
				while(scan.hasNext()) {
					inline = scan.nextLine();
					String[] inToken = inline.split(",");
					
					if(count + inToken.length >= arrayMaxSize)
						doubleArraySize();
					
					for(int i=0; i<inToken.length; i++) {
						try {
							int tempIn = Integer.parseInt(inToken[i]);
							if(tempIn >= low && tempIn <= high) {
								array[count] = tempIn;
								count++;
							}
						} catch (NumberFormatException nfe) {}
					}
				}  // end while loop
			}
			
			else if(fc.getSelectedFile().getName().contains(".txt")){
				while(scan.hasNext()) {
					if(count >= arrayMaxSize)
						doubleArraySize();
					
					try {
						int temp = scan.nextInt();
						if(temp >= low && temp <= high) {
							count++;
							array[count-1] = temp;
						}
					} catch(InputMismatchException e) {}
				}
			} else {
				errorMessage.setText("Invalid file format");
				errorMessage.setBackground(Color.RED);
				writeError("Invalid file format\n");
			}
			
			//Arrays.sort(array);
		} catch (FileNotFoundException e) {
			errorMessage.setText("File not found");
			errorMessage.setBackground(Color.RED);
			writeError("File not found\n");
			
		}
	}
	
	private void deleteArray(int value) {
		int index;
		boolean found = false;
		for(index = 0; index < count; index++)
			if(array[index] == value) {
				found = true;
				break;
			}
		
		if(found) {
			history += "\t" + value + " successfully deleted from data set\n";
			for(int i=index; i<count; i++)
				array[i] = array[i+1];
			count--;
			updateAnalysis();
		}
		else {
			errorMessage.setText("Deleting value not found");
			writeError("Deleting value not found\n");
			history += "\tDeleting value not found. Failed to delete from data set\n";
		}
	}
	
	private void updateAnalysis() {
		int[] temp = sortArray(count);
		
		int dummy = 0;
		displayData.setText(temp[count-1] + "\t");
		for(int i=count-2; i>=0; i--) {
			if(dummy < 3) {
				displayData.append(temp[i] + "\t");
				dummy++;
			}
			else {
				displayData.append("\n" + temp[i] + "\t");
				dummy = 0;
			}
		}
		
		if(count > 0) {
			analysis();
			analysis.setText("Count: " + count);
			analysis.append("\nMean: " + mean);
			analysis.append("\nMedian: " + median);
			analysis.append("\nMode: " + mode);
			analysis.append("\nHigh: " + temp[count-1]);
			analysis.append("\nLow: " + temp[0]);
		}
	}
	
	private void analysis() {
		// mean
		int total = 0;
		for(int i=0; i<count; i++)
			total += array[i];
		mean = total/count;
		
		// median
		int [] temp = sortArray(count);
		if(count%2 != 0)
			median = temp[(count+1)/2];
		else
			median = (temp[count/2] + temp[(count+1)/2])/2;
		
		// mode
		int tempCount = 0, highestCount = 0;
		int tempMode = 0;
		for(int i=0; i<count-1; i++) {
			if(temp[i] == temp[i+1])
				tempCount ++;
			
			else {
				if(tempCount >= highestCount) {
					tempMode = temp[i];
					highestCount = tempCount;
				}
				tempCount = 0;
			}
		}
		
		// count highest value frequency
		tempCount = 0;
		for(int i=count-1; i>0; i--) {
			if(temp[i] == temp[i-1]) {
				tempCount++;
			} else
				break;
		}
		if(tempCount > highestCount)
			tempMode = temp[count-1];
		
		mode = tempMode;
	}
	
	private int[] sortArray(int count) {
		int [] ret = new int[count];
		for(int i =0; i<count; i++)
			ret[i] = array[i];
		Arrays.sort(ret);
		
		return ret;
	}
	
	private void doubleArraySize() {
		int[] temp = new int[arrayMaxSize];
		for(int i=0; i<count; i++) 
			temp[i] = array[i];
		
		arrayMaxSize *= 2;
		array = new int[arrayMaxSize];
		
		for(int i=0; i<count; i++)
			array[i] = temp[i];
	}
	
	private void writeError(String message) {
		try {
			os.write(message.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
