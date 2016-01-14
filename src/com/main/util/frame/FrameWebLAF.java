package com.main.util.frame;

import javax.swing.JFrame;
import javax.swing.table.TableColumn;

import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.table.WebTable;
import com.alee.*;

public class FrameWebLAF {

	private JFrame frame = new JFrame();
	
	public FrameWebLAF(){
			
	}
	
	public void table(){
		String[] headers = { "Header 1", "Header 2", "Header 3", "Header 4", "Header 5", "Header 6" };
		
		WebTable table = new WebTable();
		
		WebScrollPane scrollPane = new WebScrollPane(table);
		
		TableColumn column = table.getColumnModel().getColumn(1);
		
	}

}
