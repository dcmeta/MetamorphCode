package generator;
import java.io.File;
import helper.Temporary;
import objectfactory.Table;
import helper.Helper;
import java.io.IOException;
import java.util.List;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;

public class ControllerConfigGenerator {
	public static void generateControllerConfig() throws JClassAlreadyExistsException{
		JCodeModel codeModel = new JCodeModel();
	  	JPackage jp = codeModel._package(Temporary.configPkg);
	  	JDefinedClass jc = jp._class("MetaOControllerConfigurator");
	  	jc._extends( codeModel.ref(Temporary.controllerConfigPkg+".ControllerConfigurator"));
	  	JMethod configMethod = jc.method(JMod.PUBLIC, codeModel.VOID, "config");
	  	configMethod.annotate(Override.class);
	  	JClass metaNest = codeModel.directClass(Temporary.controllerConfigPkg+".ControllerMap");
	  	JVar metaVar = configMethod.body().decl(metaNest, "controllerMap",JExpr._new(metaNest));
	  	for(Table tbl : Temporary.tableList){
	  		String tableName = tbl.getName();
	  		JClass c = codeModel.directClass(Temporary.controllerPkg+"."+Helper.sentenceCase(tableName) +"Controller");
	  		configMethod.body().add(metaVar.invoke("addController").arg(c.dotclass()));
	  	}
	  	configMethod.body().add(metaVar.invoke("process"));
	  	try{
	  		  codeModel.build(new File("src/"));
	  	  }catch(IOException e){System.out.println(e);}
	  	
	}
}
