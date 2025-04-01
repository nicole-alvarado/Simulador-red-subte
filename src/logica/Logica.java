package logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import aplicacion.Constantes;
import controlador.Coordinador;
import modelo.Estacion;
import modelo.Tramo;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Position;
import net.datastructures.PositionalList;
import net.datastructures.Vertex;


public class Logica {

	private Graph<Estacion, Tramo> subte; // representa la red de subtes
	private Graph<Estacion, Integer> subteRapido; // grafo para resolver el recorrido mas rapido
	private Graph<Estacion, Integer> subteMenosTransbordos; // grafo para resolver el recorrido con menor número de transbordos
	private Graph<Estacion, Integer> subteMenosCongestionado; // grafo para resolver el recorrido menos congestionado
	private Hashtable<ArrayList<String>, Vertex<Estacion>> estaciones;
	private TreeMap<Integer, Double> congestiones;
	private Map<Estacion, Vertex<Estacion>> resRapido; 
	private Map<Estacion, Vertex<Estacion>> resMenosCongestionado;
	private Map<Estacion, Vertex<Estacion>> resMenosTransbordos;
	private Coordinador miCoordinador;
	
	
	public Logica(Graph<Estacion, Tramo> subte, Hashtable<ArrayList<String>, Vertex<Estacion>> estaciones,
			TreeMap<Integer, Double> congestiones) {

		this.subte = subte;
		this.estaciones = estaciones;
		this.congestiones = congestiones;
		copiarSubteRapido();
		copiarSubteMenosTransbordos();
		copiarSubteMenosCongestionado();

	}
	
	/**
	 *
	 * @return Retorna una copia de un grafo, que tendrá vértices de tipo Estacion, y los arcos contendrán las duraciones en minutos que 
	 * hay entre tramos.
	 */
	public Graph<Estacion, Integer> copiarSubteRapido() {
		subteRapido = new AdjacencyMapGraph<>(false);
		resRapido = new HashMap<>();
		
		for (Vertex<Estacion> result : subte.vertices()) {
			resRapido.put(result.getElement(), subteRapido.insertVertex(result.getElement()));
		}
		Vertex<Estacion>[] vert;

		for (Edge<Tramo> result : subte.edges()) {
			vert = subte.endVertices(result);
			subteRapido.insertEdge(resRapido.get(vert[0].getElement()), resRapido.get(vert[1].getElement()),
					result.getElement().getDuracion());
		}
		return subteRapido;
	}

	/**
	 * Genera el recorrido más rapido mediante el método shortestPathList, que implementa el algoritmo de Dijkstra. Devuelve el cámino más corto dados
	 * dos vértices, sumando los pesos de los arcos de un grafo.
	 * @param estacionA Estación origen.
	 * @param estacionDestino Estación destino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido más rápido dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> recorridoMasRapido(Estacion estacionOrigen, Estacion estacionDestino) {
		
		ArrayList<String> keysOrigen = new ArrayList<String>();
		keysOrigen.add(estacionOrigen.getLinea().getRed().getCodigo());
		keysOrigen.add(estacionOrigen.getLinea().getCodigo());
		keysOrigen.add(estacionOrigen.getCodigo());
		ArrayList<String> keysDestino = new ArrayList<String>();
		keysDestino.add(estacionDestino.getLinea().getRed().getCodigo());
		keysDestino.add(estacionDestino.getLinea().getCodigo());
		keysDestino.add(estacionDestino.getCodigo());
		
		return GraphAlgorithms.shortestPathList(subteRapido, resRapido.get(estaciones.get(keysOrigen).getElement()),
				resRapido.get(estaciones.get(keysDestino).getElement()));
	}

	/**
	 * 
	 * @return Retorna una copia de un grafo, que tendrá vertices de tipo Estacion y de arcos tendrá una constante TRASBORDO
	 * si es combinación de línea, y NO_TRASBORDO si no lo es.
	 */
	public Graph<Estacion, Integer> copiarSubteMenosTransbordos() {
		subteMenosTransbordos = new AdjacencyMapGraph<>(false);
		resMenosTransbordos = new HashMap<>();
		
		for (Vertex<Estacion> result : subte.vertices())
			resMenosTransbordos.put(result.getElement(), subteMenosTransbordos.insertVertex(result.getElement()));

		Vertex<Estacion>[] vertMenosTransbordos;

		for (Edge<Tramo> result : subte.edges()) {
			vertMenosTransbordos = subte.endVertices(result);
			
			if (result.getElement().getEsCombinacion()) {
				subteMenosTransbordos.insertEdge(resMenosTransbordos.get(vertMenosTransbordos[0].getElement()),
						resMenosTransbordos.get(vertMenosTransbordos[1].getElement()), Constantes.TRANSBORDO);
			} else {
				subteMenosTransbordos.insertEdge(resMenosTransbordos.get(vertMenosTransbordos[0].getElement()),
						resMenosTransbordos.get(vertMenosTransbordos[1].getElement()), Constantes.NO_TRANSBORDO);
			}

		}
		return subteMenosTransbordos;
	}

