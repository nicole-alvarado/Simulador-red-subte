package vista.administrador;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import modelo.RedSubte;

public class RedList extends JDialog {

	private RedSubte red;
	private JTable tableRedes;
	private JButton btnAgregar;
	private JPanel contentPane;
	private JScrollPane scrollPane;
	private Coordinador miCoordinador;
	private int accion;

	public RedList() {
		setTitle("Redes de subtes");
		setBounds(100, 100, 677, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(242, 230, 126, 30);
		contentPane.add(btnAgregar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 651, 208);
		contentPane.add(scrollPane);

		tableRedes = new JTable();
		tableRedes.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Código", "Nombre", "Ciudad", "Modificar", "Eliminar", "Visualizar" }) {

			boolean[] columnEditables = new boolean[] { false, false, false, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		tableRedes.getColumn("Modificar").setCellRenderer(new ButtonRenderer());
		tableRedes.getColumn("Modificar").setCellEditor(new ButtonEditor(new JCheckBox()));

		tableRedes.getColumn("Eliminar").setCellRenderer(new ButtonRenderer());
		tableRedes.getColumn("Eliminar").setCellEditor(new ButtonEditor(new JCheckBox()));

		tableRedes.getColumn("Visualizar").setCellRenderer(new ButtonRenderer());
		tableRedes.getColumn("Visualizar").setCellEditor(new ButtonEditor(new JCheckBox()));

		scrollPane.setViewportView(tableRedes);

		Handler handler = new Handler();
		btnAgregar.addActionListener(handler);
		
		setModal(true);	
	}

	private class Handler implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == btnAgregar)
				miCoordinador.agregarRedForm();

		}
	}

	public void loadTable() {
		// Eliminar todas las filas
		((DefaultTableModel) tableRedes.getModel()).setRowCount(0);
		for (RedSubte red : miCoordinador.listaRedSubtes()) {	
			addRow(red);
		}
	}

	public void addRow(RedSubte red) {
		Object[] row = new Object[tableRedes.getModel().getColumnCount()];
		row[0] = red.getCodigo();
		row[1] = red.getNombre();
		row[2] = red.getNombreCiudad();
		row[3] = "edit";
		row[4] = "drop";
		row[5] = "visualize";
		((DefaultTableModel) tableRedes.getModel()).addRow(row);
	}

	private void updateRow(int row) {
		tableRedes.setValueAt(red.getCodigo(), row, 0);
		tableRedes.setValueAt(red.getNombre(), row, 1);
		tableRedes.setValueAt(red.getNombreCiudad(), row, 2);
		
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
			if (value.toString().equals("visualize"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_visualize.png"));
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
			if (value.toString().equals("visualize"))
				icon = new ImageIcon(getClass().getResource("/imagen/b_visualize.png"));
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
				
				String id = tableRedes.getValueAt(tableRedes.getSelectedRow(), 0).toString();
				RedSubte red = miCoordinador.buscarRed(new RedSubte(id, null, null));

				if (label.equals("edit")) {
					miCoordinador.modificarRedForm(red);
				}
				if (label.equals("drop")) {
					miCoordinador.eliminarRedForm(red);
				}
				if (label.equals("visualize")) {
					miCoordinador.visualizarRed(red);
				}
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
	
	public void setAccion(int accion) {
		this.accion = accion;
	}

	public int getAccion() {
		return accion;
	}

	public void setRed(RedSubte red) {
		this.red = red;
		
	}

	public void setCoordinador(Coordinador miCoordinador) {
		this.miCoordinador = miCoordinador;
	}



}
