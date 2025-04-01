package controlador;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.JPanel;

import aplicacion.Constantes;
import logica.Logica;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;
import negocio.Empresa;
import negocio.EmpresaUsuario;
import negocio.EstacionExisteException;
import negocio.LineaExisteException;
import negocio.RedExisteException;
import negocio.TramoExisteException;
import net.datastructures.PositionalList;
import net.datastructures.Vertex;
import vista.administrador.EstacionForm;
import vista.administrador.EstacionList;
import vista.administrador.LineaForm;
import vista.administrador.LineaList;
import vista.administrador.RedForm;
import vista.administrador.RedList;
import vista.administrador.TramoForm;
import vista.administrador.TramoList;
import vista.administrador.Visualizacion;
import vista.aplicacion.DesktopFrame;
import vista.aplicacion.Principal;
import vista.usuario.PantallaUsuario;
import vista.usuario.Recorrido;

public class Coordinador {

	private RedForm miRedForm;
	private LineaForm miLineaForm;
	private EstacionForm miEstacionForm;
	private TramoForm miTramoForm;
	
	private RedList miRedList;
	private LineaList miLineaList;
	private EstacionList miEstacionList;
	private TramoList miTramoList;
	
	private Empresa miEmpresa;
	private DesktopFrame miDesktopFrame;
	private Logica miLogica;
	private Principal miVentanaPrincipal;
	private PantallaUsuario miPantallaUsuario;
	private Visualizacion miVisualizacion;
	private EmpresaUsuario miEmpresaUsuario;
	private Recorrido miRecorrido;

	/////////////////// MÉTODOS PARA REDES///////////////////////////
	public void agregarRedForm() {
		miRedForm.accion(Constantes.AGREGAR, null);
		miRedForm.setVisible(true);
	}

	public void modificarRedForm(RedSubte red) {
		miRedForm.accion(Constantes.MODIFICAR, red);
		miRedForm.setVisible(true);
	}

	public void eliminarRedForm(RedSubte red) {
		miRedForm.accion(Constantes.ELIMINAR, red);
		miRedForm.setVisible(true);
	}
	
	public void cancelarRed() {
		miRedList.setAccion(Constantes.CANCELAR);
		miRedForm.setVisible(false);
	}
	
	public void agregarRed(RedSubte red) throws RedExisteException {
		miEmpresa.agregarRed(red);
		miRedForm.setVisible(false);
		miRedList.addRow(red);
	}

	public void modificarRed(RedSubte red) {
		miEmpresa.modificarRed(red);
		miRedList.setAccion(Constantes.MODIFICAR);
		miRedList.setRed(red);
		miRedForm.setVisible(false);		
	}

	public void eliminarRed(RedSubte red) {
		miEmpresa.eliminarRed(red);		
		miRedList.setAccion(Constantes.ELIMINAR);
		miRedForm.setVisible(false);	
	}
	
	public void mostrarRedList() {
		miRedList.loadTable();
		miRedList.setVisible(true);

	}
	
	/***
	 * Método para visualizar la red de subte ingresada, junto a sus líneas y estacion respectivamente.
	 * @param Red.
	 */
	public void visualizarRed(RedSubte red) {
		miVisualizacion.cargarLista(red);
		miVisualizacion.setVisible(true);
		miRedList.setVisible(false);	
	}
		
	/////////////////// MÉTODOS PARA LÍNEAS///////////////////////////
	public void agregarLineaForm(String red) {
		miLineaForm.accion(Constantes.AGREGAR, null);
		miLineaForm.cargarRedes();
		setRed(red);
		miLineaForm.setVisible(true);
	}

	public void modificarLineaForm(Linea linea) {
		miLineaForm.accion(Constantes.MODIFICAR, linea);
		miLineaForm.setVisible(true);
	}

	public void eliminarLineaForm(Linea linea) {
		miLineaForm.accion(Constantes.ELIMINAR, linea);
		miLineaForm.setVisible(true);
	}
	
