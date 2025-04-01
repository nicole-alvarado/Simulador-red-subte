package empresa.servicio;

import java.util.List;

import modelo.Estacion;


public interface EstacionService {

	void insertar(Estacion estacion);

	void actualizar(Estacion estacion);

	void borrar(Estacion estacion);

	List<Estacion> buscarTodos();

}
