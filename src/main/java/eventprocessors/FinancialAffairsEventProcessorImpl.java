package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.License;
import models.Record;
import models.Selling;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import services.AlbumService;
import services.FinancialAffairsService;

@Component
public class FinancialAffairsEventProcessorImpl implements FinancialAffairsEventProcessor {
	
	@Autowired
	private FinancialAffairsService financialAffairsService;
	
	@Autowired
	private AlbumService albumService;

	private JPanel mainPanel;
	
	private JPanel filtersPanel;
	private JPanel formPanel;
	private JPanel tablePanel;
	
	private String currentFilterQuery;
	final String[] options = { "All", "Records", "Licenses" };
	private String category = options[0];
	private Date dateFrom;
	private Date dateTo;
	private final int recordsPerPage = 10;
	private int currentPage = 1;
	private int rowHeight;
	
	private Dimension filtersPanelSize;
	private Dimension formPanelSize;
	private Dimension tablePanelSize;
	
	public JPanel process(Dimension sizeOfParentElement) {
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		
		dateFrom = financialAffairsService.getDateOfTheOldestSelling();
		dateTo = financialAffairsService.getDateOfTheNewestSelling();
		
		final int filtersPanelHeight = 150;
		final int formPanelWidth = 300;
		filtersPanelSize = new Dimension(sizeOfParentElement.width - formPanelWidth, filtersPanelHeight);
		formPanelSize = new Dimension(formPanelWidth, filtersPanelHeight);
		tablePanelSize = new Dimension(sizeOfParentElement.width, sizeOfParentElement.height - filtersPanelHeight);
		
		filtersPanel = buildFiltersPanel(filtersPanelSize);
		formPanel = buildFormPanel(formPanelSize);
		tablePanel = buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery);
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(filtersPanel, BorderLayout.CENTER);
		northPanel.add(formPanel, BorderLayout.EAST);

		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private JPanel buildFiltersPanel(Dimension preferredSize) {
		final int numOfRows = 3;
		JPanel res = new JPanel(new GridLayout(numOfRows, 1));
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		Dimension oneThirdOfSize = new Dimension(preferredSize.width, preferredSize.height/3);
		JPanel whatToShowPanel = buildWhatToShowPanel(oneThirdOfSize);
		JPanel datePickerPanel = buildDatePickerPanel(oneThirdOfSize);
		JPanel searchPanel = buildSearchPanel(oneThirdOfSize);
		
		res.add(whatToShowPanel);
		res.add(datePickerPanel);
		res.add(searchPanel);
		
		return res;
	}
	
