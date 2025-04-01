package vista.administrador;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controlador.Coordinador;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;

public class Visualizacion extends JFrame{
	
	private Coordinador miCoordinador;	
	private Dimension screenSize;
	private int h;
	private int w;
	
	public Visualizacion() {
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		h = (int) screenSize.getHeight();
		w = (int) screenSize.getWidth();
		setBounds(100, 100, w, h);	
		setLocationRelativeTo(null);
		setResizable(false);

	}
	
	public void cargarLista(RedSubte red) {
		limpiarPanel();
		ArrayList<Linea> lineas = new ArrayList<Linea>();
		String cadena = "Red: "+ red.getNombre();
		JTextArea lista = new JTextArea();
		JScrollPane jsp;
		for (Linea l : miCoordinador.listaLineas()) {
			if (l.getRed().equals(red)) {
				lineas.add(l);
			}
		}
		
		
		for (Linea l : lineas) {
			cadena += "\n\nLínea: " + l.getCodigo() + "\t\t Combinación con líneas:\n";
			cadena += "Estaciones:\n";
			for (Estacion e : miCoordinador.listaEstaciones()) {
				if (e.getLinea().equals(l)) {
					cadena+=e.getNombre()+" \t\t ";
					for (String combinaciones : esCombinacionCon(e)) {
						cadena+=combinaciones+" ";
					}
					cadena+="\n";
				}
			}
		}
		lista.setText(cadena);
		lista.setEditable(false);
		jsp = new JScrollPane(lista){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(480, 320);
            }
        };
        getContentPane().add(jsp);
	}
	public ArrayList<String> esCombinacionCon(Estacion estacion) {
		ArrayList<String> esCombinacionCon = new ArrayList<String> ();
		for (Tramo t : miCoordinador.listaTramos()) {
			if (t.getEstacion1().equals(estacion) && t.getEsCombinacion()) {
				esCombinacionCon.add(t.getEstacion2().getLinea().getCodigo());
			}
		}
		return esCombinacionCon;
	}

	public void limpiarPanel() {
		getContentPane().removeAll();
		getContentPane().revalidate();
		getContentPane().repaint();;
		getContentPane().validate();

	}
	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}


	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
	
}
