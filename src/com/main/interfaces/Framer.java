package com.main.interfaces;

import java.awt.Dimension;

import javax.swing.JFrame;


/**
 * The Framer interface is to be used alongside a class extending Frame.java
 * The Structure of a Frame is:
 *    - A initialization method
 *    - A action listener method
 *    - (Followed by a list of action methods)
 * @author Brendan Kellam
 */
public interface Framer {
	
	/**
	 * Initializes the given Frame.
	 * Accepts: Nil, Returns: Nil
	 */
	void init();
	
	/**
	 * Holds all action listeners for the given class.
	 * Accepts: Nil, Returns: Nil
	 */
	void action();

		
}
