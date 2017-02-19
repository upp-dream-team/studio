package eventprocessors;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import models.Musician;
import services.MusicianService;

@Component
public class MusicianEventProcessorImpl  implements MusicianEventProcessor{

	@Autowired
	private MusicianService musicianService;
	private JPanel mainPanel;
	private String currentFilterQuery;
	private int currentPage;
	private int musiciansPerPage = 10;
	private Dimension musicianListPanelPreferredSize;
	private Dimension paginationBarPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;

	public JPanel process(Dimension sizeOfParentElement) {
		currentPage = 1;
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		
		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,mainPanel.getPreferredSize().height/7);
		JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		musicianListPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4.5*mainPanel.getPreferredSize().height)/7);
		JPanel musicianListPanel = buildMusicianListPanel(musicianListPanelPreferredSize, musiciansPerPage, 0, null);
		paginationBarPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height/7);
		JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
		
		mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
		mainPanel.add(musicianListPanel, BorderLayout.CENTER);
		mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
		
		mainPanel.revalidate();
		mainPanel.repaint();
		return mainPanel;
	}

	private JPanel buildPaginationBarPanel(Dimension dimension) {
		JPanel paginationPanel = new JPanel();
		paginationPanel.setPreferredSize(dimension);
		paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		
		int count = musicianService.getNumOfMusicians(currentFilterQuery);
		
		if(count > musiciansPerPage) {
			int i = 0;
			int numOfPages = count/musiciansPerPage;
			if(count%musiciansPerPage != 0)
				++numOfPages;
			while(i < numOfPages) {
				final int index = i;
				final JButton pageBtn = new JButton(""+(++i));
				pageBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						currentPage = index+1;
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						mainPanel.add(buildMusicianListPanel(musicianListPanelPreferredSize, musiciansPerPage, (currentPage-1)*musiciansPerPage, currentFilterQuery),BorderLayout.CENTER);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				paginationPanel.add(pageBtn);
			}
		}
		
		return paginationPanel;
	}

	private JPanel buildMusicianListPanel(Dimension panelDimension, int limit, int offset, String filterQuery) {
		JPanel musicianListPanel = new JPanel();
		musicianListPanel.setPreferredSize(panelDimension);
		musicianListPanel.setBackground(Color.WHITE);
		
		List<Musician> musicians = musicianService.getMusicians(filterQuery, offset, offset+limit);
		
		DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[] { "#", "Name", "Phone", "Actions" });
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(1).setPreferredWidth(4*musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(2).setPreferredWidth(3*musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(3).setPreferredWidth(2*musicianListPanel.getPreferredSize().width/10);
        if(musicians.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "No results" , "" , "" });
        } else {
        	for (int i = 0; i < musicians.size(); ++i){
        		System.out.println(musicians.get(i).getId() + ", " + musicians.get(i).getName() + ", " + musicians.get(i).getPhone());
                model.insertRow(i, new Object[]{ i+1, musicians.get(i).getName() , musicians.get(i).getPhone(),"Some Actions" });
                table.setRowHeight(i, musicianListPanel.getPreferredSize().height/11);
            }
        }
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);
        musicianListPanel.add(table.getTableHeader());
        musicianListPanel.add(table); 
        
		return musicianListPanel;
	}

	private JPanel buildSearchAndCreatePanel(Dimension dimension) {
		JPanel searchAndCreatePanel = new JPanel();
		searchAndCreatePanel.setPreferredSize(dimension);
		searchAndCreatePanel.setLayout(new BorderLayout());
		
		JPanel searchPanel = buildSearchPanel(new Dimension(3*searchAndCreatePanel.getPreferredSize().width/4, searchAndCreatePanel.getPreferredSize().height));
		JPanel createMusicianPanel = buildCreateMusicianPanel(new Dimension(searchAndCreatePanel.getPreferredSize().width/4, searchAndCreatePanel.getPreferredSize().height));
		
		searchAndCreatePanel.add(searchPanel, BorderLayout.WEST);
		searchAndCreatePanel.add(createMusicianPanel, BorderLayout.EAST);
		return searchAndCreatePanel;
	}

	private JPanel buildCreateMusicianPanel(Dimension preferredSize) {
		JPanel createMusicianPanel = new JPanel();
		createMusicianPanel.setPreferredSize(preferredSize);
		
		JButton addMusicianBtn = new JButton("Add Musician");
		addMusicianBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel createMusicianFormPanel = new JPanel();
				createMusicianFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				createMusicianFormPanel.setSize(searchAndCreatePanelPreferredSize);
				createMusicianFormPanel.setLayout(new GridLayout(3,2));
				
				JLabel nameLabel = new JLabel("Name");
				JLabel phoneLabel = new JLabel("Phone Number");
				final JTextField nameInput = new JTextField(40);
				final JTextField phoneInput = new JTextField(40);
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
						String phone = phoneInput.getText().trim();
						
						if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Name' is required");
						} else if(phone == null || phone.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Phone Number' is required");
						} else {
							Musician m = new Musician();
							m.setName(name);
							m.setPhone(phone);
							try {
								musicianService.createMusician(m);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save the musician to db.");
							}
							
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel musicianListPanel = buildMusicianListPanel(musicianListPanelPreferredSize, musiciansPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
							
							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(musicianListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
							
							mainPanel.revalidate();
							mainPanel.repaint();
						} 
					}
				});
				
				createMusicianFormPanel.add(nameLabel);
				createMusicianFormPanel.add(nameInput);
				createMusicianFormPanel.add(phoneLabel);
				createMusicianFormPanel.add(phoneInput);
				createMusicianFormPanel.add(cancelBtn);
				createMusicianFormPanel.add(saveBtn);
				
				mainPanel.add(createMusicianFormPanel, BorderLayout.NORTH);
				
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		createMusicianPanel.add(addMusicianBtn);
		return createMusicianPanel;
	}

	private JPanel buildSearchPanel(Dimension preferredSize) {
		JPanel searchPanel = new JPanel();
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
				mainPanel.add(buildMusicianListPanel(musicianListPanelPreferredSize, musiciansPerPage, (currentPage-1)*musiciansPerPage, currentFilterQuery),BorderLayout.CENTER);
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
				mainPanel.add(buildPaginationBarPanel(paginationBarPanelPreferredSize), BorderLayout.SOUTH);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		
		searchPanel.add(filterQueryInput);
		searchPanel.add(searchBtn);
		return searchPanel;
	}
}
