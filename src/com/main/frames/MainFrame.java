package com.main.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.utils.swing.WebDefaultCellEditor;
import com.main.interfaces.Framer;
import com.main.util.frame.Frame;



public class MainFrame extends Frame implements Framer{

	/**
	 * Renders the main frame of the program.
	 * @author Brendan Kellam
	 */

	private String title = "Main";
	private int width = 700, height = 500;
	private boolean resizable = false;
	private LayoutManager layout = null;
	  
	//---Components---
	private JButton b = new JButton();
	
	public MainFrame(){
		init();
	}

	public void init() {
		WebLookAndFeel.install();
		generalConfig(title, width, height, resizable, JFrame.EXIT_ON_CLOSE, layout);
		
		
		b = button(100, 100, 140, 25, "Test");	
		
		 String[] headers = { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" };
	        String[][] data = { { "1", "2", "3", "4", "5", "6" }, { "7", "8", "9", "10", "11", "12" }, { "13", "14", "15", "16", "17", "18" },
	                { "19", "20", "21", "22", "23", "24" }, { "25", "26", "27", "28", "29", "30" }, { "31", "32", "33", "34", "35", "36" },
	                { "37", "38", "39", "40", "41", "42" }, { "43", "44", "45", "46", "47", "48" }, { "49", "50", "51", "52", "53", "54" } };

	    // Static table
	    WebTable table = new WebTable ( data, headers );
        table.setEditable ( false );
        table.setAutoResizeMode ( WebTable.AUTO_RESIZE_ALL_COLUMNS );
        table.setRowSelectionAllowed ( false );
        table.setColumnSelectionAllowed ( true );
        
        TableColumn column = table.getColumnModel ().getColumn (0);
        
        WebTableCellRenderer renderer = new WebTableCellRenderer ();
        column.setCellRenderer ( renderer );
   
        JLabel label = new JLabel(new ImageIcon(MainFrame.class.getResource("/img.png")));
        //column.setCellEditor (label);


        
		JScrollPane scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED); //never have a horizontal scrollbar
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); //have a vertical scroll bar when needed
		scroll.setViewportView(table);
		scroll.setBounds(10, 10, 600, 300);
		window.add(scroll); //add the scroll pane to the window
		
		SetBGColor(Color.LIGHT_GRAY);
		action();
	}

	public void hello(){
		System.out.println("hello");
	}
	
	public void action() {
		b.addActionListener(
				new java.awt.event.ActionListener(){
					public void actionPerformed(ActionEvent event) {
						hello();
					}
				}
		);
	}
}
