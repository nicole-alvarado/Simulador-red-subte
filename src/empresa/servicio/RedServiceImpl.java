package empresa.servicio;

import java.util.List;

import empresa.conexion.Factory;
import empresa.dao.RedDAO;
import modelo.RedSubte;

public class RedServiceImpl implements RedService {

	private RedDAO redDAO; 
		
	public RedServiceImpl(){
		redDAO = (RedDAO) Factory.getInstancia("RED");
	}
	
	@Override
	public void insertar(RedSubte red) {
		redDAO.insertar(red);				
	}

	@Override
	public void actualizar(RedSubte red) {
		redDAO.actualizar(red);						
	}

	@Override
	public void borrar(RedSubte red) {
		redDAO.borrar(red);
		
	}

	@Override
	public List<RedSubte> buscarTodos() {
		return redDAO.buscarTodos();
		
	}

}
