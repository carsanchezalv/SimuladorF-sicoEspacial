package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {

	private static final long serialVersionUID = 7867048332690894552L;

	public BodiesTable(Controller ctrl)
	{
		super();

		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
		BorderFactory.createLineBorder(Color.black, 2),
		"Bodies",
		TitledBorder.LEFT, TitledBorder.TOP));
		
		BodiesTableModel btm = new BodiesTableModel(ctrl);
		JTable table = new JTable(btm);
		this.add(new JScrollPane(table));
		table.setBackground(Color.WHITE);
		
		setPreferredSize(new Dimension(200, 200));
	}
}