package vista.administrador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import aplicacion.Constantes;
import controlador.Coordinador;
import modelo.Linea;
import modelo.RedSubte;
import negocio.RedExisteException;

public class RedForm extends JDialog {

	private JPanel contentPane;
	private JTextField jtfCodigo;
	private JTextField jtfNombre;
	private JButton btnModificar;
	private JButton btnEliminar;
	private JButton btnAgregar;
	private JButton btnCancelar;
	private JLabel lblNombre;
	private JLabel lblCodigo;
	private JLabel lblCiudad;
	private JTextField jtfCiudad;
	private JLabel lblErrorCodigo;
	private JLabel lblErrorNombre;
	private Icon iconoError;
	private Coordinador miCoordinador;

	public RedForm() {
		setTitle("Redes");
		setBounds(100, 100, 470, 216);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		lblCodigo = new JLabel("C\u00F3digo:");
		lblCodigo.setBounds(10, 29, 64, 14);
		contentPane.add(lblCodigo);

		lblNombre = new JLabel("Nombre: ");
		lblNombre.setBounds(10, 65, 64, 14);
		contentPane.add(lblNombre);

		jtfCodigo = new JTextField();
		jtfCodigo.setBounds(84, 26, 86, 20);
		contentPane.add(jtfCodigo);
		jtfCodigo.setColumns(10);

		jtfNombre = new JTextField();
		jtfNombre.setBounds(84, 62, 86, 20);
		contentPane.add(jtfNombre);
		jtfNombre.setColumns(10);

		Handler handler = new Handler();

		btnModificar = new JButton("Modificar");
		btnModificar.setBounds(94, 134, 89, 23);
		contentPane.add(btnModificar);
		btnModificar.setVisible(false);
		btnModificar.addActionListener(handler);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.setBounds(94, 134, 89, 23);
		contentPane.add(btnEliminar);
		btnEliminar.setVisible(false);
		btnEliminar.addActionListener(handler);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(94, 134, 89, 23);
		contentPane.add(btnAgregar);
		btnAgregar.setVisible(false);
		btnAgregar.addActionListener(handler);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(229, 134, 89, 23);
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(handler);

		lblErrorCodigo = new JLabel("");
		lblErrorCodigo.setBounds(202, 65, 242, 14);
		lblErrorCodigo.setForeground(Color.RED);
		contentPane.add(lblErrorCodigo);
		
		lblErrorNombre = new JLabel("");
		lblErrorNombre.setBounds(202, 29, 242, 14);
		lblErrorNombre.setForeground(Color.RED);
		contentPane.add(lblErrorNombre);

		lblCiudad = new JLabel("Ciudad:");
		lblCiudad.setBounds(10, 104, 64, 14);
		contentPane.add(lblCiudad);

		jtfCiudad = new JTextField();
		jtfCiudad.setColumns(10);
		jtfCiudad.setBounds(84, 101, 86, 20);
		contentPane.add(jtfCiudad);
		
		iconoError = new ImageIcon(getClass().getResource("/imagen/iconoError.jpeg"));

		setModal(true);

	}

	public void accion(int accion, RedSubte red) {
		btnAgregar.setVisible(false);
		btnModificar.setVisible(false);
		btnEliminar.setVisible(false);
		jtfNombre.setEditable(true);
		jtfCodigo.setEditable(true);
		jtfCiudad.setEditable(true);

		if (accion == Constantes.AGREGAR) {
			btnAgregar.setVisible(true);
			limpiar();
		}

		if (accion == Constantes.MODIFICAR) {
			btnModificar.setVisible(true);
			jtfCodigo.setEditable(false);
			mostrar(red);
		}

		if (accion == Constantes.ELIMINAR) {
			btnEliminar.setVisible(true);
			jtfCodigo.setEditable(false);
			jtfNombre.setEditable(false);
			jtfCiudad.setEditable(false);
			mostrar(red);
		}
	}

	private void mostrar(RedSubte red) {
		jtfNombre.setText(red.getNombre());
		jtfCodigo.setText(red.getCodigo());
		jtfCiudad.setText(red.getNombreCiudad());
	}

	private void limpiar() {
		jtfNombre.setText("");
		jtfCodigo.setText("");
		jtfCiudad.setText("");
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnEliminar) {
				int resp = JOptionPane.showConfirmDialog(null, "¿Estás seguro que desea borrar este registro?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == resp) {
					
					for (Linea l : miCoordinador.listaLineas()) {
						if (l.getRed().getCodigo().equals(jtfCodigo.getText())) {
							JOptionPane.showMessageDialog(null, "Esta red tiene lineas, no se puede eliminar",
									"Error", JOptionPane.ERROR_MESSAGE,iconoError);
							return;
						}
					}
					miCoordinador.eliminarRed(new RedSubte(jtfCodigo.getText(), null, null));
				
				}
				return;
			}
			
			if (event.getSource() == btnCancelar) {
				miCoordinador.cancelarRed();
				return;
			}
			
			lblErrorCodigo.setText("");
			lblErrorNombre.setText("");

			boolean valido = true;

			// validar código
			String codigo = jtfCodigo.getText().trim().toUpperCase();

			if (codigo.isEmpty()) {
				lblErrorCodigo.setText("Campo obligatorio");
				valido = false;
			} 

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
			
			String ciudad = jtfCiudad.getText().trim();

			if(!valido)
				return;
			
			RedSubte red = new RedSubte(codigo, nombre, ciudad);
			if (event.getSource() == btnAgregar) {
				try {
					miCoordinador.agregarRed(red);
				} catch (RedExisteException e) {
					JOptionPane.showMessageDialog(null, "¡Esta red ya existe!");
					return;
				}
			}	
			if (event.getSource() == btnModificar) {
				miCoordinador.modificarRed(red);
			}
		}
	}

	public JPanel getRedForm() {
		return this.contentPane;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
}
