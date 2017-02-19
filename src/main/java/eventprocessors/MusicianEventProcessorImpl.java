package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
import services.MusicianService;
import util.IconBuilder;
import util.JTableButtonMouseListener;
import util.JTableButtonRenderer;

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
	private int rowHeight;

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
		       //all cells false
		       return false;
		    }
		};
	      
        model.setColumnIdentifiers(new Object[] { "#", "Name", "Phone", "","" });
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(1).setPreferredWidth(4*musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(2).setPreferredWidth(3*musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(3).setPreferredWidth(musicianListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(4).setPreferredWidth(musicianListPanel.getPreferredSize().width/10);
        
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(3).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(buttonRenderer);
        if(musicians.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "No results" , "" , null, null });
        } else {
        	rowHeight = musicianListPanel.getPreferredSize().height/12;
        	for (int i = 0; i < musicians.size(); ++i){
        		JButton editBtn = buildEditMusicianButton(musicians.get(i));
        		JButton deleteBtn = buildDeleteMusicianButton(musicians.get(i));
                model.insertRow(i, new Object[]{ ""+((currentPage-1)*musiciansPerPage+i+1), musicians.get(i).getName() , musicians.get(i).getPhone(),editBtn, deleteBtn });
                table.setRowHeight(i, rowHeight);
            }
        }
        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);
        musicianListPanel.add(table.getTableHeader());
        musicianListPanel.add(table); 
        
		return musicianListPanel;
	}

	private JButton buildDeleteMusicianButton(final Musician musician) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Edit Musician");
		JButton deleteBtn = new JButton(deleteIcon);
		deleteBtn.setBackground(Color.WHITE);
		deleteBtn.setBorderPainted(false);
		deleteBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you really want to delete'"+musician.getName()+"'");
				if(dialogResult == JOptionPane.YES_OPTION){
					musicianService.deleteMusician(musician.getId());
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
		return deleteBtn;
	}

	private JButton buildEditMusicianButton(final Musician musician) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit Musician");
		JButton editBtn = new JButton(editIcon);
		editBtn.setBackground(Color.WHITE);
		editBtn.setBorderPainted(false);
		editBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel editMusicianFormPanel = new JPanel();
				editMusicianFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				editMusicianFormPanel.setSize(searchAndCreatePanelPreferredSize);
				editMusicianFormPanel.setLayout(new GridLayout(3,2));
				
				JLabel nameLabel = new JLabel("Name");
				JLabel phoneLabel = new JLabel("Phone Number");
				final JTextField nameInput = new JTextField(40);
				nameInput.setText(musician.getName());
				final JTextField phoneInput = new JTextField(40);
				phoneInput.setText(musician.getPhone());
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
						String phone = phoneInput.getText().trim();
						
						if(name == null || name.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Name' is required");
						} else if(phone == null || phone.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Phone Number' is required");
						} else {
							musician.setName(name);
							musician.setPhone(phone);
							try {
								musicianService.updateMusician(musician);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save changes to db.");
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
				
				editMusicianFormPanel.add(nameLabel);
				editMusicianFormPanel.add(nameInput);
				editMusicianFormPanel.add(phoneLabel);
				editMusicianFormPanel.add(phoneInput);
				editMusicianFormPanel.add(cancelBtn);
				editMusicianFormPanel.add(saveBtn);
				
				mainPanel.add(editMusicianFormPanel, BorderLayout.NORTH);
				
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		return editBtn;
	}

	private JPanel buildSearchAndCreatePanel(Dimension dimension) {
		JPanel searchAndCreatePanel = new JPanel();
		searchAndCreatePanel.setBackground(Color.WHITE);
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
		createMusicianPanel.setBackground(Color.WHITE);
		createMusicianPanel.setPreferredSize(preferredSize);
		
		Icon addIcon = SwingUtils.createImageIcon("/icons/add.png","Add Musician");
		
		JButton addMusicianBtn = new JButton("Add Musician",addIcon);
		addMusicianBtn.setBackground(Color.WHITE);
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
