
public class TraceFileTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// some comment here
		TraceFile x = new TraceFile("c:\\aaa\\wth.txt");
		x.record("here is a trace line");
		
		x.closeFile();
	}

}
