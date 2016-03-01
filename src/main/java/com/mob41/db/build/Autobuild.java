package com.mob41.db.build;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	
	public static String[] bus_db = {
			"1", "1A", "10", "11", "11B", "11C", "11D", "11K", "11X", "12", "12A",
			"13D", "13M", "13P", "13S", "13X", "14", "14B", "14D", "14X", "15", "15A", "15P", "15X",
			"16", "16M", "16X", "17", "18", "108",  "2", "2A", "2B", "2D", "2E", "2F", "2X",
			"21", "23", "23M", "24", "26", "26M", "27", "28", "28B", "28S", "29M", "203C", "203E",
			"203S", "208", "211", "212", "215P", "215X", "216M", "219X", "224X", "230X", "234A",
			"234B", "234C", "234P", "234X", "235", "235M", "237A", "238M", "238P", "238S", "238X",
			"240X", "242X", "243M", "243P", "248M", "249M", "249X", "251A", "251B", "251M", "252B",
			"258D", "258P", "258S", "259B", "259C", "259D", "259E", "259X", "260B", "260C", "260X",
			"261", "261B", "261P", "263", "264R", "265B", "265M", "265S", "267S", "268B", "268C",
			"268P", "268X", "269A", "269B", "269C", "269D", "269M", "269P", "270", "270A", "270B",
			"270P", "270S", "271", "271P", "272A", "272K", "272P", "272S", "272X", "273", "273A",
			"273B", "273C", "273D", "273P", "273S", "274P", "275R", "276", "276A", "276B", "276P",
			"277E", "277P", "277X", "278K", "278P", "278X", "279X", "280X", "281A", "281B", "281M",
			"281X", "282", "283", "284", "286C", "286M", "286P", "286X", "287X", "288", "289K", "290",
			"290A", "292P", "296A", "296C", "296D", "296M", "297", "297P", "298E", "299X", 
			"3B", "3C", "3D", "3M", "3P", "30", "30X", "31", "31B", "31M", "32", "32M", "33A", "34",
			"34M", "35A", "35X", "36", "36A", "36B", "36M", "36X", "37", "37M", "38", "38A", "39A",
			"39M", "373",  "40", "40P", "40X", "41", "41A", "41M", "41P", "42", "42A", "42C",
			"42M", "43", "43A", "43B", "43C", "43M", "43P", "43X", "44", "44M", "45", "46", "46P", "46X",
			"47X", "48X", "49P", "49X",  "5", "5A", "5C", "5D", "5M", "5P", "5R", "5S", "51",
			"52X", "53", "54", "57M", "58M", "58P", "58X", "59A", "59M", "59X",  "6", "6C", "6D",
			"6F", "60M", "60X", "61M", "61X", "62X", "63X", "64K", "64S", "65K", "66M", "66X", "67M",
			"67X", "68A", "68E", "68F", "68M", "68X", "69C", "69M", "69P", "69X", "603", "603P", "603S",
			"673",  "7", "7B", "7M", "70K", "71A", "71B", "71K", "71S", "72", "72A", "72C", "72X",
			"73", "73A", "73K", "73X", "74A", "74B", "74C", "74D", "74K", "74P", "74X", "75K", "75P", "75X",
			"76K", "77K", "78K", "79K",  "8", "8A", "8P", "80", "80K", "80M", "80P", "80X", "81",
			"81C", "81K", "81S", "82B", "82C", "82K", "82P", "82X", "83A", "83K", "83S", "83X", "84M",
			"85", "85A", "85B", "85K", "85M", "85S", "85X", "86", "86A", "86C", "86K", "86S", "87B",
			"87D", "87K", "87P", "87S", "88K", "88X", "89", "89B", "89C", "89D", "89P", "89X", 
			"9", "91", "91M", "91P", "91R", "92", "93A", "93K", "93M", "94", "95", "95M", "96R", "98A",
			"98C", "98D", "98P", "98S", "99", "99R", "934", "934A", "935", "936", "960", "960A", "960B",
			"960P", "960S", "960X", "961", "961P", "968", "968X", "978", "978A", "978B",  "A31",
			"A33", "A33P", "A36", "A41", "A41P", "A43", "A43P", "A47",  "B1",  "E31",
			"E32", "E33", "E33P", "E34A", "E34B", "E34P", "E34X", "E41", "E42",  "N30", "N30P",
			"N30S", "N31", "N36", "N39", "N42", "N42A", "N42P", "N64", "N73", "N216", "N237", "N241",
			"N260", "N269", "N271", "N281", "N293", "N368", "NA33", "NA34",  "R33", "R42",
			 "S64", "S64C", "S64P", "S64X",  "T270", "T277",  "X42C"
	};
	private static List<List<List<String[]>>> data = new ArrayList<List<List<String[]>>>(bus_db.length);
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
	private JComboBox langbox;
	
	private void save(){
		try {
			lblStatus.setText("Status: Preparing for saving...");
			pb.setIndeterminate(true);
			int busamount = data.size();
			File file = new File("bus_stopdb.properties");
			if(!file.exists()){
				log.append("File not exist. Creating... \n");
				file.createNewFile();
			}
			Properties prop = new Properties();
			int b;
			int bd;
			int st;
			int boundamount;
			int stopamount;
			List<List<String[]>> buslist;
			List<String[]> boundlist;
			String busname;
			System.out.println(Arrays.deepToString(data.toArray()));
			prop.setProperty("buses", Integer.toString(busamount));
			pb.setValue(0);
			pb.setMinimum(0);
			pb.setMaximum(busamount);
			for (b = 0; b < busamount; b++){
				pb.setValue(b + 1);
				buslist = data.get(b);
				busname = bus_db[b];
				boundamount = buslist.size();
				lblStatus.setText("Status: Saving data for bus " + busname);
				prop.setProperty(busname + "-bounds", Integer.toString(boundamount));
				for (bd = 1; bd <= boundamount; bd++){
					boundlist = buslist.get(bd - 1);
					stopamount = boundlist.size();
					prop.setProperty(busname + "-bound" + bd + "-stops", Integer.toString(stopamount));
					for (st = 0; st < stopamount; st++){
						prop.setProperty(busname + "-bound" + bd + "-stop" + st + "-stopcode", boundlist.get(st)[1]);
						prop.setProperty(busname + "-bound" + bd + "-stop" + st + "-stopseq", boundlist.get(st)[2]);
						prop.setProperty(busname + "-bound" + bd + "-stop" + st + "-stopname", boundlist.get(st)[3]);
					}
				}
			}
			FileOutputStream out = new FileOutputStream(file);
			prop.store(out, "KMB Bus Stops Database (Automatically generated)");
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
					String lastbuild = prop.getProperty("lastbuild");
					String lastbound = prop.getProperty("lastbound");
					String lastmaxbounds = prop.getProperty("lastmaxbounds");
					lblBusstopdbproperties.setText("bus_stopdb.properties (Last Build: " + lastbuild + " Last Bound: " + lastbound + " Last Max Bounds: " + lastmaxbounds + ")");
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
	}
	
	private JSONArray search(String bn, int dir) throws Exception{
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
	    	search(bn, dir);
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
	}
	
	private void func(int lang){
		int i;
		int j;
		int x;
		try {
			running = true;
			lblStatus.setText("Setting up");
			String language = "eng";
			switch (lang){
			default:
			case 0:
				language = "eng";
				break;
			case 1:
				language = "chi";
				break;
			}
			JSONArray arr;
			String busno;
			String[] busrow;
			BusDetails busd;
			langbox.setEnabled(false);
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
						build[0] = Integer.toString(arr.length());
						build[1] = injson.getString("subarea").replaceAll("[-+.^:,]","");
						build[2] = Integer.toString(x);
						build[3] = injson.getString("title_" + language);
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
						boundlist.add(build);
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
			langbox.setEnabled(true);
			btnAutobuild.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Thank you for using this builder!\nPlease put a star on KmbETA-API and\nKmbETA-DBBuilder to support me!");
			running = false;
		} catch (Exception e){
			e.printStackTrace();
			running = false;
			langbox.setEnabled(true);
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
		frame.setTitle("KMB ETA Database Builder v1.5.1 Beta");
		frame.setBounds(100, 100, 958, 654);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblKmbEtaDatabase = new JLabel("KMB ETA Database Builder v1.5 Beta");
		lblKmbEtaDatabase.setFont(new Font("Tahoma", Font.PLAIN, 33));
		
		btnAutobuild = new JButton("Auto-build");
		btnAutobuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			  new Thread(){
					public void run(){
						func(langbox.getSelectedIndex());
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
		
		langbox = new JComboBox();
		langbox.setModel(new DefaultComboBoxModel(new String[] {"ENGLISH", "CHINESE"}));
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
							.addComponent(lblKmbEtaDatabase, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(langbox, 0, 94, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAutobuild, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblKmbEtaDatabase)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(langbox, Alignment.LEADING)
							.addComponent(btnSave, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnAutobuild, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pb, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
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
		
		lblBusstopdbproperties = new JLabel("bus_stopdb.properties");
		lblBusstopdbproperties.setFont(new Font("Tahoma", Font.BOLD, 15));
		splitPane.setRightComponent(lblBusstopdbproperties);
		
		log = new JTextArea();
		log.setWrapStyleWord(true);
		log.setEditable(false);
		log.setLineWrap(true);
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
