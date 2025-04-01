package empresa.dao.archivo;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import empresa.conexion.FConnection;
import empresa.dao.EstacionDAO;
import empresa.dao.LineaDAO;
import modelo.Estacion;
import modelo.Linea;
import util.FileUtil;

public class EstacionArchivoDAO implements EstacionDAO {

	private RandomAccessFile file = null;
	private Hashtable<ArrayList<String>, Integer> index; // representa estaciones
	private int nDeleted;

	private static final int SIZE_CODIGO = 15;
	private static final int SIZE_NOMBRE = 90;
	private static final int SIZE_CODIGO_LINEA = 15;
	private static final int SIZE_CODIGO_RED = 15;

	private static final int SIZE_RECORD = Character.BYTES + Character.BYTES * SIZE_CODIGO
			+ Character.BYTES * SIZE_NOMBRE + Character.BYTES * SIZE_CODIGO_LINEA + Character.BYTES * SIZE_CODIGO_RED;

	private Hashtable<ArrayList<String>, Linea> lineas;


	public EstacionArchivoDAO() {
		if (file == null) {
			file = FConnection.getInstancia("estacion");
			index = new Hashtable<ArrayList<String>, Integer>();
			nDeleted = 0;
			Estacion estacion;
			int pos = 0;
			char deleted;
			lineas = cargarLineas();
			try {
				file.seek(0);
				while (true) {
					deleted = file.readChar();
					estacion = readRecord();
					
					if (deleted != FileUtil.DELETED) {
						ArrayList<String> e = new ArrayList<String>();
						e.add(estacion.getLinea().getRed().getCodigo());
						e.add(estacion.getLinea().getCodigo());
						e.add(estacion.getCodigo());
						index.put(e, pos++);
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

	public List<Estacion> buscarTodos() {
		List<Estacion> retornar = new ArrayList<Estacion>();
		Estacion estacion;
		char deleted;
		lineas = cargarLineas();
		try {
			file.seek(0);
			while (true) {
				deleted = file.readChar();
				estacion = readRecord();
				if (deleted != FileUtil.DELETED)
					retornar.add(estacion);
			}
		} catch (EOFException e) {
			return retornar;
		} catch (IOException e) {
			return null;
		}
	}
	
	@Override
	public void insertar(Estacion estacion) {
		ArrayList<String> e = new ArrayList<String>();
		e.add(estacion.getLinea().getRed().getCodigo());
		e.add(estacion.getLinea().getCodigo());
		e.add(estacion.getCodigo());
		Integer pos = index.get(e);
		if (pos != null)
			return;
		int nr = (index.size() + nDeleted) * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(estacion);
			index.put(e, nr / SIZE_RECORD);
		} catch (IOException ex) {
		}
	}

	@Override
	public void actualizar(Estacion estacion) {
		ArrayList<String> e = new ArrayList<String>();
		e.add(estacion.getLinea().getRed().getCodigo());
		e.add(estacion.getLinea().getCodigo());
		e.add(estacion.getCodigo());
		Integer pos = index.get(e);
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(estacion);
		} catch (IOException ex) {
		}

	}

	@Override
	public void borrar(Estacion estacion) {
		ArrayList<String> e = new ArrayList<String>();
		e.add(estacion.getLinea().getRed().getCodigo());
		e.add(estacion.getLinea().getCodigo());
		e.add(estacion.getCodigo());
		Integer pos = index.get(e);
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(FileUtil.DELETED);
			index.remove(e);
			nDeleted++;
		} catch (IOException ex) {
		}
	}

	public void pack() throws IOException {
		List<Estacion> estaciones = buscarTodos();
		FConnection.backup("estacion");
		FConnection.delete("estacion");
		file = FConnection.getInstancia("estacion");
		index = new Hashtable<ArrayList<String>, Integer>();
		nDeleted = 0;
		for (Estacion en : estaciones)
			insertar(en);
	}

	private Hashtable<ArrayList<String>, Linea> cargarLineas() {
		Hashtable<ArrayList<String>, Linea> lineas = new Hashtable<ArrayList<String>, Linea>();
		LineaDAO lineaDAO = new LineaArchivoDAO();
		List<Linea> ls = lineaDAO.buscarTodos();
		for (Linea l : ls) {
			ArrayList<String> c = new ArrayList<String>();
			c.add(l.getRed().getCodigo());
			c.add(l.getCodigo());
			lineas.put(c, l);
		}
		return lineas;
	}

	private Estacion readRecord() throws IOException {
		ArrayList<String> c = new ArrayList<String>();
		c.add(FileUtil.readString(file, SIZE_CODIGO_RED));
		c.add(FileUtil.readString(file, SIZE_CODIGO_LINEA));
		return new Estacion(FileUtil.readString(file, SIZE_CODIGO),
				FileUtil.readString(file, SIZE_NOMBRE), lineas.get(c));
	}

	private void writeRecord(Estacion estacion) throws IOException {
		FileUtil.writeString(file, estacion.getLinea().getRed().getCodigo(), SIZE_CODIGO_RED);
		FileUtil.writeString(file, estacion.getLinea().getCodigo(), SIZE_CODIGO_LINEA);
		FileUtil.writeString(file, estacion.getCodigo(), SIZE_CODIGO);
		FileUtil.writeString(file, estacion.getNombre(), SIZE_NOMBRE);
		
		

	}
}
