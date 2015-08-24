package gui;

import java.io.File;

public interface GUIController {

	public void actionEditTags(File file);
	public void actionEditDescription(File file);
	public void eventFileSelected(File file);
	public void actionSearch(boolean searchTags, boolean searchDescription, String text);
	public void openLocation(File child);
	public void setRoot();
	public void editFileLocation(File file);
	public void refreshVideoList();

}
