package vista.aplicacion;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controlador.Coordinador;

public class Principal extends JFrame{
	
	private Coordinador miCoordinador;
	private JButton btnAdministrador;
	private JButton btnUsuario;

	public Principal() {
	
		setBounds(100, 100, 445, 326);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		btnUsuario = new JButton("Buscar");
		btnUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miCoordinador.mostrarPantallaUsuario();
			}
		});
		btnUsuario.setBounds(60, 174, 132, 41);
		getContentPane().add(btnUsuario);
		
		btnAdministrador = new JButton("Administrar");
		btnAdministrador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				miCoordinador.mostrarPantallaAdministrador();
			}
		});
		btnAdministrador.setBounds(237, 174, 132, 41);
		getContentPane().add(btnAdministrador);
		
		JLabel lblTitulo = new JLabel("REDES DE SUBTES");
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Verdana", Font.BOLD, 20));
		lblTitulo.setBounds(87, 54, 255, 26);
		getContentPane().add(lblTitulo);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		
		JLabel lblOpcion = new JLabel("Escoja que acci\u00F3n desea realizar:");
		lblOpcion.setBounds(79, 144, 226, 14);
		getContentPane().add(lblOpcion);
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}

}
