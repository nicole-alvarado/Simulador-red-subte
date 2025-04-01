package vista.administrador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import negocio.LineaExisteException;

public class LineaForm extends JDialog {

	private JPanel contentPane;
	private JTextField jtfCodigo;
	private JTextField jtfNombre;

	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnAgregar;
	private JButton btnCancelar;
	private JLabel lblNombre;
	private JLabel lblCodigo;
	private JComboBox<String> cbListRedes;
	private JLabel lblErrorCodigo;
	private JLabel lblErrorNombre;

	private Icon iconoError;
	private Coordinador miCoordinador;

	public LineaForm() {
		initialize();
	}

	private void initialize() {

		setTitle("ABM - L\u00EDneas");
		setBounds(100, 100, 470, 212);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
	
		lblCodigo = new JLabel("C\u00F3digo:");
		lblCodigo.setBounds(10, 45, 64, 14);
		contentPane.add(lblCodigo);

		lblNombre = new JLabel("Nombre: ");
		lblNombre.setBounds(10, 81, 64, 14);
		contentPane.add(lblNombre);

		jtfCodigo = new JTextField();
		jtfCodigo.setBounds(84, 42, 86, 20);
		contentPane.add(jtfCodigo);
		jtfCodigo.setColumns(10);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(84, 78, 86, 20);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);

		Handler handler = new Handler();

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(112, 126, 89, 23);
		contentPane.add(btnModificar);
		btnModificar.setVisible(false);
		btnModificar.addActionListener(handler);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(112, 126, 89, 23);
		contentPane.add(btnEliminar);
		btnEliminar.setVisible(false);
		btnEliminar.addActionListener(handler);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(112, 126, 89, 23);
		contentPane.add(btnAgregar);
		btnAgregar.setVisible(false);
		btnAgregar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(234, 126, 89, 23);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		cbListRedes = new JComboBox<String>();
		cbListRedes.setBounds(84, 11, 200, 20);
		contentPane.add(cbListRedes);

		JLabel lblRed = new JLabel("Red:");
		lblRed.setBounds(10, 14, 46, 14);
		contentPane.add(lblRed);

		lblErrorCodigo = new JLabel("");
		lblErrorCodigo.setForeground(Color.RED);
		lblErrorCodigo.setBackground(Color.WHITE);
		lblErrorCodigo.setBounds(197, 45, 247, 14);
		contentPane.add(lblErrorCodigo);

		lblErrorNombre = new JLabel("");
		lblErrorNombre.setForeground(Color.RED);
		lblErrorNombre.setBackground(Color.WHITE);
		lblErrorNombre.setBounds(197, 81, 250, 14);
		contentPane.add(lblErrorNombre);

		iconoError = new ImageIcon(getClass().getResource("/imagen/iconoError.jpeg"));

		setModal(true);

	}

	public void accion(int accion, Linea linea) {
		btnAgregar.setVisible(false);
		btnModificar.setVisible(false);
		btnEliminar.setVisible(false);
		jtfNombre.setEditable(true);
		jtfCodigo.setEditable(true);
		cbListRedes.setEnabled(true);
		cargarRedes();

		if (accion == Constantes.AGREGAR) {
			btnAgregar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			btnModificar.setVisible(true);
			jtfCodigo.setEditable(false);
			mostrar(linea);
		}

		if (accion == Constantes.ELIMINAR) {
			btnEliminar.setVisible(true);
			jtfCodigo.setEditable(false);
			jtfNombre.setEditable(false);
			cbListRedes.setEnabled(false);
			cbListRedes.setEnabled(false);
			mostrar(linea);
		}
	}

	private void mostrar(Linea linea) {
		jtfNombre.setText(linea.getNombre());
		jtfCodigo.setText(linea.getCodigo());

		cbListRedes.setSelectedItem(linea.getRed().getCodigo()+ " - " +linea.getRed().getNombre());
	}

	private void limpiar() {
		jtfNombre.setText("");
		jtfCodigo.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnCancelar) {
				miCoordinador.cancelarLinea();
				return;
			}

			String s = cbListRedes.getSelectedItem().toString();
		    String[] result = s.split(" - ", 2);
		    String codigoRed = result[0];			    
		    
			if (event.getSource() == btnEliminar) {
				int resp = JOptionPane.showConfirmDialog(null, "¿Estás seguro que desea borrar este registro?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp) {

					for(Estacion e : miCoordinador.listaEstaciones()) {
						if(e.getLinea().getRed().getCodigo().equals(codigoRed) &&
								e.getLinea().getCodigo().equals(jtfCodigo.getText())) {
							JOptionPane.showMessageDialog(null, "Esta línea tiene estaciones, no se puede eliminar",
									"Error", JOptionPane.ERROR_MESSAGE,iconoError);
							return;
						}
					}

					RedSubte red = miCoordinador.buscarRed(new RedSubte(codigoRed, null, null));
					Linea linea = miCoordinador.buscarLinea(new Linea(red,
							jtfCodigo.getText(), jtfNombre.getText()));
					miCoordinador.eliminarLinea(linea);
				}
				return;
			}

			boolean valido = true;

			lblErrorCodigo.setText("");
			lblErrorNombre.setText("");

			// validar código
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
				if (nombre.matches("[A-Z][a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+")!= true) {
				lblErrorNombre.setText("Sólo letras. La primera con mayúscula");
				valido = false;
			}

			if (!valido) 			
				return;
			
			Linea linea = new Linea(miCoordinador.buscarRed(new RedSubte(codigoRed,null,null)), codigo, nombre);
			if (event.getSource() == btnAgregar)
				try {
					miCoordinador.agregarLinea(linea);
				} catch (LineaExisteException e) {
					JOptionPane.showMessageDialog(null, "¡Esta línea ya existe!");
					return;
				}
			if (event.getSource() == btnModificar) {
				miCoordinador.modificarLinea(linea);
			}
		}
	}

	public void cargarRedes() {
		cbListRedes.removeAllItems();
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListRedes.addItem(red.getCodigo() + " - " + red.getNombre());
		}
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
	
}
