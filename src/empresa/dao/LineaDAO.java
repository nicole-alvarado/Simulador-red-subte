package empresa.dao;

import java.util.List;

import modelo.Linea;

public interface LineaDAO {
	
	void insertar(Linea linea);

	void actualizar(Linea linea);

	void borrar(Linea linea);

	List<Linea> buscarTodos();
}
