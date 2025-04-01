package empresa.dao.archivo;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import empresa.conexion.FConnection;
import empresa.dao.EstacionDAO;
import empresa.dao.TramoDAO;
import modelo.Estacion;
import modelo.Tramo;
import util.FileUtil;

public class TramoArchivoDAO implements TramoDAO {

	private RandomAccessFile file = null;
	private Hashtable<ArrayList<String>, Integer> index; // representa estaciones
	private int nDeleted;

	private static final int SIZE_RED= 15;
	private static final int SIZE_LINEA_ESTACION_ORIGEN = 15;
	private static final int SIZE_LINEA_ESTACION_DESTINO= 15;
	private static final int SIZE_ESTACION_ORIGEN = 15;
	private static final int SIZE_ESTACION_DESTINO = 15;
	private static final int SIZE_DURACION = 15;
	private static final int SIZE_TIPO_CONGESTION = 15;
	private static final int SIZE_ES_COMBINACION= 15;

	private static final int SIZE_RECORD = Character.BYTES 
			+ Character.BYTES * SIZE_RED
			+ Character.BYTES * SIZE_LINEA_ESTACION_ORIGEN
			+ Character.BYTES * SIZE_LINEA_ESTACION_DESTINO
			+ Character.BYTES * SIZE_ESTACION_ORIGEN
			+ Character.BYTES * SIZE_ESTACION_DESTINO 
			+ Character.BYTES * SIZE_DURACION 
			+ Character.BYTES * SIZE_TIPO_CONGESTION
			+ Character.BYTES * SIZE_ES_COMBINACION;

	private Hashtable<ArrayList<String>, Estacion> estaciones;

	public TramoArchivoDAO() {
		if (file == null) {
			
			file = FConnection.getInstancia("tramo");
			index = new Hashtable<ArrayList<String>, Integer>();
			nDeleted = 0;
			Tramo tramo;
			int pos = 0;
			char deleted;
			estaciones = cargarEstaciones();
			try {
				file.seek(0);
				while (true) {
					deleted = file.readChar();
					tramo = readRecord();
					if (deleted != FileUtil.DELETED) {
						ArrayList<String> c = new ArrayList<String>();
						c.add(tramo.getEstacion1().getCodigo());
						c.add(tramo.getEstacion2().getCodigo());	
						index.put(c, pos++);
					}
					else {
						nDeleted++;
					}
				}
			} catch (EOFException e) {
				return;
			} catch (IOException e) {

			}
		}
	}

	public List<Tramo> buscarTodos() {
		List<Tramo> retornar = new ArrayList<Tramo>();
		Tramo tramo;
		char deleted;
		estaciones = cargarEstaciones();
		try {
			file.seek(0);
			while (true) {
				deleted = file.readChar();
				tramo = readRecord();
				if (deleted != FileUtil.DELETED)
					retornar.add(tramo);
			}
		} catch (EOFException e) {
			return retornar;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void insertar(Tramo tramo) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(tramo.getEstacion1().getCodigo());
		c.add(tramo.getEstacion2().getCodigo());
		Integer pos = index.get(c);
		if (pos != null)
			return;
		int nr = (index.size() + nDeleted) * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(tramo);
			index.put(c, nr / SIZE_RECORD);
		} catch (IOException e) {
		}
	}

	@Override
	public void actualizar(Tramo tramo) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(tramo.getEstacion1().getCodigo());
		c.add(tramo.getEstacion2().getCodigo());
		Integer pos = index.get(c);
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(tramo);
		} catch (IOException e) {
		}

	}

	@Override
	public void borrar(Tramo tramo) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(tramo.getEstacion1().getCodigo());
		c.add(tramo.getEstacion2().getCodigo());
		Integer pos = index.get(c);
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(FileUtil.DELETED);
			index.remove(c);
			nDeleted++;
		} catch (IOException e) {
		}
	}

	public void pack() throws IOException {
		List<Tramo> tramos = buscarTodos();
		FConnection.backup("tramo");
		FConnection.delete("tramo");
		file = FConnection.getInstancia("tramo");
		index = new Hashtable<ArrayList<String>, Integer>();
		nDeleted = 0;
		for (Tramo t : tramos)
			insertar(t);
	}

	private Hashtable<ArrayList<String>, Estacion> cargarEstaciones() {
		Hashtable<ArrayList<String>, Estacion> estaciones = new Hashtable<ArrayList<String>, Estacion>();
		EstacionDAO estacionDAO = new EstacionArchivoDAO();
		List<Estacion> ls = estacionDAO.buscarTodos();
		for (Estacion e : ls) {
			ArrayList<String> c = new ArrayList<String>();
			c.add(e.getLinea().getRed().getCodigo());
			c.add(e.getLinea().getCodigo());
			c.add(e.getCodigo());
			estaciones.put(c, e);
		}
		return estaciones;
	}

	private Tramo readRecord() throws IOException {
		ArrayList<String> e1 = new ArrayList<String>();
		ArrayList<String> e2 = new ArrayList<String>();

		String codigoRed,lineaOrigen,lineaDestino,estacionOrigen,estacionDestino;
		int duracion, congestion;
		boolean combinacion;
		codigoRed = FileUtil.readString(file, SIZE_RED);
		lineaOrigen = FileUtil.readString(file, SIZE_LINEA_ESTACION_ORIGEN);
		lineaDestino = FileUtil.readString(file, SIZE_LINEA_ESTACION_DESTINO);
		estacionOrigen = FileUtil.readString(file, SIZE_ESTACION_ORIGEN);
		estacionDestino = FileUtil.readString(file, SIZE_ESTACION_DESTINO);
		duracion = Integer.parseInt(FileUtil.readString(file, SIZE_DURACION));
		congestion = Integer.parseInt(FileUtil.readString(file, SIZE_TIPO_CONGESTION));

		combinacion = esCombinacion(FileUtil.readString(file, SIZE_ES_COMBINACION));
		e1.add(codigoRed);
		e1.add(lineaOrigen);
		e1.add(estacionOrigen);
		
		e2.add(codigoRed);
		e2.add(lineaDestino);
		e2.add(estacionDestino);
	
		return new Tramo(estaciones.get(e1),
						estaciones.get(e2),
						duracion,
						congestion,
						combinacion);
		}
	private boolean esCombinacion(String s) {

		if (s.equals("True") ){
			
			return true;
		}
		return false;
	}
	private void writeRecord(Tramo tramo) throws IOException {
		FileUtil.writeString(file, tramo.getEstacion1().getLinea().getRed().getCodigo(), SIZE_RED);
		FileUtil.writeString(file, tramo.getEstacion1().getLinea().getCodigo(), SIZE_LINEA_ESTACION_ORIGEN);
		FileUtil.writeString(file, tramo.getEstacion2().getLinea().getCodigo(), SIZE_LINEA_ESTACION_DESTINO);
		FileUtil.writeString(file, tramo.getEstacion1().getCodigo(), SIZE_ESTACION_ORIGEN);
		FileUtil.writeString(file, tramo.getEstacion2().getCodigo(), SIZE_ESTACION_DESTINO);
		FileUtil.writeString(file, tramo.getDuracion()+"", SIZE_DURACION);
		FileUtil.writeString(file, tramo.getTipo()+"", SIZE_TIPO_CONGESTION);
		if (tramo.getEsCombinacion()) {
			FileUtil.writeString(file, "True", SIZE_TIPO_CONGESTION);
		}else {
			FileUtil.writeString(file, "False", SIZE_TIPO_CONGESTION);
		}
	}
}
