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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import aplicacion.Constantes;
import controlador.Coordinador;
import modelo.Linea;
import modelo.RedSubte;
import javax.swing.JLabel;

public class LineaList extends JDialog {

	private Linea linea;
	private JTable tableLineas;
	private JButton btnAgregar;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JComboBox<String> cbListRedes;
	private Coordinador miCoordinador;
	private int accion;

	public LineaList() {

		setTitle("L\u00EDneas");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(149, 224, 100, 29);
		contentPane.add(btnAgregar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 38, 414, 181);
		contentPane.add(scrollPane);

		tableLineas = new JTable();
		tableLineas.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "C\u00F3digo red", "C\u00F3digo", "Nombre", "Modificar", "Eliminar" }) {

			boolean[] columnEditables = new boolean[] { false, false, false, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		tableLineas.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableLineas.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));
		tableLineas.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
		tableLineas.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(tableLineas);

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

		cbListRedes.setBounds(109, 11, 197, 20);
		contentPane.add(cbListRedes);
		
		JLabel lblRed = new JLabel("Red:");
		lblRed.setBounds(53, 14, 46, 14);
		contentPane.add(lblRed);
		Handler handler = new Handler();
		btnAgregar.addActionListener(handler);

		setModal(true);
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnAgregar)
				miCoordinador.agregarLineaForm(cbListRedes.getSelectedItem().toString());
		}
	}

	private void loadFilteredTable() {
		((DefaultTableModel) tableLineas.getModel()).setRowCount(0);
		String s = cbListRedes.getSelectedItem().toString().trim();
	    String[] result = s.split(" - ", 2);
	    String codigo = result[0];

		for (Linea linea : miCoordinador.listaLineas()) {
			if (linea.getRed().getCodigo().equals(codigo)) {
				addRow(linea);
			}
		}
	}

	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableLineas.getModel()).setRowCount(0);

		for (Linea linea : miCoordinador.listaLineas()) {
			addRow(linea);
		}
	}

	public void addRow(Linea l) {
		Object[] row = new Object[tableLineas.getModel().getColumnCount()];
		row[0] = l.getRed().getCodigo();
		row[1] = l.getCodigo();
		row[2] = l.getNombre();
		row[3] = "edit";
		row[4] = "drop";
		((DefaultTableModel) tableLineas.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableLineas.setValueAt(linea.getRed().getCodigo(), row, 0);
		tableLineas.setValueAt(linea.getCodigo(), row, 1);
		tableLineas.setValueAt(linea.getNombre(), row, 2);
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
				String codigoRed = tableLineas.getValueAt(tableLineas.getSelectedRow(), 0).toString();
				String codigoLinea = tableLineas.getValueAt(tableLineas.getSelectedRow(), 1).toString();
				Linea linea = miCoordinador.buscarLinea(new Linea(miCoordinador.buscarRed(new RedSubte(codigoRed,null,null)), codigoLinea, null));

				if (label.equals("edit"))
					miCoordinador.modificarLineaForm(linea);
				else
					miCoordinador.eliminarLineaForm(linea);

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

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public Coordinador getMiCoordinador() {
		return miCoordinador;
	}

	public void setMiCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}
}
