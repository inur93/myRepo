package common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class PropertyHelper {
	
	public static ReadWriteLock lock = new ReentrantReadWriteLock(true);//Fancy lock to ensure synchronized read/writes

	private static Properties getProperties(FileInputStream stream){
		Properties prop = new Properties();
		try {
			prop.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private static FileOutputStream getOutputStream(String filename){
		try{
			return new FileOutputStream(filename + Const.PROPERTIES_EXT);
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}
	}

	private static FileInputStream getInputStream(String filename){
		try {
			return new FileInputStream(filename + Const.PROPERTIES_EXT);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void closeInputStream(FileInputStream in){
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void closeOutputStream(FileOutputStream out){
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToProperty(String filename, String key, String value){
		lock.writeLock().lock();
		FileInputStream in = getInputStream(filename); 
		Properties prop = null;
		if(in != null){
			prop = getProperties(in);
			closeInputStream(in);
			FileOutputStream out = getOutputStream(filename);
			prop.put(key, value);
			try {
				prop.store(out, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		lock.writeLock().unlock();

	}


	public static String readFromProperty(String filename, String key){
		lock.readLock().lock();
		String value = null;
		FileInputStream in = getInputStream(filename);
		if(in != null){
			Properties prop = getProperties(in);

			value = prop.getProperty(key);
			closeInputStream(in);
		}
		lock.readLock().unlock();
		return value;
	}

	private synchronized static ArrayList<File> searchTags(Set<String> keys, String keyword){
		if(keyword == null) return null;
		lock.readLock().lock();
		ArrayList<File> fileMatches = new ArrayList<File>();
		FileInputStream in = getInputStream(Const.TAG_DATA);
		if(in != null){
			Properties prop = getProperties(in);

			keyword = keyword.toLowerCase();
			String[] tags = keyword.split(",");
			for(String s : keys){
				boolean match = true;
				String value = prop.getProperty(s);
				if(value != null){
					value = value.toLowerCase();
					for(String tag : tags){
						if(!value.contains(tag)){
							match = false;
						}
					}
				}else{
					match = false;
				}
				if(match){
					fileMatches.add(new File(s));
				}
			}
			closeInputStream(in);
		}
		lock.readLock().unlock();
		return fileMatches;
	}

	private synchronized static ArrayList<File> searchDescription(Set<String> keys, String keyword){
		lock.readLock().lock();
		ArrayList<File> fileMatches = new ArrayList<File>();
		keyword = keyword.toLowerCase();
		FileInputStream in = getInputStream(Const.DESCRIPTION_DATA);
		if(in != null){
			Properties prop = getProperties(in);
			for(String s : keys){
				String value = prop.getProperty(s);

				if(value != null){
					value = value.toLowerCase();
					if(value.contains(keyword)){
						fileMatches.add(new File(s));
					}
				}
			}
			closeInputStream(in);
		}
		lock.readLock().unlock();
		return fileMatches;
	}

	public synchronized static ArrayList<File> searchFiles(boolean searchTags, boolean searchDescription, String keyword){
		lock.readLock().lock();

		ArrayList<File> list1 = null;
		ArrayList<File> list2 = null;
		ArrayList<File> fileMatches = new ArrayList<File>();
		FileInputStream in = getInputStream(Const.FILE_DATA);
		if(in != null){
		Properties prop = getProperties(in);

		Set<String> keys = prop.stringPropertyNames();

		if(keyword == null || keyword.equals("")){
			for(String s : keys){
				fileMatches.add(new File(s));
			}
		}

		if(searchDescription){
			list1 = searchDescription(keys, keyword);
		}
		if(searchTags){
			list2 = searchTags(keys, keyword);
		}

		closeInputStream(in);

		lock.readLock().unlock();
		if(list1 != null){
			for(File f : list1){
				fileMatches.add(f);
			}
		}
		if(list2 != null){
			for(File f: list2){
				fileMatches.add(f);
			}
		}
		}
		return fileMatches;

	}

	public static void deleteKey(String filename, String key){
		lock.readLock().lock();
		FileInputStream in = getInputStream(filename);
		if(in != null){
		Properties prop = getProperties(in);
		closeInputStream(in);
		prop.remove(key);
		pushData(filename, prop);
		}
		lock.readLock().unlock();
	}

	private static void pushData(String filename, Properties prop) {
		FileOutputStream out = getOutputStream(filename);
		try {
			prop.store(out, null);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		closeOutputStream(out);
	}

	public static Set<String> readAll(String filename){
		lock.readLock().lock();
		Set<String> keys = null;
		FileInputStream in = getInputStream(filename);
		if(in != null){
		Properties prop = getProperties(in);

		keys = prop.stringPropertyNames();
		closeInputStream(in);
		}
		lock.readLock().unlock();
		return keys;
	}

}