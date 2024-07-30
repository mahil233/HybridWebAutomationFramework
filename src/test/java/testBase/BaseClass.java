package testBase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;  //Log4j
import org.apache.logging.log4j.Logger;  //Log4j
import org.apache.poi.hpsf.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public class BaseClass{        //main objective of base class is to acheive reusability
	public static WebDriver driver;
	public Logger logger;
	public Properties p;
	
	@BeforeClass(groups= {"Sanity","Regression","Master"})
	@Parameters({"os","browser"})
	  public void setup(String os, String br) throws IOException {        //based on this parameter we launch browser & os
		//loading config.properties file
		FileReader file=new FileReader("./src//test//resources//config.properties"); //specify the path of property file
		p=new Properties();
		p.load(file);		//this command will load the property file
		
		
		logger=LogManager.getLogger(this.getClass());//Log4j code
		  
		  //based on condition we launch browser. this switch case block will decide which browser will launch based on the parameter we passed 		  
		  switch(br.toLowerCase())
		  {
		  case "chrome"  : driver=new ChromeDriver(); break; 
		  case "edge"    : driver=new EdgeDriver(); break; 
		  case "firefox" : driver=new FirefoxDriver(); break; 
		  default : System.out.println("invalid browser name.."); return; //return means it will exit from execution
		  }
		  
		  driver.manage().deleteAllCookies();
		  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		  driver.get(p.getProperty("appURL")); //reading url from properties file 
		  driver.manage().window().maximize();  
	  }
	  
	@AfterClass(groups= {"Sanity","Regression","Master"})
	 public void tearDown() {
		 driver.quit();
	 }
	  
	public String randomeString(){
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
		
	public String randomeNumber(){
		String generatedString=RandomStringUtils.randomNumeric(10);
		return generatedString;
	}
		
	public String randomAlphaNumeric(){
		String str=RandomStringUtils.randomAlphabetic(3);
		String num=RandomStringUtils.randomNumeric(3);
			
		return (str+"@"+num);
	}
	
	public String captureScreen(String tname) throws IOException {

		String timeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				
		TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
		
		String targetFilePath=System.getProperty("user.dir")+"\\screenshots\\" + tname + "_" + timeStamp + ".png";
		File targetFile=new File(targetFilePath);
		
		sourceFile.renameTo(targetFile);
			
		return targetFilePath;

	}
	
}