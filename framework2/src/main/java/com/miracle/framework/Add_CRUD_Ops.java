package com.miracle.framework;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;

public class Add_CRUD_Ops {
	
	private static final String BASIC_PATH = MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/";
	
	private static final String pack = "com.springbasic.start.";
	
	public static void addCrudMethods(String entity, Map<String, String> fieldMap) throws IOException {
		
		createEntity(entity, fieldMap);
		
		createRepo(entity);
		
		createService(entity);
		
		updateController(entity);
		
		
	}//end of method
	
	
	private static void createEntity(String entity, Map<String, String> fieldMap) throws IOException {
		
		CompilationUnit compilationUnit = ParserHelper.getCompilationUnit(BASIC_PATH + "model/"+entity+".java");
		
		ParserHelper.createClass(compilationUnit, BASIC_PATH + "model/"+entity+".java",entity, com.github.javaparser.ast.Modifier.Keyword.PUBLIC);
		
		ParserHelper.addPackage(BASIC_PATH + "model/"+entity+".java", pack+"model");
		
		String[] importStmtArray = new String[] {"javax.persistence.Column" , 
				"javax.persistence.Entity" ,
				"javax.persistence.GeneratedValue" ,
				"javax.persistence.GenerationType" , 
				"javax.persistence.Id" , 
				"javax.persistence.SequenceGenerator" , 
				"javax.persistence.Table"};
		
		
		ParserHelper.addImportStatements(BASIC_PATH + "model/"+entity+".java", importStmtArray);		
		
		ParserHelper.addAnnotations(BASIC_PATH + "model/"+entity+".java", "Entity","");
		
		ParserHelper.addAnnotations(BASIC_PATH + "model/"+entity+".java", "Table", "name="+entity);
		
		for(Entry<String, String> entry :  fieldMap.entrySet()) {
			
			ParserHelper.addMemberFields(BASIC_PATH + "model/"+entity+".java", entry.getValue(), null, entry.getKey(), null);
			
			com.github.javaparser.ast.NodeList<Parameter> paramList = new NodeList<Parameter>();
			
			Type dType = ParserHelper.getDataType(entry.getKey());
			
			Parameter param = new Parameter(dType, entry.getKey());
			
			paramList.add(param);
			
			if("ID".contentEquals(entry.getKey().subSequence(0, 2))) {
				
				String[] annotation = new String[] {
						"Id",	  
						"GeneratedValue(strategy = GenerationType.SEQUENCE, generator = \"sequence\")",
						"SequenceGenerator(name = \"sequence\", sequenceName = \"sequence\",allocationSize = 1)", 
						"Column(name ="+entry.getKey()+", unique = true, nullable = false)"
				};
				
				
										
				ParserHelper.addMemberMethods(BASIC_PATH + "model/"+entity+".java",
						"get"+entry.getKey(), entry.getValue(), annotation,"", "return "+entry.getKey(), null);
				
				ParserHelper.addMemberMethods(BASIC_PATH + "model/"+entity+".java",
						"set"+entry.getKey(), "void", null,"", "this."+entry.getKey()+" = "+entry.getKey(), paramList);
				
				
				
			}else {
				
				String[] annotation = new String[] {
						"Column(name = \""+entry.getKey()+"\")"
				};
										
				ParserHelper.addMemberMethods(BASIC_PATH + "model/"+entity+".java",
						"get"+entry.getKey(), entry.getValue(), annotation, "","return "+entry.getKey(), null);
				
				ParserHelper.addMemberMethods(BASIC_PATH + "model/"+entity+".java",
						"set"+entry.getKey(), "void", null,"", "this."+entry.getKey()+" = "+entry.getKey(), paramList);
				
				
				
			}
			
		}		
		
	}
	private static void createRepo(String entityName) throws IOException {
		String repoName = "MyRepo";
		CompilationUnit compilationUnit = ParserHelper.getCompilationUnit(BASIC_PATH+"repo/"+repoName+".java");
		ParserHelper.createInterface(compilationUnit, BASIC_PATH+"repo/"+repoName+".java",entityName, repoName, Keyword.PUBLIC);
		ParserHelper.addPackage(BASIC_PATH+"repo/"+repoName+".java", pack+"repo");
		
		String[] importStmtArray = new String[] {"com.springbasic.start.model."+entityName+"" ,
				"org.springframework.data.jpa.repository.JpaRepository"
				};
		
		ParserHelper.addImportStatements(BASIC_PATH+"repo/"+repoName+".java", importStmtArray);
		ParserHelper.addAnnotations(MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/repo/MyRepo.java", "Repository","");

		
	}
	
	private static void createService(String entityName) throws IOException {
		
		String resPath = MyStartPoint.rootPath + "springbasic/src/main/java/com/springbasic/start/service/"+entityName+"Service.java";
		
		ParserHelper.getCompilationUnit(BASIC_PATH + "repo/"+entityName+"Service.java");
		
        ParserHelper.addPackage(BASIC_PATH + "repo/"+entityName+"Service.java", pack+"service");
		
		String[] importStmtArray = new String[] {"java.util.List" ,
				"org.springframework.beans.factory.annotation.Autowired" , 
				"org.springframework.stereotype.Service" ,
				"com.springbasic.start.model."+entityName, 
				"com.springbasic.start.repo."+entityName+"Repo"};
		
		
		ParserHelper.addImportStatements(resPath, importStmtArray);		
		
		ParserHelper.addAnnotations(resPath, "Service","");
		
		ParserHelper.addMemberFields(resPath, entityName+"Repo", "", "repo", new String[] {"Autowired"});
		
		ParserHelper.addMemberMethods(resPath, "get", "List<"+entityName+">", null,"", "return (List<"+entityName+">)repo.findAll();", null);
		
		
		com.github.javaparser.ast.NodeList<Parameter> paramList = new NodeList<Parameter>();
		
		Parameter param = new Parameter(new ClassOrInterfaceType().setName(entityName), entityName.toLowerCase());
		
		paramList.add(param);
		
		ParserHelper.addMemberMethods(resPath, "add", entityName, null,"", "return repo.save(p);", paramList);
		
		ParserHelper.addMemberMethods(resPath, "remove", "void", null,"", "repo.delete(p);", paramList);
		/*
		 * ParserHelper.addMemberMethods(resPath, "update", entityName, null,
		 * "return repo.save(p);", paramList);
		 */
		
		
	}//end of service Method
	
	
    private static void updateController(String entity) {
    	
    	
		
		
	}

}//end of class
