package aplicacion;

import java.io.IOException;

import controlador.Coordinador;
import negocio.Empresa;
import negocio.EmpresaUsuario;
import negocio.EstacionExisteException;
import negocio.LineaExisteException;
import negocio.RedExisteException;
import negocio.TramoExisteException;
import vista.administrador.EstacionForm;
import vista.administrador.EstacionList;
import vista.administrador.LineaForm;
import vista.administrador.LineaList;
import vista.administrador.RedForm;
import vista.administrador.RedList;
import vista.administrador.TramoForm;
import vista.administrador.TramoList;
import vista.administrador.Visualizacion;
import vista.aplicacion.DesktopFrame;
import vista.aplicacion.Principal;
import vista.usuario.PantallaUsuario;
import vista.usuario.Recorrido;

public class Aplicacion {

	public static void main(String[] args) throws RedExisteException, LineaExisteException,
		EstacionExisteException, TramoExisteException {
		Empresa miEmpresa = Empresa.getEmpresa();

		try {
			Empresa.leerArchivoConfiguraciones("config.properties");
		} catch (IOException e) {
			System.err.print("Nombre del archivo inválido");
			System.exit(-1);
		}
		EmpresaUsuario miEmpresaUsuario = new EmpresaUsuario();

		DesktopFrame desktopFrame = new DesktopFrame();

		RedList miRedList = new RedList();

		RedForm miRedForm = new RedForm();

		LineaList miLineaList = new LineaList();

		LineaForm miLineaForm = new LineaForm();

		EstacionList miEstacionList = new EstacionList();

		EstacionForm miEstacionForm = new EstacionForm();

		TramoList miTramoList = new TramoList();

		TramoForm miTramoForm = new TramoForm();
		
		Visualizacion miVisualizacion = new Visualizacion();

		Coordinador miCoordinador = new Coordinador();

		Principal miVentanaPrincipal = new Principal();
		
		PantallaUsuario miVentanaUsuario = new PantallaUsuario();
		
		Recorrido miRecorrido = new Recorrido();

		miEmpresa.setMiCoordinador(miCoordinador);
		
		miEmpresaUsuario.setMiCoordinador(miCoordinador);
		
		miRedForm.setMiCoordinador(miCoordinador);
		miRedList.setCoordinador(miCoordinador);
		miLineaList.setMiCoordinador(miCoordinador);
		miLineaForm.setMiCoordinador(miCoordinador);
		miEstacionList.setMiCoordinador(miCoordinador);
		miEstacionForm.setMiCoordinador(miCoordinador);
		miTramoList.setMiCoordinador(miCoordinador);
		miTramoForm.setMiCoordinador(miCoordinador);
		miVisualizacion.setMiCoordinador(miCoordinador);
		miVentanaPrincipal.setMiCoordinador(miCoordinador);
		miVentanaUsuario.setMiCoordinador(miCoordinador);
		desktopFrame.setMiCoordinador(miCoordinador);
		miRecorrido.setMiCoordinador(miCoordinador);

		miCoordinador.setMiClaseMaster(miEmpresa);
		miCoordinador.setMiRedList(miRedList);
		miCoordinador.setMiRedForm(miRedForm);
		miCoordinador.setMiDesktopFrame(desktopFrame);
		miCoordinador.setMiLineaList(miLineaList);
		miCoordinador.setMiLineaForm(miLineaForm);
		miCoordinador.setMiEstacionList(miEstacionList);
		miCoordinador.setMiEstacionForm(miEstacionForm);
		miCoordinador.setMiTramoList(miTramoList);
		miCoordinador.setMiTramoForm(miTramoForm);
		miCoordinador.setMiVentanaPrincipal(miVentanaPrincipal);
		miCoordinador.setMiPantallaUsuario(miVentanaUsuario);
		miCoordinador.setMiVisualizacion(miVisualizacion);
		miCoordinador.setMiEmpresaUsuario(miEmpresaUsuario);
		miCoordinador.setMiRecorrido(miRecorrido);
				
		miVentanaPrincipal.setVisible(true);
	}

	
}
