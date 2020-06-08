package downloader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Pattern;

import generatorhelper.PageGeneratorHelper;

public class FactoryDownloader {
	public static void download(String path) throws IOException{
		PageGeneratorHelper.createNewDirectory(getFilePath(path));
		 URL url = new URL("http://18.163.239.181/meta-repository/"+path);
		    BufferedInputStream bufferedInputStream = new  BufferedInputStream(url.openStream());
		    String newFilePath = convertFileName(path);
		    FileOutputStream stream = new FileOutputStream("WebContent/"+newFilePath);
		    int count=0;
		    byte[] b1 = new byte[100];

		    while((count = bufferedInputStream.read(b1)) != -1) {
		        System.out.println("Download "+newFilePath+" : "+new File("WebContent/"+newFilePath).length()/1024 +" KB");
		        stream.write(b1, 0, count);
		   }
	}
	public static void downloadRepoReference() throws IOException{
		 URL url = new URL("http://18.163.239.181/repository.txt");
		    BufferedInputStream bufferedInputStream = new  BufferedInputStream(url.openStream());
		    FileOutputStream stream = new FileOutputStream("repository.txt");
		    int count=0;
		    byte[] b1 = new byte[100];

		    while((count = bufferedInputStream.read(b1)) != -1) {
		        stream.write(b1, 0, count);
		   }
	}
	public static void readRepository() throws IOException{
		 try {
			 downloadRepoReference();
		     File myObj = new File("repository.txt");
		     Scanner myReader = new Scanner(myObj);
		     while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        download(data);
		     }
		     myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static String getFilePath(String path){
		String[] fileLocation = path.split("/");
		String filePath = "WebContent\\";
		for(int i=0;i<fileLocation.length-1;i++){
			filePath += fileLocation[i]+"\\";
		}
		return filePath;
	}
	public static String convertFileName(String path){
		String[] file = path.split("\\.");
		return file[1].equals("mg") ? file[0]+".jsp" : path;
	}
}