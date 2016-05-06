package co.com.uniandes.main;

import java.io.File;
import java.net.URL;

import jpl.Query;

public class Main {

	public static void main(String[] args) {
		try {
			
			//"-Djava.library.path=/usr/bin/swipl";
			String t1 = "consult('" + getAbsoluteSrcPath() + File.separator + "baseprolog.pl')";
			System.out.println(getAbsoluteSrcPath());
			Query q1 = new Query(t1);
			System.out.println(t1 + " " + (q1.hasSolution() ? "succeeded" : "failed"));
			
			String t4 = "todos(X)";
			Query q4 = new Query(t4);
			System.out.println(q4.oneSolution().get("X"));
			System.out.println(q4.oneSolution().get("X").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getAbsoluteSrcPath() {
        URL url = Main.class.getResource("Main.class");
        File f = new File(url.getPath()).getParentFile();
        String srcPath = f.getAbsolutePath().replaceAll("%20", " ");
        return srcPath;
    }
}