	/**
	 * Genera el recorrido con menor número de transbordos, mediante el método shortestPathList, que implementa el algoritmo de Dijkstra. Devuelve el cámino más corto dados
	 * dos vértices, sumando los pesos de los arcos de un grafo.
	 * @param estacionOrigen Estación origen.
	 * @param estacionDestino Estación destino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido con menor número de transbordos dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> recorridoMenosTransbordos(Estacion estacionOrigen, Estacion estacionDestino) {
		ArrayList<String> keysOrigen = new ArrayList<String>();
		keysOrigen.add(estacionOrigen.getLinea().getRed().getCodigo());
		keysOrigen.add(estacionOrigen.getLinea().getCodigo());
		keysOrigen.add(estacionOrigen.getCodigo());
		ArrayList<String> keysDestino = new ArrayList<String>();
		keysDestino.add(estacionDestino.getLinea().getRed().getCodigo());
		keysDestino.add(estacionDestino.getLinea().getCodigo());
		keysDestino.add(estacionDestino.getCodigo());
		return GraphAlgorithms.shortestPathList(subteMenosTransbordos, resMenosTransbordos.get(estaciones.get(keysOrigen).getElement()),
				resMenosTransbordos.get(estaciones.get(keysDestino).getElement()));
	}

	/**
	 * 
	 * @return Retorna una copia de un grafo, que tendrá vertices de tipo Estacion y de arcos enteros, los niveles de congestión.
	 */
	public Graph<Estacion, Integer> copiarSubteMenosCongestionado() {
		subteMenosCongestionado = new AdjacencyMapGraph<>(false);
		resMenosCongestionado = new HashMap<>();

		for (Vertex<Estacion> result : subte.vertices())
			resMenosCongestionado.put(result.getElement(), subteMenosCongestionado.insertVertex(result.getElement()));

		Vertex<Estacion>[] vertCongestion;

		for (Edge<Tramo> result : subte.edges()) {
			vertCongestion = subte.endVertices(result);
			double peso = congestiones.get(result.getElement().getTipo()) * result.getElement().getDuracion(); // contiene la multiplicación en tramos.
			int  pesoCasteado = (int) peso;
			subteMenosCongestionado.insertEdge(resMenosCongestionado.get(vertCongestion[0].getElement()),
					resMenosCongestionado.get(vertCongestion[1].getElement()), pesoCasteado);
		}
		return subteMenosCongestionado;
	}

