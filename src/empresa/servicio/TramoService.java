package empresa.servicio;

import java.util.List;

import modelo.Tramo;


public interface TramoService {

	void insertar(Tramo tramo);

	void actualizar(Tramo tramo);

	void borrar(Tramo tramo);

	List<Tramo> buscarTodos();

}
