package generator;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

import helper.Helper;
import helper.Temporary;
import objectfactory.Column;
import objectfactory.Table;

public class ContollerGenerator {
	public static void generateController(Table table) throws JClassAlreadyExistsException{
		String tableName = table.getName();
		JCodeModel codeModel = new JCodeModel();
	  	JPackage jp = codeModel._package(Temporary.controllerPkg);
	  	JDefinedClass jc = jp._class(Helper.sentenceCase(tableName) +"Controller");
	  	
	  	jc.annotate(codeModel.ref("javax.servlet.annotation.WebServlet")).param("value", "/"+tableName+"/*");
	  	jc._extends( codeModel.ref(Temporary.controllerParentPkg+".BaseController"));
	  	
	  	JClass genericClass = codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(tableName));
	  	
	  	generateStoreMethod(table,codeModel,jc);
	  	generateUpdateMethod(table,codeModel,jc);
	  	generateEditMethod(table,codeModel,jc,genericClass);
	  	generateShowAllMethod(table,codeModel,jc,genericClass);
	  	generateShowByIdMethod(table,codeModel,jc,genericClass);
	  	generateCreateMethod(table,codeModel,jc);
	  	generateDeleteMethod(table,codeModel,jc);
	  	try{
	  		  codeModel.build(new File("src/"));
	  	  }catch(IOException e){System.out.println(e);}
	}
	public static void generateStoreMethod(Table table, JCodeModel codeModel, JDefinedClass jc){
		String tableName = table.getName();
		JMethod storeMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "store");
	  	storeMethod.annotate(codeModel.ref(Temporary.annotationPkg+".PostMethod")).param("url", "/store");
	  	JVar requestBody = storeMethod.param(codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(tableName)), tableName);
	  	requestBody.annotate(codeModel.ref(Temporary.annotationPkg+".RequestBody"));
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = storeMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	storeMethod.body().add(daoDeclaration.invoke("save").arg(requestBody));
	  	JVar cp = storeMethod.body().decl(codeModel.ref(String.class),"cp",JExpr.ref("requestResponse").invoke("getRequest").invoke("getContextPath"));
	  	storeMethod.body().add(JExpr.ref("requestResponse").invoke("sendRedirect").arg(cp.assignPlus(JExpr.lit("/" + tableName + "/show"))));
	  	
	}
	public static void generateUpdateMethod(Table table, JCodeModel codeModel, JDefinedClass jc){
		String tableName = table.getName();
		JMethod updateMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "update");
	  	updateMethod.annotate(codeModel.ref(Temporary.annotationPkg+".PostMethod")).param("url", "/update");
	  	JVar requestBody = updateMethod.param(codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(tableName)), tableName);
	  	requestBody.annotate(codeModel.ref(Temporary.annotationPkg+".RequestBody"));
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = updateMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	updateMethod.body().add(daoDeclaration.invoke("update").arg(requestBody));
	  	JVar cp = updateMethod.body().decl(codeModel.ref(String.class),"cp",JExpr.ref("requestResponse").invoke("getRequest").invoke("getContextPath"));
	  	updateMethod.body().add(JExpr.ref("requestResponse").invoke("sendRedirect").arg(cp.assignPlus(JExpr.lit("/" + tableName + "/show"))));
	}
	public static void generateEditMethod(Table table, JCodeModel codeModel, JDefinedClass jc, JClass genericClass){
		String tableName = table.getName();
		JMethod editMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "edit");
	  	editMethod.annotate(codeModel.ref(Temporary.annotationPkg+".GetMethod")).param("url", "/edit/{id}");
	  	JVar urlVariable = editMethod.param(Integer.class, "id");
	  	urlVariable.annotate(codeModel.ref(Temporary.annotationPkg+".URLVariable"));
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = editMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	JVar result = editMethod.body().decl(genericClass, "result",daoDeclaration.invoke("getById").arg(urlVariable));
	  	
	  	JClass listClass = codeModel.ref(List.class);
	  	
	  	for(Column col : table.getCols()){
	  		if(col.isForeignKey()){
	  			JClass dao2 = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(col.getTableReference())+"DAO");
	  			JClass genericClass2 = codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(col.getTableReference()));
		  		JVar daoDeclaration2 = editMethod.body().decl(dao2, col.getTableReference()+"DAO",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(col.getTableReference())+"DAO")));
			  	JVar result2 = editMethod.body().decl(listClass.narrow(genericClass2), col.getTableReference(),daoDeclaration2.invoke("getAll"));
			  	editMethod.body().add(JExpr.ref("requestResponse").invoke("getRequest").invoke("setAttribute").arg(col.getTableReference()).arg(result2));
	  		}
	  		
	  	}
	  	editMethod.body().add(JExpr.ref("requestResponse").invoke("getRequest").invoke("setAttribute").arg("result").arg(result));
	  	editMethod.body().add(JExpr.ref("requestResponse").invoke("forward").arg(String.format("/page/edit_%s_page.jsp",tableName)));
	}
	public static void generateShowAllMethod(Table table, JCodeModel codeModel, JDefinedClass jc, JClass genericClass){
		String tableName = table.getName();
	  	JClass listClass = codeModel.ref(List.class);
		JMethod showAllMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "show");
	  	showAllMethod.annotate(codeModel.ref(Temporary.annotationPkg+".GetMethod")).param("url", "/show");
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = showAllMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	JVar result = showAllMethod.body().decl(listClass.narrow(genericClass), "result",daoDeclaration.invoke("getAll"));
	  	showAllMethod.body().add(JExpr.ref("requestResponse").invoke("getRequest").invoke("setAttribute").arg("result").arg(result));
	  	showAllMethod.body().add(JExpr.ref("requestResponse").invoke("forward").arg(String.format("/page/table_%s_page.jsp",tableName)));
	}
	public static void generateShowByIdMethod(Table table, JCodeModel codeModel, JDefinedClass jc, JClass genericClass){
		String tableName = table.getName();
		JMethod showByIdMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "showById");
	  	showByIdMethod.annotate(codeModel.ref(Temporary.annotationPkg+".GetMethod")).param("url", "/show/{id}");
	  	JVar urlVariable = showByIdMethod.param(Integer.class, "id");
	  	urlVariable.annotate(codeModel.ref(Temporary.annotationPkg+".URLVariable"));
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = showByIdMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	JVar result = showByIdMethod.body().decl(genericClass, "result",daoDeclaration.invoke("getById").arg(urlVariable));
	}
	public static void generateCreateMethod(Table table, JCodeModel codeModel, JDefinedClass jc){
		String tableName = table.getName();
		JMethod createMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "create");
	  	createMethod.annotate(codeModel.ref(Temporary.annotationPkg+".GetMethod")).param("url", "/create");
	  	JClass listClass = codeModel.ref(List.class);
	  	
	  	for(Column col : table.getCols()){
	  		if(col.isForeignKey()){
	  			JClass genericClass = codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(col.getTableReference()));
	  			JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(col.getTableReference())+"DAO");
		  		JVar daoDeclaration = createMethod.body().decl(dao, col.getTableReference()+"DAO",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(col.getTableReference())+"DAO")));
			  	JVar result = createMethod.body().decl(listClass.narrow(genericClass),col.getTableReference(),daoDeclaration.invoke("getAll"));
			  	createMethod.body().add(JExpr.ref("requestResponse").invoke("getRequest").invoke("setAttribute").arg(col.getTableReference()).arg(result));
	  		}
	  	}
	  	createMethod.body().add(JExpr.ref("requestResponse").invoke("forward").arg(String.format("/page/create_%s_page.jsp",tableName)));
	}
	public static void generateDeleteMethod(Table table, JCodeModel codeModel, JDefinedClass jc){
		String tableName = table.getName();
		JMethod deleteMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "delete");
	  	deleteMethod.annotate(codeModel.ref(Temporary.annotationPkg+".GetMethod")).param("url", "/delete/{id}");
	  	JVar urlVariable = deleteMethod.param(Integer.class, "id");
	  	urlVariable.annotate(codeModel.ref(Temporary.annotationPkg+".URLVariable"));
	  	JClass dao = codeModel.directClass(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO");
	  	JVar daoDeclaration = deleteMethod.body().decl(dao, "dao",JExpr._new(codeModel.ref(Temporary.daoPkg+"."+Helper.sentenceCase(tableName)+"DAO")));
	  	deleteMethod.body().add(daoDeclaration.invoke("delete").arg(urlVariable));
	  	JVar cp = deleteMethod.body().decl(codeModel.ref(String.class),"cp",JExpr.ref("requestResponse").invoke("getRequest").invoke("getContextPath"));
	  	deleteMethod.body().add(JExpr.ref("requestResponse").invoke("sendRedirect").arg(cp.assignPlus(JExpr.lit("/" + tableName + "/show"))));
	}
}
