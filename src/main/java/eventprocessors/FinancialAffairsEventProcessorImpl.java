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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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
import models.License;
import models.Record;
import models.Selling;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import services.AlbumService;
import services.FinancialAffairsService;
import services.RecordService;

@Component
public class FinancialAffairsEventProcessorImpl implements FinancialAffairsEventProcessor {
	
	@Autowired
	private RecordEventProcessor recordEventProcessor;
	
	@Autowired
	private LicenseEventProcessor licenseEventProcessor;
	
	@Autowired
	private FinancialAffairsService financialAffairsService;

	private JPanel mainPanel;
	private JPanel financesPanel;
	
	private String currentFilterQuery;
	private Date dateFrom;
	private Date dateTo;
	private final int recordsPerPage = 10;
	private int currentPage = 1;
	private int rowHeight;
	
	private int startForRecords = 0;
	private int startForLicenses = 0;
	
	private Dimension whatToShowPanelSize;
	private Dimension financesPanelSize;
	
	private Dimension listPanelPreferredSize;
	private Dimension paginationPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;
	
	// govnokod
	private List<Selling> allSellings;

	public JPanel process(Dimension sizeOfParentElement) {
		System.out.println("in FinancialAffairsEventProcessor");
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		
		dateFrom = financialAffairsService.getDateOfTheOldestSelling();
		dateTo = financialAffairsService.getDateOfTheNewestSelling();
		allSellings = financialAffairsService.getAll(currentFilterQuery, dateFrom, dateTo);
		
		final int whatToShowPanelHeight = 50;
		whatToShowPanelSize = new Dimension((int) (sizeOfParentElement.getWidth()), whatToShowPanelHeight);
		financesPanelSize = new Dimension((int) (sizeOfParentElement.getWidth()), (int) (sizeOfParentElement.getHeight() - whatToShowPanelHeight));
		
		JPanel whatToShow = buildWhatToShowPanel(whatToShowPanelSize);
		financesPanel = buildFinancesPanel(financesPanelSize);

		mainPanel.add(whatToShow, BorderLayout.NORTH);
		mainPanel.add(financesPanel, BorderLayout.CENTER);

		return mainPanel;
	}

