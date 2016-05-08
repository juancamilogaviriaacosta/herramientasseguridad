package co.com.uniandes.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
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
 * Parametros del programa: lista.txt, permisos.txt y baseprolog.pl
 */
public class Main {

	public static void main(String[] args) {
		try {
			Query q1 = new Query("consult('" + args[2] + "')");
			System.out.println(q1.hasSolution() ? "Archivo cargado" : "Error");
			
			File fPermisos = new File (args[1]);
			FileReader frp = new FileReader (fPermisos);
			BufferedReader brp = new BufferedReader(frp);
			String linea;
			while((linea=brp.readLine())!=null)
			{
				Query q2 = null;
				if(!linea.contains(",")) {
					q2 = new Query("assert(permiso_peligroso(p"+linea+"))");
				} else {
					String[] split = linea.split(",");
					q2 = new Query("assert(combinacion_peligrosa(p"+split[0]+", p"+split[1]+"))");
				}
				if(!q2.hasSolution()) {
					System.out.println("Error en " + linea);
				}
			}
			frp.close();
			brp.close();
			System.out.println("Hechos cargados");
			
			File fLista = new File (args[0]);
			FileReader fr = new FileReader (fLista);
			BufferedReader br = new BufferedReader(fr);
			String linea3;
			while((linea3=br.readLine())!=null)
			{
				String[] split = linea3.split("/");
				String app = split[split.length-2];
				List<String> permisos = getPermisos(linea3);
				Query q4 = new Query("assert(aplicacion_permiso("+app+","+permisos.toString()+"))");
				Hashtable<?,?>[] sol = q4.allSolutions();
				System.out.println(Arrays.deepToString(sol));
			}
			fr.close();
			br.close();
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
				parametros.add("p" + attribute.substring(attribute.lastIndexOf(".") + 1, attribute.length()));
			}
		}
		return parametros;
	}
}