	public void cancelarLinea() {
		miLineaList.setAccion(Constantes.CANCELAR);
		miLineaForm.setVisible(false);
	}
	
	public void agregarLinea(Linea linea) throws LineaExisteException {
		miEmpresa.agregarLinea(linea);
		miLineaForm.setVisible(false);
		miLineaList.addRow(linea);
	}

	public void modificarLinea(Linea linea) {
		miEmpresa.modificarLinea(linea);
		miLineaList.setAccion(Constantes.MODIFICAR);
		miLineaList.setLinea(linea);
		miLineaForm.setVisible(false);		
	}

	public void eliminarLinea(Linea linea) {
		miEmpresa.eliminarLinea(linea);		
		miLineaList.setAccion(Constantes.ELIMINAR);
		miLineaForm.setVisible(false);	
	}
	
	public void mostrarLineaList() {
		miLineaList.loadTable();
		miLineaList.cargarRedes();
		miLineaList.setVisible(true);
	}
	
	/////////////////// MÉTODOS PARA ESTACIONES///////////////////////////
	public void agregarEstacionForm(String red, String linea) {
		miEstacionForm.accion(Constantes.AGREGAR, null);
		miEstacionForm.cargarRedes();
		setRedEstacion(red);
		setLinea(linea);
		miEstacionForm.setVisible(true);
	}

	public void modificarEstacionForm(Estacion estacion) {
		miEstacionForm.accion(Constantes.MODIFICAR, estacion);
		miEstacionForm.setVisible(true);
	}

	public void eliminarEstacionForm(Estacion estacion) {
		miEstacionForm.accion(Constantes.ELIMINAR, estacion);
		miEstacionForm.setVisible(true);
	}
	
	public void cancelarEstacion() {
		miEstacionList.setAccion(Constantes.CANCELAR);
		miEstacionForm.setVisible(false);
	}
	
	public void agregarEstacion(Estacion estacion) throws EstacionExisteException {
		miEmpresa.agregarEstacion(estacion);
		miEstacionForm.setVisible(false);
		miEstacionList.addRow(estacion);
	}

	public void modificarEstacion(Estacion estacion) {
		miEmpresa.modificarEstacion(estacion);
		miEstacionList.setAccion(Constantes.MODIFICAR);
		miEstacionList.setEstacion(estacion);
		miEstacionForm.setVisible(false);		
	}

	public void eliminarEstacion(Estacion estacion) {
		miEmpresa.eliminarEstacion(estacion);		
		miEstacionList.setAccion(Constantes.ELIMINAR);
		miEstacionForm.setVisible(false);	
	}
	
	public void mostrarEstacionList() {
		miEstacionList.loadTable();
		miEstacionList.cargarRedes();
		miEstacionList.setVisible(true);
	}
	
	////////////////////// MÉTODOS PARA TRAMOS ///////////////////////
	public void agregarTramoForm() {
		miTramoForm.accion(Constantes.AGREGAR, null);
		miTramoForm.setVisible(true);
	}

	public void modificarTramoForm(Tramo tramo) {
		miTramoForm.accion(Constantes.MODIFICAR, tramo);
		miTramoForm.setVisible(true);
	}

	public void eliminarTramoForm(Tramo tramo) {
		miTramoForm.accion(Constantes.ELIMINAR, tramo);
		miTramoForm.setVisible(true);
	}
	
	public void cancelarTramo() {
		miTramoList.setAccion(Constantes.CANCELAR);
		miTramoForm.setVisible(false);
	}
	
	public void agregarTramo(Tramo tramo) throws TramoExisteException {
		miEmpresa.agregarTramo(tramo);
		miTramoForm.setVisible(false);
		miTramoList.addRow(tramo);
	}

	public void modificarTramo(Tramo tramo) {
		miEmpresa.modificarTramo(tramo);
		miTramoList.setAccion(Constantes.MODIFICAR);
		miTramoList.setTramo(tramo);
		miTramoForm.setVisible(false);		
	}

