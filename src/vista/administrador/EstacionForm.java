package vista.administrador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import negocio.EstacionExisteException;

public class EstacionForm extends JDialog {
	
	private int accion;
	private Icon iconoError;
	private JPanel contentPane;
	private JTextField jtfCodigo;
	private JTextField jtfNombre;

	private JLabel lblErrorNombre;
	private JLabel lblErrorCodigo;
	private JLabel lblRed;
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnCancelar;
	
	private JComboBox<String> cbListLineas;
	private JComboBox <String> cbListRedes;
	
	private Coordinador miCoordinador;


	public EstacionForm() {
		
		setBounds(100, 100, 662, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		iconoError = new ImageIcon(getClass().getResource("/imagen/iconoError.jpeg"));

		JLabel lblLinea = new JLabel("L\u00EDnea:");
		lblLinea.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLinea.setBounds(42, 58, 107, 14);
		contentPane.add(lblLinea);

		JLabel lblCodigo = new JLabel("Codigo:");
		lblCodigo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCodigo.setBounds(42, 86, 107, 14);
		contentPane.add(lblCodigo);

		jtfCodigo = new JTextField();
		jtfCodigo.setBounds(159, 83, 86, 20);
		contentPane.add(jtfCodigo);
		jtfCodigo.setColumns(10);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombre.setBounds(42, 117, 107, 14);
		contentPane.add(lblNombre);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(159, 114, 86, 20);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);
		
		lblErrorNombre = new JLabel("");
		lblErrorNombre.setForeground(Color.RED);
		lblErrorNombre.setBounds(255, 117, 300, 14);
		contentPane.add(lblErrorNombre);

		lblErrorCodigo = new JLabel("");
		lblErrorCodigo.setForeground(Color.RED);
		lblErrorCodigo.setBounds(255, 89, 300, 14);
		contentPane.add(lblErrorCodigo);

		Handler handler = new Handler();

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(85, 201, 114, 32);
		contentPane.add(btnAgregar);
		btnAgregar.setVisible(false);
		btnAgregar.addActionListener(handler);

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(85, 202, 114, 32);
		contentPane.add(btnModificar);
		btnModificar.setVisible(false);
		btnModificar.addActionListener(handler);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(85, 202, 114, 32);
		contentPane.add(btnEliminar);
		btnEliminar.setVisible(false);
		btnEliminar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(225, 202, 114, 32);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		
		cbListLineas = new JComboBox<String> ();
		
		cbListLineas.setBounds(160, 55, 85, 20);
		contentPane.add(cbListLineas);
		
		lblRed = new JLabel("Red:");
		lblRed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRed.setBounds(42, 24, 46, 14);
		contentPane.add(lblRed);
		
		cbListRedes = new JComboBox<String>();
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state== ItemEvent.SELECTED) {
					cargarLineas();
		        }
		      }
		    };
		    cbListRedes.addItemListener(itemListener);
		cbListRedes.setBounds(159, 24, 86, 20);
		contentPane.add(cbListRedes);
		
		setModal(true);

	}	
		public void accion(int accion, Estacion estacion) {
			btnAgregar.setVisible(false);
			btnModificar.setVisible(false);
			btnEliminar.setVisible(false);
			jtfNombre.setEditable(true);
			jtfCodigo.setEditable(true);
			cbListRedes.setEnabled(true);
			cbListLineas.setEnabled(true);
			this.cargarRedes();
			this.cargarLineas();

			if (accion == Constantes.AGREGAR) {
				btnAgregar.setVisible(true);
				limpiar();
			}

			if (accion == Constantes.MODIFICAR) {
				btnModificar.setVisible(true);
				jtfCodigo.setEditable(false);
				mostrar(estacion);
			}

			if (accion == Constantes.ELIMINAR) {
				btnEliminar.setVisible(true);
				jtfCodigo.setEditable(false);
				jtfNombre.setEditable(false);
				cbListRedes.setEnabled(false);
				cbListLineas.setEnabled(false);
				mostrar(estacion);
			}
		}

		private void mostrar(Estacion estacion) {
			jtfNombre.setText(estacion.getNombre());
			jtfCodigo.setText(estacion.getCodigo());
		
			cbListRedes.setSelectedItem(estacion.getLinea().getRed().getCodigo() + " - " + estacion.getLinea().getRed().getNombre());

			cbListLineas.setSelectedItem(estacion.getLinea().getCodigo());

		}

		private void limpiar() {
			jtfNombre.setText("");
			jtfCodigo.setText("");
		}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				miCoordinador.cancelarEstacion();
				return;
			}

			String s = cbListRedes.getSelectedItem().toString();
		    String[] result = s.split(" - ", 2);
		    String codigoRed = result[0];
		    
			Linea l = miCoordinador.buscarLinea(new Linea(
					miCoordinador.buscarRed(new RedSubte(codigoRed,null,null)),
					cbListLineas.getSelectedItem().toString(),null));
						
			if (event.getSource() == btnEliminar) {
				int resp = JOptionPane.showConfirmDialog(null, "Está seguro que borra este registro?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp) {

					for(Tramo t : miCoordinador.listaTramos()) {
						if(t.getEstacion1().getLinea().getRed().getCodigo().equals(codigoRed) ||
								t.getEstacion2().getLinea().getRed().getCodigo().equals(codigoRed)){
								
							if(t.getEstacion1().getLinea().getCodigo().equals(l.getCodigo()) ||
									t.getEstacion2().getLinea().getCodigo().equals(l.getCodigo())){

								if(t.getEstacion1().getCodigo().equals(jtfCodigo.getText()) || 
								t.getEstacion2().getCodigo().equals(jtfCodigo.getText())) {
									
									JOptionPane.showMessageDialog(null, "Existen tramos referentes a esta estación, no se puede eliminar",
											"Error", JOptionPane.ERROR_MESSAGE,iconoError);
									return;
								}
							}
						}			
						
					}
					miCoordinador.eliminarEstacion(new Estacion(jtfCodigo.getText(),null,l));
							
				}
				return;
			}

			boolean valido = true;

			lblErrorNombre.setText("");
			lblErrorCodigo.setText("");
			
			//validar código
			String codigo = jtfCodigo.getText().trim().toUpperCase();
			if (codigo.isEmpty()) {
				lblErrorCodigo.setText("Campo obligatorio");
				valido = false;
			} 
		
			// validar nombre
			String nombre = jtfNombre.getText().trim();
			if (nombre.isEmpty()) {
				lblErrorNombre.setText("Campo obligatorio");
				valido = false;
			}
			else 
				if (nombre.matches("[A-Z][a-zA-Z0-9ñÑáéíóúÁÉÍÓÚ.\\s-]+")!= true) {
				lblErrorNombre.setText("La primera letra tiene que ser mayúscula");
				valido = false;
			}
			
			if (!valido)
				return;
			
			Estacion estacion = new Estacion(codigo,nombre,l); 
			
			if (event.getSource() == btnAgregar)
				try {
					miCoordinador.agregarEstacion(estacion);
				} catch (EstacionExisteException e) {
					JOptionPane.showMessageDialog(null, "¡Esta estación ya existe!");
					return;
				}
			if (event.getSource() == btnModificar) {
				miCoordinador.modificarEstacion(estacion);
			}
		}
	}
	
	public void cargarRedes() {
		cbListRedes.removeAllItems();;
		
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListRedes.addItem(red.getCodigo() + " - " + red.getNombre());
		}
	}
	
	public void cargarLineas() {
		cbListLineas.removeAllItems();
		String s = cbListRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigoRed = result[0];
		for(Linea linea : miCoordinador.listaLineas()) {
			if (linea.getRed().getCodigo().equals(codigoRed)) {
				cbListLineas.addItem(linea.getCodigo());
			}
		}
	}
	
	public int getAccion() {
		return accion;
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
	public void setLinea(String linea) {
		cbListLineas.setSelectedItem(linea);
	}
	
}
