package com.miracle.framework;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
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
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.VoidType;

public class ParserHelper {

	private static final String LOG = "log";
	
	
	
	  //create new class //Method to return compilation unit public static CompilationUnit
	
	public static CompilationUnit createClass(CompilationUnit compilationUnit, String path, String className, 
			Modifier.Keyword... modifiers) throws IOException {

		compilationUnit.addClass(className, modifiers);
		
		
		Files.write(Paths.get(path), compilationUnit.toString().getBytes());

		return compilationUnit;

	}// end of createCompilationUnit
	 
	public static CompilationUnit createInterface(CompilationUnit compilationUnit,String path,String className,String entity,
			Modifier.Keyword... modKeywords) throws IOException {
		
		//compilationUnit.addAnnotationDeclaration("Repository");
		compilationUnit.addInterface(className, modKeywords);
		
		com.github.javaparser.ast.NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
		ClassOrInterfaceDeclaration classDeclaration =
				(ClassOrInterfaceDeclaration)types.get(0);
		
		classDeclaration.addExtendedType("JPARepository<"+entity+","+"Integer>");
		
		Files.write(Paths.get(path), compilationUnit.toString().getBytes());
		
				return compilationUnit;
		
	}

	//Method to return compilation unit public static CompilationUnit
	public static CompilationUnit getCompilationUnit(String path) throws IOException {
		
		CompilationUnit compilationUnit = null;
		
		File file = new File(path);
		
		if(!file.exists()) {
			
			file.createNewFile();
			
			compilationUnit = new CompilationUnit();
			
		}else {
			
			FileInputStream fis  = new FileInputStream(path);

			ParseResult<CompilationUnit> parseResult =  new JavaParser().parse(fis);

			compilationUnit = parseResult.getResult().get();
			
		}
		
  		

		return compilationUnit; 

	}// end of getCompilationUnit
	
	
	
	public static void addPackage(String path, String clpackage) throws IOException {
		
		CompilationUnit compilationUnit = getCompilationUnit(path);
	
		compilationUnit.setPackageDeclaration(clpackage);
		
		Files.write(Paths.get(path), compilationUnit.toString().getBytes());
		
	}
	
	public static void addAnnotations(String path, String annotation, String param) throws IOException {
		
		CompilationUnit compilationUnit = getCompilationUnit(path);

		com.github.javaparser.ast.NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
		ClassOrInterfaceDeclaration classDeclaration =
				(ClassOrInterfaceDeclaration)types.get(0);

		if(param != null && param.length() > 0) {
			
			classDeclaration.addSingleMemberAnnotation(annotation, param);
			
		}else {
			
			classDeclaration.addAnnotation(annotation);
		}
		
		
		Files.write(Paths.get(path), compilationUnit.toString().getBytes());
		
	}//end of addAnnotations
	
	

	public static void addImportStatements(String path, String[] importStmtArray) throws IOException {

		CompilationUnit compilationUnit = getCompilationUnit(path);
		
		List<ImportDeclaration> impList = new ArrayList<ImportDeclaration>();

		//adding import statements to the existing class
		
		for(int i=0; i<importStmtArray.length; i++) {
			
			ImportDeclaration importStmt = new ImportDeclaration(importStmtArray[i], false, false);
			
			impList.add(importStmt);
			
		}

		

		com.github.javaparser.ast.NodeList<ImportDeclaration> idList = compilationUnit.getImports();
		
		if(impList != null && idList !=null) 
		{
			
			for(ImportDeclaration importDeclaration : impList) {
				
				idList.add(importDeclaration);
			}
			
		}

		compilationUnit.setImports(idList);	


		Files.write(Paths.get(path), compilationUnit.toString().getBytes());


	}//end of addImportStatements


