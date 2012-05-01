// File backup project.
// greg kelley 12-2011
// given an input location and an output loc, copy all files that
// do not exist or that have changed

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GakBackup {

	private static TraceFile x;
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in );
		
		x = new TraceFile("c:\\bbb\\wth.txt");
		System.out.println("Enter from directory name: ");
		String fromDir = new String(input.nextLine());

		System.out.println("Enter destination directory name: ");
		String destDir = new String(input.nextLine());

		// verify that the destination is a Directory. If it does not exist, 
		// create it.
		if( verifyDir(destDir))
			try {
				copyDir(fromDir, destDir );
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
				System.out.println("copyDir threw up an exception.");
			} // then copy files over.
		
		x.closeFile();  // close the trace file
	}
	
	// verify that the destination dir exists and if not, create it.
	public static boolean verifyDir(String dir){
		File dirname = new File(dir);
		if(dirname.isDirectory()) {
			System.out.printf("%s is a Directory\n", dirname.toString());
			x.record(dir + " is a Directory");
			return true;
		}
		
		// if the dir does not exist, can we create it?
		if(!dirname.exists()){
			if (dirname.mkdir()){
				System.out.printf("Created %s\n", dirname.toString());
				x.record(dir + " created.");
			}
			else {
				System.out.printf("FAIL! Creating %s\n", dirname.toString());
				x.record("Failed to create " + dir + " !!");
				return false;
			}
		}
		return true;
	}
	
	// Assume - we are given a directory to copy -- NOT a file.
	// so need to get a list of files from the directory and call copy for each one.
	public static void copyDir(String fromDir, String destDir ) throws IOException {
		System.out.printf("copyDir: From: %s\tTo: %s\n", fromDir, destDir);
		x.record("copy: " + fromDir + " to " + destDir);
		
		File fname = new File(fromDir);
		// verify that source location exists
		if( fname.exists()) {
			if( fname.isFile()) {  // is it a file?
				// special case - a file is input as source instead of directory. Don't know
				// why someone would do this but best to be safe.
				String fullFileName = fname.getPath() + "\\" + fromDir;
				String destFileName = destDir + "\\" + fromDir;
				copyFile(fullFileName, destFileName);
			} 
			else if ( fname.isDirectory()) {
				// need to create this sub-dir and then recursively call our self.
				// have - x:\destDir
				// need - x:\destDir\subDir
				//
				// copy each item in the directory to new location
				
				// get a list of the from Directory's files and sub-Dirs
				String[] dirList = fname.list();
				
				for( String dirName: dirList ){
					String fullFileName = fname.getPath() + "\\" + dirName;
					String destFileName = destDir + "\\" + dirName;
					
					File tname = new File(fullFileName);
					if(tname.isDirectory()) {
						verifyDir(destFileName); // create dest dir if !exist
						copyDir(fullFileName, destFileName);
					} else {
						copyFile(fullFileName, destFileName);
					}
				}
			}
		}
		else {
			// source does not exist... 
			System.out.printf("Source %s does not exist. \n", fromDir);
		}
	}
	
	// copy a given file to a destination dir if the file has changed.
	public static void copyFile(String fromFile, String toFile) throws IOException {
		File src = new File(fromFile);
		File dest = new File(toFile);
		Path pSrc = src.toPath();
		Path pDest = dest.toPath();
		if(!dest.exists()){
			Files.copy(pSrc, pDest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		} else if(src.lastModified() > dest.lastModified()) {
			Files.copy(pSrc, pDest, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
		}
		else {
			System.out.printf("%s exists and is up to date", toFile);
			x.record(toFile + " exists and is up to date.");
		}
	}
}