	public void eliminarTramo(Tramo tramo) {
		miEmpresa.eliminarTramo(tramo);		
		miTramoList.setAccion(Constantes.ELIMINAR);
		miTramoForm.setVisible(false);	
	}
	
	public void mostrarTramoList() {
		miTramoList.loadTable();
		miTramoList.cargarRedes();
		miTramoList.setVisible(true);
	}

	////////// MÉTODOS PARA REDES ///////////////////
	public ArrayList<RedSubte> listaRedSubtes() {
		return miEmpresa.getRedSubtes();
	}

	public RedSubte buscarRed(RedSubte redSubte) {
		return miEmpresa.buscarRed(redSubte);
	}

	////////// MÉTODOS PARA LÍNEAS ///////////////////
	public ArrayList<Linea> listaLineas() {
		return  miEmpresa.getLineas();
	}

	public Linea buscarLinea(Linea linea) {
		return miEmpresa.buscarLinea(linea);
	}
	
	////////// MÉTODOS PARA ESTACIONES///////////////////
	public ArrayList<Estacion> listaEstaciones() {
		return miEmpresa.getEstaciones();
	}

	public Estacion buscarEstacion(Estacion estacion) {
		return miEmpresa.buscarEstacion(estacion);
	}
	
	////////// MÉTODOS PARA TRAMOS ///////////////////
	public ArrayList<Tramo> listaTramos() {
		return  miEmpresa.getTramos();
	}

	public Tramo buscarTramo(Tramo tramo) {
		Tramo t =miEmpresa.buscarTramo(tramo);
		return t;
	}

	public ArrayList<Integer> obtenerCongestiones() {
		return miEmpresa.obtenerCongestiones();
	}
	
	public TreeMap<String,Double> obtenerVelocidades() {
		return miEmpresa.obtenerVelocidades();
	}
	
	
	/***
	 * Muestra la ventana PantallaUsuario, para realizar la búsqueda de los recorridos,
	 * cargando las redes previamente creadas y datos asociados a tales redes.
	 */
	public void mostrarPantallaUsuario() {		miPantallaUsuario.cargarRedes();
		miEmpresaUsuario.cargarDatos();
		miPantallaUsuario.setVisible(true);
	}	
	/***
	 * Hace visible la ventana del menú perteneciente a la parte del administrador.
	 */
	public void mostrarPantallaAdministrador() {		miDesktopFrame.setVisible(true);	}
	/***
	 * Llama al método procesarRecorridos(), que recibe como parámetros las estaciones origen y destino,
	 * para obtener los recorridos.
	 * @param estacionOrigen.
	 * @param estacionDestino.
	 */
	public void mostrarVentanaRecorrido(Estacion estacionOrigen, Estacion estacionDestino) {
		miRecorrido.procesarRecorridos(estacionOrigen, estacionDestino);
		miRecorrido.setVisible(true);
	}
	
	//////// MÉTODOS PARA LA BÚSQUEDA DE RECORRIDOS /////////
	/***
	 * 
	 * @param estacionOrigen.
	 * @param estacionDestino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido más rápido dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> buscarRecorridoMasRapido(Estacion estacionOrigen,
			Estacion estacionDestino) {
		return miLogica.recorridoMasRapido(estacionOrigen, estacionDestino);
	}

	/**
	 * 
	 * @param estacionOrigen.
	 * @param estacionDestino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido con menor combinaciones de líneas dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> buscarRecorridoMenorCombinacion(Estacion estacionOrigen,
			Estacion estacionDestino) {
		return miLogica.recorridoMenosTransbordos(estacionOrigen, estacionDestino);
	}

	/***
	 * 
	 * @param estacionOrigen.
	 * @param estacionDestino.
	 * @return Lista posicional con las estaciones que forman parte del recorrido menos congestionado dadas dos estaciones.
	 */
	public PositionalList<Vertex<Estacion>> buscarRecorridoMenosCongestionado(Estacion estacionOrigen,
			Estacion estacionDestino) {
		return miLogica.recorridoMenosCongestionado(estacionOrigen, estacionDestino);
	}
	
