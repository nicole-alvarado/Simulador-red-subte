package negocio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

import controlador.Coordinador;
import empresa.servicio.EstacionService;
import empresa.servicio.EstacionServiceImpl;
import empresa.servicio.LineaService;
import empresa.servicio.LineaServiceImpl;
import empresa.servicio.RedService;
import empresa.servicio.RedServiceImpl;
import empresa.servicio.TramoService;
import empresa.servicio.TramoServiceImpl;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;

public class Empresa {

	private Coordinador miCoordinador;
	private ArrayList<RedSubte> redSubtes;
	private ArrayList<Linea> lineas;
	private ArrayList<Estacion> estaciones;
	private ArrayList<Tramo> tramos;

	private static TreeMap<Integer, Double> congestiones = new TreeMap<Integer, Double>();
	private static TreeMap<String, Double> velocidades = new TreeMap<String, Double>();
	
	private RedService redService;

	private LineaService lineaService;

	private EstacionService estacionService;

	private TramoService tramoService;

	private static Empresa empresa = null;

	public static Empresa getEmpresa() {
		if (empresa == null) {
			empresa = new Empresa();
		}
		return empresa;
	}

	private Empresa() {
		redSubtes = new ArrayList<RedSubte>();
		lineas = new ArrayList<Linea>();
		estaciones = new ArrayList<Estacion>();
		tramos = new ArrayList<Tramo>();
		redService = new RedServiceImpl();
		lineaService = new LineaServiceImpl();
		estacionService = new EstacionServiceImpl();
		tramoService = new TramoServiceImpl();

		redSubtes.addAll(redService.buscarTodos());

		lineas.addAll(lineaService.buscarTodos());
		
		estaciones.addAll(estacionService.buscarTodos());
		
		tramos.addAll(tramoService.buscarTodos());
	}

	///////////////// MÉTODOS PARA REDES /////////////////

	public RedSubte agregarRed(RedSubte red) throws RedExisteException {
		if (redSubtes.contains(red))
			throw new RedExisteException();
		redSubtes.add(red);
		redService.insertar(red);
		return red;
	}
	
	public RedSubte modificarRed(RedSubte red) {
		RedSubte r = buscarRed(red);
		int pos = redSubtes.indexOf(r);
		redSubtes.set(pos, red);
		redService.actualizar(red);
		return r;
	}

	public RedSubte eliminarRed(RedSubte red) {
		RedSubte r = buscarRed(red);
		int pos = redSubtes.indexOf(r);
		redSubtes.remove(pos);
		redService.borrar(red);
		return r;
	}

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

	//////////////// MÉTODOS PARA LÍNEAS /////////////////

	public Linea agregarLinea(Linea linea) throws LineaExisteException {
		if (lineas.contains(linea))
			throw new LineaExisteException();
		lineas.add(linea);
		lineaService.insertar(linea);
		return linea;
	}
	public Linea modificarLinea(Linea linea) {
		Linea l = buscarLinea(linea);
		lineas.remove(l);
		lineas.add(linea);
		lineaService.actualizar(linea);
		return l;
	}

	public Linea eliminarLinea(Linea linea) {
		Linea l = buscarLinea(linea);
		lineas.remove(l);
		lineaService.borrar(linea);
		return l;
	}

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
	///////////////// MÉTODOS PARA ESTACIONES /////////////////
	public Estacion agregarEstacion(Estacion estacion) throws EstacionExisteException {
		if (estaciones.contains(estacion))
			throw new EstacionExisteException();
		estaciones.add(estacion);
		estacionService.insertar(estacion);
		return estacion;
	}
	public Estacion modificarEstacion(Estacion estacion) {
		Estacion e = buscarEstacion(estacion);
		estaciones.remove(e);
		estaciones.add(estacion);
		estacionService.actualizar(estacion);
		return e;
	}

	public Estacion eliminarEstacion(Estacion estacion) {
		Estacion e = buscarEstacion(estacion);
		estaciones.remove(e);
		estacionService.borrar(estacion);
		return e;
	}

	public ArrayList<Estacion> getEstaciones() {
		return estaciones;
	}

	public void setEstaciones(ArrayList<Estacion> estaciones) {
		this.estaciones = estaciones;
	}

