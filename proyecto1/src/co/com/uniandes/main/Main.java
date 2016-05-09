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

import jpl.Compound;
import jpl.Query;
import jpl.Term;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author juan
 * Probado en ubuntu 14.04 X64: sudo apt-get install swi-prolog swi-prolog-java
 * Para ejecutar se deben agregar los siguientes parametros
 * Parametros de la maquina virtual: -Djava.library.path=/usr/lib/swi-prolog/lib/amd64
 * Parametros del programa: lista.txt, permisos.txt y baseprolog.pl
 */
public class Main {

	public static void main(String[] args) {
		try {
			Query q1 = new Query("consult('" + args[2] + "')");
			System.out.println(q1.hasSolution() ? "****Archivo cargado****\n" : "Error");
			
			File fPermisos = new File (args[1]);
			FileReader frp = new FileReader (fPermisos);
			BufferedReader brp = new BufferedReader(frp);
			String linea;
			while((linea=brp.readLine())!=null)
			{
				Query q2 = null;
				if(!linea.contains(",")) {
					String hecho1 = "assert(permiso_peligroso(p"+linea.trim()+")).";
					System.out.println(hecho1);
					q2 = new Query(hecho1);
				} else {
					String hecho2 = "assert(combinacion_peligrosa("+formatearCombinacion(linea)+")).";
					System.out.println(hecho2);
					q2 = new Query(hecho2);
				}
				if(!q2.hasSolution()) {
					System.out.println("Error en " + linea);
				}
			}
			frp.close();
			brp.close();
			
			File fLista = new File (args[0]);
			FileReader fr = new FileReader (fLista);
			BufferedReader br = new BufferedReader(fr);
			String linea3;
			while((linea3=br.readLine())!=null)
			{
				String[] split = linea3.split("/");
				String app = split[split.length-2];
				String permisos = Arrays.deepToString(getPermisos(linea3).toArray());
				String hecho3 = "assert(aplicacion_permiso("+app+","+permisos+")).";
				System.out.println(hecho3);
				Query q4 = new Query(hecho3);
				if(!q4.hasSolution()) {
					System.out.println("Error en " + linea3);
				}
			}
			fr.close();
			br.close();
			System.out.println("****Hechos cargados****\n");
			System.out.println(llamarSolucion("aplicacion_peligrosa_simple(X)",			"Las aplicaciones que usan al menos un permiso peligroso son: "));
			System.out.println(llamarSolucion("aplicacion_peligrosa_combinacion(X)",	"Las aplicaciones que usan alguna combinacion de permisos peligrosos son: "));
			System.out.println(llamarSolucion("aplicaciones_peligrosas(X)",				"Todas las aplicaciones peligrosas son: "));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String llamarSolucion(String predicado, String texto) {
		Query q5 = new Query(predicado);
		Hashtable<?,?> oneSolution = q5.oneSolution();
		Compound comp = (Compound) oneSolution.get("X");
		Term[] termArray = comp.toTermArray();
		return texto + Arrays.deepToString(termArray);
	}

	private static List<String> getPermisos(String archivo) throws ParserConfigurationException, SAXException, IOException {
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
				String p = "p" + attribute.substring(attribute.lastIndexOf(".") + 1, attribute.length());
				parametros.add(p.trim());
			}
		}
		return parametros;
	}
	
	private static String formatearCombinacion(String linea) {
		List<String> temp = new ArrayList<>();
		String[] split = linea.split(",");
		for (String s : split) {
			temp.add("p" + s.trim());
		}
		return Arrays.deepToString(temp.toArray());
	}
}