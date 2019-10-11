package com.miracle.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.github.javaparser.ParseException;

public class MyStartPoint {

	public static final String YES = "Yes";

	public static final String ORACLE = "Oracle";

	public static final String MY_SQL = "MySql";

	static final String rootPath = "C:/Users/Joy/CustomFramework/GitFolder/";

	static final String homePath = "C:/Users/Joy/CustomFramework/";

	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException,
			TransformerFactoryConfigurationError, TransformerException, ParseException {

		Scanner sc = new Scanner(System.in);

		System.out.println("Do you want to create a Spring Boot Application?(Yes/No)");

		String springResponse = sc.next();

		String appName = "SpringApp";

		// if yes

		
		  if(YES.equalsIgnoreCase(springResponse)) {
		  
		  
		  System.out.println("Application Name?");
		  
		  appName = sc.next();
		  
		  
		  
		  System.out.println("Which build tool you want to use? Maven?(Yes/No)");
		  
		  String buildToolResponse = sc.next();
		  
		  if(YES.equalsIgnoreCase(buildToolResponse)) {
		  
		  try {
		  
		  
		  //Build a basic Spring Boot application with Maven
		  
		  
		  Map<String,String> params = new HashMap<String, String>();
		  
		  params.put("AppName",appName);
		  
		  MyUtility.buildSpringStarter(params);
		  
		  } catch (Exception e) { // TODO Auto-generated catch block
		  e.printStackTrace(); }
		  
		  }
		  
		  }else {
		  
		  //if No
		  
		  
		  }
		  
		 
		// Create Database

		
		
		  System.out.println("Which backend you want to use?(MySql)");
		  
		  String dbResponse = sc.next();
		  
		  if (MY_SQL.equalsIgnoreCase(dbResponse)) {
		  
		  System.out.println("Enter url");
		  
		  String url = sc.next();
		  
		  System.out.println("Enter username");
		  
		  String username = sc.next();
		  
		  System.out.println("Enter password");
		  
		  String password = sc.next();
		  
		  System.out.println("Enter service name");
		  
		  String serviceName = sc.next();
		  
		  MyUtility.createDatabaseConnection(url, username, password, serviceName,
		  appName);
		  
		  MyUtility.editProp(new File(rootPath + appName +
		  "/src/main/resources/application.properties"), appName);
		  
		  MyUtility.startApplication(appName);
		  
		  } // end of database connection part
		  
		  // logging part System.out.println("Want to add Log Dependency?(Yes)");
		  
		  String logResponse = sc.next();
		  
		  if (YES.contentEquals(logResponse)) {
		  
		  MyUtility.addLogDependency();
		  
		  }
		  
		  // API documentation part
		  System.out.println("Want to add Swagger Dependency?(Yes)");
		  
		  String swagResponse = sc.next();
		  
		  if (YES.contentEquals(swagResponse)) {
		  
		  MyUtility.addSwaggerDependency();
		  
		  }
		 

		// DAO part
		System.out.println("Entity Name?");

		String entity = sc.next();

		System.out.println("How many fields");

		Integer noOfFields = sc.nextInt();

		Map<String, String> fieldMap = new HashMap<>();

		System.out.println("ID");

		String Id = sc.next();

		System.out.println("DataType");

		String dataType = sc.next();

		fieldMap.put("ID" + Id, dataType);

		for (int i = 0; i < noOfFields - 1; i++) {

			System.out.println("Field Name");

			String field = sc.next();

			System.out.println("DataType");

			String fieldType = sc.next();

			fieldMap.put(field, fieldType);

		}

		Add_CRUD_Ops.addCrudMethods(entity, fieldMap);

	}// end of main method

}// end of class
