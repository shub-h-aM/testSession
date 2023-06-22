package main.java.utilities;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
	static Connection con = null;
	 File folder=null;
	String propertyFileName=null;
	public String vehicleNumber;

	public static String generateRandomVehicleNumber() {
		return null;
	}
	
	
	
	
	
	
	/*==============================================================================================
	This method is for getting database connection
   ================================================================================================*/	
	
		 public Connection gettingConnectionToMSSQLDataBase( String connectionurl,String username,String password) throws Exception{
		 String  connectionUrl=connectionurl+";user="+username+";Password="+password;
		  System.out.println(connectionUrl);
		   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		   return con = DriverManager.getConnection(connectionUrl);
		   
		 }
		 /*==============================================================================================
			This method is for insertion test case result insertion in DB
		   ================================================================================================*/	
		 public  void reportRequiredEntryInsertionInDB(Connection conn,String testcaseName, String TestclassName,String status ){
		        Statement stmt = null;
		 		            try {
		                               
		                    // Create and execute an SQL statement that returns some data.
		                    stmt = conn.createStatement();
		                    String sql = "Insert into DBForAutomation.dbo.reportdata values("+"'"+TestclassName+"'"+","+"'"+testcaseName+"'"+","+"'"+status+"'"+","+"getDate())";
		                    stmt.executeUpdate(sql);
		                    System.out.println("Record inserted successfully");
		            }catch(Exception e){
		            	System.out.println(e.getMessage());
		            }
			
		}
		 /*==============================================================================================
			This method are for creating folder
		   ================================================================================================*/	
		public void creatingFolder(String folderpath) {
			try{
			 folder=new File(folderpath);
		    	System.out.println(folder.exists());
		    	if (folder.exists()==false)
		    	{
		    		
		    		boolean result=folder.mkdirs();
		       	     if(result) {    
		    	       System.out.println(" Directory successfully created  ");  
		    	     }
		       	     else{
		       	    	 System.out.println("Failed to create  directory"); 
		       	     }
		    	}else{
		    		 System.out.println("  Directory already exist");
		    		FileUtils.cleanDirectory(folder);
		    	}
			}catch(Exception e){
				System.out.println(e);
			}
		}
		
		/*==============================================================================================
		This method is for deleting file or folder 
	   ================================================================================================*/	
		public void deletingFileAndEmptyFolder(String folderpath){
			File fPath=new File(folderpath);
	    	if(fPath.exists())
	    	{
	    		boolean result=fPath.delete();
	       	     if(result) {    
	    	       System.out.println("Given File or Folder  successfully deleted  ");  
	    	      }
	       	     else{
	       	    	 System.out.println("It exist but failed to delete given file or folder, May be given it's folder and non"); 
	       	      }
	    	}else{
	    		 System.out.println(" Given File or Folder can not be deleted because it do not exist");
	    		
	    	    }
	    		
	    	}
		/*==============================================================================================
		This method are for deleting all files and folder in a directory recursively
	   ================================================================================================*/
		
		public void recursivelyCleaningDirectory(String folderpath) throws IOException{
		File dir=new File(folderpath);
		listingFilesAndFolderInDirectory(folderpath);
		if(dir.exists()){
			FileUtils.cleanDirectory(dir);
		}else{
   		 System.out.println(" Given File or Folder can not be deleted because it do not exist");
 		
	    }
		System.out.println("===============Folder "+folderpath+" has been cleaned=============================");
		}
		
		public void listingFilesAndFolderInDirectory(String folderpath){
			File dir=new File(folderpath);
			File[] fList = dir.listFiles();
			if(fList !=null)
			 {
			   for (File file : fList)
			   {			
			    System.out.println(file.getName());
		
			   }
			 }
			
		}
			
		
		
	/*==============================================================================================
		These method are for reading property file
	   ================================================================================================*/	
		public void setPropertyFile( String propertyFileName){
			this.propertyFileName=propertyFileName;
		}
		
		public String gettingValueOfProperty(String property) throws Exception{
		Properties prop = new Properties();
		InputStream input = null;
		String requiredPropertyValue = null ;
		try{
		input = new FileInputStream(propertyFileName);
	 		// load a properties file
			prop.load(input);
	 		// get the property value 
			requiredPropertyValue=prop.getProperty(property);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(input!=null)
			 {
				input.close();
		     }

		      }
		return requiredPropertyValue;
		
	}
		
		public void settingValueOfProperty(String property,String value) throws Exception{
			Properties prop = new Properties();
			InputStream input = null;
			FileOutputStream out=null;
			try{
			input = new FileInputStream(propertyFileName);
		 		// load a properties file
				prop.load(input);
		 		// set the property value 
			   out = new FileOutputStream(propertyFileName);
				prop.setProperty(property, value);
				prop.store(out, null);
				out.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}
			finally {
				if(input!=null)
				 {
					input.close();
			     }
				if(out!=null)
				 {
					out.close();
			     }

			      }
			
			
		}	
		/*==============================================================================================
		These method is for getting content between two string
	   ================================================================================================*/
	public String substringBetweenTwoString(String document,String Regexpattern,String secondString)
	{
		 String matchedData=null;
		 Pattern r = Pattern.compile(Regexpattern);

         // Now create matcher object.
         Matcher m = r.matcher(document);
        
         if (m.find( )) {
        	 matchedData= StringUtils.substringBetween(document, m.group(), secondString);
            
          } else {
        	  matchedData="";
          }
         

		return matchedData;
		
	}
	
	public ArrayList<String>  Iterarting_Table(String insource){
		String source ="<table>" +insource+ "<table>";
	ArrayList<String> dataSet=new ArrayList<String> ();
	Document doc = Jsoup.parse(source, "UTF-8");
	 for (Element rowElmt : doc.getElementsByTag("tr"))
	 {
	     Elements cols = rowElmt.getElementsByTag("th");
	     if (cols.size() == 0 )
	         cols = rowElmt.getElementsByTag("td");

	      String rowTxt[] = new String[cols.size()];
	     for (int i = 0; i < rowTxt.length; i++)
	     {
	         rowTxt[i] = cols.get(i).text();
	         dataSet.add(cols.get(i).text());
	         
	     }
	     
	 }
	return dataSet;
	}
	
