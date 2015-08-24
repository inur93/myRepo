package gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import common.Const;
import common.PropertyHelper;

public class GUIControllerImpl implements GUIController {
	private GUI gui;
	private String editTagsMsg = "edit tags delimited with \",\"";
	private String editDescriptionMsg = "edit description: ";

	private ArrayList<File> missingFiles = new ArrayList<File>();
	public GUIControllerImpl(){
		this.gui = new GUIFrame(this);
		refreshVideoList();
	}
	
	public void refreshVideoList(){
		ArrayList<File> currentFiles = VideoExplorer.getCurrentFiles();
		if(currentFiles == null) return;
		int missingFileCount = 0;
		String msgFileList = "";

		for(File f : currentFiles){
			if(!f.exists()){
				missingFiles.add(f);
				missingFileCount++;
				msgFileList += f.getAbsolutePath() + "\n";
			}
		}
		if(missingFileCount > 0){
		String msg = missingFileCount + " file(s) were not found: \n" + msgFileList;
		this.gui.errorMsg(msg);
		}
		this.gui.setVideoList(currentFiles);
	}
	
	public void actionEditTags(File file){
		if(file == null) return;
		String filePath = file.getAbsolutePath();
		String currentTags = PropertyHelper.readFromProperty(Const.TAG_DATA, filePath);
		String newTags = this.gui.promptInput(editTagsMsg, currentTags);
		if(newTags != null && !newTags.equalsIgnoreCase("")){
			PropertyHelper.writeToProperty(Const.TAG_DATA, filePath, newTags);
		}
		eventUpdateTags(file);
	}

	public void actionEditDescription(File file){
		if(file == null) return;
		String filePath = file.getAbsolutePath();
		String currentDescription = PropertyHelper.readFromProperty(Const.DESCRIPTION_DATA, filePath);
		String newDescription = this.gui.promptInput(editDescriptionMsg, currentDescription);
		if(newDescription != null && !newDescription.equalsIgnoreCase("")){
			PropertyHelper.writeToProperty(Const.DESCRIPTION_DATA, filePath, newDescription);
		}
		eventUpdateDescription(file);
	}

	private void eventUpdateTags(File file){
		
		String tags = null;
		if(file != null){
		tags = PropertyHelper.readFromProperty(Const.TAG_DATA, file.getAbsolutePath());
		}
		if(tags == null){
			tags = "";
		}
			this.gui.setTags(tags.split(","));
		
	}
	
	public void setRoot(){
		String currentRoot = PropertyHelper.readFromProperty(Const.CONFIG_FILE, "root");
		String newRoot = this.gui.getFolder(currentRoot, Const.SELECT_ROOT_MSG);
		if(newRoot == null) return;
		PropertyHelper.writeToProperty(Const.CONFIG_FILE, "root", newRoot);
		ArrayList<File> files = VideoExplorer.findFiles(null, PropertyHelper.readFromProperty(Const.CONFIG_FILE, "root"));
		this.gui.setVideoList(files);
	}

	private void eventUpdateDescription(File file){
		String description = null;
	
		if(file != null){
		description = PropertyHelper.readFromProperty(Const.DESCRIPTION_DATA, file.getAbsolutePath());
		}
		if(description == null){
			description = "";
		}
			this.gui.setDescription(description);
	}

	public void eventFileSelected(File file){
		eventUpdateDescription(file);
		eventUpdateTags(file);
		gui.setFilepath(file == null ? "" : file.getAbsolutePath());
	}
	@Override
	public void actionSearch(boolean searchTags, boolean searchDescription, String text) {
		ArrayList<File> matches = PropertyHelper.searchFiles(searchTags, searchDescription, text);
		this.gui.setVideoList(matches);
		
	}
	@Override
	public void openLocation(File child) {
		if(child == null) {
			this.gui.errorMsg("no file selected");
			return;
		}
		try {
			Desktop.getDesktop().open(child.getParentFile());
		} catch (IOException e) {}
	}

	@Override
	public void editFileLocation(File file) {
		String startFolder = PropertyHelper.readFromProperty(Const.CONFIG_FILE, "root");
		String folder = this.gui.getFolder(startFolder, "select folder where missing files are located");
		for(File f : missingFiles){
			File newFile = new File(folder + "\\" + f.getName());
			if(newFile.exists()){
				String oldPath = f.getAbsolutePath();
				String newPath = newFile.getAbsolutePath();
				String tags = PropertyHelper.readFromProperty(Const.TAG_DATA, oldPath);
				String descr = PropertyHelper.readFromProperty(Const.DESCRIPTION_DATA, oldPath);
				System.out.println("oldpath: " + oldPath);
				PropertyHelper.deleteKey(Const.FILE_DATA, oldPath);
				
				System.out.println("path: " + newPath + " tags: " + tags);
				if(tags != null) PropertyHelper.writeToProperty(Const.TAG_DATA,newPath, tags);
				if(descr != null) PropertyHelper.writeToProperty(Const.DESCRIPTION_DATA, newPath, descr);
				PropertyHelper.writeToProperty(Const.FILE_DATA, newPath, newFile.getName());
			}
		}
		refreshVideoList();
	}
}
