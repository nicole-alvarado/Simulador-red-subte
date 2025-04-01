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

public class EstacionList extends JDialog {

	private Estacion estacion;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JTable tableEstaciones;
	private JButton btnAgregar;
	private JLabel lblRedes;
	private JLabel lblLineas;

	private JComboBox<String> cbListLineas;
	private JComboBox<String> cbListRedes;
	
	private Coordinador miCoordinador;
	private int accion;

	public EstacionList() {

		setTitle("Estaciones");
		setBounds(100, 100, 756, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(316, 284, 114, 32);
		contentPane.add(btnAgregar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 31, 720, 244);
		contentPane.add(scrollPane);
		tableEstaciones = new JTable();
		tableEstaciones.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Red",
				"Línea", "Código de estación", "Nombre", "Modificar", "Borrar" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		// Medidas de las columnas
		tableEstaciones.getColumnModel().getColumn(0).setPreferredWidth(10);
		tableEstaciones.getColumnModel().getColumn(1).setPreferredWidth(10);
		tableEstaciones.getColumnModel().getColumn(2).setPreferredWidth(40);
		tableEstaciones.getColumnModel().getColumn(3).setPreferredWidth(100);
		tableEstaciones.getColumnModel().getColumn(4).setPreferredWidth(40);
		tableEstaciones.getColumnModel().getColumn(5).setPreferredWidth(40);
		
		tableEstaciones.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableEstaciones.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
		tableEstaciones.getColumn("Borrar").setCellRenderer(new ButtonRenderer());
		tableEstaciones.getColumn("Borrar").setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableEstaciones);

		cbListLineas = new JComboBox<String>();
		ItemListener itemListenerLineas = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state== ItemEvent.SELECTED) {
		        	
					loadTablesForLines();
		        }
		      }
		    };
		cbListLineas.addItemListener(itemListenerLineas);
		cbListLineas.setBounds(524, 3, 58, 20);
		contentPane.add(cbListLineas);
		cbListLineas.setEnabled(false);

		cbListRedes = new JComboBox<String>();
		ItemListener itemListener = new ItemListener() {
		      public void itemStateChanged(ItemEvent itemEvent) {
		        int state = itemEvent.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		        	cbListLineas.setEnabled(true);
					cargarLineas();
					loadTablesForRed();
		        }
		        else {
		        	cbListLineas.setEnabled(false);
		        }
		      }
		    };
		    cbListRedes.addItemListener(itemListener);
		
		cbListRedes.setBounds(70, 3, 205, 20);
		contentPane.add(cbListRedes);

		lblRedes = new JLabel("Red:");
		lblRedes.setBounds(38, 6, 46, 14);
		contentPane.add(lblRedes);

		lblLineas = new JLabel("L\u00EDnea:");
		lblLineas.setBounds(468, 6, 46, 14);
		contentPane.add(lblLineas);

		Handler handler = new Handler();
		btnAgregar.addActionListener(handler);

		setModal(true);	

	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnAgregar)
				miCoordinador.agregarEstacionForm(cbListRedes.getSelectedItem().toString(),
						cbListLineas.getSelectedItem().toString());

		}
	}

	private void loadTablesForRed() {
		// Eliminar todas las filas
		((DefaultTableModel) tableEstaciones.getModel()).setRowCount(0);
		String s1 = cbListRedes.getSelectedItem().toString();
	    String[] result = s1.split(" - ", 2);
	    String codigoRed = result[0];
		for (Estacion estacion : miCoordinador.listaEstaciones()) {
			if(estacion.getLinea().getRed().getCodigo().equals(codigoRed))
				addRow(estacion);
		}

	}
	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableEstaciones.getModel()).setRowCount(0);
		for(Estacion estacion : miCoordinador.listaEstaciones()) {
			addRow(estacion);
		}

	}

	private void loadTablesForLines() {
		// Eliminar todas las filas
		((DefaultTableModel) tableEstaciones.getModel()).setRowCount(0);
		String s1 = cbListRedes.getSelectedItem().toString();
	    String[] result = s1.split(" - ", 2);
	    String codigoRed = result[0];
		for(Estacion estacion : miCoordinador.listaEstaciones()) {
			if(estacion.getLinea().getRed().getCodigo().equals(codigoRed)
					 && estacion.getLinea().getCodigo().equals(cbListLineas.getSelectedItem())) {
				addRow(estacion);
			}
		}
	}

	public void addRow(Estacion estacion) {
		Object[] row = new Object[tableEstaciones.getModel().getColumnCount()];
		row[0] = estacion.getLinea().getRed().getCodigo();
		row[1] = estacion.getLinea().getCodigo();
		row[2] = estacion.getCodigo();
		row[3] = estacion.getNombre();
		row[4] = "edit";
		row[5] = "drop";
		((DefaultTableModel) tableEstaciones.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableEstaciones.setValueAt(estacion.getLinea().getRed().getCodigo(), row, 0);
		tableEstaciones.setValueAt(estacion.getLinea().getCodigo(), row, 1);
		tableEstaciones.setValueAt(estacion.getCodigo(), row, 2);
		tableEstaciones.setValueAt(estacion.getNombre(), row, 3);
	}

	class ButtonRenderer extends JButton implements TableCellRenderer {

		public ButtonRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
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
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

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

				String codigoRed = tableEstaciones.getValueAt(tableEstaciones.getSelectedRow(), 0).toString();
				String codigoLinea = tableEstaciones.getValueAt(tableEstaciones.getSelectedRow(), 1).toString();
				String codigoEstacion = tableEstaciones.getValueAt(tableEstaciones.getSelectedRow(), 2).toString();
				Estacion estacion = miCoordinador.buscarEstacion(new Estacion(codigoEstacion, null,
						miCoordinador.buscarLinea(new Linea(
								miCoordinador.buscarRed(
										new RedSubte(codigoRed,null,null)),codigoLinea,null))));
				if (label.equals("edit"))
					miCoordinador.modificarEstacionForm(estacion);
				else
					miCoordinador.eliminarEstacionForm(estacion);
					
				if (accion == Constantes.ELIMINAR)
					isDeleteRow = true;
				if (accion == Constantes.MODIFICAR) {
					isUpdateRow = true;
				}
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

	public void cargarLineas() {
		cbListLineas.removeAllItems();
		String s = cbListRedes.getSelectedItem().toString();
	    String[] result = s.split(" - ", 2);
	    String codigo = result[0];
		if(miCoordinador.listaLineas().size() == 0) {
			btnAgregar.setEnabled(false);
		}else {
			btnAgregar.setEnabled(true);
		}
		for (Linea linea : miCoordinador.listaLineas()) {
			if (linea.getRed().getCodigo().equals(codigo)) {
				cbListLineas.addItem(linea.getCodigo());
			}
		}
	}

	public void cargarRedes() {
		cbListRedes.removeAllItems();
		for (RedSubte red : miCoordinador.listaRedSubtes()) {
			cbListRedes.addItem(red.getCodigo()+ " - " + red.getNombre());
		}
		
	}
	
	public Estacion getEstacion() {
		return estacion;
	}
	public void setEstacion(Estacion estacion) {
		this.estacion = estacion;
	}
	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}
	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
	public int getAccion() {
		return accion;
	}
	public void setAccion(int accion) {
		this.accion = accion;
	}
		
}
