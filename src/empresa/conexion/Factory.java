package empresa.conexion;

import java.util.Hashtable;
import java.util.ResourceBundle;

public class Factory {
	private static Hashtable<String, Object> instancias = new Hashtable<String, Object>();

	public static Object getInstancia(String objName) {
		try {
			// verifico si existe un objeto relacionado a objName
			// en la hashtable
			Object obj = instancias.get(objName);
			// si no existe entonces lo instancio y lo agrego
			if (obj == null) {
				ResourceBundle rb = ResourceBundle.getBundle("factory");
				String sClassname = rb.getString(objName);
				obj = Class.forName(sClassname).newInstance();
				// agrego el objeto a la hashtable
				instancias.put(objName, obj);
			}
			return obj;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
}