package empresa.dao.archivo;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import empresa.conexion.FConnection;
import empresa.dao.RedDAO;
import modelo.RedSubte;
import util.FileUtil;

public class RedArchivoDAO implements RedDAO {

	private RandomAccessFile file = null;
	private Hashtable<String, Integer> index;
	private int nDeleted;

	private static final int SIZE_CODIGO = 15;
	private static final int SIZE_NOMBRE = 30;
	private static final int SIZE_CIUDAD = 15;

	private static final int SIZE_RECORD = Character.BYTES 	+ Character.BYTES * SIZE_CODIGO
															+ Character.BYTES * SIZE_NOMBRE
															+ Character.BYTES * SIZE_CIUDAD;

	public RedArchivoDAO() {
		if (file == null) {
			file = FConnection.getInstancia("red");
			index = new Hashtable<String, Integer>();
			nDeleted = 0;
			RedSubte red;
			int pos = 0;
			char deleted;
			try {
				file.seek(0);
				while (true) {
					deleted = file.readChar();
					red = readRecord();
					if (deleted != FileUtil.DELETED)
						index.put(red.getCodigo(), pos++);
					else
						nDeleted++;
				}
			} catch (EOFException e) {
				return;
			} catch (IOException e) {

			}
		}
	}

	public List<RedSubte> buscarTodos() {
		List<RedSubte> ret = new ArrayList<RedSubte>();
		RedSubte red;
		char deleted;
		try {
			file.seek(0);
			while (true) {
				deleted = file.readChar();
				red = readRecord();
				if (deleted != FileUtil.DELETED)
					ret.add(red);
			}
		} catch (EOFException e) {
			return ret;
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void insertar(RedSubte red) {
		Integer pos = index.get(red.getCodigo());
		if (pos != null)
			return;
		int nr = (index.size() + nDeleted) * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(red);
			index.put(red.getCodigo(), nr / SIZE_RECORD);
		} catch (IOException e) {
		}
	}

	@Override
	public void actualizar(RedSubte red) {
		Integer pos = index.get(red.getCodigo());
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(' ');
			writeRecord(red);
		} catch (IOException e) {
		}

	}

	@Override
	public void borrar(RedSubte red) {
		Integer pos = index.get(red.getCodigo());
		if (pos == null)
			return;
		int nr = pos * SIZE_RECORD;
		try {
			file.seek(nr);
			file.writeChar(FileUtil.DELETED);
			index.remove(red.getCodigo());
			nDeleted++;
		} catch (IOException e) {
		}
	}

	public void pack() throws IOException {
		List<RedSubte> redes = buscarTodos();
		FConnection.backup("red");
		FConnection.delete("red");
		file = FConnection.getInstancia("red");
		index = new Hashtable<String, Integer>();
		nDeleted = 0;
		for (RedSubte r : redes)
			insertar(r);
	}
	

	private RedSubte readRecord() throws IOException {

		return new RedSubte(FileUtil.readString(file, SIZE_CODIGO),
							FileUtil.readString(file, SIZE_NOMBRE),
							FileUtil.readString(file, SIZE_CIUDAD));
	}

	private void writeRecord(RedSubte red) throws IOException {
		FileUtil.writeString(file, red.getCodigo(), SIZE_CODIGO);
		FileUtil.writeString(file, red.getNombre(), SIZE_NOMBRE);
		FileUtil.writeString(file, red.getNombreCiudad(), SIZE_CIUDAD);
	}
}
