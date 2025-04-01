package vista.usuario;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controlador.Coordinador;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;

public class PantallaUsuario extends JFrame{
	
	private Coordinador miCoordinador;
	private JComboBox<String> cbListaRedes;
	private JComboBox<String> cbListaLineas1;
	private JComboBox<String> cbListaLineas2;
	private JComboBox<String> cbListaEstaciones1;
	private JComboBox<String> cbListaEstaciones2;
	private JButton btnBuscar;
	
	public PantallaUsuario() {
		setBounds(100, 100, 616, 338);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		JLabel lblRed = new JLabel("Red:");
		lblRed.setBounds(23, 24, 46, 14);
		getContentPane().add(lblRed);
		setResizable(false);
	
		cbListaRedes = new JComboBox<String>();
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state== ItemEvent.SELECTED) {
					cargarLineas();
		        }
		      }
		    };
		    cbListaRedes.addItemListener(itemListener);
		cbListaRedes.setBounds(79, 21, 226, 20);
		getContentPane().add(cbListaRedes);
		
		JLabel lblLineaOrigen = new JLabel("L\u00EDnea:");
		lblLineaOrigen.setBounds(23, 81, 46, 14);
		getContentPane().add(lblLineaOrigen);
		
		cbListaLineas1 = new JComboBox<String>();
		cbListaLineas1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarEstacionesOrigen();
			}
		});
		cbListaLineas1.setBounds(79, 78, 73, 20);
		getContentPane().add(cbListaLineas1);
		
		JLabel lblLineaDestino = new JLabel("L\u00EDnea:");
		lblLineaDestino.setBounds(23, 134, 46, 14);
		getContentPane().add(lblLineaDestino);
		
		cbListaLineas2 = new JComboBox<String>();
		cbListaLineas2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarEstacionesDestino();
			}
		});
		cbListaLineas2.setBounds(79, 131, 73, 20);
		getContentPane().add(cbListaLineas2);
		
		JLabel lblOrigen = new JLabel("Origen:");
		lblOrigen.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOrigen.setBounds(23, 56, 46, 14);
		getContentPane().add(lblOrigen);
		
		JLabel lblDestino = new JLabel("Destino:");
		lblDestino.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDestino.setBounds(23, 109, 46, 14);
		getContentPane().add(lblDestino);
		
		JLabel lblEstacionOrigen = new JLabel("Estaci\u00F3n Origen:");
		lblEstacionOrigen.setBounds(230, 81, 113, 14);
		getContentPane().add(lblEstacionOrigen);
		
		JLabel lblEstacionDestino = new JLabel("Estaci\u00F3n Destino:");
		lblEstacionDestino.setBounds(230, 134, 113, 14);
		getContentPane().add(lblEstacionDestino);
		
		cbListaEstaciones1 = new JComboBox<String>();
		cbListaEstaciones1.setBounds(353, 78, 209, 20);
		getContentPane().add(cbListaEstaciones1);
		
		cbListaEstaciones2 = new JComboBox<String>();
		cbListaEstaciones2.setBounds(353, 131, 209, 20);
		getContentPane().add(cbListaEstaciones2);
		
		btnBuscar = new JButton("Buscar");
		btnBuscar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnBuscar.setBounds(204, 190, 101, 41);
		getContentPane().add(btnBuscar);
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Código de red
				String s1 = cbListaRedes.getSelectedItem().toString();
			    String[] resultRed = s1.split(" - ", 2);
			    String codigoRed = resultRed[0];
			    
				String s2 = cbListaEstaciones1.getSelectedItem().toString();
			    String[] resultEstacionOrigen = s2.split(" - ", 2);
			    String codigoEstacionOrigen = resultEstacionOrigen[0];
	 
				Estacion estacionOrigen = miCoordinador.buscarEstacion(new Estacion(codigoEstacionOrigen, null,
						miCoordinador.buscarLinea(new Linea(
								miCoordinador.buscarRed(
										new RedSubte(codigoRed,null,null)),cbListaLineas1.getSelectedItem().toString(),null))));
				
				String s3 = cbListaEstaciones2.getSelectedItem().toString();
			    String[] resultEstacionDestino = s3.split(" - ", 2);
			    String codigoEstacionDestino = resultEstacionDestino[0];
				Estacion estacionDestino = miCoordinador.buscarEstacion(new Estacion(codigoEstacionDestino, null,
						miCoordinador.buscarLinea(new Linea(
								miCoordinador.buscarRed(
										new RedSubte(codigoRed,null,null)),cbListaLineas2.getSelectedItem().toString(),null))));

							miCoordinador.mostrarVentanaRecorrido(estacionOrigen, estacionDestino);
			}
		});
		
	}

	public void cargarRedes() {
		cbListaRedes.removeAllItems();
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListaRedes.addItem(red.getCodigo() + " - " + red.getNombre());
		}
		
	}
	public void cargarEstacionesOrigen() {
		cbListaEstaciones1.removeAllItems();
		for (Estacion estacion : miCoordinador.listaEstaciones()) {
			if (estacion.getLinea().getCodigo().equals(cbListaLineas1.getSelectedItem())) {
				cbListaEstaciones1.addItem(estacion.getCodigo() + " - " + estacion.getNombre());
			}
		}
	}
	public void cargarEstacionesDestino() {
		cbListaEstaciones2.removeAllItems();
		for (Estacion estacion : miCoordinador.listaEstaciones()) {
			if (estacion.getLinea().getCodigo().equals(cbListaLineas2.getSelectedItem())) {
				cbListaEstaciones2.addItem(estacion.getCodigo() + " - " + estacion.getNombre());
			}
			
		}
	}
	public void cargarLineas() {
		cbListaLineas1.removeAllItems();
		cbListaLineas2.removeAllItems();
		String s = cbListaRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigo = result[0];
		for (Linea linea : miCoordinador.listaLineas()) {
			if (linea.getRed().getCodigo().equals(codigo)) {
				cbListaLineas1.addItem(linea.getCodigo());
				cbListaLineas2.addItem(linea.getCodigo());
			}
		}
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
}
