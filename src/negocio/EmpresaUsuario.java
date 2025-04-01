package negocio;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import controlador.Coordinador;

import logica.Logica;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Graph;
import net.datastructures.Vertex;

public class EmpresaUsuario {

	private Coordinador miCoordinador;
	private ArrayList<RedSubte> redSubtes;
	private ArrayList<Linea> lineas;
	private Graph<Estacion, Tramo> subte;
	private Hashtable<ArrayList<String>, Vertex<Estacion>> estaciones;

	private static TreeMap<Integer, Double> congestiones = new TreeMap<Integer, Double>(); 
	
	private Logica logica;

	public EmpresaUsuario() {
		
	}
	
	/***
	 * Carga todos los datos necesarios para la resolución del problema en sus respectivos tipos de estructura.
	 * Además de inicializar la clase Logica pasandole dichas estructuras.
	 */
	public void cargarDatos() {
		redSubtes = new ArrayList<RedSubte>();
		lineas = new ArrayList<Linea>();
		estaciones = new Hashtable<ArrayList<String>, Vertex<Estacion>>();
		subte =  new AdjacencyMapGraph<>(false);
		redSubtes.addAll(miCoordinador.listaRedSubtes());
		lineas.addAll(miCoordinador.listaLineas());
		
		for (Estacion e: miCoordinador.listaEstaciones()) {
			ArrayList<String> keys = new ArrayList<String>();
			keys.add(e.getLinea().getRed().getCodigo());
			keys.add(e.getLinea().getCodigo());
			keys.add(e.getCodigo());
			estaciones.put(keys, subte.insertVertex(e));
		}
		
		for (Tramo t: miCoordinador.listaTramos()){
			subte.insertEdge(buscarEstacion(t.getEstacion1()), buscarEstacion(t.getEstacion2()), t);
		}
		logica = new Logica(subte,estaciones,Empresa.getCongestiones());
		miCoordinador.setMiLogica(logica);
		logica.setMiCoordinador(miCoordinador);
	}

	////////////////// MÉTODOS PARA REDES ///////////////////////

	public ArrayList<RedSubte> getRedSubtes() {
		return redSubtes;
	}

	public void setRedSubtes(ArrayList<RedSubte> redSubtes) {
		this.redSubtes = redSubtes;
	}

	public RedSubte buscarRed(RedSubte red) {
		int pos = redSubtes.indexOf(red);
		if (pos == -1)
			return null;
		return redSubtes.get(pos);
	}

	//////////////// MÉTODOS PARA LINEAS //////////////

	public ArrayList<Linea> getLineas() {
		return lineas;
	}

	public void setLineas(ArrayList<Linea> lineas) {
		this.lineas = lineas;
	}

	public Linea buscarLinea(Linea linea) {
		int pos = lineas.indexOf(linea);
		if (pos == -1)
			return null;
		return lineas.get(pos);
	}
	//////////////// MÉTODOS PARA ESTACIONES//////////////////////////// 

	public ArrayList<Estacion> getEstaciones() {
		ArrayList<Estacion> lista = new ArrayList<Estacion>();
		for (Vertex<Estacion> e : estaciones.values()) {
			lista.add(e.getElement());
		}
		
		return lista;
	}

	public Vertex<Estacion>buscarEstacion(Estacion estacion) {
		ArrayList<String> keys = new ArrayList<String>();
		keys.add(estacion.getLinea().getRed().getCodigo());
		keys.add(estacion.getLinea().getCodigo());
		keys.add(estacion.getCodigo());
		return estaciones.get(keys);
	}

	public ArrayList<Integer> obtenerCongestiones() {
		ArrayList<Integer> congestiones = new ArrayList<Integer>();
		for (Integer c : getCongestiones().keySet()) {
			congestiones.add(c);
		}
		return congestiones;
	}
	
	public TreeMap<Integer, Double> getCongestiones(){
		return congestiones;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
			
}
