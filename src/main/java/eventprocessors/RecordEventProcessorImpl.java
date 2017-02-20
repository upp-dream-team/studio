package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Musician;
import models.Record;
import services.AlbumService;
import services.RecordService;

@Component
public class RecordEventProcessorImpl implements RecordEventProcessor {

	@Autowired
	private RecordService recordService;
	
	@Autowired
	private AlbumService albumService;
	
	private JPanel mainPanel;
	private String currentFilterQuery;
	final int recordsPerPage = 10;
	int currentPage = 1;
	int rowHeight;
	private Dimension listPanelPreferredSize;
	private Dimension paginationPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;

	public JPanel process(Dimension sizeOfParentElement) {
		System.out.println("in RecordEventProcessor");
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.white);
		mainPanel.setLayout(new BorderLayout());
		
		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,mainPanel.getPreferredSize().height/7);
		listPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4.5*mainPanel.getPreferredSize().height)/7);
		paginationPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height/7);
		
		JPanel searchPanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		JPanel tablePanel = buildRecordList(listPanelPreferredSize, recordsPerPage, 0, "");
		JPanel paginationPanel = buildPaginationPanel(paginationPanelPreferredSize);
		
		mainPanel.add(searchPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(paginationPanel, BorderLayout.SOUTH);

		return mainPanel;
	}
	
	private JPanel buildRecordList(Dimension preferredSize, int limit, int start, String query){
		JPanel res = new JPanel(new BorderLayout());
		res.setPreferredSize(preferredSize);
		rowHeight = mainPanel.getPreferredSize().height/20;
		
		List<Record> records = recordService.getRecords("", start, start+limit);
		JScrollPane recordTable = buildRecordTable(records, preferredSize);
		
		res.add(recordTable, BorderLayout.CENTER);
		
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
	
	private JPanel buildPaginationPanel(final Dimension preferredSize) {
		int n = recordService.getNumOfRecords(currentFilterQuery);
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
	
	private JPanel buildSearchAndCreatePanel(Dimension dimension) {
		JPanel res = new JPanel();
		
		res.setBackground(Color.WHITE);
		res.setPreferredSize(dimension);
		res.setLayout(new BorderLayout());
		
		JPanel searchPanel = buildSearchPanel(new Dimension(3*res.getPreferredSize().width/4, res.getPreferredSize().height));
		JPanel createPanel = buildCreatePanel(new Dimension(res.getPreferredSize().width/4, res.getPreferredSize().height));
		
		res.add(searchPanel, BorderLayout.WEST);
		res.add(createPanel, BorderLayout.EAST);
		return res;
	}

	private JPanel buildCreatePanel(Dimension preferredSize) {
		JPanel res = new JPanel();
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		Icon addIcon = SwingUtils.createImageIcon("/icons/add.png","Add");
		
		JButton addBtn = new JButton("Add Sale Record", addIcon);
		addBtn.setBackground(Color.WHITE);
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel form = new JPanel();
				form.setBorder(new EmptyBorder(10, 10, 30, 10));
				form.setSize(searchAndCreatePanelPreferredSize);
				form.setLayout(new GridLayout(5,2));
				
				JLabel dateL = new JLabel("Date");
				JLabel nameL = new JLabel("Client Name");
				JLabel albumL = new JLabel("Album");
				JLabel quantityL = new JLabel("Quantity");

				final JTextField dateInput = new JTextField(40);
				dateInput.setText("mm/dd/yyyy");
				final JTextField nameInput = new JTextField(40);
				final JComboBox albumList = new JComboBox(albumService.getAlbumTitles().toArray());
				final JTextField quantityInput = new JTextField(40);
				
				JPanel inputPanel = new JPanel();
				JButton cancelBtn = new JButton("Cancel");
				JButton saveBtn = new JButton("Save");
				
				cancelBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
						mainPanel.add(buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize),BorderLayout.NORTH);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				
				saveBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						String name = nameInput.getText().trim();
						String date = dateInput.getText().trim();
						String quantity = quantityInput.getText().trim();
						String albumTitle = (String)albumList.getSelectedItem();
						
						if(date == null || date.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Date' is required");
						} else if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
						} else if(quantity == null || quantity.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Quantity' is required");
						}else if(albumTitle == null || albumTitle.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Album' is required");
						}else{
							System.out.println("Record r = new Record();");
							Record r = new Record();
							r.setClient(name);
							DateFormat formatter = new SimpleDateFormat("mm/dd/yyyy"); 
							try {
								r.setDate((Date)formatter.parse(date));
							} catch (ParseException e2) {
								JOptionPane.showMessageDialog(null, "Incorrect date format");
							}
							try{
								r.setQuantity(Integer.parseInt(quantity));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Quantity must be integer");
							}
							r.setAlbum(albumService.get(1, 0, albumTitle).get(0));
							System.out.println("r.setAlbum(albumService.get(1, 0, albumTitle).get(0));");
							try {
								System.out.println("recordService.createRecord(r);");
								recordService.createRecord(r);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save the record to the DB");
							}
		
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel musicianListPanel = buildRecordList(listPanelPreferredSize, recordsPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);
							
							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(musicianListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
							
							mainPanel.revalidate();
							mainPanel.repaint();
						} 
					}
				});
				
				form.add(dateL);
				form.add(dateInput);
				form.add(nameL);
				form.add(nameInput);
				form.add(albumL);
				form.add(albumList);
				form.add(quantityL);
				form.add(quantityInput);
				form.add(saveBtn);
				form.add(cancelBtn);
				
				mainPanel.add(form, BorderLayout.NORTH);
				
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		res.add(addBtn);
		return res;
	}

	private JPanel buildSearchPanel(Dimension preferredSize) {
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setLayout(new FlowLayout());
		searchPanel.setPreferredSize(preferredSize);
		
		final JTextField filterQueryInput = new JTextField(40); 
		filterQueryInput.setText("Search for . . .");
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {     
				currentFilterQuery = filterQueryInput.getText();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildRecordList(listPanelPreferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				mainPanel.add(buildPaginationPanel(paginationPanelPreferredSize), BorderLayout.SOUTH);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		
		searchPanel.add(filterQueryInput);
		searchPanel.add(searchBtn);
		return searchPanel;
	}
}
