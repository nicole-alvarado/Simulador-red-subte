package vista.administrador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import aplicacion.Constantes;
import controlador.Coordinador;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;
import negocio.TramoExisteException;

public class TramoForm extends JDialog {

	private Tramo tramo;
	private int accion;

	private JPanel contentPane;

	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnAgregar;
	private JButton btnCancelar;

	private JComboBox<String> cbListEstaciones;
	private JComboBox<String> cbListEstaciones2;
	private JComboBox<String> cbListLineas1;
	private JComboBox<String> cbListLineas2;
	private JLabel lblEstacionOrigen;
	private JLabel lblEstacionDestino;
	private JLabel lblTiempo;
	private JTextField jtfTiempo;
	private JComboBox <String> cbListRedes;

	private JComboBox <String> cbListCongestiones;
	private Coordinador miCoordinador;

	public TramoForm() {
		initialize();
	}

	private void initialize() {

		setTitle("ABM Tramos");
		setBounds(100, 100, 847, 296);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		Handler handler = new Handler();

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(123, 223, 89, 22);
		contentPane.add(btnAgregar);
		btnAgregar.setVisible(false);
		btnAgregar.addActionListener(handler);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(123, 223, 89, 23);
		contentPane.add(btnModificar);
		btnModificar.setVisible(false);
		btnModificar.addActionListener(handler);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(123, 222, 89, 23);
		contentPane.add(btnEliminar);
		btnEliminar.setVisible(false);
		btnEliminar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(281, 223, 89, 23);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		cbListEstaciones = new JComboBox<String>();
		cbListEstaciones.setBounds(303, 79, 208, 20);
		contentPane.add(cbListEstaciones);

		cbListEstaciones2 = new JComboBox<String>();
		cbListEstaciones2.setBounds(303, 110, 208, 20);
		contentPane.add(cbListEstaciones2);

		lblEstacionOrigen = new JLabel("Estaci\u00F3n origen:");
		lblEstacionOrigen.setBounds(193, 82, 100, 14);
		contentPane.add(lblEstacionOrigen);

		lblEstacionDestino = new JLabel("Estaci\u00F3n destino:");
		lblEstacionDestino.setBounds(193, 113, 112, 14);
		contentPane.add(lblEstacionDestino);

		lblTiempo = new JLabel("Tiempo:");
		lblTiempo.setBounds(20, 175, 67, 14);
		contentPane.add(lblTiempo);

		jtfTiempo = new JTextField();
		jtfTiempo.setBounds(80, 172, 86, 20);
		contentPane.add(jtfTiempo);
		jtfTiempo.setColumns(10);

		JLabel lblTipoCongestion = new JLabel("Congesti\u00F3n:");
		lblTipoCongestion.setBounds(562, 82, 67, 14);
		contentPane.add(lblTipoCongestion);

		cbListCongestiones = new JComboBox<String>();
		cbListCongestiones.setBounds(639, 79, 150, 20);
		contentPane.add(cbListCongestiones);

		JLabel lblSegundos = new JLabel("seg.");
		lblSegundos.setBounds(176, 175, 46, 14);
		contentPane.add(lblSegundos);

		JLabel lblRed = new JLabel("Red:");
		lblRed.setBounds(20, 40, 46, 14);
		contentPane.add(lblRed);

		cbListRedes = new JComboBox<String>();
		
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state== ItemEvent.SELECTED) {
		        	cargarLineas();
					cargarCongestiones();
		        }
		      }
		    };
		    cbListRedes.addItemListener(itemListener);
		cbListRedes.setBounds(79, 37, 203, 20);
		contentPane.add(cbListRedes);

		JLabel lblRecorrido = new JLabel("Recorrido:");
		lblRecorrido.setBounds(20, 150, 98, 14);
		contentPane.add(lblRecorrido);

		cbListLineas1 = new JComboBox<String>();
		cbListLineas1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarEstacionesOrigen();
			}
		});
		
		cbListLineas1.setBounds(80, 79, 67, 20);
		contentPane.add(cbListLineas1);

		cbListLineas2 = new JComboBox<String>();
		cbListLineas2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarEstacionesDestino();
			}
		});
		
		cbListLineas2.setBounds(80, 110, 67, 20);
		contentPane.add(cbListLineas2);

		JLabel lblLinea1 = new JLabel("L\u00EDnea:");
		lblLinea1.setBounds(20, 82, 46, 14);
		contentPane.add(lblLinea1);

		JLabel lblLinea2 = new JLabel("L\u00EDnea:");
		lblLinea2.setBounds(20, 112, 46, 14);
		contentPane.add(lblLinea2);		
		
		setModal(true);

	}

	public void accion(int accion, Tramo tramo) {
		btnAgregar.setVisible(false);
		btnModificar.setVisible(false);
		btnEliminar.setVisible(false);
		
		jtfTiempo.setEnabled(true);
		cbListRedes.setEnabled(true);
		cbListEstaciones.setEnabled(true);
		cbListEstaciones2.setEnabled(true);
		cbListLineas1.setEnabled(true);
		cbListLineas2.setEnabled(true);
		cbListCongestiones.setEnabled(true);
		
		this.cargarRedes();
		this.cargarLineas();
		this.cargarEstacionesOrigen();
		this.cargarEstacionesDestino();
		this.cargarCongestiones();
		

		if (accion == Constantes.AGREGAR) {
			btnAgregar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			btnModificar.setVisible(true);
			cbListCongestiones.setEnabled(false);
			cbListRedes.setEnabled(false);
			cbListEstaciones.setEnabled(false);
			cbListEstaciones2.setEnabled(false);
			cbListLineas1.setEnabled(false);
			cbListLineas2.setEnabled(false);
			mostrar(tramo);
		}

		if (accion == Constantes.ELIMINAR) {
			btnEliminar.setVisible(true);
			jtfTiempo.setEnabled(false);
			cbListCongestiones.setEnabled(false);
			cbListRedes.setEnabled(false);
			cbListEstaciones.setEnabled(false);
			cbListEstaciones2.setEnabled(false);
			cbListLineas1.setEnabled(false);
			cbListLineas2.setEnabled(false);
			mostrar(tramo);
		}

	}

	private void mostrar(Tramo tramo) {
		
		jtfTiempo.setText(tramo.getDuracion() + "");
		cbListRedes.setSelectedItem(tramo.getEstacion1().getLinea().getRed().getCodigo() + " - " + 
				tramo.getEstacion1().getLinea().getRed().getNombre());
		
		cbListLineas1.setSelectedItem(tramo.getEstacion1().getLinea().getCodigo());
		cbListLineas2.setSelectedItem(tramo.getEstacion2().getLinea().getCodigo());
		
		cbListEstaciones.setSelectedItem(tramo.getEstacion1().getCodigo() + " - " +
		tramo.getEstacion1().getNombre());
		
		cbListEstaciones2.setSelectedItem(tramo.getEstacion2().getCodigo() + " - " +
		tramo.getEstacion2().getNombre());
		
		switch (tramo.getTipo()) {
		case 1:
			cbListCongestiones.setSelectedItem(tramo.getTipo() + " - Baja");
			break;
		case 2:
			cbListCongestiones.setSelectedItem(tramo.getTipo() + " - Mediana");
			break;
		case 3:
			cbListCongestiones.setSelectedItem(tramo.getTipo() + " - Alta");
			break;
		}
		
	}

	private void limpiar() {
		jtfTiempo.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				miCoordinador.cancelarTramo();
				return;
			}
			int duracion;

			int tipoCongestion;

			boolean esCombinacion;
			Linea lineaOrigen;
			Linea lineaDestino;
			Estacion estacionOrigen;
			Estacion estacionDestino;
			Tramo tramo;
			
			duracion = Integer.parseInt(jtfTiempo.getText().trim());
			// Para congestiones
			String s = cbListCongestiones.getSelectedItem().toString();
		    String[] result = s.split(" - ", 2);
		    String tipo = result[0];
		    
			tipoCongestion = Integer.parseInt(tipo);

			esCombinacion = esCombinacion(cbListLineas1.getSelectedItem().toString(),
					cbListLineas2.getSelectedItem().toString());
		  
		    // Para sacar el código red
		    String sRed = cbListRedes.getSelectedItem().toString();
		    String[] resultRed = sRed.split(" - ", 2);
		    String codigoRed = resultRed[0];
		    
			lineaOrigen = miCoordinador.buscarLinea(
					new Linea(miCoordinador.buscarRed(
							new RedSubte(codigoRed,null,null)),
					cbListLineas1.getSelectedItem().toString(), null));
			lineaDestino = miCoordinador.buscarLinea(
					new Linea(miCoordinador.buscarRed(
							new RedSubte(codigoRed,null,null)),
					cbListLineas2.getSelectedItem().toString(), null));
			
			// Para sacar el código de la estación origen
		    String sEstacionOrigen = cbListEstaciones.getSelectedItem().toString();
		    String[] resultEstacionOrigen = sEstacionOrigen.split(" - ", 2);
		    String codigoEstacionOrigen = resultEstacionOrigen[0];
		   
			estacionOrigen = miCoordinador
					.buscarEstacion(new Estacion(codigoEstacionOrigen, null, lineaOrigen));
			
			// Para sacar el código red
		    String sEstacionDestino = cbListEstaciones2.getSelectedItem().toString();
		    String[] resultEstacionDestino = sEstacionDestino.split(" - ", 2);
		    String codigoEstacionDestino = resultEstacionDestino[0];
			estacionDestino = miCoordinador
					.buscarEstacion(new Estacion(codigoEstacionDestino, null, lineaDestino));
			tramo = new Tramo(estacionOrigen, estacionDestino, duracion, tipoCongestion, esCombinacion);

			
			if (event.getSource() == btnEliminar) {
				int resp = JOptionPane.showConfirmDialog(null, "¿Estás seguro que desea borrar este registro?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp) {
					miCoordinador.eliminarTramo(tramo);

				}
				return;
			}

			if (event.getSource() == btnAgregar) {
				try {
					miCoordinador.agregarTramo(tramo);
				} catch (TramoExisteException e) {
					JOptionPane.showMessageDialog(null, "¡Este tramo ya existe!");
					return;
				}
			}
			if (event.getSource() == btnModificar) {
				miCoordinador.modificarTramo(tramo);
			}
		}

	}

	public void cargarEstacionesOrigen() {
		cbListEstaciones.removeAllItems();
		String s = cbListRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigoRed = result[0];
		for (Estacion estacion : miCoordinador.listaEstaciones()) {
			if (estacion.getLinea().getRed().getCodigo().equals(codigoRed) &&
					estacion.getLinea().getCodigo().equals(cbListLineas1.getSelectedItem())) {
				cbListEstaciones.addItem(estacion.getCodigo() + " - " + estacion.getNombre());
			}
		}
	}
	public void cargarEstacionesDestino() {
		cbListEstaciones2.removeAllItems();
		String s = cbListRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigoRed = result[0];
		for (Estacion estacion : miCoordinador.listaEstaciones()) {
			if (estacion.getLinea().getRed().getCodigo().equals(codigoRed) &&
					estacion.getLinea().getCodigo().equals(cbListLineas2.getSelectedItem())) {
				cbListEstaciones2.addItem(estacion.getCodigo() + " - " + estacion.getNombre());
			}
			
		}
	}
	public void cargarLineas() {
		cbListLineas1.removeAllItems();
		cbListLineas2.removeAllItems();
		String s = cbListRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigoRed = result[0];
		for (Linea linea : miCoordinador.listaLineas()) {
			if (linea.getRed().getCodigo().equals(codigoRed)) {
				cbListLineas1.addItem(linea.getCodigo());
				cbListLineas2.addItem(linea.getCodigo());
			}
		}
	}

	public Tramo getTramo() {
		return tramo;
	}

	public int getAccion() {
		return accion;
	}

	public void cargarRedes() {
		cbListRedes.removeAllItems();
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListRedes.addItem(red.getCodigo() + " - " + red.getNombre());
		}
	}

	public void cargarCongestiones() {
		cbListCongestiones.removeAllItems();
		for (Integer congestion : miCoordinador.obtenerCongestiones()) {
			switch (congestion) {
			case 1:
				cbListCongestiones.addItem(congestion + " - Baja");
				break;
			case 2:
				cbListCongestiones.addItem(congestion + " - Mediana");
				break;
			case 3:
				cbListCongestiones.addItem(congestion + " - Alta");
				break;
			}
		}
	}

	private boolean esCombinacion(String codigoLineaOrigen, String codigoLineaDestino) {

		if (codigoLineaOrigen == codigoLineaDestino) {
			return false;
		}
		return true;
	}

	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}

	public void setRed(String red) {
		cbListRedes.setSelectedItem(red);	
	}

	public void setLineaOrigen(String lineaOrigen) {
		cbListLineas1.setSelectedItem(lineaOrigen);
	}

	public void setLineaDestino(String lineaDestino) {
		cbListLineas2.setSelectedItem(lineaDestino);
		
	}

	public void setEstacionOrigen(String estacionOrigen) {
		cbListEstaciones.setSelectedItem(estacionOrigen);
	}

	public void setEstacionDestino(String estacionDestino) {
		cbListEstaciones2.setSelectedItem(estacionDestino);		
	}
	
	
}
