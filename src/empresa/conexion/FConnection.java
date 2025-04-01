package empresa.conexion;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.ResourceBundle;

import util.FileUtil;

public class FConnection {
	private static Hashtable<String, RandomAccessFile> files = new Hashtable<String, RandomAccessFile>();

	public static RandomAccessFile getInstancia(String name) {
		try {
			// verifico si existe un objeto relacionado a objName
			// en la hashtable
			RandomAccessFile file = files.get(name);
			// si no existe entonces lo instancio y lo agrego
			if (file == null) {
				ResourceBundle rb = ResourceBundle.getBundle("file");
				String fileName = rb.getString(name);
				file = new RandomAccessFile(fileName, "rw");
				// agrego el objeto a la hashtable
				files.put(name, file);
			}
			return file;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error al crear la conexion", ex);
		}
	}

	static class MiShDwnHook extends Thread {
		// justo antes de finalizar el programa la JVM invocara
		// a este metodo donde podemos cerrar la conexion
		public void run() {
			try {
				for (RandomAccessFile file : files.values())
					file.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
	}

	public static void backup(String name) {
		ResourceBundle rb = ResourceBundle.getBundle("file");
		String fileName = rb.getString(name);
		try {
			FileUtil.copyFile(fileName, fileName + ".bak");
		} catch (IOException e) {
		}
	}

	private static void close(String name) {			
		RandomAccessFile file = files.get(name);
		try {
			file.close();		
			files.remove(name);			
		} catch (IOException e) {
			
		}
	}
	
	public static void delete(String name) {
		ResourceBundle rb = ResourceBundle.getBundle("file");
		String fileName = rb.getString(name);		
		try {
			close(name);
			Files.delete(Paths.get(fileName));
		} catch (IOException e) {
			
		}
	}
}