	/**
	 * Genera el recorridoMenosCongestionado, mediante el método shortestPathList, que implementa el algoritmo de Dijkstra. Devuelve el cámino más corto dados
	 * dos vértices, sumando los pesos de los arcos de un grafo.
	 * @param estacionOrigen Estación origen.
	 * @param estacionDestino Estación destino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido con menor número de transbordos dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> recorridoMenosCongestionado(Estacion estacionOrigen, Estacion estacionDestino) {
		ArrayList<String> keysOrigen = new ArrayList<String>();
		keysOrigen.add(estacionOrigen.getLinea().getRed().getCodigo());
		keysOrigen.add(estacionOrigen.getLinea().getCodigo());
		keysOrigen.add(estacionOrigen.getCodigo());
		ArrayList<String> keysDestino = new ArrayList<String>();
		keysDestino.add(estacionDestino.getLinea().getRed().getCodigo());
		keysDestino.add(estacionDestino.getLinea().getCodigo());
		keysDestino.add(estacionDestino.getCodigo());
		return GraphAlgorithms.shortestPathList(subteMenosCongestionado,
				resMenosCongestionado.get(estaciones.get(keysOrigen).getElement()), resMenosCongestionado.get(estaciones.get(keysDestino).getElement()));
	}
	
	/**
	 * Crea el recorrido en tramos, desde una estación origen a destino, que se imprimirá en la pantalla.
	 * @param lista Lista posicional con las estaciones que forman parte del recorrido que se haya elegido. 
	 * @return Retorna un String "camino" que contiene la impresión final del recorrido.
	 */
	public String obtenerRecorrido(PositionalList<Vertex<Estacion>> lista) {
		String camino = ""; //
		Position<Vertex<Estacion>> aux = lista.first();
		Vertex<Estacion> verticeA, verticeB; // sirve para armar los tramos
		Edge<Tramo> tramo;
		int tiempoCaminando = 0;
		int tiempoSubte = 0; 

		while (lista.after(aux) != null) {
			verticeA = aux.getElement();
			aux = lista.after(aux);
			camino += (verticeA.getElement() + " (" + verticeA.getElement().getLinea().getNombre() + ") " + " ---> "
					+ aux.getElement().getElement() + " (" + aux.getElement().getElement().getLinea().getNombre() + ") ");
			verticeB = aux.getElement();
			
			ArrayList<String> keysOrigen = new ArrayList<String>();
			keysOrigen.add(verticeA.getElement().getLinea().getRed().getCodigo());
			keysOrigen.add(verticeA.getElement().getLinea().getCodigo());
			keysOrigen.add(verticeA.getElement().getCodigo());
			ArrayList<String> keysDestino = new ArrayList<String>();
			keysDestino.add(verticeB.getElement().getLinea().getRed().getCodigo());
			keysDestino.add(verticeB.getElement().getLinea().getCodigo());
			keysDestino.add(verticeB.getElement().getCodigo());
			tramo = subte.getEdge(estaciones.get(keysOrigen),
					estaciones.get(keysDestino));
			int nivelDeCongestion = tramo.getElement().getTipo();

			if (tramo.getElement().getEsCombinacion()) {
				camino += " =======> CAMBIO DE LÍNEA <======= \n";
				tiempoCaminando += tramo.getElement().getDuracion();
				camino += "Tiempo que se tarda caminando: " + (tramo.getElement().getDuracion()/60) + " minutos.\nDistancia: " +
				 (String.format("%.0f", Double.valueOf(tramo.getElement().getDuracion())/3600 * 
						miCoordinador.obtenerVelocidades().get("Caminando")*1000)) + " metros.\n\n" ;
			} else {
				camino += "\nDuración en subte: " + (tramo.getElement().getDuracion()/60);
				tiempoSubte += tramo.getElement().getDuracion();
				camino += " minutos.\nDistancia: " + (String.format("%.0f", Double.valueOf(tramo.getElement().getDuracion())/3600 * 
						miCoordinador.obtenerVelocidades().get("enSubte")*1000)) + " metros.\nNivel de congestión: " + nivelDeCongestion + ".\n\n";
			}
			
		}

		if(tiempoCaminando != 0) {			
			return camino = camino + "DURACIÓN DEL RECORRIDO:\nTiempo en subte: " + (tiempoSubte/60) + 
					" minutos.\nTiempo caminando: " + (tiempoCaminando/60)
				+ " minutos.\nTiempo total: " + ((tiempoSubte + tiempoCaminando)/60) + " minutos.";
		}else
			return camino = camino + "DURACIÓN DEL RECORRIDO:\nTiempo en subte: " + (tiempoSubte/60) + " minutos\nNo se encontraron cambios de líneas.";
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}


	
}