package gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GUIListener extends MouseAdapter implements ActionListener, KeyListener {

	private GUIController guiController;
	private GUI gui;
	public GUIListener(GUIController guiController, GUI gui){
		this.guiController = guiController;
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("command: " + e.getActionCommand());
		switch(e.getActionCommand()){
		case "setRoot":
			this.guiController.setRoot();
			break;
		case "refreshList":
			this.guiController.refreshVideoList();
			break;
		case "editTags":
			File curSelection = gui.getCurrentSelection();
			if(curSelection == null){
				gui.errorMsg("no file chosen");
			}else{
				this.guiController.actionEditTags(curSelection);
			}
			break;
		case "editDescription":
			File currentSelection = gui.getCurrentSelection();
			if(currentSelection == null){
				gui.errorMsg("no file chosen");
			}else{
				this.guiController.actionEditDescription(currentSelection);
			}
			break;
		case "search":
			boolean searchTags = gui.isSearchTagsSelected();
			boolean searchDescr = gui.isSearchDescriptionSelected();
			String searchStr = gui.getSearchString();
			this.guiController.actionSearch(searchTags, searchDescr, searchStr);
			break;
		case "openLocation":
			this.guiController.openLocation(gui.getCurrentSelection());
			break;
		case "editLocation":
			this.guiController.editFileLocation(gui.getCurrentSelection());
			break;
		default:
			break;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		guiController.eventFileSelected(gui.getCurrentSelection());
		File file = gui.getCurrentSelection();
		if(e.getClickCount() > 1 && file != null){
			try {
				if(file.exists()){
				Desktop.getDesktop().open(file);
				}else{
					this.gui.errorMsg("file not found");
				}
			} catch (IOException e1) {}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP){
			System.out.println("update with key");
			guiController.eventFileSelected(gui.getCurrentSelection());;
		}
	}
	
	

}
