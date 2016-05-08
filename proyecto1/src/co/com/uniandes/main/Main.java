package co.com.uniandes.main;

import jpl.Query;

/**
 * @author juan
 * Para ejecutar se de agregar los siguientes parametros
 * Parametros de la maquina virtual: -Djava.library.path=/usr/lib/swi-prolog/lib/amd64
 * Parametros del programa: ruta completa del archivo prolog
 */
public class Main {

	public static void main(String[] args) {
		try {
			Query q1 = new Query("consult('" + args[0] + "')");
			System.out.println(q1.hasSolution() ? "Archivo cargado" : "Error");

			Query q4 = new Query("todos(X)");
			System.out.println(q4.oneSolution().get("X"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}