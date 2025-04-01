package empresa.servicio;

import java.util.List;

import empresa.conexion.Factory;
import empresa.dao.LineaDAO;
import modelo.Linea;

public class LineaServiceImpl implements LineaService {

	private LineaDAO lineaDAO; 
		
	public LineaServiceImpl(){
		lineaDAO = (LineaDAO) Factory.getInstancia("LINEA");
	}
	
	@Override
	public void insertar(Linea linea) {
		lineaDAO.insertar(linea);				
	}

	@Override
	public void actualizar(Linea linea) {
		lineaDAO.actualizar(linea);						
	}

	@Override
	public void borrar(Linea linea) {
		lineaDAO.borrar(linea);
		
	}

	@Override
	public List<Linea> buscarTodos() {
		return lineaDAO.buscarTodos();
		
	}

}
