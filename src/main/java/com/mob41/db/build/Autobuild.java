package com.mob41.db.build;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Autobuild  {
	
	private static final String busesdb = "http://etadatafeed.kmb.hk:1933/GetData.ashx?type=ETA_R";
	private static final String routedb = "http://www.kmb.hk/ajax/getRouteMapByBusno.php";

	private boolean running = false;
	private Thread thread;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		start();
	}
	
	public static void start(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Autobuild window = new Autobuild();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static final String[] colident = {"Bus", "Bounds"};
	
	public static String[] bus_db;
	private static List<List<List<String[]>>> data;
	private static final int ENGLISH_LANG = 0;
	private static final int CHINESE_LANG = 1;

	private DefaultTableModel totalbustablemodel;
	private JTextArea log;
	private JTextPane properties;

	private JButton btnAutobuild;
	private JTable totalbustable;
	private JProgressBar pb;
	private JLabel lblStatus;
	private JTabbedPane tab;
	private JLabel lblBusstopdbproperties;
	private JButton btnSave;
	private JMenuBar menuBar;
	private JMenu mnBuilder;
	private JMenuItem mntmSwitchToManual;
	
	private void save(){
		try {
			lblStatus.setText("Status: Preparing for saving...");
			pb.setIndeterminate(true);
			
			if (data == null){
				lblStatus.setText("Status: Nothing to save. Ready.");
				pb.setIndeterminate(false);
				btnSave.setEnabled(true);
				btnAutobuild.setEnabled(true);
				return;
			}
			
			int busamount = data.size();
			
			File file = new File("kmbeta_db.json");
			if(!file.exists()){
				log.append("File not exist. Creating... \n");
				file.createNewFile();
			}
			
			JSONObject outjson = new JSONObject();
			
			int b;
			int bd;
			int st;
			int boundamount;
			int stopamount;
			
			List<List<String[]>> buslist;
			List<String[]> boundlist;
			
			String busname;
			
			pb.setValue(0);
			pb.setMinimum(0);
			pb.setMaximum(busamount);
			
			JSONArray jsonBuses = new JSONArray();
			outjson.put("buses", jsonBuses);
			JSONObject jsonBusData;
			JSONArray jsonBusBounds;
			JSONObject jsonBoundData;
			JSONArray jsonBoundStops;
			JSONObject jsonStopData;
			for (b = 0; b < busamount; b++){
				jsonBusData = new JSONObject();
				pb.setValue(b + 1);
				buslist = data.get(b);
				busname = bus_db[b];
				boundamount = buslist.size();
				lblStatus.setText("Status: Saving data for bus " + busname);
				jsonBusData.put("name", busname);
				jsonBusBounds = new JSONArray();
				jsonBusData.put("bounds", jsonBusBounds);
				for (bd = 1; bd <= boundamount; bd++){
					boundlist = buslist.get(bd - 1);
					stopamount = boundlist.size();
					jsonBoundData = new JSONObject();
					jsonBoundStops = new JSONArray();
					jsonBoundData.put("stops", jsonBoundStops);
					for (st = 0; st < stopamount; st++){
						jsonStopData = new JSONObject();
						jsonStopData.put("lat", boundlist.get(st)[0]);
						jsonStopData.put("lng", boundlist.get(st)[1]);
						jsonStopData.put("area", boundlist.get(st)[2]);
						jsonStopData.put("stopcode", boundlist.get(st)[3].replaceAll("[-+.^:,]",""));
						jsonStopData.put("stopname-eng", boundlist.get(st)[4]);
						jsonStopData.put("stopname-chi", new String(boundlist.get(st)[5].getBytes("Big5"), "Big5"));
						jsonStopData.put("addr-eng", boundlist.get(st)[6]);
						jsonStopData.put("addr-chi", new String(boundlist.get(st)[7].getBytes("Big5"), "Big5"));
						jsonStopData.put("normal-fare", boundlist.get(st)[8]);
						jsonStopData.put("air-cond-fare", boundlist.get(st)[9]);
						jsonStopData.put("stopseq", Integer.toString(st));
						jsonBoundStops.put(jsonStopData);
					}
					jsonBusBounds.put(jsonBoundData);
				}
				jsonBuses.put(jsonBusData);
			}
			
			JSONArray jsonRoutes = new JSONArray();
			outjson.put("routes", jsonRoutes);
			for (int i = 0; i < bus_db.length; i++){
				jsonRoutes.put(bus_db[i]);
			}
			
			PrintWriter out = new PrintWriter(file);
			out.println(outjson.toString());
			out.flush();
			out.close();
			reloadFile();
			pb.setIndeterminate(false);
			lblStatus.setText("Status: Ready");
			btnSave.setEnabled(true);
			btnAutobuild.setEnabled(true);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void reloadFile(){
		new Thread(){
			public void run(){
				try {
					btnSave.setEnabled(false);
					btnAutobuild.setEnabled(false);
					pb.setIndeterminate(true);
					lblStatus.setText("Status: Reloading properties file...");
					File file = new File("bus_stopdb.properties");
					if (!file.exists()){
						properties.setText("File Not Exist");
						lblStatus.setText("Status: Ready");
						btnSave.setEnabled(true);
						btnAutobuild.setEnabled(true);
						pb.setIndeterminate(false);
						return;
					}
					FileInputStream fis = new FileInputStream(file);
					FileInputStream in = new FileInputStream(file);
					StringBuilder builder = new StringBuilder();
					int ch;
					while((ch = fis.read()) != -1){
					    builder.append((char)ch);
					}
					properties.setText(builder.toString());
					Properties prop = new Properties();
					prop.load(in);
					fis.close();
					in.close();
					pb.setIndeterminate(false);
					lblStatus.setText("Status: Ready");
					btnSave.setEnabled(true);
					btnAutobuild.setEnabled(true);
				} catch (Exception e){
					e.printStackTrace();
					properties.setText("Error occurred.");
				}
			}
		}.start();
	}
	
	private void printlnLog(String log){
		this.log.append(log + "\n");
		System.out.println(log);
	}
	
	private JSONArray search(String bn, int dir) throws Exception{
		try {
			URL url = new URL(routedb);
		    URLConnection conn = url.openConnection();
		    conn.setDoOutput(true);

		    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		    writer.write("bn=" + bn + "&dir=" + dir);
		    writer.flush();
		    String line;
		    String data = "";
		    try {
		    	BufferedReader reader = new BufferedReader(new 
		                InputStreamReader(conn.getInputStream()));
			    while ((line = reader.readLine()) != null) {
			        data += line;
			     }
		    } catch (Exception e){
		    	lblStatus.setText("Status: Connection error. Restarting the same proccess in 30 sec (Don't worry it is okay)");
		    	System.err.println("Error occurred. Restarting the same proccess in 30 seconds.");
		    	Thread.sleep(30000);
		    	lblStatus.setText("Status: Retrying...");
		    	return search(bn, dir);
		    }
		    if (data == ""){
		    	return null;
		    }
		    data = data.substring(2, data.length());
		    data = data.substring(0, data.length() - 2);
		    data = "[" + data + "]";
		    if (data.equals("[]")){
		    	return null;
		    }
			return new JSONArray(data);
		} catch (Exception e){
			printlnLog("Error occurred! However, I am trying to recover! 30 seconds restarting");
			lblStatus.setText("Status: Connection error. Restarting the same proccess in 30 sec (Don't worry it is okay)");
	    	System.err.println("Error occurred. Restarting the same proccess in 30 seconds.");
	    	Thread.sleep(30000);
	    	lblStatus.setText("Status: Retrying...");
			return search(bn, dir);
		}
	}
	
	private void getRoutes() throws IOException{
		printlnLog("Downloading Routes...");
		URL url = new URL(busesdb);
	    URLConnection conn = url.openConnection();
	    
	    String line;
	    String data = "";
	    try {
	    	BufferedReader reader = new BufferedReader(new 
	                InputStreamReader(conn.getInputStream()));
		    while ((line = reader.readLine()) != null) {
		        data += line;
		     }
	    } catch (Exception e){
	    	JOptionPane.showMessageDialog(frame, "Opps... could not get buses db! App will now close now!", "Error", JOptionPane.OK_OPTION);
	    	System.exit(0);
	    	return;
	    }
	    
	    if (data == ""){
	    	System.out.println(data);
	    	JOptionPane.showMessageDialog(frame, "Opps... no bus db is fetched! App will now close now!", "Error", JOptionPane.OK_OPTION);
	    	System.exit(0);
	    	return;
	    }
	    printlnLog("Downloaded. Decoding...");
	    JSONArray arr = new JSONArray(data);
	    printlnLog("Separating Routes...");
	    bus_db = separate(arr.getJSONObject(0).getString("r_no"));
    	printlnLog("Separated Routes!");
		return;
	}
	
	private String[] separate(String stringarray){
		int tmp1 = 0;
		int tmp2 = 0;
		List<String> list = new ArrayList<String>(500);
		for (int i = 0; i < stringarray.length(); i++){
			if (i == stringarray.length() || stringarray.charAt(i) == ','){
				tmp2 = i;
				list.add(stringarray.substring(tmp1, tmp2));
				tmp1 = i + 1;
			}
		}
		
		String[] output = new String[list.size()];
		for (int i = 0; i < list.size(); i++){
			output[i] = list.get(i);
		}
		return output;
	}
	
	private void func(){
		try {
			lblStatus.setText("Fetching bus db...");
			getRoutes();
			lblStatus.setText("Fetched!");
		} catch (IOException e1) {
			lblStatus.setText("Error fetching db! Restart the app!");
			e1.printStackTrace();
			return;
		}
		data = new ArrayList<List<List<String[]>>>(bus_db.length);
		int i;
		int j;
		int x;
		try {
			running = true;
			lblStatus.setText("Setting up");
			JSONArray arr;
			String busno;
			String[] busrow;
			BusDetails busd;
			List<List<String[]>> buslist;
			lblStatus.setForeground(Color.BLACK);
			pb.setIndeterminate(false);
			pb.setValue(0);
			pb.setMinimum(0);
			pb.setMaximum(bus_db.length);
			for (i = 0; i < bus_db.length; i++){
				pb.setValue(i + 1);
				busrow = new String[2];
				busno = bus_db[i];
				lblStatus.setText("Status: Preparing database... " + busno);
				buslist = new ArrayList<List<String[]>>(bus_db.length);
				List<String[]> boundlist;
				printlnLog("Getting data for " + busno + "...");
				String[] busdarr;
				for (j = 1; j <= 2; j++){
					lblStatus.setText("Status: Downloading " + busno + " data on bound " + j + " (Missing " + (bus_db.length - i + 1) + " entries)");
					boundlist = new ArrayList<String[]>(2);
					printlnLog("Trying to get bound " + j +" for " + busno + "...");
					arr = search(busno, j);
					if (arr == null){
						printlnLog("Invaild bound " + j +" for " + busno + " !");
						if (j == 1){
							System.err.println("ERR: No data is grant " + j +" for " + busno + "...");
							printlnLog("ERR: No data is grant " + j +" for " + busno + "...");
							j = 0;
							boundlist.add(null);
							break;
						}
						break;
					}
					busd = new BusDetails();
					tab.add(busno + " B" + j, busd);
					String[] build;
					JSONObject injson;
					for (x = 0; x < arr.length(); x++){
						lblStatus.setText("Status: Building " + busno + " stop " + x);
						build = new String[4];
						busdarr = new String[11];
						injson = arr.getJSONObject(x);
						busdarr[0] = injson.getString("lat");
						busdarr[1] = injson.getString("lng");
						busdarr[2] = injson.getString("area");
						busdarr[3] = injson.getString("subarea");
						busdarr[4] = injson.getString("title_eng");
						busdarr[5] = injson.getString("title_chi");
						busdarr[6] = injson.getString("address_eng");
						busdarr[7] = injson.getString("address_chi");
						busdarr[8] = injson.getString("normal_fare");
						busdarr[9] = injson.getString("air_cond_fare");
						busdarr[10] = injson.getString("rel_bus");
						busd.busstoptablemodel.addRow(busdarr);
						busd.busstoptablemodel.fireTableDataChanged();
						boundlist.add(busdarr);
					}
					buslist.add(boundlist);
				}
				busrow[0] = bus_db[i];
				busrow[1] = Integer.toString(j - 1);
				totalbustablemodel.addRow(busrow);
				data.add(buslist);
			}
			lblStatus.setText("Status: Building database... this may takes one to two minutes");
			save();
			lblStatus.setText("Status: Ready");
			btnAutobuild.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Thank you for using this builder!\nPlease put a star on KmbETA-API and\nKmbETA-DBBuilder to support me!");
			running = false;
		} catch (Exception e){
			e.printStackTrace();
			running = false;
			lblStatus.setForeground(Color.RED);
			lblStatus.setText("Status: Error occurred. Check stack trace.");
			pb.setIndeterminate(true);
		}
	}

	public Autobuild() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (running){
					int result = JOptionPane.showConfirmDialog(null, "You are downloading database data. You will lost your data\nif you leave. Are you sure?");
					if (result == 0){
						System.exit(0);
					}
				}
				else
				{
					System.exit(0);
				}
			}
		});
		frame.setTitle("KMB ETA Database Builder v1.5.2 Beta");
		frame.setBounds(100, 100, 958, 654);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblKmbEtaDatabase = new JLabel("KMB ETA Database Builder v2.0.0 Beta");
		lblKmbEtaDatabase.setFont(new Font("Tahoma", Font.PLAIN, 33));
		
		btnAutobuild = new JButton("Auto-build");
		btnAutobuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  new Thread(){
					public void run(){
						func();
					}
				}.start();
				btnAutobuild.setEnabled(false);
				btnSave.setEnabled(false);
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JScrollPane scrollPane = new JScrollPane();
		
		lblStatus = new JLabel("Status: Ready");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		pb = new JProgressBar();
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Thread(){
					public void run(){
						save();
					}
				}.start();
				btnSave.setEnabled(false);
				btnAutobuild.setEnabled(false);
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(pb, GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
						.addComponent(lblStatus, GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblKmbEtaDatabase, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAutobuild, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblKmbEtaDatabase)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnSave, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnAutobuild, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		tab = new JTabbedPane(JTabbedPane.BOTTOM);
		scrollPane.setViewportView(tab);
		
		totalbustablemodel = new DefaultTableModel();
		totalbustablemodel.setColumnIdentifiers(colident);
		
		JScrollPane totalbusscroll = new JScrollPane();
		tab.addTab("Main Tab", null, totalbusscroll, null);
		totalbustable = new JTable(totalbustablemodel){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};

		totalbusscroll.setViewportView(totalbustable);
		
		
		properties = new JTextPane();
		scrollPane_2.setViewportView(properties);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		scrollPane_2.setColumnHeaderView(splitPane);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				reloadFile();
			}
		});
		splitPane.setLeftComponent(btnReload);
		
		lblBusstopdbproperties = new JLabel("kmbeta_db.json");
		lblBusstopdbproperties.setFont(new Font("Tahoma", Font.BOLD, 15));
		splitPane.setRightComponent(lblBusstopdbproperties);
		
		log = new JTextArea();
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		log.setEditable(false);
		pb.setStringPainted(true);
		scrollPane_1.setViewportView(log);
		
		JLabel lblLogs = new JLabel("Logs");
		scrollPane_1.setColumnHeaderView(lblLogs);
		panel.setLayout(gl_panel);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		mnBuilder = new JMenu("Builder");
		menuBar.add(mnBuilder);
		
		mntmSwitchToManual = new JMenuItem("Switch to Manual Mode");
		mntmSwitchToManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!running){
					Maunalbuild.start();
					frame.dispose();
				} else {
					int result = JOptionPane.showConfirmDialog(null, "You are downloading database data. You will lost your data\nif you leave. Are you sure?");
					if (result == 0){
						Maunalbuild.start();
						frame.dispose();
					}
				}
			}
		});
		mnBuilder.add(mntmSwitchToManual);
		reloadFile();
	}
}