//	public static void isLinkBroken(List<String> urls) throws Exception{
//	ExtentTestManager.getTest().log(LogStatus.INFO,"Number of url reached:-"+urls.size());
//	int count=0;
//    for(String url:urls){
//    	if(url.contains("http")){
//    	try{
//    	//
//    	HttpClient httpClient = HttpClientBuilder.create().build();
//		HttpGet request = new HttpGet(url);
//		HttpResponse response = httpClient.execute(request);
//		count++;
//		if(response.getStatusLine().getStatusCode()==200 ||response.getStatusLine().getStatusCode()==201)
//		{
//         System.out.println("Request got executed successfully");
//         ExtentTestManager.getTest().log(LogStatus.INFO," "+url+" is opening successfully:-"+response.getStatusLine().getStatusCode());
//		}
//    	}catch(Exception e){
//			ExtentTestManager.getTest().log(LogStatus.FAIL," "+url+"is broken:-"+e);
//
//		}
//    	}
//    }
//    ExtentTestManager.getTest().log(LogStatus.INFO,"Number of url checked:-"+count);
//
//
//	}
//
public static class RandomVehicleNumberGenerator {

	public String generateRandomVehicleNumber() {
		// Generate a random number between 10 and 99 for the first two digits
		int firstTwoDigits = generateRandomNumberInRange(10, 99);

		// Generate three random uppercase letters for the next three characters
		String letters = generateRandomLetters(2);

		// Generate a random number between 1000 and 9999 for the last four digits
		int lastFourDigits = generateRandomNumberInRange(1000, 9999);

		// Combine the generated parts to form the vehicle number
		String vehicleNumber = "HR" + firstTwoDigits + letters + lastFourDigits;

		return vehicleNumber;
	}

	public int generateRandomNumberInRange(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	public String generateRandomLetters(int count) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			char letter = (char) (random.nextInt(26) + 'A');
			sb.append(letter);
		}
		return sb.toString();
	}

}

}
