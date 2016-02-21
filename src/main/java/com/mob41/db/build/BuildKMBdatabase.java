package com.mob41.db.build;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;

public class BuildKMBdatabase {

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
	
	private JFrame frame;
	private int busamount = 0;;
	private List<String[]> stopcode = new ArrayList<String[]>(bus_db.length);
	private JTextArea log;
	private JTextArea html;
	private JTextField busname;
	private JTextArea properties;
	private JCheckBoxMenuItem chckbxClearHTML;
	private JSpinner spinner;
	private JToggleButton btnbound;
	private JCheckBoxMenuItem chckbxBoundsClean;
	private JLabel lblBusstopdbproperties;
	private void save(String data, String busname, int bounds, boolean boundmode){
		try {
			if (busname.isEmpty()){
				JOptionPane.showMessageDialog(null, "You must enter the bus name!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (bounds <= 0){
				JOptionPane.showMessageDialog(null, "Bounds mustn't be less than or equal to 0", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			stopcode = new ArrayList<String[]>(bus_db.length);
			Document doc = Jsoup.parse(data);
			Elements listbusstop = doc.select("ul[class=list-busstop] li");
			
			log.append("Loading from file... \n");
			File file = new File("bus_stopdb.properties");
			if(!file.exists()){
				log.append("File not exist. Creating... \n");
				file.createNewFile();
				busamount = -1;
			}
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(file);
			if (busamount > -1){
				log.append("File do exist. Loading from file... \n");
				prop.load(in);
				busamount = Integer.parseInt(prop.getProperty("buses"));
				log.append("Amount of buses: " + busamount + "\n");
			} else {
				busamount = 0;
			}
			if (busamount < bus_db.length){
				log.append("!!WARN!! Database is not complete. Missing " + (bus_db.length - busamount) + " entries! \n");
				System.err.println("Missing: " + (bus_db.length - busamount));
			}
			if (!boundmode){
				int newamount = busamount + 1;
				prop.setProperty("buses", Integer.toString(newamount));
				log.append("New entries: " + newamount);
			}
			prop.setProperty(busname + "-bounds", Integer.toString(bounds));
			Element listbus;
			String stopcode;
			for (int i = 0; i < listbusstop.size(); i++){
				listbus = listbusstop.get(i);
				stopcode = listbus.attr("data-busstop").replaceAll("[-+.^:,]","");
				prop.setProperty(busname + "-bound" + listbus.attr("data-bound") + "-stops", Integer.toString(listbusstop.size()));
				prop.setProperty(busname + "-bound" + listbus.attr("data-bound") + "-stop" + i + "-stopcode", stopcode);
				prop.setProperty(busname + "-bound" + listbus.attr("data-bound") + "-stop" + i + "-stopseq", listbus.attr("data-stopseq"));
				prop.setProperty(busname + "-bound" + listbus.attr("data-bound") + "-stop" + i + "-stopname", listbus.ownText());
			}
			prop.setProperty("lastbuild", busname);
			prop.setProperty("lastmaxbounds", Integer.toString(bounds));
			prop.setProperty("lastbound", listbusstop.first().attr("data-bound"));
			FileOutputStream out = new FileOutputStream(file);
			prop.store(out, "KMB Bus Stops Database");
			reloadFile();
			if (chckbxClearHTML.isSelected()){
				html.setText("");
			}
			if (chckbxBoundsClean.isSelected() && bounds == Integer.parseInt(listbusstop.first().attr("data-bound"))){
				spinner.setValue(2);
				this.busname.setText("");
				btnbound.setSelected(false);
				this.busname.grabFocus();
				return;
			}
			if (bounds > 1 && !boundmode){
				btnbound.setSelected(true);
				boundmode = true;
			}
			html.grabFocus();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void testLoad(String busname){
		try {
			if (busname.isEmpty()){
				JOptionPane.showMessageDialog(null, "You must enter the bus name!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			stopcode = new ArrayList<String[]>(bus_db.length);
			
			log.append("Loading from file... \n");
			File file = new File("bus_stopdb.properties");
			if(!file.exists()){
				log.append("File not exist. Creating... \n");
				file.createNewFile();
				busamount = -1;
			}
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(file);
			if (busamount > -1){
				log.append("File do exist. Loading from file... \n");
				prop.load(in);
				busamount = Integer.parseInt(prop.getProperty("buses"));
				log.append("Amount of buses: " + busamount + "\n");
				String[] buildarr = new String[4];
				int stops;
				int bounds;
				for (int i = 0; i < busamount; i++){
					buildarr[0] = prop.getProperty(bus_db[i] + "-stops");
					bounds =  Integer.parseInt(prop.getProperty(bus_db[i] + "-bounds"));
					for (int b = 0; b < bounds; b++){
						stops = Integer.parseInt(prop.getProperty(bus_db[i] + "-bound" + b + "-stops"));
						for (int j = 0; j < stops; j++){
							buildarr[1] = prop.getProperty(bus_db[i] + b + "-stop" + j + "-stopcode");
							buildarr[2] = prop.getProperty(bus_db[i] + b + "-stop" + j + "-stopseq");
							buildarr[3] = prop.getProperty(bus_db[i] + b + "-stop" + j + "-stopname");
							log.append("Array " + (j+i) + ": " + Arrays.deepToString(buildarr) + "\n");
							stopcode.add(buildarr);
						}
					}
				}
			} else {
				busamount = 0;
			}
			if (busamount < bus_db.length){
				log.append("!!WARN!! Database is not complete. Missing " + (bus_db.length - busamount) + " entries! \n");
				System.err.println("Missing: " + (bus_db.length - busamount));
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	private void reloadFile(){
		try {
			File file = new File("bus_stopdb.properties");
			if (!file.exists()){
				properties.setText("File Not Exist");
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
		} catch (Exception e){
			e.printStackTrace();
			properties.setText("Error occurred.");
		}
	}
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BuildKMBdatabase window = new BuildKMBdatabase();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BuildKMBdatabase() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("KMB Database Builder");
		frame.setBounds(100, 100, 949, 704);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblImg = new JLabel("");
		lblImg.setHorizontalAlignment(SwingConstants.CENTER);
		lblImg.setIcon(new ImageIcon(BuildKMBdatabase.class.getResource("/image/icn_kmb.gif")));
		
		JLabel lblKmbEtaDatabase = new JLabel("KMB ETA Database Builder");
		lblKmbEtaDatabase.setFont(new Font("Tahoma", Font.PLAIN, 38));
		
		JLabel lblPasteHtmlGenerated = new JLabel("Paste HTML Generated Data here:");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JButton btnBuildDatabase = new JButton("Build Database");
		btnBuildDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save(html.getText(), busname.getText(), (Integer) spinner.getValue(), btnbound.isSelected());
			}
		});
		
		busname = new JTextField();
		busname.setToolTipText("Bus name");
		busname.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JLabel lblBusName = new JLabel("Bus name:");
		
		JLabel lblBound = new JLabel("Bound:");
		
		spinner = new JSpinner();
		spinner.setValue(2);
		
		btnbound = new JToggleButton("Bounds mode");
		
		JLabel lblDatabaseBuilderBy = new JLabel("Database builder by Anthony Law. (Automation not completed.)");
		lblDatabaseBuilderBy.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
							.addGap(587))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblPasteHtmlGenerated)
									.addGap(96)
									.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblImg)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblKmbEtaDatabase)
										.addComponent(lblDatabaseBuilderBy))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel.createSequentialGroup()
											.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel.createSequentialGroup()
													.addComponent(lblBound)
													.addPreferredGap(ComponentPlacement.RELATED)
													.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(lblBusName, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addComponent(btnbound, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
												.addComponent(busname, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)))
										.addComponent(btnBuildDatabase, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))))
							.addContainerGap())))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblBound)
								.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnbound))
							.addGap(2)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(busname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblBusName))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnBuildDatabase))
						.addComponent(lblImg, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(lblDatabaseBuilderBy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblKmbEtaDatabase)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblPasteHtmlGenerated)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
					.addGap(5))
		);
		
		properties = new JTextArea();
		properties.setLineWrap(true);
		properties.setWrapStyleWord(true);
		scrollPane_2.setViewportView(properties);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		scrollPane_2.setColumnHeaderView(splitPane);
		
		lblBusstopdbproperties = new JLabel("bus_stopdb.properties");
		lblBusstopdbproperties.setFont(new Font("Tahoma", Font.BOLD, 15));
		splitPane.setRightComponent(lblBusstopdbproperties);
		
		JButton btnReload = new JButton("Reload");
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadFile();
			}
		});
		splitPane.setLeftComponent(btnReload);
		
		log = new JTextArea();
		scrollPane_1.setViewportView(log);
		
		JLabel lblLog = new JLabel("Log");
		lblLog.setFont(new Font("Tahoma", Font.BOLD, 15));
		scrollPane_1.setColumnHeaderView(lblLog);
		
		html = new JTextArea();
		html.setLineWrap(true);
		html.setWrapStyleWord(true);
		scrollPane.setViewportView(html);
		panel.setLayout(gl_panel);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		chckbxBoundsClean = new JCheckBoxMenuItem("Bounds Full Auto Clean");
		chckbxBoundsClean.setSelected(true);
		mnSettings.add(chckbxBoundsClean);
		
		chckbxClearHTML = new JCheckBoxMenuItem("Clear HTML after build");
		chckbxClearHTML.setSelected(true);
		mnSettings.add(chckbxClearHTML);
		reloadFile();
		busname.grabFocus();
	}
}
