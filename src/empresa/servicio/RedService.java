package empresa.servicio;

import java.util.List;

import modelo.RedSubte;


public interface RedService {

	void insertar(RedSubte red);

	void actualizar(RedSubte red);

	void borrar(RedSubte red);

	List<RedSubte> buscarTodos();

}