	private JPanel buildWhatToShowPanel(final Dimension preferredSize) {
		JPanel res = new JPanel(new FlowLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);

		JLabel label = new JLabel("What to display");
		final JComboBox<String> comboBox = new JComboBox(options);
		comboBox.setSelectedItem(category);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				category = (String) comboBox.getSelectedItem();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				tablePanel = buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery);
				mainPanel.add(tablePanel, BorderLayout.CENTER);;
				mainPanel.revalidate();
				mainPanel.repaint();
			}

		});

		res.add(label);
		res.add(comboBox);

		return res;
	}

	private JPanel buildDatePickerPanel(final Dimension preferredSize){
		JPanel res = new JPanel(new FlowLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		JLabel fromL = new JLabel("From");
		UtilDateModel model1 = new UtilDateModel(dateFrom);
		model1.setSelected(true);
		final JDatePickerImpl datePickerFrom = new JDatePickerImpl(new JDatePanelImpl(model1));
		
		datePickerFrom.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dateFrom = (Date) datePickerFrom.getModel().getValue();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery), BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
			
		});
		
		JLabel toL = new JLabel("To");
		UtilDateModel model2 = new UtilDateModel(dateTo);
		model2.setSelected(true);
		final JDatePickerImpl datePickerTo = new JDatePickerImpl(new JDatePanelImpl(model2));

		datePickerTo.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dateTo = (Date) datePickerTo.getModel().getValue();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery), BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
			
		});
		
		res.add(fromL);
		res.add(datePickerFrom);
		res.add(toL);
		res.add(datePickerTo);
		
		return res;
	}
	
	private JPanel buildSearchPanel(final Dimension preferredSize){

		JPanel res = new JPanel(new FlowLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		final JTextField filterQueryInput = SwingUtils.createTextFIeldWithPlaceholder(40, "Search for ...");
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {     
				currentFilterQuery = filterQueryInput.getText();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery));
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		
		res.add(filterQueryInput);
		res.add(searchBtn);
		
		return res;		
	}
	
	private JPanel buildFormPanel(final Dimension preferredSize){
		JPanel res = new JPanel(new FlowLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				formPanel.removeAll();
				final JPanel createFormPanel = new JPanel();
				createFormPanel.setPreferredSize(formPanelSize);
				createFormPanel.setBackground(Color.WHITE);
				createFormPanel.setBorder(new EmptyBorder(40, 10, 50, 10));
				createFormPanel.setLayout(new GridLayout(2, 1));
				
				JLabel typeL = new JLabel("Pick type");
				String[] typeOptions = {"", "record", "license"};
				final JComboBox typeCB = new JComboBox(typeOptions);
				typeCB.setSelectedIndex(0);
				typeCB.addActionListener(new ActionListener(){

					public void actionPerformed(ActionEvent e) {
						int index = typeCB.getSelectedIndex();
						if (index == 0)
							return;
						createFormPanel.removeAll();
						JLabel dateL = new JLabel("Date");
						JLabel clientL = new JLabel("Client Name");
						JLabel albumL = new JLabel("Album");
						UtilDateModel model = new UtilDateModel();
						model.setSelected(true);
						final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
						final JTextField nameInput = new JTextField(40);
						final JComboBox<String> albumCB = new JComboBox(albumService.getAlbumTitles().toArray());
						JLabel quantityL = new JLabel("Quantity");
						final JTextField quantityInput = new JTextField(40);
						JLabel periodL = new JLabel("Period");
						JLabel priceL = new JLabel("Price");
						final JTextField periodInput = new JTextField(40);
						final JTextField priceInput = new JTextField(40);
						JButton cancelBtn = new JButton("Cancel");
						JButton saveBtn = new JButton("Save");
						cancelBtn.addActionListener(new ActionListener(){

							public void actionPerformed(ActionEvent e) {
								formPanel.removeAll();
								formPanel.add(buildFormPanel(formPanelSize));
								formPanel.revalidate();
								formPanel.repaint();
							}
							
						});
						
						createFormPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
						if (index == 1)
							createFormPanel.setLayout(new GridLayout(5, 2));		
						else
							createFormPanel.setLayout(new GridLayout(6, 2));
						
						createFormPanel.add(dateL);
						createFormPanel.add(datePicker);
						createFormPanel.add(clientL);
						createFormPanel.add(nameInput);
						createFormPanel.add(albumL);
						createFormPanel.add(albumCB);
						
						if (index == 1){
							createFormPanel.add(quantityL);
							createFormPanel.add(quantityInput);
							
							saveBtn.addActionListener(new ActionListener(){

								public void actionPerformed(ActionEvent e) {
									String name = nameInput.getText().trim();
									Date date = (Date) datePicker.getModel().getValue();
									String quantity = quantityInput.getText().trim();
									String albumTitle = (String)albumCB.getSelectedItem();
									
									if(date == null) {
										JOptionPane.showMessageDialog(null, "Field 'Date' is required");
									} else if(name == null || name.isEmpty()) {
										JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
									} else if(quantity == null || quantity.isEmpty()){
										JOptionPane.showMessageDialog(null, "Field 'Quantity' is required");
									}else if(albumTitle == null || albumTitle.isEmpty()){
										JOptionPane.showMessageDialog(null, "Field 'Album' is required");
									}else{
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
										try {
											financialAffairsService.createRecord(r);
										} catch(Exception ex) {
											ex.printStackTrace();
											JOptionPane.showMessageDialog(null, "Failed to save the record to the DB");
										}
					
										currentPage = 1;
										formPanel.removeAll();
										formPanel.add(buildFormPanel(formPanelSize));
										formPanel.revalidate();
										formPanel.repaint();
										BorderLayout layout = (BorderLayout) mainPanel.getLayout();
										mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
										mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, 0, currentFilterQuery));
										mainPanel.revalidate();
										mainPanel.repaint();
	
									}
								}
							
							});
							
						} else {
							createFormPanel.add(periodL);
							createFormPanel.add(periodInput);
							createFormPanel.add(priceL);
							createFormPanel.add(priceInput);
							
							saveBtn.addActionListener(new ActionListener(){

								public void actionPerformed(ActionEvent e) {
									String name = nameInput.getText().trim();
									Date date = (Date) datePicker.getModel().getValue();
									String period = periodInput.getText().trim();
									String price = priceInput.getText().trim();
									String albumTitle = (String)albumCB.getSelectedItem();
									
									if(date == null) {
										JOptionPane.showMessageDialog(null, "Field 'Date' is required");
									} else if(name == null || name.isEmpty()) {
										JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
									} else if(period == null || period.isEmpty()){
										JOptionPane.showMessageDialog(null, "Field 'Period' is required");
									}else if(price == null || price.isEmpty()){
										JOptionPane.showMessageDialog(null, "Field 'Price' is required");
									}else if(albumTitle == null || albumTitle.isEmpty()){
										JOptionPane.showMessageDialog(null, "Field 'Album' is required");
									}else{
										License l = new License();
										l.setClient(name);
										l.setDate(date);
										try{
											l.setPeriod(Integer.parseInt(period));
										}catch(NumberFormatException e1){
											JOptionPane.showMessageDialog(null, "Period must be integer");
										}
										try{
											l.setPrice(Float.parseFloat(price));
										}catch(NumberFormatException e1){
											JOptionPane.showMessageDialog(null, "Price must be float");
										}
										l.setAlbum(albumService.get(1, 0, albumTitle).get(0));
										try {
											financialAffairsService.createLicense(l);
										} catch(Exception ex) {
											ex.printStackTrace();
											JOptionPane.showMessageDialog(null, "Failed to save the license to the DB");
										}
					
										currentPage = 1;
										formPanel.removeAll();
										formPanel.add(buildFormPanel(formPanelSize));
										formPanel.revalidate();
										formPanel.repaint();
										BorderLayout layout = (BorderLayout) mainPanel.getLayout();
										mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
										mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, 0, currentFilterQuery));
										mainPanel.revalidate();
										mainPanel.repaint();
									} 
								}
								
							});
						}
						
						createFormPanel.add(cancelBtn);
						createFormPanel.add(saveBtn);
						
						createFormPanel.revalidate();
						createFormPanel.repaint();
					}
					
				});
				
				createFormPanel.add(typeL);
				createFormPanel.add(typeCB);
				
				formPanel.add(createFormPanel);
				formPanel.revalidate();
				formPanel.repaint();
			}
			
		});
		res.add(addBtn);
		return res;	
	}

	private JPanel buildTablePanel(Dimension preferredSize, int limit, int start, String filterQuery) {
		System.out.println("in buildTablePanel");
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		JTable table = null;
		rowHeight = 30;
		Double sum = 0.0;
		
		if (category.equals(options[1])){
			// ���� ����������� ������
			List<Record> records = financialAffairsService.getRecords(filterQuery, start, start+limit, dateFrom, dateTo);
			table = buildRecordsTable(records, preferredSize);
			for (Record r : records){
				sum += r.getQuantity() * r.getAlbum().getPrice();
			}
		} else if(category.equals(options[2])) {
			// ���� ������ ������
			List<License> licenses = financialAffairsService.getLicenses(filterQuery, start, start+limit, dateFrom, dateTo);
			table = buildLicenseTable(licenses, preferredSize);
			for (License l : licenses){
				sum += l.getPrice() * l.getPeriod();
			}			
		} else {
			List<Selling> sellings = financialAffairsService.getSellings(filterQuery, start, limit, dateFrom, dateTo);	
			sellings = sellings.subList(0, 10 <= sellings.size() ? 10 : sellings.size());
			System.out.println(sellings.size());
			table = buildSellingsTable(sellings, preferredSize);
			for (Selling s : sellings){
				if (s.isLicense()){
					sum += s.getPrice() * s.getPeriod();
				} else {
					sum += s.getQuantity() * s.getAlbum().getPrice();
				}
				
			}
		}
		
		table.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		JPanel totalPanel = buildTotalPanel(filterQuery, sum);
		JPanel paginationPanel = buildPaginationPanel();
		
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(totalPanel, BorderLayout.EAST);
		southPanel.add(paginationPanel, BorderLayout.CENTER);
		
		res.add(scrollPane, BorderLayout.CENTER);
		res.add(southPanel, BorderLayout.SOUTH);
		
		return res;
	}
	
	private JTable buildSellingsTable(List<Selling> sellings, Dimension preferredSize) {
		int numRecords = sellings.size();
		
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
		
		model.setColumnIdentifiers(new Object[] { "#", "Date", "Client", "Album", "Info", "Total", "", "" });
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		columnModel.getColumn(6).setCellRenderer(buttonRenderer);
		columnModel.getColumn(7).setCellRenderer(buttonRenderer);

		for (int i : new int[]{0,5,6,7}){
			columnModel.getColumn(i).setPreferredWidth(preferredSize.width/16);
		}
		columnModel.getColumn(1).setPreferredWidth(2*preferredSize.width/16);
		columnModel.getColumn(2).setPreferredWidth(3*preferredSize.width/16);
		columnModel.getColumn(3).setPreferredWidth(3*preferredSize.width/16);
		columnModel.getColumn(4).setPreferredWidth(4*preferredSize.width/16);
		
		if(sellings.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "No results" , null , null, null, null, null, null });
		} else {
			table.setRowHeight(rowHeight);

			for (int i = 0; i < numRecords; i++) {
				Object[] data = new Object[8];
				data[0] = Integer.toString((currentPage-1) * recordsPerPage + i + 1);
				if (sellings.get(i).isLicense()){
					License l = (License) sellings.get(i);
					data[1] = l.getDate().toString();
					data[2] = l.getClient();
					data[3] = l.getAlbum().getTitle();
					data[4] = "For " + l.getPeriod() + " months, price: " + l.getPrice();
					data[5] = Double.toString(l.getPrice() * l.getPeriod());
					data[6] = buildEditButton(l);
					data[7] = buildDeleteButton(l);
				} else {
					Record r = (Record) sellings.get(i);
					data[1] = r.getDate().toString();
					data[2] = r.getClient();
					data[3] = r.getAlbum().getTitle();
					data[4] = r.getQuantity() + " Quantity";
					data[5] = Double.toString(r.getAlbum().getPrice() * r.getQuantity());
					data[6] = buildEditButton(r);
					data[7] = buildDeleteButton(r);
				}
				model.insertRow(i, data);
			}
		}
		
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		
		return table;
	}
	
	private JTable buildRecordsTable(List<Record> records, Dimension preferredSize) {
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
	
	private JTable buildLicenseTable(List<License> licenses, Dimension preferredSize) {
		int numLicenses = licenses.size();
		
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

		model.setColumnIdentifiers(new Object[] { "#", "Date", "Client", "Album", "Period", "Price", "Total", "", "" });
		JTable table = new JTable(model);

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		table.getColumnModel().getColumn(7).setCellRenderer(buttonRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(buttonRenderer);

		for (int i : new int[]{0,4,5,6,7,8}){
			table.getColumnModel().getColumn(i).setPreferredWidth(preferredSize.width/14);
		}
		table.getColumnModel().getColumn(1).setPreferredWidth(2*preferredSize.width/14);
		table.getColumnModel().getColumn(2).setPreferredWidth(3*preferredSize.width/14);
		table.getColumnModel().getColumn(3).setPreferredWidth(3*preferredSize.width/14);
		
		table.setRowHeight(rowHeight);

		for (int i = 0; i < numLicenses; i++) {
			License l = licenses.get(i);
			Object[] data = new Object[9];
			data[0] = Integer.toString((currentPage-1) * recordsPerPage + i + 1);
			data[1] = l.getDate().toString();
			data[2] = l.getClient();
			data[3] = l.getAlbum().getTitle();
			data[4] = Integer.toString(l.getPeriod());
			data[5] = Double.toString(l.getPrice());
			data[6] = Double.toString(l.getPrice() * l.getPeriod());
			data[7] = buildEditButton(l);
			data[8] = buildDeleteButton(l);
			model.insertRow(i, data);
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return table;
	}
	
	private JPanel buildTotalPanel(String query, Double totalForPage){
		JPanel res = new JPanel(new GridLayout(2, 2));
		res.setBackground(Color.WHITE);
		res.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10));
		
		Font f1 = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		Font f2 = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		
		Double totalForAllPages = 0.0;
		
		res.add(SwingUtils.createJLabelWithSpecifiedFont("Total for this page: ", f1));
		res.add(SwingUtils.createJLabelWithSpecifiedFont(totalForPage.toString(), f2));
		if (category.equals(options[1])){
			totalForAllPages = financialAffairsService.getTotalForRecords(query, dateFrom, dateTo);
		} else if (category.equals(options[2])){
			totalForAllPages = financialAffairsService.getTotalForLicenses(query, dateFrom, dateTo);
		} else {
			totalForAllPages = financialAffairsService.getTotal(query, dateFrom, dateTo);
		}
		res.add(SwingUtils.createJLabelWithSpecifiedFont("Total for all pages: ", f1));
		res.add(SwingUtils.createJLabelWithSpecifiedFont(totalForAllPages.toString(), f2));
		
		return res;
	}
	
	private JPanel buildPaginationPanel() {
		
		int n = 0;
		if (category.equals(options[1])){
			n = financialAffairsService.getNumOfRecords(currentFilterQuery, dateFrom, dateTo);
		} else if (category.equalsIgnoreCase(options[2])){
			n = financialAffairsService.getNumOfLicenses(currentFilterQuery, dateFrom, dateTo);
		} else {
			n = financialAffairsService.getNumOfSellings(currentFilterQuery, dateFrom, dateTo);
		}
		
		JPanel paginationPanel = new JPanel();
		paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		paginationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10));
		
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
						mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery), BorderLayout.CENTER);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				paginationPanel.add(pageBtn);
			}
		}
		return paginationPanel;
	}
	
	private JButton buildDeleteButton(final Record r){
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this record?");
				if(dialogResult == JOptionPane.YES_OPTION){
					System.out.println("in REP deleteBtn actionListener");
					financialAffairsService.deleteRecord(r.getId());
					BorderLayout layout = (BorderLayout) mainPanel.getLayout();
					mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
					mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery));
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
			
		});
		
		return b;
	}
	
	private JButton buildDeleteButton(final License l){
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this license?");
				if(dialogResult == JOptionPane.YES_OPTION){
					financialAffairsService.deleteLicense(l.getId());
					BorderLayout layout = (BorderLayout) mainPanel.getLayout();
					mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
					mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery));
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
			
		});
		
		return b;
	}
	
	private JButton buildEditButton(final Record r){
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit");
		JButton b = new JButton(editIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				formPanel.removeAll();
				JPanel editFormPanel = new JPanel();
				editFormPanel.setPreferredSize(formPanelSize);
				editFormPanel.setBackground(Color.WHITE);
				editFormPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
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
						formPanel.removeAll();
						formPanel.add(buildFormPanel(formPanelSize));
						formPanel.revalidate();
						formPanel.repaint();
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
								financialAffairsService.updateRecord(r);
							} catch(Exception ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(null, "Failed to update the record in the DB");
							}
							currentPage = 1;
							formPanel.removeAll();
							formPanel.add(buildFormPanel(formPanelSize));
							formPanel.revalidate();
							formPanel.repaint();
							BorderLayout layout = (BorderLayout) mainPanel.getLayout();
							mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
							mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, 0, currentFilterQuery));
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
				formPanel.add(editFormPanel);
				formPanel.revalidate();
				formPanel.repaint();
			}
			
		});
		return b;
	}
	
	private JButton buildEditButton(final License l){
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit");
		JButton b = new JButton(editIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				formPanel.removeAll();
				JPanel editFormPanel = new JPanel();
				editFormPanel.setPreferredSize(formPanelSize);
				editFormPanel.setBackground(Color.WHITE);
				editFormPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
				editFormPanel.setLayout(new GridLayout(5, 2));
				
				JLabel dateL = new JLabel("Date");
				JLabel nameL = new JLabel("Client Name");
				JLabel periodL = new JLabel("Period");
				JLabel priceL = new JLabel("Price");

				UtilDateModel model = new UtilDateModel(l.getDate());
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField nameInput = new JTextField(40);
				nameInput.setText(l.getClient());
				final JTextField periodInput = new JTextField(40);
				periodInput.setText(l.getPeriod()+"");
				final JTextField priceInput = new JTextField(40);
				priceInput.setText(l.getPrice()+"");
				
				JButton cancelBtn = new JButton("Cancel");
				JButton saveBtn = new JButton("Save Changes");
				
				cancelBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						formPanel.removeAll();
						formPanel.add(buildFormPanel(formPanelSize));
						formPanel.revalidate();
						formPanel.repaint();
					}
				});
				
				saveBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						String name = nameInput.getText().trim();
						Date date = (Date) datePicker.getModel().getValue();
						String period = periodInput.getText().trim();
						String price = priceInput.getText().trim();
						
						if(date == null) {
							JOptionPane.showMessageDialog(null, "Field 'Date' is required");
						} else if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Client Name' is required");
						} else if(period == null || period.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Period' is required");
						}else if(price == null || price.isEmpty()){
							JOptionPane.showMessageDialog(null, "Field 'Price' is required");
						}else{
							l.setClient(name);
							l.setDate(date);
							try{
								l.setPeriod(Integer.parseInt(period));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Period must be integer");
							}
							try{
								l.setPrice(Float.parseFloat(price));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Period must be float");
							}
							try {
								financialAffairsService.updateLicense(l);
							} catch(Exception ex) {
								ex.printStackTrace();
								JOptionPane.showMessageDialog(null, "Failed to update the license in the DB");
							}
							currentPage = 1;
							formPanel.removeAll();
							formPanel.add(buildFormPanel(formPanelSize));
							formPanel.revalidate();
							formPanel.repaint();
							BorderLayout layout = (BorderLayout) mainPanel.getLayout();
							mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
							mainPanel.add(buildTablePanel(tablePanelSize, recordsPerPage, 0, currentFilterQuery));
							mainPanel.revalidate();
							mainPanel.repaint();
							
						}
					}
					
				});
				
				editFormPanel.add(dateL);
				editFormPanel.add(datePicker);
				editFormPanel.add(nameL);
				editFormPanel.add(nameInput);
				editFormPanel.add(periodL);
				editFormPanel.add(periodInput);
				editFormPanel.add(priceL);
				editFormPanel.add(priceInput);
				editFormPanel.add(cancelBtn);
				editFormPanel.add(saveBtn);
				
				formPanel.add(editFormPanel);
				formPanel.revalidate();
				formPanel.repaint();
			}
		});
		return b;
	}

}
