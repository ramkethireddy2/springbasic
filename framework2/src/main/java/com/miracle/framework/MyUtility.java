package com.miracle.framework;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;



public class MyUtility {

	private static final String pomPath = "C:/Users/Joy/CustomFramework/GitFolder/springbasic/", LOG = "log";
	
	static final Runtime runTime = Runtime.
			   getRuntime();
			
	 public static void buildSpringStarter(Map<String,String> parameters){
			
			//build the basic starter spring application
					
			try {
				runTime.
				   exec("cmd /c start "+MyStartPoint.rootPath+"BatchFiles/clone.bat "+parameters.get("AppName"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
		}//end of method
	
    public static void createDatabaseConnection(String url, String username, String password, String serviceName, String appName) {
		
		//add the properties to application.properties file in the application
		
		try(OutputStream out = new FileOutputStream(MyStartPoint.rootPath+appName+"/src/main/resources/application.properties")){
		
		Properties prop = new Properties();
		
		StringBuilder sb = new StringBuilder("");
		
		sb.append("jdbc:mysql://");
		
		prop.setProperty("spring.datasource.url",sb.toString()+url+":3306/"+serviceName);
		prop.setProperty("spring.datasource.username", username);
		prop.setProperty("spring.datasource.password", password);
	//	prop.setProperty("spring.datasource.driver-class-name", "oracle.jdbc.driver.OracleDriver");
	//	prop.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.OracleDialect");
		
		prop.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		
		
		prop.store(out, null);
		
		}catch(Exception e) {
			
		}
		
		
	}//end of method
    
    
    public static void startApplication(String appName) { 


		try { 
			runTime.exec("cmd /c start "+MyStartPoint.rootPath+"BatchFiles/compile.bat "+appName); 

		} catch(IOException e) 

		{
			e.printStackTrace(); 
		} 
	}//end of method
	
	
	
	

	public static void editProp(File file, String appName) throws IOException {	


		FileWriter fw = null; Reader fr = null; BufferedReader br = null;

		try{

			File tempFile = new File(MyStartPoint.rootPath+appName+"/src/main/resources/temp.properties");

			fw = new FileWriter(tempFile);
			fr = new FileReader(file);
			br = new BufferedReader(fr);

			while(br.ready()) {

				String str = br.readLine().replace("\\", "");

				fw.write(str + "\n");
			}	



			fw.close();
			fr.close();
			br.close();

			fw = new FileWriter(file);
			fr = new FileReader(tempFile);
			br = new BufferedReader(fr);

			while(br.ready()) {

				String str = br.readLine();

				fw.write(str + "\n");
			}


		}catch(Exception e) {	
			e.printStackTrace();

		}finally {
			fw.close();
			fr.close();
			br.close();
		}

	}//end of edit method


	public static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}//end of copy method



	public static void addLogDependency() throws ParserConfigurationException, IOException, SAXException, TransformerFactoryConfigurationError, TransformerException, ParseException {

		ParserHelper.addDependency(pomPath, "org.springframework.boot", "spring-boot-starter-log4j2",LOG,""); 	

		//moveFile(MyStartPoint.homePath+"Files/log4j2.xml", MyStartPoint.rootPath + "springbasic/src/main/resources/log4j2.xml");

		//Files.write(Paths.get(MyStartPoint.homePath+"Files/log4j2.xml", MyStartPoint.rootPath + "springbasic/src/main/resources/log4j2.xml"), null);


		//add imports

		ParserHelper.addImportStatements(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/MyController.java", new String[] {"org.apache.logging.log4j.LogManager", "org.apache.logging.log4j.Logger"});
		
		//add member fields

		ParserHelper.addMemberFields(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/MyController.java", "Logger", 
				"LogManager.getLogger(MyController.class)", "logger",null);		  


		//Adding a log statement inside the method	

		ParserHelper.addMethodBody(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/MyController.java", "logger.debug(\"Hello from Log4j\");", "sayHello");


	}//end of addLogDependency

	public static void addSwaggerDependency() throws ParserConfigurationException, IOException, SAXException, TransformerFactoryConfigurationError, TransformerException {

		ParserHelper.addDependency(pomPath,"io.springfox", "springfox-swagger2","",""); 
		ParserHelper.addDependency(pomPath,"io.springfox", "springfox-swagger-ui","",""); 
		
		ParserHelper.addImportStatements(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/SpringBasic.java", new String[] {"springfox.documentation.swagger2.annotations.EnableSwagger2", 
				                           "springfox.documentation.spring.web.plugins.Docket",
				                           "springfox.documentation.builders.RequestHandlerSelectors",
				                           "springfox.documentation.spi.DocumentationType"});
		
		ParserHelper.addAnnotations(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/SpringBasic.java", "EnableSwagger2","");
		
		ParserHelper.addMemberMethods(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/SpringBasic.java", "productApi", 
				"Docket", new String[] {"Bean"},"", "return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(\"com.miracle\")).build();", null);
		
		
	}//end of addSwaggerDependency


	public static void moveFile(String src, String dest ) {
		Path result = null;

		try {
			result =  Files.move(Paths.get(src), Paths.get(dest));
		} catch (IOException e) {
			System.out.println("Exception while moving file: " + e.getMessage());
		}
		if(result != null) {
			System.out.println("File moved successfully.");
		}else{
			System.out.println("File movement failed.");
			
		}  
	}

}//end of class
