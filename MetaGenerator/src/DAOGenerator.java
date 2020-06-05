import java.io.File;
import java.io.IOException;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

public class DAOGenerator {
	public static void generateDAO(Table t) throws JClassAlreadyExistsException{
		String tableName = t.getName();
		JCodeModel codeModel = new JCodeModel();
	  	JPackage jp = codeModel._package(Temporary.daoPkg);
	  	JDefinedClass jc = jp._class(Helper.sentenceCase(tableName) +"DAO");
	  	jc._extends( codeModel.ref(Temporary.daoParentPkg+".DAO").narrow(codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(tableName))));
	  	JMethod constructor = jc.constructor(JMod.PUBLIC);
	  	constructor.body().add(JExpr.invoke("super").arg(JExpr._new(codeModel.ref(Temporary.entityPkg+"."+Helper.sentenceCase(tableName)))));
	  	try{
	  		  codeModel.build(new File("src/"));
	  	  }catch(IOException e){System.out.println(e);}
	}
}
