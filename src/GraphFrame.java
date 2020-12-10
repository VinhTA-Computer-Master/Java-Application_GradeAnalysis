import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GraphFrame extends JFrame {
	private JTextArea graph;
	private JScrollPane sp;
	private int[] array;
	private int count;
	
	public GraphFrame(int[] mainArray, int mainCount) {
		setSize(1300,800);
		array = mainArray;
		count = mainCount;
		createPanel();
	}
	
	private void createPanel() {
		graph = new JTextArea();
		sp = new JScrollPane(graph, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graph.setEditable(false);
		graph.setLineWrap(true);
		graph.setWrapStyleWord(true);
		graph.setFont(new Font("Serif", 0, 20));
		sp.setViewportView(graph);
		sp.setPreferredSize(new Dimension(1200, 700));
		drawGraph();
		
		JPanel main = new JPanel();
		main.add(sp);
		add(sp);
		
		repaint();
	}
	
	public void drawGraph() {
		graph.setText("DRAW HERE\n");
		
        int grade_over100 = 0;
        int grade_90to100 = 0 ;
        int grade_80to89 = 0;
        int grade_70to79 = 0;
        int grade_60to69 = 0;
        int grade_below60 = 0;
        
        for(int i = 0; i < count; i++){
            if(array[i] >= 100)
                grade_over100++;
            else if(array[i] >= 90 && array[i] < 100)
                grade_90to100++;
            else if(array[i] >= 80 && array[i] < 90)
                grade_80to89++;
            else if(array[i] >= 70 && array[i] < 80)
                grade_70to79++;
            else if(array[i] >= 60 && array[i] < 70)
                grade_60to69++;
            else if(array[i] < 60)
                grade_below60++;
            else
                System.out.println("Error Grade Input");
        }
        
        
        boolean scale = false;
    	if(grade_over100 > 100 || grade_90to100 > 100 || grade_80to89 > 100 || grade_70to79 > 100 || grade_60to69 > 100 || grade_below60 > 100)
    	{
    		grade_over100 = grade_over100/10;
    		grade_90to100 = grade_90to100/10;
    		grade_80to89  = grade_80to89 /10;
    		grade_70to79  = grade_70to79 /10;
    		grade_60to69  = grade_60to69 /10;
    		grade_below60 = grade_below60/10;
    		scale = true;
    	}

    	if(grade_over100 < 1 && grade_over100 > 0)
    		grade_over100 = 1;
    	if(grade_90to100 < 1 && grade_90to100 > 0)
    		grade_90to100 = 1;
    	if(grade_80to89 < 1 && grade_80to89 > 0)
    		grade_80to89  = 1;
    	if(grade_70to79 < 1 && grade_70to79 > 0)
    		grade_70to79  = 1;
    	if(grade_60to69 < 1 && grade_60to69 > 0)
    		grade_60to69  = 1;
    	if(grade_below60 < 1 && grade_below60 > 0)
    		grade_below60 = 1;
        
        
        graph.setText("X-Bar Graph for Student Grade:\n\n");
        
        //Bar Graph for students that own a grade more than 100%;
        graph.append("[100-100+]:\t");
        for(int i = 0; i < grade_over100; i++)
        	graph.append("X");
        graph.append("\n");
        
        //Bar Graph for students that own a grade from 90%-100%;
        graph.append("[90-99]:\t");
        for(int i = 0; i < grade_90to100; i++)
        	graph.append("X");
        graph.append("\n");
        
        //Bar Graph for students that own a grade from 80%-89%;
        graph.append("[80-89]:\t");
        for(int i = 0; i < grade_80to89; i++)
        	graph.append("X");
        graph.append("\n");
        
        //Bar Graph for students that own a grade from 70%-79%;
        graph.append("[70-79]:\t");
        for(int i = 0; i < grade_70to79; i++)
        	graph.append("X");
        graph.append("\n");
        
        //Bar Graph for students that own a grade from 60%-69%;
        graph.append("[60-69]:\t");
        for(int i = 0; i < grade_60to69; i++)
        	graph.append("X");
        graph.append("\n");
        
        //Bar Graph for students that own a grade below than 60%;
        graph.append("[Below 60]:\t");
        for(int i = 0; i < grade_below60; i++)
        	graph.append("X");
        graph.append("\n");
        
        if(scale)
        	graph.append("\nGraph scaled by 10");
	}

}
