package empresa.dao.archivo;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import empresa.conexion.FConnection;
import empresa.dao.LineaDAO;
import empresa.dao.RedDAO;
import modelo.Linea;
import modelo.RedSubte;
import util.FileUtil;

public class LineaArchivoDAO implements LineaDAO {

	private RandomAccessFile file = null;
	private Hashtable<ArrayList<String>, Integer> index;
	private int nDeleted;

	private static final int SIZE_CODIGO_RED = 15;
	private static final int SIZE_CODIGO = 15;
	private static final int SIZE_NOMBRE = 15;
	

	private static final int SIZE_RECORD = Character.BYTES 
			+ Character.BYTES * SIZE_CODIGO_RED
			+ Character.BYTES * SIZE_CODIGO
			+ Character.BYTES * SIZE_NOMBRE ;
	
	private Hashtable<String, RedSubte> redes;

	public LineaArchivoDAO() {
		if (file == null) {
			file = FConnection.getInstancia("linea");
			index = new Hashtable<ArrayList<String>, Integer>();
			nDeleted = 0;
			Linea linea;
			int pos = 0;
			char deleted;
			redes = cargarRedes();
			try {
				file.seek(0);
				while (true) {
					deleted = file.readChar();
					linea = readRecord();
					if (deleted != FileUtil.DELETED) {
						ArrayList<String> c = new ArrayList<String>();
						c.add(linea.getRed().getCodigo());
						c.add(linea.getCodigo());
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

	public List<Linea> buscarTodos() {
		List<Linea> ret = new ArrayList<Linea>();
		Linea linea;
		char deleted;
		redes = cargarRedes();
		try {
			file.seek(0);
			while (true) {
				deleted = file.readChar();
				linea = readRecord();
				if (deleted != FileUtil.DELETED)
					ret.add(linea);
			}
		} catch (EOFException e) {
			return ret;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void insertar(Linea linea) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(linea.getRed().getCodigo());
		c.add(linea.getCodigo());
		Integer pos = index.get(c);
		if (pos != null)
			return;
		int nr = (index.size() + nDeleted) * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(linea);
			index.put(c, nr / SIZE_RECORD);
		} catch (IOException e) {
		}
	}

	@Override
	public void actualizar(Linea linea) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(linea.getRed().getCodigo());
		c.add(linea.getCodigo());
		Integer pos = index.get(c);
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(linea);
		} catch (IOException e) {
		}

	}

	@Override
	public void borrar(Linea linea) {
		ArrayList<String> c = new ArrayList<String>();
		c.add(linea.getRed().getCodigo());
		c.add(linea.getCodigo());
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
	private Hashtable<String, RedSubte> cargarRedes() {
		Hashtable<String, RedSubte> redes = new Hashtable<String, RedSubte>();
		RedDAO redDAO = new RedArchivoDAO();
		List<RedSubte> rs = redDAO.buscarTodos();
		for (RedSubte l : rs) {
			redes.put(l.getCodigo(), l);
		}
		return redes;
	}

	public void pack() throws IOException {
		List<Linea> lineas = buscarTodos();
		FConnection.backup("linea");
		FConnection.delete("linea");
		file = FConnection.getInstancia("linea");
		index = new Hashtable<ArrayList<String>, Integer>();
		nDeleted = 0;
		for (Linea la : lineas)
			insertar(la);
	}

	private Linea readRecord() throws IOException {
		return new Linea(redes.get(FileUtil.readString(file, SIZE_CODIGO_RED)), FileUtil.readString(file, SIZE_CODIGO),
				FileUtil.readString(file, SIZE_NOMBRE));
	}

	private void writeRecord(Linea linea) throws IOException {
		FileUtil.writeString(file, linea.getRed().getCodigo(), SIZE_CODIGO_RED);
		FileUtil.writeString(file, linea.getCodigo(), SIZE_CODIGO);
		FileUtil.writeString(file, linea.getNombre(), SIZE_NOMBRE);
		
	}
}
