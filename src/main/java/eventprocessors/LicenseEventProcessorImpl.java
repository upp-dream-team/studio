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
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import models.License;
import services.AlbumService;
import services.LicenseService;

@Component
public class LicenseEventProcessorImpl implements LicenseEventProcessor {

	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private AlbumService albumService;
	
	private JPanel mainPanel;
	private String currentFilterQuery;
	private Date dateFrom;
	private Date dateTo;
	final int licensesPerPage = 10;
	int currentPage = 1;
	int rowHeight;
	private Dimension listPanelPreferredSize;
	private Dimension paginationPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;

	public JPanel process(Dimension sizeOfParentElement) {
		System.out.println("in LicenseEventProcessor");
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		
		dateFrom = licenseService.getDateOfTheOldestLicense();
		dateTo = licenseService.getDateOfTheNewestLicense();
		
		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,mainPanel.getPreferredSize().height/7);
		listPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4*mainPanel.getPreferredSize().height)/7);
		paginationPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height/7);
		
		JPanel searchPanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		JPanel tablePanel = buildLicenseList(listPanelPreferredSize, licensesPerPage, 0, "");
		JPanel paginationPanel = buildPaginationPanel(paginationPanelPreferredSize);
		
		mainPanel.add(searchPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(paginationPanel, BorderLayout.SOUTH);

		return mainPanel;
	}
	
	private JPanel buildLicenseList(Dimension preferredSize, int limit, int start, String query){
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.BLUE);
		res.setPreferredSize(preferredSize);
		rowHeight = mainPanel.getPreferredSize().height/20;
		
		List<License> licenses = licenseService.getLicenses(query, start, start+limit, dateFrom, dateTo);
		JTable licenseTable = buildLicenseTable(licenses, preferredSize);
		
		Double sum = 0.0;
		for (License l : licenses){
			sum += l.getPrice() * l.getPeriod();
		}
		JPanel totalPanel = buildTotalPanel(query, sum);
		
		res.add(licenseTable.getTableHeader(), BorderLayout.NORTH);
		res.add(licenseTable, BorderLayout.CENTER);
		res.add(totalPanel, BorderLayout.SOUTH);
		
		return res;
	}

	private JPanel buildTotalPanel(String query, Double totalForPage) {
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.WHITE);
		JPanel totalPanelCont = new JPanel(new GridLayout(2, 2));
		totalPanelCont.setBackground(Color.WHITE);
		
		Font f1 = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
		Font f2 = new Font(Font.SANS_SERIF, Font.BOLD, 16);
		
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont("Total for this page: ", f1));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont(totalForPage.toString(), f2));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont("Total for all pages: ", f1));
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont(licenseService.getTotal(query, dateFrom, dateTo).toString(), f2));
		
		res.add(totalPanelCont, BorderLayout.EAST);
		return res;
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
			License r = licenses.get(i);
			Object[] data = new Object[9];
			data[0] = Integer.toString((currentPage-1) * licensesPerPage + i + 1);
			data[1] = r.getDate().toString();
			data[2] = r.getClient();
			data[3] = r.getAlbum().getTitle();
			data[4] = Integer.toString(r.getPeriod());
			data[5] = Double.toString(r.getPrice());
			data[6] = Double.toString(r.getPrice() * r.getPeriod());
			data[7] = buildEditButton(r);
			data[8] = buildDeleteButton(r);
			model.insertRow(i, data);
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return table;
	}

	public JButton buildEditButton(final License r) {
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
				editFormPanel.setLayout(new GridLayout(5, 2));
				
				JLabel dateL = new JLabel("Date");
				JLabel nameL = new JLabel("Client Name");
				JLabel periodL = new JLabel("Period");
				JLabel priceL = new JLabel("Price");

				UtilDateModel model = new UtilDateModel(r.getDate());
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField nameInput = new JTextField(40);
				nameInput.setText(r.getClient());
				final JTextField periodInput = new JTextField(40);
				periodInput.setText(r.getPeriod()+"");
				final JTextField priceInput = new JTextField(40);
				priceInput.setText(r.getPeriod()+"");
				
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
							r.setClient(name);
							r.setDate(date);
							try{
								r.setPeriod(Integer.parseInt(period));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Period must be integer");
							}
							try{
								r.setPrice(Float.parseFloat(price));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Period must be float");
							}
							try {
								licenseService.updateLicense(r);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to update the license in the DB");
							}
							currentPage = 1;
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel musicianListPanel = buildLicenseList(listPanelPreferredSize, licensesPerPage, 0, null);
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
				editFormPanel.add(periodL);
				editFormPanel.add(periodInput);
				editFormPanel.add(priceL);
				editFormPanel.add(priceInput);
				editFormPanel.add(cancelBtn);
				editFormPanel.add(saveBtn);
				
				mainPanel.add(editFormPanel, BorderLayout.NORTH);
				mainPanel.revalidate();
				mainPanel.repaint();
				
			}
		});
		return b;
	}

	public JButton buildDeleteButton(final License r) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure you want to delete this license?");
				if(dialogResult == JOptionPane.YES_OPTION){
					licenseService.deleteLicense(r.getId());
					
					mainPanel.removeAll();
					currentPage = 1;
					JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
					JPanel albumListPanel = buildLicenseList(listPanelPreferredSize, licensesPerPage, 0, null);
					JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);
					
					mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
					mainPanel.add(albumListPanel, BorderLayout.CENTER);
					mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
					
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
			
		});
		
		return b;
	}
	
	private JPanel buildPaginationPanel(final Dimension preferredSize) {
		int n = licenseService.getNumOfLicenses(currentFilterQuery, dateFrom, dateTo);
		JPanel paginationPanel = new JPanel();
		paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		paginationPanel.setBorder(new EmptyBorder(50, 0, 50, 0));
		
		if(n > licensesPerPage) {
			int i = 0;
			int numOfPages = n/licensesPerPage + (n%licensesPerPage==0 ? 0 : 1);
			while(i < numOfPages) {
				final int index = i;
				final JButton pageBtn = new JButton(""+(++i));
				pageBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						currentPage = index+1;
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						mainPanel.add(buildLicenseList(preferredSize, licensesPerPage, (currentPage-1)*licensesPerPage, currentFilterQuery),BorderLayout.CENTER);
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
		
		JButton addBtn = new JButton("Add License Sale", addIcon);
		addBtn.setBackground(Color.WHITE);
		addBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel form = new JPanel();
				form.setBorder(new EmptyBorder(10, 10, 30, 10));
				form.setSize(searchAndCreatePanelPreferredSize);
				form.setLayout(new GridLayout(6, 2));
				
				JLabel dateL = new JLabel("Date");
				JLabel nameL = new JLabel("Client Name");
				JLabel albumL = new JLabel("Album");
				JLabel periodL = new JLabel("Period");
				JLabel priceL = new JLabel("Price");

				UtilDateModel model = new UtilDateModel();
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField nameInput = new JTextField(40);
				final JComboBox albumList = new JComboBox(albumService.getAlbumTitles().toArray());
				final JTextField periodInput = new JTextField(40);
				final JTextField priceInput = new JTextField(40);
				
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
						String period = periodInput.getText().trim();
						String price = priceInput.getText().trim();
						String albumTitle = (String)albumList.getSelectedItem();
						
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
							System.out.println("License l = new License();");
							License r = new License();
							r.setClient(name);
							r.setDate(date);
							try{
								r.setPeriod(Integer.parseInt(period));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Period must be integer");
							}
							try{
								r.setPrice(Float.parseFloat(price));
							}catch(NumberFormatException e1){
								JOptionPane.showMessageDialog(null, "Price must be float");
							}
							r.setAlbum(albumService.get(1, 0, albumTitle).get(0));
							System.out.println("r.setAlbum(albumService.get(1, 0, albumTitle).get(0));");
							try {
								System.out.println("licenseService.createLicense(r);");
								licenseService.createLicense(r);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save the license to the DB");
							}
		
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel musicianListPanel = buildLicenseList(listPanelPreferredSize, licensesPerPage, 0, null);
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
				form.add(datePicker);
				form.add(nameL);
				form.add(nameInput);
				form.add(albumL);
				form.add(albumList);
				form.add(periodL);
				form.add(periodInput);
				form.add(priceL);
				form.add(priceInput);
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
				mainPanel.add(buildLicenseList(listPanelPreferredSize, licensesPerPage, (currentPage-1)*licensesPerPage, currentFilterQuery),BorderLayout.CENTER);
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
				
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateFormat = formatter.format(dateFrom);
				System.out.println(dateFormat);
				
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildLicenseList(listPanelPreferredSize, licensesPerPage, (currentPage-1)*licensesPerPage, currentFilterQuery),BorderLayout.CENTER);
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
				
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateFormat = formatter.format(dateTo);
				System.out.println(dateFormat);
				
				currentPage = 1;
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				mainPanel.add(buildLicenseList(listPanelPreferredSize, licensesPerPage, (currentPage-1)*licensesPerPage, currentFilterQuery),BorderLayout.CENTER);
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
