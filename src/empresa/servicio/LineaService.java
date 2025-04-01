package empresa.servicio;

import java.util.List;

import modelo.Linea;


public interface LineaService {

	void insertar(Linea linea);

	void actualizar(Linea linea);

	void borrar(Linea linea);

	List<Linea> buscarTodos();

}
