package com.example.testybite;
import javax.swing.SwingUtilities;

public class Main {
	public static void name() {
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new Login();
	        }
	    });
	}
	
}

