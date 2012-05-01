// gak 12-2011
// write trace information to a file.
// 
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.io.IOException;

public class TraceFile {

	private Formatter tFile;
	
	public TraceFile() {
		// no arg constructor - call self with a name of a file to open.
		this("c:\\gakbackuptrace.txt");
	}
	public TraceFile(String fname){
		// 
		openFile(fname);
	}
	
	public void openFile(String fname){
		// open the trace file for writing. 
		try
		{
			tFile = new Formatter(fname);
		}
		catch(SecurityException se)
		{
			System.err.printf("Error opening file %s\n", fname);
			System.exit(1);
		}
		catch( FileNotFoundException fnfe)
		{
			System.err.printf("Error opening or creating file %s\n", fname);
			System.exit(1);
		}
	}
	public void record(String stuff){
		try {
			tFile.format("%s\n", stuff);
		}
		catch(FormatterClosedException fce) {
			System.err.printf("Error printing to file %s\n", tFile.out().toString());
			return;
		}
	}
	
	public void closeFile() {
		if (tFile != null ) tFile.close();
	}
}
