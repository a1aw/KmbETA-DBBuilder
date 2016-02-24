package com.mob41.db.build;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;

public class BusDetails extends JPanel {

	private static final String[] colident = {"lat", "lng", "area", "subarea", "title_eng", "title_chi",
			"address_eng", "address_chi", "normal_fare", "air_cond_fare", "rel_bus"};
	public JTable busstoptable;
	public DefaultTableModel busstoptablemodel;
	
	/**
	 * Create the panel.
	 */
	public BusDetails() {
		setLayout(new BorderLayout(0, 0));
		JScrollPane busstopscroll = new JScrollPane();
		add(busstopscroll, BorderLayout.CENTER);
		
		busstoptablemodel = new DefaultTableModel();
		busstoptablemodel.setColumnIdentifiers(colident);
		busstoptable = new JTable(busstoptablemodel){

			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
			
		};
		busstoptable.setFont(new Font("MS Song", Font.PLAIN, 11));
		busstopscroll.setViewportView(busstoptable);

	}

}
