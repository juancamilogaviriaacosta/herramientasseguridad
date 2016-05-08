package co.com.uniandes.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jpl.Query;

/**
 * @author juan
 * Para ejecutar se de agregar los siguientes parametros
 * Parametros de la maquina virtual: -Djava.library.path=/usr/lib/swi-prolog/lib/amd64
 * Parametros del programa: rutas completas de los archivos lista.txt y baseprolog.pl
 */
public class Main {

	public static void main(String[] args) {
		try {
			
			File lista = new File (args[0]);
			FileReader fr = new FileReader (lista);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			Query q1 = new Query("consult('" + args[1] + "')");
			System.out.println(q1.hasSolution() ? "Archivo cargado" : "Error");
			while((linea=br.readLine())!=null)
			{
				List<String> permisos = getPermisos(linea);
				for (String p : permisos) {
					Query q4 = new Query("permiso_peligroso(p"+p+")");
					if(q4.hasSolution()) {
						String[] split = linea.split("/");
						System.out.println(split[split.length-2] + " tiene permisos peligrosos");
						break;
					}
				}
			}
			fr.close();
			br.close();
			
			
//			Query q1 = new Query("consult('" + args[1] + "')");
//			System.out.println(q1.hasSolution() ? "Archivo cargado" : "Error");
//
//			Query q4 = new Query("todos(X)");
//			System.out.println(q4.oneSolution().get("X"));
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> getPermisos(String archivo) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File(archivo));

		NodeList listaParametros = doc.getElementsByTagName("uses-permission");
		List<String> parametros = new ArrayList<String>();
		for (int i = 0; i < listaParametros.getLength(); i++) {
			Node parametro = listaParametros.item(i);
			if (parametro.getNodeType() == Node.ELEMENT_NODE) {
				Element elemento = (Element) parametro;
				String attribute = elemento.getAttribute("android:name");
				parametros.add(attribute.substring(attribute.lastIndexOf(".") + 1, attribute.length()));
			}
		}
		return parametros;
	}
}