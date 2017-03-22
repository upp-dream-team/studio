package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Record;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
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
	private Date dateFrom;
	private Date dateTo;
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
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		
		dateFrom = recordService.getDateOfTheOldestRecord();
		dateTo = recordService.getDateOfTheNewestRecord();
		
		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,mainPanel.getPreferredSize().height/7);
		listPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4*mainPanel.getPreferredSize().height)/7);
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
		
		List<Record> records = recordService.getRecords(query, start, start+limit, dateFrom, dateTo);
		JTable recordTable = buildRecordTable(records, preferredSize);
		
		Double sum = 0.0;
		for (Record r : records){
			sum += r.getQuantity() * r.getAlbum().getPrice();
		}
		JPanel totalPanel = buildTotalPanel(query, sum);
		
		res.add(recordTable.getTableHeader(), BorderLayout.NORTH);
		res.add(recordTable, BorderLayout.CENTER);
		res.add(totalPanel, BorderLayout.SOUTH);
		
		return res;
	}
	
	private JPanel buildTotalPanel(String query, Double totalForPage){
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.WHITE);
		JPanel totalPanelCont = new JPanel(new GridLayout(2, 2));
		totalPanelCont.setBackground(Color.WHITE);
		
		Font f1 = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		Font f2 = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont("Total for this page: ", f1));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont(totalForPage.toString(), f2));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont("Total for all pages: ", f1));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont(recordService.getTotal(query, dateFrom, dateTo).toString(), f2));
		
		res.add(totalPanelCont, BorderLayout.EAST);
		return res;
	}

	private JTable buildRecordTable(List<Record> records, Dimension preferredSize) {
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
		TableColumnModel columnModel = table.getColumnModel();

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		columnModel.getColumn(6).setCellRenderer(buttonRenderer);
		columnModel.getColumn(7).setCellRenderer(buttonRenderer);

		for (int i : new int[]{0,4,5,6,7}){
			columnModel.getColumn(i).setPreferredWidth(preferredSize.width/13);
		}
		columnModel.getColumn(1).setPreferredWidth(2*preferredSize.width/13);
		columnModel.getColumn(2).setPreferredWidth(3*preferredSize.width/13);
		columnModel.getColumn(3).setPreferredWidth(3*preferredSize.width/13);
		
		if(records.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "No results" , null , null, null, null, null, null });
		} else {
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
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return table;
	}

	public JButton buildEditButton(final Record r) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit");
		JButton b = new JButton(editIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("Edit button " + r.getId());
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel editFormPanel = new JPanel();
				editFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				editFormPanel.setSize(searchAndCreatePanelPreferredSize);
				editFormPanel.setLayout(new GridLayout(4, 2));
				
				JLabel dateL = new JLabel("Date");
				JLabel nameL = new JLabel("Client Name");
				JLabel quantityL = new JLabel("Quantity");

				UtilDateModel model = new UtilDateModel(r.getDate());
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField nameInput = new JTextField(40);
				nameInput.setText(r.getClient());
				final JTextField quantityInput = new JTextField(40);
				quantityInput.setText(r.getQuantity()+"");
				
				JButton cancelBtn = new JButton("Cancel");
				JButton saveBtn = new JButton("Save Changes");
				
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
						Date date = (Date) datePicker.getModel().getValue();
						String quantity = quantityInput.getText().trim();
						
						if(date == null) {
							JOptionPane.showMessageDialog(null, "Field 'Date' is required");
						} else if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
						} else if(quantity == null || quantity.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Quantity' is required");
						}else{
							r.setClient(name);
							r.setDate(date);
							try{
								r.setQuantity(Integer.parseInt(quantity));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Quantity must be integer");
							}
							try {
								recordService.updateRecord(r);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to update the record in the DB");
							}
							currentPage = 1;
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
				
				editFormPanel.add(dateL);
				editFormPanel.add(datePicker);
				editFormPanel.add(nameL);
				editFormPanel.add(nameInput);
				editFormPanel.add(quantityL);
				editFormPanel.add(quantityInput);
				editFormPanel.add(cancelBtn);
				editFormPanel.add(saveBtn);
				
				mainPanel.add(editFormPanel, BorderLayout.NORTH);
				mainPanel.revalidate();
				mainPanel.repaint();
				
			}
		});
		return b;
	}

	public JButton buildDeleteButton(final Record r) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this record?");
				if(dialogResult == JOptionPane.YES_OPTION){
					recordService.deleteRecord(r.getId());
					
					mainPanel.removeAll();
					currentPage = 1;
					JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
					JPanel recordListPanel = buildRecordList(listPanelPreferredSize, recordsPerPage, 0, null);
					JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);
					
					mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
					mainPanel.add(recordListPanel, BorderLayout.CENTER);
					mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
					
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
			
		});
		
		return b;
	}
	
	private JPanel buildPaginationPanel(final Dimension preferredSize) {
		int n = recordService.getNumOfRecords(currentFilterQuery, dateFrom, dateTo);
		JPanel paginationPanel = new JPanel();
		paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		paginationPanel.setBorder(new EmptyBorder(50, 0, 50, 0));
		
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

				UtilDateModel model = new UtilDateModel();
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				
				final JTextField nameInput = new JTextField(40);
				final JComboBox<String> albumList = new JComboBox(albumService.getAlbumTitles().toArray());
				final JTextField quantityInput = new JTextField(40);
				
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
						Date date = (Date) datePicker.getModel().getValue();
						String quantity = quantityInput.getText().trim();
						String albumTitle = (String)albumList.getSelectedItem();
						
						if(date == null) {
							JOptionPane.showMessageDialog(null, "Field 'Date' is required");
						} else if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
						} else if(quantity == null || quantity.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Quantity' is required");
						}else if(albumTitle == null || albumTitle.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Album' is required");
						}else{
							//System.out.println("Record r = new Record();");
							Record r = new Record();
							r.setClient(name);
							r.setDate(date);
							System.out.println(r.getDate());
							try{
								r.setQuantity(Integer.parseInt(quantity));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Quantity must be integer");
							}
							r.setAlbum(albumService.get(1, 0, albumTitle).get(0));
							//System.out.println("r.setAlbum(albumService.get(1, 0, albumTitle).get(0));");
							try {
								//System.out.println("recordService.createRecord(r);");
								recordService.createRecord(r);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save the record to the DB");
							}
		
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel recordListPanel = buildRecordList(listPanelPreferredSize, recordsPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);
							
							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(recordListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
							
							mainPanel.revalidate();
							mainPanel.repaint();
						} 
					}
				});
				
				form.add(dateL);
				form.add(datePicker);
				form.add(nameL);
				form.add(nameInput);
				form.add(albumL);
				form.add(albumList);
				form.add(quantityL);
				form.add(quantityInput);
				form.add(cancelBtn);
				form.add(saveBtn);
				
				mainPanel.add(form, BorderLayout.NORTH);
				
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		res.add(addBtn);
		return res;
	}

	private JPanel buildSearchPanel(Dimension preferredSize) {
		JPanel resPanel = new JPanel();
		resPanel.setLayout(new GridLayout(2, 1));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setLayout(new FlowLayout());
		searchPanel.setPreferredSize(preferredSize);
		
		final JTextField filterQueryInput = SwingUtils.createTextFIeldWithPlaceholder(40, "Search for...");
		
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
		
		JPanel datePanel = new JPanel();
		datePanel.setBackground(Color.WHITE);
		
		JLabel fromL = new JLabel("Показати продажі з ");
		UtilDateModel model1 = new UtilDateModel(dateFrom);
		model1.setSelected(true);
		final JDatePickerImpl datePickerFrom = new JDatePickerImpl(new JDatePanelImpl(model1));
		
		datePickerFrom.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dateFrom = (Date) datePickerFrom.getModel().getValue();
				
				// DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				// String dateFormat = formatter.format(dateFrom);
				// System.out.println(dateFormat);
				
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
		
		JLabel toL = new JLabel(" до ");
		UtilDateModel model2 = new UtilDateModel(dateTo);
		model2.setSelected(true);
		final JDatePickerImpl datePickerTo = new JDatePickerImpl(new JDatePanelImpl(model2));

		datePickerTo.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dateTo = (Date) datePickerTo.getModel().getValue();
				
				// DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				// String dateFormat = formatter.format(dateTo);
				// System.out.println(dateFormat);
				
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
		
		datePanel.add(fromL);
		datePanel.add(datePickerFrom);
		datePanel.add(toL);
		datePanel.add(datePickerTo);
		
		resPanel.add(searchPanel);
		resPanel.add(datePanel);
		
		return resPanel;
	}
}