	/***
	 * 
	 * @param lista posicional, recorrido.
	 * @return Retorna un String "camino" que contiene la impresión final del recorrido.
	 */

	public String obtenerRecorrido(PositionalList<Vertex<Estacion>> recorrido) {
		return miLogica.obtenerRecorrido(recorrido);
	}
	
	//Getters and Setters
	public RedForm getMiRedForm() {
		return miRedForm;
	}
	
	public JPanel getPanelRedForm() {
		return miRedForm.getRedForm();
	}


	public void setMiRedForm(RedForm miRedForm) {
		this.miRedForm = miRedForm;
	}

	public LineaForm getMiLineaForm() {
		return miLineaForm;
	}

	public void setMiLineaForm(LineaForm miLineaForm) {
		this.miLineaForm = miLineaForm;
	}

	public EstacionForm getMiEstacionForm() {
		return miEstacionForm;
	}

	public void setMiEstacionForm(EstacionForm miEstacionForm) {
		this.miEstacionForm = miEstacionForm;
	}

	public TramoForm getMiTramoForm() {
		return miTramoForm;
	}

	public void setMiTramoForm(TramoForm miTramoForm) {
		this.miTramoForm = miTramoForm;
	}

	public RedList getMiRedList() {
		return miRedList;
	}

	public void setMiRedList(RedList miRedList) {
		this.miRedList = miRedList;
	}

	public LineaList getMiLineaList() {
		return miLineaList;
	}

	public void setMiLineaList(LineaList miLineaList) {
		this.miLineaList = miLineaList;
	}

	public EstacionList getMiEstacionList() {
		return miEstacionList;
	}

	public void setMiEstacionList(EstacionList miEstacionList) {
		this.miEstacionList = miEstacionList;
	}

	public TramoList getMiTramoList() {
		return miTramoList;
	}

	public void setMiTramoList(TramoList miTramoList) {
		this.miTramoList = miTramoList;
	}

	public Logica getMiLogica() {
		return miLogica;
	}

	public void setMiLogica(Logica miLogica) {
		this.miLogica = miLogica;
	}
		
	public Empresa getMiClaseMaster() {
		return miEmpresa;
	}
	
	public void setMiClaseMaster(Empresa miClaseMaster) {
		this.miEmpresa = miClaseMaster;
	}
	
	public DesktopFrame getMiDesktopFrame() {
		return miDesktopFrame;
	}

	public void setMiDesktopFrame(DesktopFrame miDesktopFrame) {
		this.miDesktopFrame = miDesktopFrame;
	}
		
	public Visualizacion getMiVisualizacion() {
		return miVisualizacion;
	}

	public void setMiVisualizacion(Visualizacion miVisualizacion) {
		this.miVisualizacion = miVisualizacion;
	}

	public void setMiVentanaPrincipal(Principal miVentanaPrincipal) {		
	this.miVentanaPrincipal = miVentanaPrincipal;	
	}	
	public void setMiPantallaUsuario(PantallaUsuario miPantallaUsuario) {		
	this.miPantallaUsuario = miPantallaUsuario;	
	}

	public void setMiEmpresaUsuario(EmpresaUsuario miEmpresaUsuario) {
		this.miEmpresaUsuario = miEmpresaUsuario;
	}
	
	public void setMiRecorrido(Recorrido miRecorrido) {
		this.miRecorrido = miRecorrido;
	}

	public void setRed(String red) {
		miLineaForm.setRed(red);	
	}
	
	public void setRedEstacion(String red) {
		miEstacionForm.setRed(red);	
	}
	
	public void setLinea(String linea) {
		miEstacionForm.setLinea(linea);	
	}
	
	
}
