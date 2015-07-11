import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;


public class Main {

		public static void main(String[] args)  {
			CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
			String jarDir = null;
			File jarFile = null;
			String name = null;
			// finds location of the jar file. to make it possible to have the file anywhere you want to
			try {
				jarFile = new File(codeSource.getLocation().toURI().getPath());
				name = new File(codeSource.getLocation().getPath()).getName(); 
				jarDir = jarFile.getParentFile().getPath();
				jarDir = jarDir.replace("\\", "/");
			} catch (URISyntaxException e) {
				System.err.println("could not find jar file location!");
				e.printStackTrace();
			}
			
			// starts command prompt and runs program
			try{
				Runtime.getRuntime().exec("cmd.exe /c cd \""+ jarDir +"\" "
						+ "& start cmd.exe /k \"java -cp "+name+" run.runProgram\"");
														//package^	class^	//TODO set the package and main class above
			} catch(Exception e){
				System.err.println("failed to load command prompt");
			}
		}
	

}
