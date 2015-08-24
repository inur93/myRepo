package gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GUIFrame extends JFrame implements GUI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GUIListener actionlistener;
	
	private JList<Object> videolist;
	private ArrayList<File> dataVideolist;
	private JList<Object> tags;
	
	private JTextArea descriptionField;
	private JTextField txtFieldSearch;
	
	JCheckBox chckbxSearchDescription;
	JCheckBox chckbxSearchTags;
	private JTextField txtFieldFilePath;
	


	public GUIFrame(GUIController guiController){
		this.actionlistener = new GUIListener(guiController, this);
		getContentPane().setLayout(null);
		setBounds(100, 100, 847, 800);

		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setupBoxes();
		setupButtons();
		setupLabels();
		setupCheckBoxes();

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);

	}
	
	private void setupCheckBoxes(){
		chckbxSearchTags = new JCheckBox("search tags");
		chckbxSearchTags.setSelected(true);
		chckbxSearchTags.setBounds(15, 650, 113, 25);
		getContentPane().add(chckbxSearchTags);
		
		chckbxSearchDescription = new JCheckBox("search description");
		chckbxSearchDescription.setSelected(true);
		chckbxSearchDescription.setBounds(15, 680, 152, 25);
		getContentPane().add(chckbxSearchDescription);
	}

	@SuppressWarnings("unchecked")
	private void setupBoxes(){

		JScrollPane scrollVideoList = new JScrollPane();
		scrollVideoList.setBounds(15, 45, 360, 520);
		getContentPane().add(scrollVideoList);

		videolist = new JList<Object>();
		scrollVideoList.setViewportView(videolist);
		videolist.addMouseListener(this.actionlistener);
		videolist.setCellRenderer(new VideoListCellRenderer());
		videolist.addKeyListener(this.actionlistener);

		JScrollPane scrollTags = new JScrollPane();
		scrollTags.setBounds(390, 45, 225, 225);
		getContentPane().add(scrollTags);

		tags = new JList<Object>();
		scrollTags.setViewportView(tags);

		JScrollPane scrollDescription = new JScrollPane();
		scrollDescription.setBounds(387, 319, 427, 211);
		getContentPane().add(scrollDescription);
		
		descriptionField = new JTextArea();
		scrollDescription.setViewportView(descriptionField);
		descriptionField.setEnabled(false);
		descriptionField.setColumns(10);
		
		txtFieldFilePath = new JTextField();
		txtFieldFilePath.setBounds(390, 584, 424, 22);
		getContentPane().add(txtFieldFilePath);
		txtFieldFilePath.setEditable(false);
		txtFieldFilePath.setColumns(10);
		
		txtFieldSearch = new JTextField();
		txtFieldSearch.setBounds(15, 616, 225, 25);
		getContentPane().add(txtFieldSearch);
		txtFieldSearch.setColumns(10);
	}

	private void setupLabels(){
		JLabel lblTags = new JLabel("tags:");
		lblTags.setBounds(385, 13, 56, 16);
		getContentPane().add(lblTags);	

		JLabel lblDescription = new JLabel("description:");
		lblDescription.setBounds(387, 290, 94, 16);
		getContentPane().add(lblDescription);

		JLabel lblRootFolder = new JLabel("root:");
		lblRootFolder.setBounds(12, 13, 94, 16);
		getContentPane().add(lblRootFolder);
		
		JLabel lblSearch = new JLabel("search:");
		lblSearch.setBounds(12, 587, 56, 16);
		getContentPane().add(lblSearch);
		
		JLabel lblFilepath = new JLabel("file location:");
		lblFilepath.setBounds(387, 546, 104, 16);
		getContentPane().add(lblFilepath);
	}

	private void setupButtons(){

		JButton btnSetRoot = new JButton("set root");
		btnSetRoot.setBounds(276, 9, 97, 25);
		btnSetRoot.setActionCommand("setRoot");
		btnSetRoot.addActionListener(this.actionlistener);
		getContentPane().add(btnSetRoot);
		
		JButton btnRefreshList = new JButton("refresh list");
		btnRefreshList.setActionCommand("refreshList");
		btnRefreshList.addActionListener(this.actionlistener);
		btnRefreshList.setBounds(169, 9, 97, 25);
		getContentPane().add(btnRefreshList);

		JButton btnEditTags = new JButton("edit tags");
		btnEditTags.setBounds(627, 43, 187, 25);
		btnEditTags.setActionCommand("editTags");
		btnEditTags.addActionListener(this.actionlistener);
		getContentPane().add(btnEditTags);

		JButton btnEditDescription = new JButton("edit description");
		btnEditDescription.setBounds(627, 81, 187, 25);
		btnEditDescription.setActionCommand("editDescription");
		btnEditDescription.addActionListener(this.actionlistener);
		getContentPane().add(btnEditDescription);
		
		JButton btnEditLocation = new JButton("edit location");
		btnEditLocation.setActionCommand("editLocation");
		btnEditLocation.addActionListener(this.actionlistener);
		btnEditLocation.setBounds(483, 543, 132, 25);
		getContentPane().add(btnEditLocation);
		
		JButton btnOpenLocation = new JButton("open file location");
		btnOpenLocation.setBounds(643, 543, 171, 25);
		btnOpenLocation.setActionCommand("openLocation");
		btnOpenLocation.addActionListener(this.actionlistener);
		getContentPane().add(btnOpenLocation);
		
		JButton btnSearch = new JButton("search");
		btnSearch.setBounds(278, 616, 97, 25);
		btnSearch.setActionCommand("search");
		btnSearch.addActionListener(this.actionlistener);
		getContentPane().add(btnSearch);

	}

	@Override
	public String getFolder(String startLocation, String msg){
		JFileChooser chooser = new JFileChooser(); 
		if(startLocation == null || startLocation.equals("")){
			chooser.setCurrentDirectory(new File("."));
		}else{
			chooser.setCurrentDirectory(new File(startLocation));
		}
		chooser.setDialogTitle(msg);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			return chooser.getSelectedFile().getAbsolutePath();
		}else{
			return null;
		}
		
	}
	
	public void setVideoList(ArrayList<File> list){
		if(list == null) return;
		this.dataVideolist = list;
		
		String[] nameList = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			File f = list.get(i);
			if(f.exists()){
			nameList[i] = f.getName();
			}else{
				nameList[i] = "!!! " + f.getName();
			}
		}
		this.videolist.setListData(nameList);
		this.videolist.ensureIndexIsVisible(nameList.length-1);
	}

	public void setTags(String[] tagList){
		this.tags.setListData(tagList);
		this.tags.ensureIndexIsVisible(tagList.length-1);
	}

	public void setDescription(String description){
		this.descriptionField.setText(description);
		descriptionField.setLineWrap(true);
		descriptionField.setWrapStyleWord(true);
	}



	public String promptInput(String msg, String currentValues){
		return JOptionPane.showInputDialog(msg, currentValues);
	}

	public void errorMsg(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}
	
	public void setFilepath(String path){
		this.txtFieldFilePath.setText(path);
	}

	public File getCurrentSelection(){
		int index = this.videolist.getSelectedIndex();	
		if(index >= 0){
			return this.dataVideolist.get(index); // .getAbsolutePath();
		}else{
			return null;
		}
	}
	
	@Override
	public boolean isSearchTagsSelected(){
		return this.chckbxSearchTags.isSelected();
	}
	
	@Override
	public boolean isSearchDescriptionSelected() {
		return this.chckbxSearchDescription.isSelected();
	};
	
	@Override
	public String getSearchString() {
		return this.txtFieldSearch.getText();
	};
	
	@SuppressWarnings({ "rawtypes", "serial" })
	public class VideoListCellRenderer extends JLabel implements ListCellRenderer {

	    public VideoListCellRenderer() {
	        setOpaque(true);
	    }

	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        // Assumes the stuff in the list has a pretty toString
	        setText(value.toString());

	        // based on the index you set the color.  This produces the every other effect.
	        if (value.toString().contains("!!")) setForeground(Color.RED);
	        else setForeground(Color.BLACK);
	        
	        if(isSelected){
	        	setBackground(Color.CYAN.darker());
	        }else{
	        	setBackground(Color.WHITE);
	        }

	        return this;
	    }
	}
	
}
