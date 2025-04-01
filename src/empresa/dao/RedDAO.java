package empresa.dao;

import java.util.List;

import modelo.RedSubte;


public interface RedDAO {
	
	void insertar(RedSubte red);

	void actualizar(RedSubte red);

	void borrar(RedSubte red);

	List<RedSubte> buscarTodos();
}