	public Estacion buscarEstacion(Estacion estacion) {
		int pos = estaciones.indexOf(estacion);
		if (pos == -1)
			return null;
		return estaciones.get(pos);
	}
	
	////////// MÉTODOS PARA TRAMOS /////////////////
	public Tramo agregarTramo(Tramo tramo) throws TramoExisteException {
		Tramo t = new Tramo(tramo.getEstacion2(),tramo.getEstacion1(),0,0,false);
		if ((tramos.contains(tramo)) || (tramos.contains(t)))
			throw new TramoExisteException();
		tramos.add(tramo);
		tramoService.insertar(tramo);
		return tramo;
	}
	public Tramo modificarTramo(Tramo tramo) {
		Tramo t = buscarTramo(tramo);
		tramos.remove(t);
		tramos.add(tramo);
		tramoService.actualizar(tramo);
		return t;
	}

	public Tramo eliminarTramo(Tramo tramo) {
		Tramo t = buscarTramo(tramo);
		tramos.remove(t);
		tramoService.borrar(tramo);
		return t;
	}

	public ArrayList<Tramo> getTramos() {
		return tramos;
	}

	public void setTramos(ArrayList<Tramo> tramos) {
		this.tramos = tramos;
	}

	public Tramo buscarTramo(Tramo tramo) {
		for (Tramo t : tramos) {
			
			if (t.equals(tramo)) {
				return t;
			}
		}
		return null;
	}
	
	/***
	 * Lee el archivo de configuraciones, donde se encuentran los nombres del archivo y propiedades.
	 * @param nombreArchivo Nombre del archivo a leer.
	 * @throws IOException Error en la entrada/salida.
	 */
	public static void leerArchivoConfiguraciones(String nombreArchivo) throws IOException  {

		InputStream input = new FileInputStream(nombreArchivo); //lee todo el archivo de texto
		Properties prop = new Properties(); // 

		prop.load(input);
		String archivoCongestiones = prop.getProperty("Congestiones");
		String archivoVelocidades = prop.getProperty("Velocidades");
		establecerCongestiones(archivoCongestiones);
		establecerVelocidades(archivoVelocidades);
	}

	/***
	 * Lee el archivo de congestiones y almacena en un mapa las congestiones con sus respectivas claves para identificarlas.
	 * @param nombreArchivo Nombre del archivo a leer.
	 * @throws IOException Error en la entrada/salida.
	 */
	public static void establecerCongestiones(String nombreArchivo) throws IOException  {
		
		InputStream input = new FileInputStream(nombreArchivo); //lee todo el archivo de texto
		Properties prop = new Properties(); // 
		prop.load(input);
		
		congestiones.put(1, Double.parseDouble(prop.getProperty("congestionBaja")));
		congestiones.put(2, Double.parseDouble(prop.getProperty("congestionMedia")));
		congestiones.put(3, Double.parseDouble(prop.getProperty("congestionAlta")));

	}
	
	/***
	 * Lee el archivo de velocidades y almacena en un mapa las velocidades con sus respectivas claves para identificarlas.
	 * @param nombreArchivo Nombre del archivo a leer.
	 * @throws IOException Error en la entrada/salida.
	 */
	public static void establecerVelocidades(String nombreArchivo) throws IOException  {
		
		InputStream input = new FileInputStream(nombreArchivo); //lee todo el archivo de texto
		Properties prop = new Properties(); // 
		// load a properties file from class path, inside static method
		prop.load(input);
		
		velocidades.put("Caminando", Double.parseDouble(prop.getProperty("velocidad_caminando")));
		velocidades.put("enSubte", Double.parseDouble(prop.getProperty("velocidad_subte")));

	}

	/***
	 * 
	 * @return Un ArrayLisy con todas congestiones que fueron cargadas en un mapa.
	 */
	public ArrayList<Integer> obtenerCongestiones() {
		ArrayList<Integer> congestiones = new ArrayList<Integer>();
		for (Integer c : getCongestiones().keySet()) {
			congestiones.add(c);
		}
		return congestiones;
	}
	
	public static TreeMap<Integer, Double> getCongestiones(){
		return congestiones;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}

	public TreeMap<String,Double> obtenerVelocidades() {
		return velocidades;
	}

}