	private JPanel buildWhatToShowPanel(final Dimension preferredSize) {
		JPanel res = new JPanel(new FlowLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);

		JLabel label = new JLabel("Показувати ");
		final String[] options = { "всі продажі", "лише індивідуальні продажі", "лише продажі ліцензій" };
		final JComboBox<String> comboBox = new JComboBox(options);

		comboBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String choice = (String) comboBox.getSelectedItem();
				if (choice.equals(options[1])) {
					// лише індивідуальні продажі
					BorderLayout layout = (BorderLayout) mainPanel.getLayout();
					mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
					mainPanel.add(recordEventProcessor.process(financesPanelSize), BorderLayout.CENTER);
					mainPanel.revalidate();
					mainPanel.repaint();
				} else if (choice.equals(options[2])) {
					// лише продажі ліцензій
					BorderLayout layout = (BorderLayout) mainPanel.getLayout();
					mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
					mainPanel.add(licenseEventProcessor.process(financesPanelSize), BorderLayout.CENTER);
					mainPanel.revalidate();
					mainPanel.repaint();
				} else {
					// всі продажі
					BorderLayout layout = (BorderLayout) mainPanel.getLayout();
					mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
					JPanel financesPanel = buildFinancesPanel(financesPanelSize);
					mainPanel.add(financesPanel, BorderLayout.CENTER);
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}

		});

		res.add(label);
		res.add(comboBox);

		return res;
	}

	private JPanel buildFinancesPanel(Dimension preferredSize){
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		searchAndCreatePanelPreferredSize = new Dimension(preferredSize.width, preferredSize.height/7);
		listPanelPreferredSize = new Dimension(preferredSize.width, (int)(4*preferredSize.height)/7);
		paginationPanelPreferredSize = new Dimension(preferredSize.width, preferredSize.height/7);
		
		JPanel searchPanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		JPanel tablePanel = buildTablePanel(listPanelPreferredSize, recordsPerPage, 0, "");
		JPanel paginationPanel = buildPaginationPanel(paginationPanelPreferredSize);
		
		res.add(searchPanel, BorderLayout.NORTH);
		res.add(tablePanel, BorderLayout.CENTER);
		res.add(paginationPanel, BorderLayout.SOUTH);
		
		return res;
	}
	
	private JPanel buildSearchAndCreatePanel(Dimension preferredSize) {
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);
		
		JPanel searchPanel = buildSearchPanel(new Dimension(3*res.getPreferredSize().width/4, res.getPreferredSize().height));
		JPanel createPanel = buildCreatePanel(new Dimension(res.getPreferredSize().width/4, res.getPreferredSize().height));
		
		res.add(searchPanel, BorderLayout.WEST);
		res.add(createPanel, BorderLayout.EAST);
		
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
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				financesPanel.add(buildTablePanel(listPanelPreferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				financesPanel.add(buildPaginationPanel(paginationPanelPreferredSize), BorderLayout.SOUTH);
				financesPanel.revalidate();
				financesPanel.repaint();
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
				currentPage = 1;
				BorderLayout layout = (BorderLayout) financesPanel.getLayout();
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				financesPanel.add(buildTablePanel(listPanelPreferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				financesPanel.add(buildPaginationPanel(paginationPanelPreferredSize), BorderLayout.SOUTH);
				financesPanel.revalidate();
				financesPanel.repaint();
			}
			
		});
		
		JLabel toL = new JLabel(" до ");
		UtilDateModel model2 = new UtilDateModel(dateTo);
		model2.setSelected(true);
		final JDatePickerImpl datePickerTo = new JDatePickerImpl(new JDatePanelImpl(model2));

		datePickerTo.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				dateTo = (Date) datePickerTo.getModel().getValue();
				currentPage = 1;
				BorderLayout layout = (BorderLayout) financesPanel.getLayout();
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				financesPanel.add(buildTablePanel(listPanelPreferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
				financesPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				financesPanel.add(buildPaginationPanel(paginationPanelPreferredSize), BorderLayout.SOUTH);
				financesPanel.revalidate();
				financesPanel.repaint();
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

	private JPanel buildCreatePanel(Dimension dimension) {
		JPanel res = new JPanel();
		// TODO 
		return res;
	}

	private JPanel buildTablePanel(Dimension preferredSize, int limit, int start, String query) {
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.RED);
		res.setPreferredSize(preferredSize);
		rowHeight = financesPanelSize.height/20;
		
		//List<Selling> sellings = financialAffairsService.getSellings(query, startForRecords, startForLicenses, limit, dateFrom, dateTo);
		//System.out.println("in buildTablePanel() ===> sellings.size() = " + sellings.size());
		
		allSellings = financialAffairsService.getAll(currentFilterQuery, dateFrom, dateTo);
		List<Selling> sellings = allSellings.subList(start, allSellings.size() < start+limit ? allSellings.size() : start+limit);
		
		JTable table = buildRecordTable(sellings, preferredSize);
		
		Double sum = 0.0;
		for (Selling s : sellings){
			if (s.isLicense()){
				// startForLicenses++;
				sum += s.getPrice() * s.getPeriod();
			} else {
				// startForRecords++;
				sum += s.getQuantity() * s.getAlbum().getPrice();
			}
			
		}
		JPanel totalPanel = buildTotalPanel(query, sum);
		
		res.add(table.getTableHeader(), BorderLayout.NORTH);
		res.add(table, BorderLayout.CENTER);
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
		totalPanelCont.add(SwingUtils.createJLabelWithSpecifiedFont(financialAffairsService.getTotal(query, dateFrom, dateTo).toString(), f2));
		
		res.add(totalPanelCont, BorderLayout.EAST);
		return res;
	}
	
	private JTable buildRecordTable(List<Selling> sellings, Dimension preferredSize) {
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

		model.setColumnIdentifiers(new Object[] { "#", "Дата", "Клієнт", "Альбом", "Деталі", "Сума", "", "" });
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
					data[4] = "на " + l.getPeriod() + " місяців за ціною " + l.getPrice();
					data[5] = Double.toString(l.getPrice() * l.getPeriod());
					data[6] = licenseEventProcessor.buildEditButton(l);
					data[7] = licenseEventProcessor.buildDeleteButton(l);
				} else {
					Record r = (Record) sellings.get(i);
					data[1] = r.getDate().toString();
					data[2] = r.getClient();
					data[3] = r.getAlbum().getTitle();
					data[4] = r.getQuantity() + " копій";
					data[5] = Double.toString(r.getAlbum().getPrice() * r.getQuantity());
					data[6] = recordEventProcessor.buildEditButton(r);
					data[7] = recordEventProcessor.buildDeleteButton(r);
				}
				model.insertRow(i, data);
			}
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return table;
	}
	
	private JPanel buildPaginationPanel(final Dimension preferredSize) {
		int n = financialAffairsService.getNumOfSellings(currentFilterQuery, dateFrom, dateTo);
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
						BorderLayout layout = (BorderLayout) financesPanel.getLayout();
						financesPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						financesPanel.add(buildTablePanel(preferredSize, recordsPerPage, (currentPage-1)*recordsPerPage, currentFilterQuery),BorderLayout.CENTER);
						financesPanel.revalidate();
						financesPanel.repaint();
					}
				});
				paginationPanel.add(pageBtn);
			}
		}
		
		return paginationPanel;
	}
}