	public static void addMemberFields(String path, String typeStr, String init, String varName, String[] annotation) throws IOException {

		CompilationUnit compilationUnit = getCompilationUnit(path);

		com.github.javaparser.ast.NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
		ClassOrInterfaceDeclaration classDeclaration =
				(ClassOrInterfaceDeclaration)types.get(0);
		
		


		//initiate instance variable

		com.github.javaparser.ast.NodeList<Modifier> modList = new com.github.javaparser.ast.NodeList<Modifier>();

		modList.add(Modifier.privateModifier());

		modList.add(Modifier.staticModifier());

		modList.add(Modifier.finalModifier());

		Type type = new TypeParameter(typeStr);
		
		Expression initializer = null;

		if(init != null && init.length()>0) {
			initializer = new NameExpr(init);
		}
		

		VariableDeclarator variableDeclarator = new VariableDeclarator(type, varName, initializer);

		com.github.javaparser.ast.NodeList<BodyDeclaration<?>> bodyDeclarationList = classDeclaration.getMembers();

		FieldDeclaration fieldDec = new FieldDeclaration(modList, variableDeclarator);
		
		if(annotation != null && annotation.length > 0) {
			
			for(int i=0; i<annotation.length ; i++) {
				
	        	fieldDec.addAndGetAnnotation(annotation[i]);
				
			}
		}
		
		bodyDeclarationList.addFirst(fieldDec);

		Files.write(Paths.get(path), compilationUnit.toString().getBytes());


	}//end of addMemberFields

	public static void addMemberMethods(String path, String methodName, String returnType, String[] annotation,String annotParam, String statement, com.github.javaparser.ast.NodeList<Parameter> params) throws IOException {

		CompilationUnit compilationUnit = getCompilationUnit(path);

		com.github.javaparser.ast.NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
		ClassOrInterfaceDeclaration classDeclaration =
				(ClassOrInterfaceDeclaration)types.get(0);

		MethodDeclaration methodDeclaration = classDeclaration.addMethod(methodName, Modifier.Keyword.PUBLIC);
		
		if(returnType.contentEquals("void")){
			methodDeclaration.setType(new VoidType()); 
		}else {
			methodDeclaration.setType(returnType); 
		}
		
		if(params != null)
		  methodDeclaration.setParameters(params);
		

		for(int i=0; i<annotation.length ; i++) {
			
			if(annotParam != null && annotParam.length() >0 ) {
				
				methodDeclaration.addSingleMemberAnnotation(annotation[i], annotParam);
			}else {
				methodDeclaration.addAndGetAnnotation(annotation[i]);
			}		
			
		}
		
		methodDeclaration.setBody(addBlockStmt(statement, new BlockStmt()));


		Files.write(Paths.get(path), compilationUnit.toString().getBytes());	 
		
	}


	public static void addConstructor() {


	}


	public static BlockStmt addBlockStmt(String statement, BlockStmt blockStmt) {


		ParseResult<Statement> stmt = new JavaParser().parseStatement(statement);

		com.github.javaparser.ast.NodeList<Statement> statements = new
				com.github.javaparser.ast.NodeList<Statement>();

		statements.add(stmt.getResult().get());

		blockStmt.setStatements(statements);
		
		return blockStmt;
	}


	public static void addMethodBody(String path, String statement, String methodName) throws IOException {

		CompilationUnit compilationUnit = getCompilationUnit(path);

		com.github.javaparser.ast.NodeList<TypeDeclaration<?>> types = compilationUnit.getTypes();
		ClassOrInterfaceDeclaration classDeclaration =
				(ClassOrInterfaceDeclaration)types.get(0);

		com.github.javaparser.ast.NodeList<BodyDeclaration<?>> bodyDeclarationList = classDeclaration.getMembers();

		BlockStmt blockStmt = new BlockStmt();

		ParseResult<Statement> stmt = new JavaParser().parseStatement(statement);

		com.github.javaparser.ast.NodeList<Statement> bodyStmts = null;

		com.github.javaparser.ast.NodeList<Statement> statements = new
				com.github.javaparser.ast.NodeList<Statement>();

		statements.add(stmt.getResult().get());

		blockStmt.setStatements(statements);


		for(BodyDeclaration<?> member : bodyDeclarationList) {

			if(member instanceof MethodDeclaration 
					&& (methodName.equals(
							((MethodDeclaration) member).getName().asString())
							)) {
		

				bodyStmts = ((MethodDeclaration) member).getBody().get().getStatements();

				bodyStmts.addFirst(stmt.getResult().get());

				((MethodDeclaration) member).getBody().get().setStatements(bodyStmts);

			}

		}

		Files.write(Paths.get(path), compilationUnit.toString().getBytes());  


	}//end of addMethodBody


