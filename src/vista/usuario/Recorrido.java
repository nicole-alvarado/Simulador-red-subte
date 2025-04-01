package vista.usuario;

import java.awt.Dimension;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import controlador.Coordinador;
import modelo.Estacion;
import net.datastructures.PositionalList;
import net.datastructures.Vertex;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class Recorrido extends JFrame{
	
	private JPanel panelRecorridoMasRapido;
	private JPanel panelRecorridoMenosCongestionado;
	private JPanel panelRecorridoMenorCombinacion;
	private JScrollPane scroll;
	private JScrollPane scroll2;
	private JScrollPane scroll3;
	private Dimension screenSize;
	private int h;
	private int w;
	JTextArea ventanaRecorridoMasRapido;
	JTextArea ventanaRecorridoMenorCombinacion;
	JTextArea ventanaRecorridoMenosCongestionado;
	JTextArea inicio;
	JTextArea fin;
	private Coordinador miCoordinador;

	public Recorrido() {
		panelRecorridoMasRapido = new JPanel();
		panelRecorridoMenosCongestionado = new JPanel();
		panelRecorridoMenorCombinacion = new JPanel();
		ventanaRecorridoMasRapido = new JTextArea();
		ventanaRecorridoMenorCombinacion = new JTextArea();
		ventanaRecorridoMenosCongestionado = new JTextArea();
		
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		h = (int) screenSize.getHeight();
		w = (int) screenSize.getWidth();
		setTitle("Ventana de búsqueda de recorridos");
		setBounds(0, 0, w, h-40);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(panelRecorridoMasRapido, BorderLayout.WEST);
		getContentPane().add(panelRecorridoMenosCongestionado, BorderLayout.EAST);
		getContentPane().add(panelRecorridoMenorCombinacion, BorderLayout.CENTER);
		setLocationRelativeTo(null);
		setResizable(false);
	}

	public void limpiarPaneles() {
		panelRecorridoMasRapido.removeAll();
		panelRecorridoMenosCongestionado.removeAll();
		panelRecorridoMenorCombinacion.removeAll();
		panelRecorridoMasRapido.revalidate();
		panelRecorridoMenosCongestionado.revalidate();
		panelRecorridoMenorCombinacion.revalidate();
		panelRecorridoMasRapido.repaint();
		panelRecorridoMenosCongestionado.repaint();
		panelRecorridoMenorCombinacion.repaint();
		panelRecorridoMasRapido.validate();
		panelRecorridoMenosCongestionado.validate();
		panelRecorridoMenorCombinacion.validate();
	}
	
	public void procesarRecorridos(Estacion estacionOrigen, Estacion estacionDestino) {
		limpiarPaneles();
		ExecutorService ejecutorSubprocesos = Executors.newCachedThreadPool();
		ProcesoRecorridoMasRapido recorridoMasRapido = new ProcesoRecorridoMasRapido(estacionOrigen, estacionDestino);
		Thread hilo1 = new Thread(recorridoMasRapido);
		ProcesoRecorridoMenorCombinacion recorridoMenorCombinacion = new ProcesoRecorridoMenorCombinacion(estacionOrigen, estacionDestino);
		Thread hilo2 = new Thread(recorridoMenorCombinacion);
		ProcesoRecorridoMenosCongestionado recorridoMenosCongestionado = new ProcesoRecorridoMenosCongestionado(estacionOrigen, estacionDestino);
		Thread hilo3 = new Thread(recorridoMenosCongestionado);
		ejecutorSubprocesos.execute(hilo1);
		ejecutorSubprocesos.execute(hilo2);
		ejecutorSubprocesos.execute(hilo3);
		
		ejecutorSubprocesos.shutdown(); 

	}
	
	private class ProcesoRecorridoMasRapido implements Runnable {

		private Estacion estacionOrigen;
		private Estacion estacionDestino;
		
		public ProcesoRecorridoMasRapido(Estacion estacionOrigen, Estacion estacionDestino) {
			this.estacionOrigen = estacionOrigen;
			this.estacionDestino = estacionDestino;
		}

		@Override
		public void run() {
			Random generador = new Random();
			// genera un numero aleatorio entre el 1000 y el 5000 
			int numero = 1000 + generador.nextInt(2000); 
			
			PositionalList <Vertex<Estacion>> recorridoMasRapido = miCoordinador.
					buscarRecorridoMasRapido(estacionOrigen, estacionDestino);
			String texto = "";							
			try {
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	
				texto = miCoordinador.obtenerRecorrido(recorridoMasRapido);
				ventanaRecorridoMasRapido.setText("RECORRIDO MÁS RÁPIDO:\n\n" + texto);
				ventanaRecorridoMasRapido.setEditable(false);
				scroll = new JScrollPane(ventanaRecorridoMasRapido){
						@Override
						public Dimension getPreferredSize() {
							return new Dimension((w/3)-20, h-80);
							
						}
					};	
					panelRecorridoMasRapido.add(scroll);
					setVisible(true);
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ProcesoRecorridoMenorCombinacion implements Runnable {

		private Estacion estacionOrigen;
		private Estacion estacionDestino;
		
		public ProcesoRecorridoMenorCombinacion(Estacion estacionOrigen, Estacion estacionDestino) {
			this.estacionOrigen = estacionOrigen;
			this.estacionDestino = estacionDestino;
		}

		@Override
		public void run() {
			Random generador = new Random();
			// genera un numero aleatorio entre el 1000 y el 5000 
			int numero = 1000 + generador.nextInt(2000); 
			
			PositionalList <Vertex<Estacion>> recorridoMenorCombinacion = miCoordinador.
					buscarRecorridoMenorCombinacion(estacionOrigen, estacionDestino);
			String texto = "";							
			try {
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	
				texto = miCoordinador.obtenerRecorrido(recorridoMenorCombinacion);
				ventanaRecorridoMenorCombinacion.setText("RECORRIDO CON MENOS COMBINACIONES DE LÍNEAS:\n\n" + texto);
				ventanaRecorridoMenorCombinacion.setEditable(false);
				scroll2 = new JScrollPane(ventanaRecorridoMenorCombinacion){
						@Override
						public Dimension getPreferredSize() {
							return new Dimension((w/3)-20, h-80);
						}
					};	
					panelRecorridoMenorCombinacion.add(scroll2);
					setVisible(true);
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ProcesoRecorridoMenosCongestionado implements Runnable {

		private Estacion estacionOrigen;
		private Estacion estacionDestino;
		
		public ProcesoRecorridoMenosCongestionado(Estacion estacionOrigen, Estacion estacionDestino) {
			this.estacionOrigen = estacionOrigen;
			this.estacionDestino = estacionDestino;
		}

		@Override
		public void run() {
			Random generador = new Random();
			// genera un numero aleatorio entre el 1000 y el 5000 
			int numero = 1000 + generador.nextInt(2000); 
			
			PositionalList <Vertex<Estacion>> recorridoMenosCongestionado = miCoordinador.
					buscarRecorridoMenosCongestionado(estacionOrigen, estacionDestino);
			String texto = "";							
			try {
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	
				texto = miCoordinador.obtenerRecorrido(recorridoMenosCongestionado);
				ventanaRecorridoMenosCongestionado.setText("RECORRIDO MENOS CONGESTIONADO:\n\n" + texto);
				ventanaRecorridoMenosCongestionado.setEditable(false);
				scroll3 = new JScrollPane(ventanaRecorridoMenosCongestionado){
						@Override
						public Dimension getPreferredSize() {
							return new Dimension((w/3)-20, h-80);
						}
					};	
					panelRecorridoMenosCongestionado.add(scroll3);
					setVisible(true);
				//realiza un pausa de un tiempo expresado en milisegundos determiando
				Thread.sleep(numero);	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
	
}
