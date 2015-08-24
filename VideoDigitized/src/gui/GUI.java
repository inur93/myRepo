package gui;

import java.io.File;
import java.util.ArrayList;

public interface GUI {

	String promptInput(String msg, String defaultValue);

	void setDescription(String description);

	void setFilepath(String path);

	void setVideoList(ArrayList<File> files);

	void errorMsg(String msg);

	void setTags(String[] tags);

	File getCurrentSelection();

	boolean isSearchTagsSelected();

	boolean isSearchDescriptionSelected();

	String getSearchString();

	String getFolder(String startLocation, String msg);
}