	public static void addDependency(String pomPath, String groupId, String artifactId, String mode, String version) throws ParserConfigurationException, IOException, SAXException, TransformerFactoryConfigurationError, TransformerException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder(); 

		String xmlString = "";

		File file = new File(pomPath+"pom.xml");

		FileReader in = new FileReader(file);

		BufferedReader br = new BufferedReader(in);

		while(br.ready()) {

			xmlString += br.readLine();
		}

		br.close();

		StringBuilder xmlStringBuilder = new StringBuilder();
		xmlStringBuilder.append(xmlString);
		ByteArrayInputStream input = new ByteArrayInputStream(
				xmlStringBuilder.toString().getBytes("UTF-8"));
		Document doc = builder.parse(input);

		doc.getDocumentElement().normalize();

		NodeList dlist =  doc.getElementsByTagName("dependencies");


		NodeList nList = doc.getElementsByTagName("dependency");

		Text a1 = doc.createTextNode(groupId); 
		Element p1 = doc.createElement("groupId"); 
		p1.appendChild(a1); 

		Text a2 = doc.createTextNode(artifactId); 
		Element p2 = doc.createElement("artifactId"); 
		p2.appendChild(a2);


		Node p3 = doc.createElement("dependency"); 

		p3.appendChild(p1);  p3.appendChild(p2);

		if(version != null && version.length() > 0) {
			Text ver = doc.createTextNode(version);
			Element vEle = doc.createElement("version"); 
			vEle.appendChild(ver);
			p3.appendChild(vEle);
		}

		dlist.item(0).appendChild(p3); 


		if(LOG.equals(mode)) {

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					NodeList childNodes =  eElement.getChildNodes();

					for(int j=0; j< childNodes.getLength(); j++){

						Node ch = childNodes.item(j);


						if(ch.getTextContent().contentEquals("spring-boot-starter-web")){


							Text grpId = doc.createTextNode(groupId); 
							Element ele = doc.createElement("groupId"); 
							ele.appendChild(grpId); 

							Text artId = doc.createTextNode("spring-boot-starter-logging"); 
							Element ele2 = doc.createElement("artifactId"); 
							ele2.appendChild(artId);


							Node exclusion = doc.createElement("exclusion"); 

							exclusion.appendChild(ele);  exclusion.appendChild(ele2);

							Node exclusions = doc.createElement("exclusions"); 

							exclusions.appendChild(exclusion);

							nNode.appendChild(exclusions);


						}

					}


				}
			}

		}	

		Source source = new DOMSource(doc);
		File xmlFile = new File(pomPath+"pom.xml");
		StreamResult result = new StreamResult(new OutputStreamWriter(
				new FileOutputStream(xmlFile), "ISO-8859-1"));
		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);

	}//end of addDependency
	
	//get datatypes
	public static Type getDataType(String dataType) {



		switch(dataType) {

		case "int":

			return PrimitiveType.intType();

		case "long":

			return PrimitiveType.longType(); 

		case "float":

			return PrimitiveType.floatType();

		case "boolean":

			return PrimitiveType.booleanType();

		default :

			return new TypeParameter(dataType);

		}

	}	

}//end of class
