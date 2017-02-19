package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Record;
import services.RecordService;

@Component
public class RecordEventProcessorImpl implements RecordEventProcessor {

	@Autowired
	private RecordService recordService;
	private JPanel mainPanel;
	private String currentFilterQuery;
	final int recordsPerPage = 10;
	int currentPage = 1;
	int rowHeight;

	public JPanel process(Dimension sizeOfParentElement) {
		System.out.println("in RecordEventProcessor");
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.white);
		mainPanel.setLayout(new BorderLayout());

		Dimension tablePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4.5*mainPanel.getPreferredSize().height)/7);
		JPanel tablePanel = buildRecordList(tablePanelPreferredSize, recordsPerPage, 0, "");
		
		mainPanel.add(tablePanel, BorderLayout.CENTER);

		return mainPanel;
	}
	
	private JPanel buildRecordList(Dimension preferredSize, int limit, int start, String query){
		JPanel res = new JPanel(new BorderLayout());
		res.setPreferredSize(preferredSize);
		rowHeight = res.getPreferredSize().height/12;
		
		List<Record> records = recordService.getRecords("", start, start+limit);
		JScrollPane recordTable = buildRecordTable(records, preferredSize);
		
		int numRecords = recordService.getNumOfRecords("");
		JPanel navbar = buildPaginationPanel(numRecords, preferredSize);
		
		res.add(recordTable, BorderLayout.CENTER);
		res.add(navbar, BorderLayout.SOUTH);
		
		return res;
	}

	private JScrollPane buildRecordTable(List<Record> records, Dimension preferredSize) {
		int numRecords = records.size();
		
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}
				return super.getColumnClass(column);
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		};

		model.setColumnIdentifiers(new Object[] { "#", "Date", "Client", "Album", "Quantity", "Total", "", "" });
		JTable table = new JTable(model);

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		table.getColumnModel().getColumn(6).setCellRenderer(buttonRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(buttonRenderer);

		for (int i : new int[]{0,4,5,6,7}){
			table.getColumnModel().getColumn(i).setPreferredWidth(preferredSize.width/13);
		}
		table.getColumnModel().getColumn(1).setPreferredWidth(2*preferredSize.width/13);
		table.getColumnModel().getColumn(2).setPreferredWidth(3*preferredSize.width/13);
		table.getColumnModel().getColumn(3).setPreferredWidth(3*preferredSize.width/13);
		
		table.setRowHeight(rowHeight);

		for (int i = 0; i < numRecords; i++) {
			Record r = records.get(i);
			Object[] data = new Object[8];
			data[0] = Integer.toString((currentPage-1) * recordsPerPage + i + 1);
			data[1] = r.getDate().toString();
			data[2] = r.getClient();
			data[3] = r.getAlbum().getTitle();
			data[4] = Integer.toString(r.getQuantity());
			data[5] = Double.toString(r.getAlbum().getPrice() * r.getQuantity());
			data[6] = buildEditButton(r);
			data[7] = buildDeleteButton(r);
			model.insertRow(i, data);
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return new JScrollPane(table);
	}

	private JButton buildEditButton(Record r) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit");
		JButton b = new JButton(editIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "You want to edit (cap)");
			}
			
		});
		
		return b;
	}

	private JButton buildDeleteButton(final Record r) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you really want to delete record No. "+r.getId()+"?");
			}
			
		});
		
		return b;
	}
	
	private JPanel buildPaginationPanel(int n, final Dimension preferredSize) {
		JPanel paginationPanel = new JPanel();
		//paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		
		if(n > recordsPerPage) {
			int i = 0;
			int numOfPages = n/recordsPerPage + (n%recordsPerPage==0 ? 0 : 1);
			while(i < numOfPages) {
				final int index = i;
				final JButton pageBtn = new JButton(""+(++i));
				pageBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						currentPage = index+1;
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						mainPanel.add(buildRecordList(preferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				paginationPanel.add(pageBtn);
			}
		}
		
		return paginationPanel;
	}

}
