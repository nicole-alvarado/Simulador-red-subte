package vista.aplicacion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import controlador.Coordinador;

public class DesktopFrame extends JFrame {

	private JPanel contentPane;
	private Coordinador miCoordinador;

	public DesktopFrame() {
		setTitle("Men\u00FA");
		setBounds(100, 100, 463, 318);
		setLocationRelativeTo(null);
		setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Administrador");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmRedes = new JMenuItem("Redes");
		mntmRedes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				miCoordinador.mostrarRedList();
			}
		});
		mnNewMenu.add(mntmRedes);

		JMenuItem mntmNewMenuItem = new JMenuItem("Líneas");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				miCoordinador.mostrarLineaList();
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmEstaciones = new JMenuItem("Estaciones");
		mntmEstaciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				miCoordinador.mostrarEstacionList();
			}
		});
		mnNewMenu.add(mntmEstaciones);
		
		JMenuItem mntmTramos = new JMenuItem("Tramos");
		mntmTramos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				miCoordinador.mostrarTramoList();
			}
		});
		mnNewMenu.add(mntmTramos);
		
		
		
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
}
