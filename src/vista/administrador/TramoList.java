package vista.administrador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import aplicacion.Constantes;
import controlador.Coordinador;
import modelo.Estacion;
import modelo.Linea;
import modelo.RedSubte;
import modelo.Tramo;

public class TramoList extends JDialog{

	private Tramo tramo;
	private JTable tableTramos;
	private JButton btnAgregar;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private int accion;
	
	private JLabel lblRed;
	private JComboBox<String> cbListRedes;
	private Coordinador miCoordinador;	
	
	public TramoList() {
		setTitle("ABM - Tramos");
		setBounds(100, 100, 749, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(342, 230, 100, 29);
		contentPane.add(btnAgregar);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 43, 713, 176);
		contentPane.add(scrollPane);
	
		
		tableTramos = new JTable();
		tableTramos.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Red", "L\u00EDnea Origen", "Estaci\u00F3n Origen", "L\u00EDnea Destino", "Estación Destino", "Tiempo", "Recorrido", "Km", "Modificar", "Eliminar"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		// Medidas de las columnas
		tableTramos.getColumnModel().getColumn(0).setPreferredWidth(50);
		tableTramos.getColumnModel().getColumn(1).setPreferredWidth(80);
		tableTramos.getColumnModel().getColumn(2).setPreferredWidth(100);
		tableTramos.getColumnModel().getColumn(3).setPreferredWidth(90);
		tableTramos.getColumnModel().getColumn(4).setPreferredWidth(120);
		tableTramos.getColumnModel().getColumn(5).setPreferredWidth(50);
		tableTramos.getColumnModel().getColumn(6).setPreferredWidth(80);
		tableTramos.getColumnModel().getColumn(7).setPreferredWidth(40);

		tableTramos.getColumn("Modificar").setCellRenderer(
				new ButtonRenderer());
		tableTramos.getColumn("Modificar").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		
		tableTramos.getColumn("Eliminar").setCellRenderer(
				new ButtonRenderer());
		tableTramos.getColumn("Eliminar").setCellEditor(
				new ButtonEditor(new JCheckBox()));
		
		scrollPane.setViewportView(tableTramos);
		
		lblRed = new JLabel("Red:");
		lblRed.setBounds(204, 18, 46, 14);
		contentPane.add(lblRed);

		cbListRedes = new JComboBox<String>();
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state== ItemEvent.SELECTED) {
		        	loadFilteredTable();
		        }
		      }
		    };
		    cbListRedes.addItemListener(itemListener);

		cbListRedes.setBounds(253, 15, 222, 20);
		contentPane.add(cbListRedes);
		Handler handler = new Handler();
		btnAgregar.addActionListener(handler);
		
		setModal(true);
	}
	
	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnAgregar)
				miCoordinador.agregarTramoForm();
		}
	}	
	
	private void loadFilteredTable() {
		((DefaultTableModel) tableTramos.getModel()).setRowCount(0);
		String s1 = cbListRedes.getSelectedItem().toString();
	    String[] result = s1.split(" - ", 2);
	    String codigoRed = result[0];
		for (Tramo tramo : miCoordinador.listaTramos()) {
			if (tramo.getEstacion1().getLinea().getRed().getCodigo().equals(codigoRed))
			addRow(tramo);
		}
	}
	
	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableTramos.getModel()).setRowCount(0);
		for (Tramo tramo : miCoordinador.listaTramos()) {
			addRow(tramo);
		}
	}

	public void addRow(Tramo tramo) {
		Object[] row = new Object[tableTramos.getModel()
				.getColumnCount()];
		row[0] = tramo.getEstacion1().getLinea().getRed().getCodigo();
		row[1] = tramo.getEstacion1().getLinea().getCodigo();
		row[2] = tramo.getEstacion1().getCodigo();
		row[3] = tramo.getEstacion2().getLinea().getCodigo();
		row[4] = tramo.getEstacion2().getCodigo();
		row[5] = tramo.getDuracion();
		
		if(tramo.getEsCombinacion()) {
			row[6] = "Caminando"; 
			row[7] = String.format("%.2f", Double.valueOf(tramo.getDuracion())/3600 * 
					miCoordinador.obtenerVelocidades().get("Caminando")); 

		}
		else {
			row[6] = "En subte";
			row[7] = String.format("%.2f", Double.valueOf(tramo.getDuracion())/3600 * 
					miCoordinador.obtenerVelocidades().get("enSubte")); 
		}
		
		row[8] = "edit";
		row[9] = "drop";
		
		((DefaultTableModel) tableTramos.getModel()).addRow(row);
	}	
	
	private void updateRow(int row) {
		tableTramos.setValueAt(tramo.getEstacion1().getLinea().getRed().getCodigo(), row,0);
		tableTramos.setValueAt(tramo.getEstacion1().getLinea().getCodigo(), row,1);		
		tableTramos.setValueAt(tramo.getEstacion1().getCodigo(), row,2);
		tableTramos.setValueAt(tramo.getEstacion2().getLinea().getCodigo(), row,3);				
		tableTramos.setValueAt(tramo.getEstacion2().getCodigo(), row,4);
		tableTramos.setValueAt(tramo.getDuracion(), row,5);
		
		if(tramo.getEsCombinacion()) {
			tableTramos.setValueAt("Caminando", row,6);
			tableTramos.setValueAt(String.format("%.2f", Double.valueOf(tramo.getDuracion())/3600 * 
					miCoordinador.obtenerVelocidades().get("Caminando")), row,7);

		}
		else {
			tableTramos.setValueAt("En subte", row,6);
			tableTramos.setValueAt(String.format("%.2f", Double.valueOf(tramo.getDuracion())/3600 * 
					miCoordinador.obtenerVelocidades().get("enSubte")), row,7);

		}	
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(UIManager.getColor("Button.background"));
			}
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));
			setIcon(icon);
			return this;
		}
	}

	class ButtonEditor extends DefaultCellEditor {

		protected JButton button;
		private String label;
		private boolean isPushed;
		private JTable table;
		private boolean isDeleteRow = false;
		private boolean isUpdateRow = false;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}
	
		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {

			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}

			label = (value == null) ? "" : value.toString();
			Icon icon = null;
			if (value.toString().equals("edit"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_edit.png"));
			if (value.toString().equals("drop"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_drop.png"));

			button.setIcon(icon);
			isPushed = true;
			this.table = table;
			isDeleteRow = false;
			isUpdateRow = false;
			return button;
		}

		@Override
		public Object getCellEditorValue() {
			if (isPushed) {
				
				String codigoRed = tableTramos.getValueAt(
						tableTramos.getSelectedRow(),0).toString();
				String codigoLineaOrigen = tableTramos.getValueAt(
						tableTramos.getSelectedRow(),1).toString();
				String codigoEstacionOrigen = tableTramos.getValueAt(
						tableTramos.getSelectedRow(), 2).toString();
				String codigoLineaDestino = tableTramos.getValueAt(
						tableTramos.getSelectedRow(),3).toString();
				String codigoEstacionDestino= tableTramos.getValueAt(
						tableTramos.getSelectedRow(), 4).toString();		
				int duracion = Integer.parseInt(tableTramos.getValueAt(
						tableTramos.getSelectedRow(), 5).toString());
				boolean esCombinacion = esCombinacion(codigoLineaOrigen, codigoLineaDestino);

				RedSubte red = miCoordinador.buscarRed(new RedSubte(codigoRed,null,null));
				Linea lineaOrigen = miCoordinador.buscarLinea(new Linea(red,codigoLineaOrigen,null));
				Linea lineaDestino = miCoordinador.buscarLinea(new Linea(red,codigoLineaDestino,null));
				Estacion estacionOrigen = miCoordinador.buscarEstacion(
						new Estacion(codigoEstacionOrigen,null,lineaOrigen));
				Estacion estacionDestino = miCoordinador.buscarEstacion(
						new Estacion(codigoEstacionDestino,null,lineaDestino));
				
				Tramo tramo = miCoordinador.buscarTramo(new Tramo(estacionOrigen,estacionDestino,duracion,0,esCombinacion));
				
				if (label.equals("edit"))
					miCoordinador.modificarTramoForm(tramo);
				else
					miCoordinador.eliminarTramoForm(tramo);
				
			}
			if (accion == Constantes.ELIMINAR)
				isDeleteRow = true;
			if (accion == Constantes.MODIFICAR) {
				isUpdateRow = true;						
			}
			isPushed = false;
			return new String(label);
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		
		protected void fireEditingStopped() {
			super.fireEditingStopped();

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
			if (isDeleteRow)
				tableModel.removeRow(table.getSelectedRow());

			if (isUpdateRow) {
				updateRow(table.getSelectedRow());
			}
		}
	}
	
	public void cargarRedes() {
		cbListRedes.removeAllItems();
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListRedes.addItem(red.getCodigo() + " - " + red.getNombre());
		}
	}
	
	private boolean esCombinacion(String codigoLineaOrigen, String codigoLineaDestino) {

		if (codigoLineaOrigen == codigoLineaDestino) {
			return false;
		}
		return true;
	}
	
	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setTramo(Tramo tramo) {
		this.tramo = tramo;
	}

	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
	
}
