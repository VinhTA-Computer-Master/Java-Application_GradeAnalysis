import java.awt.Dimension;
import java.awt.Font;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.text.DecimalFormat;

public class DistributionFrame extends JFrame {
	private JTextArea data;
	private JScrollPane sp;
	private int[] array;
	private int count;
	private int highBound;
	private int lowBound;
	private double increment = 0.0;
	private int in1, in2, in3, in4, in5, in6, in7, in8, in9, in10 = 0;
	private int totalScores = 0;
	private double sum1, sum2, sum3, sum4, sum5, sum6, sum7, sum8, sum9, sum10 = 0.0;
	private double avg1, avg2, avg3, avg4, avg5, avg6, avg7, avg8, avg9, avg10 = 0.0;
	private double per1, per2, per3, per4, per5, per6, per7, per8, per9, per10 = 0.0;
	
	public DistributionFrame(int[] mainArray, int mainCount, int high, int low) {
		setSize(1000,600);
		array = mainArray;
		count = mainCount;
		highBound = high;
		lowBound = low;
		createPanel();
	}
	
	private void createPanel() {
		data = new JTextArea();
		sp = new JScrollPane(data, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		data.setEditable(false);
		data.setLineWrap(true);
		data.setWrapStyleWord(true);
		data.setFont(new Font("Serif", 0, 18));
		sp.setViewportView(data);
		sp.setPreferredSize(new Dimension(1200, 700));
		calculations();
		distributeData();
		
			
		JPanel main = new JPanel();
		main.add(sp);
		add(sp);
			
		repaint();
		
		}
	public void calculations()
	{
		increment = (highBound - lowBound)/10;
		for(int i = 0; i < count; i++)
		{
			if(array[i] >= lowBound && array[i] < lowBound+increment)
			{
				in1++;
				sum1 = sum1 + array[i];
			}
			if(array[i] >= lowBound+increment && array[i] < lowBound+increment*2)
			{
				in2++;
				sum2 = sum2 + array[i];
			}
			if(array[i] >= 2*increment+lowBound && array[i] < 3*increment+lowBound)
			{
				in3++;
				sum3 = sum3 + array[i];
			}
			if(array[i] >= 3*increment+lowBound && array[i] < 4*increment+lowBound)
			{
				in4++;
				sum4 = sum4 + array[i];
			}
			if(array[i] >= 4*increment+lowBound && array[i] < 5*increment+lowBound)
			{
				in5++;
				sum5 = sum5 + array[i];
			}
			if(array[i] >= 5*increment+lowBound && array[i] < 6*increment+lowBound)
			{
				in6++;
				sum6 = sum6 + array[i];
			}
			if(array[i] >= 6*increment+lowBound && array[i] < 7*increment+lowBound)
			{
				in7++;
				sum7 = sum7 + array[i];
			}
			if(array[i] >= 7*increment+lowBound && array[i] < 8*increment+lowBound)
			{
				in8++;
				sum8 = sum8 + array[i];
			}
			if(array[i] >= 8*increment+lowBound && array[i] < 9*increment+lowBound)
			{
				in9++;
				sum9 = sum9 + array[i];
			}
			if(array[i] >= 9*increment+lowBound && array[i] <= 10*increment+lowBound)
			{
				in10++;
				sum10 = sum10 + array[i];
			}
		}
		
		//getting the average for each increment
		avg1 = (in1 != 0)? sum1/in1:0;
		avg2 = (in2 != 0)? sum2/in2:0;
		avg3 = (in3 != 0)? sum3/in3:0;
		avg4 = (in4 != 0)? sum4/in4:0;
		avg5 = (in5 != 0)? sum5/in5:0;
		avg6 = (in6 != 0)? sum6/in6:0;
		avg7 = (in7 != 0)? sum7/in7:0;
		avg8 = (in8 != 0)? sum8/in8:0;
		avg9 = (in9 != 0)? sum9/in9:0;
		avg10 = (in10 != 0)? sum10/in10:0;
		
		//getting the percentage for each increment
		totalScores = in1+in2+in3+in4+in5+in6+in7+in8+in9+in10;
		
		per1 = (((double)in1/(double)totalScores));
		per2 = (((double)in2/(double)totalScores));
		per3 = (((double)in3/(double)totalScores));
		per4 = (((double)in4/(double)totalScores));
		per5 = (((double)in5/(double)totalScores));
		per6 = (((double)in6/(double)totalScores));
		per7 = (((double)in7/(double)totalScores));
		per8 = (((double)in8/(double)totalScores));
		per9 = (((double)in9/(double)totalScores));
		per10 = (((double)in10/(double)totalScores));
		

	}
	public void distributeData()
	{
		DecimalFormat df = new DecimalFormat("###.##");
		
		DecimalFormat p = new DecimalFormat("##.##%");
		
		int [] inc = {in1,in2,in3,in4,in5,in6,in7,in8,in9,in10};
		double [] avg = {avg1,avg2,avg3,avg4,avg5,avg6,avg7,avg8,avg9,avg10};
		double [] per = {per1,per2,per3,per4,per5,per6,per7,per8,per9,per10};
		
		
		data.setText("Distributed Data ");
		data.append(" \n");
		data.append(" \n");
		data.append(" \n");
		data.append("\t\t\t Increment\tAverage\tPercentage ");
		data.append(" \n");
		
		for(int i=0; i<10; i++)
			if(increment*i + lowBound < 100)
				data.append("Test Scores >= " + (int) (increment*i + lowBound) + " and < "+ (int) (increment*(i+1) + lowBound) + ": " + "\t\t " + inc[i] + "\t " + df.format(avg[i]) + "\t" + p.format(per[i]) + "\n");
			else
				data.append("Test Scores >= " + (int) (increment*i + lowBound) + " and < "+ (int) (increment*(i+1) + lowBound) + ": " + "\t " + inc[i] + "\t " + df.format(avg[i]) + "\t" + p.format(per[i]) + "\n");
		
	}
}
