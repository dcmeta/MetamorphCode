package metamorph.utility;

import java.io.File;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import metamorph.helper.Temporary;

public abstract class MetamorphStarter extends HttpServlet{
	public void init(){
		config();
		System.out.println("Metamorph Objects have been processed");
		
	}
	public abstract void config();
}
