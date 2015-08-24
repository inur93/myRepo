package gui;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import common.Const;
import common.PropertyHelper;


public class VideoExplorer {

	public static ArrayList<File> findFiles(ArrayList<File> fileList, String rootName){
		if(fileList == null) fileList = new ArrayList<>();
		if(rootName == null) return null;
		File root = new File(rootName);
		File[] list2 = root.listFiles();
		if(list2 == null) return null;
		for(File f : list2){
			System.out.println("\t" + f.getName() + " " + f.isDirectory());
			if(f.isDirectory()){
				System.out.println("is directory");
				findFiles(fileList, f.getAbsolutePath());
			}else{
				String path = PropertyHelper.readFromProperty(Const.FILE_DATA, f.getAbsolutePath());
				if(path == null || path.equals("")){
					PropertyHelper.writeToProperty(Const.FILE_DATA, f.getAbsolutePath(), f.getName());
				}
				fileList.add(f);
			}
			
		}
		return fileList;
	}
	
	public static ArrayList<File> getCurrentFiles(){
		Set<String> names = PropertyHelper.readAll(Const.FILE_DATA);
		ArrayList<File> files = new ArrayList<>();
		if(names == null) return null;
		for(String s : names){
			files.add(new File(s));
		}
		return files;
	}
}
