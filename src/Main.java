import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class Main {
	private static JFileChooser fc = new JFileChooser(new File("./"));
	private static File errorLog = new File("");
	
	public static void main(String[] args) throws IOException {		
		
		System.out.println("Select working location, where the data file input and error log output should be.\n");
		int approved = fc.showOpenDialog(null);
		errorLog = new File(fc.getCurrentDirectory() + "\\errorLog.txt");
		
		Frame f = new Frame(fc, errorLog, approved);
		f.setTitle("Grade Analysis");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
