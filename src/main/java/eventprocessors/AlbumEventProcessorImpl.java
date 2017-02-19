package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Album;
import models.Musician;
import models.Song;
import services.AlbumService;
import services.MusicianService;
import services.SongService;

@Component
public class AlbumEventProcessorImpl implements AlbumEventProcessor {

	@Autowired
	private AlbumService albumService;
	@Autowired
	private MusicianService musicianService;
	@Autowired
	private SongService songService;
	private JPanel mainPanel;
	private JPanel songsPanel;
	private String currentFilterQuery;
	private int currentPage;
	private int albumsPerPage = 10;
	private Dimension albumListPanelPreferredSize;
	private Dimension paginationBarPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;
	private int rowHeight;
	private List<Song> songsToAddToAlbum; 

	public JPanel process(Dimension sizeOfParentElement) {
		currentPage = 1;
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());
		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,mainPanel.getPreferredSize().height/7);
		JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		albumListPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int)(4.5*mainPanel.getPreferredSize().height)/7);
		JPanel albumListPanel = buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, 0, null);
		paginationBarPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height/7);
		JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
		
		mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
		mainPanel.add(albumListPanel, BorderLayout.CENTER);
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
		
		int count = albumService.getNumOfAlbums(currentFilterQuery);
		
		if(count > albumsPerPage) {
			int i = 0;
			int numOfPages = count/albumsPerPage;
			if(count%albumsPerPage != 0)
				++numOfPages;
			while(i < numOfPages) {
				final int index = i;
				final JButton pageBtn = new JButton(""+(++i));
				pageBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						currentPage = index+1;
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						mainPanel.add(buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, (currentPage-1)*albumsPerPage, currentFilterQuery),BorderLayout.CENTER);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				paginationPanel.add(pageBtn);
			}
		}
		
		return paginationPanel;
	}

	private JPanel buildAlbumListPanel(Dimension panelDimension, int limit, int offset, String filterQuery) {
		JPanel albumListPanel = new JPanel();
		albumListPanel.setPreferredSize(panelDimension);
		albumListPanel.setBackground(Color.WHITE);
		
		List<Album> albums = albumService.get(albumsPerPage, albumsPerPage*(currentPage-1) , currentFilterQuery);
		
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
	      
        model.setColumnIdentifiers(new Object[] { "#", "Title", "","" });
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(1).setPreferredWidth(7*albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(2).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(3).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(buttonRenderer);
        if(albums.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "No results" , null , null });
        } else {
        	rowHeight = albumListPanel.getPreferredSize().height/12;
        	for (int i = 0; i < albums.size(); ++i){
        		JButton editBtn = buildEditAlbumButton(albums.get(i));
        		JButton deleteBtn = buildDeleteAlbumButton(albums.get(i));
                model.insertRow(i, new Object[]{ ""+((currentPage-1)*albumsPerPage+i+1), albums.get(i).getTitle(), editBtn, deleteBtn });
                table.setRowHeight(i, rowHeight);
            }
        }
        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);
        albumListPanel.add(table.getTableHeader());
        albumListPanel.add(table); 
        
		return albumListPanel;
	}

	private JButton buildDeleteAlbumButton(final Album album) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png","Delete Album");
		JButton deleteBtn = new JButton(deleteIcon);
		deleteBtn.setBackground(Color.WHITE);
		deleteBtn.setBorderPainted(false);
		deleteBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "Do you really want to delete'"+album.getTitle()+"'");
				if(dialogResult == JOptionPane.YES_OPTION){
					albumService.deleteAlbum(album.getId());
					mainPanel.removeAll();
					JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
					JPanel albumListPanel = buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, 0, null);
					JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
					
					mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
					mainPanel.add(albumListPanel, BorderLayout.CENTER);
					mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
					
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
		});
		return deleteBtn;
	}

	private JButton buildEditAlbumButton(final Album album) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png","Edit Album");
		JButton editBtn = new JButton(editIcon);
		editBtn.setBackground(Color.WHITE);
		editBtn.setBorderPainted(false);
		editBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
				
				JPanel editAlbumFormPanel = new JPanel();
				editAlbumFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				editAlbumFormPanel.setSize(searchAndCreatePanelPreferredSize);
				editAlbumFormPanel.setLayout(new GridLayout(2,2));
				
				JLabel titleLabel = new JLabel("Title");
				final JTextField titleInput = new JTextField(40);
				titleInput.setText(album.getTitle());
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
						String title = titleInput.getText().trim();
						
						if(title == null || title.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Title' is required");
						} else {
							album.setTitle(title);
							try {
								albumService.updateAlbum(album);
							} catch(Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save changes to db.");
							}
							
							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel albumListPanel = buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
							
							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(albumListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
							
							mainPanel.revalidate();
							mainPanel.repaint();
						} 
					}
				});
				
				editAlbumFormPanel.add(titleLabel);
				editAlbumFormPanel.add(titleInput);
				editAlbumFormPanel.add(cancelBtn);
				editAlbumFormPanel.add(saveBtn);
				
				mainPanel.add(editAlbumFormPanel, BorderLayout.NORTH);
				
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
		JPanel createAlbumPanel = buildCreateAlbumPanel(new Dimension(searchAndCreatePanel.getPreferredSize().width/4, searchAndCreatePanel.getPreferredSize().height));
		
		searchAndCreatePanel.add(searchPanel, BorderLayout.WEST);
		searchAndCreatePanel.add(createAlbumPanel, BorderLayout.EAST);
		return searchAndCreatePanel;
	}

	private JPanel buildCreateAlbumPanel(Dimension preferredSize) {
		JPanel createAlbumPanel = new JPanel();
		createAlbumPanel.setBackground(Color.WHITE);
		createAlbumPanel.setPreferredSize(preferredSize);
		
		Icon addIcon = SwingUtils.createImageIcon("/icons/add.png","Add Album");
		JButton addAlbumBtn = new JButton("Add Album",addIcon);
		addAlbumBtn.setBackground(Color.WHITE);
		addAlbumBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				songsToAddToAlbum = new ArrayList<Song>();
				mainPanel.removeAll();
				
				JPanel createAlbumFormPanel = new JPanel();
				createAlbumFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				createAlbumFormPanel.setSize(searchAndCreatePanelPreferredSize);
				createAlbumFormPanel.setLayout(new GridLayout(1,3));
				
				JButton cancelBtn = new JButton("Cancel");
				JPanel labelPanel = new JPanel();
				labelPanel.setBackground(Color.WHITE);
				labelPanel.setLayout(new GridLayout(15, 1,15,15));
				labelPanel.add(new JLabel("Title", SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Record Date",SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Price", SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Musician Royalties", SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Producer", SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Producer Royalties", SwingConstants.RIGHT));
				labelPanel.add(new JLabel("Songs", SwingConstants.RIGHT));
				labelPanel.add(cancelBtn);
				
				JButton saveBtn = new JButton("Save");
				final JTextField titleInput = new JTextField(40);
				final JTextField recorDateInput = new JTextField(40);
				recorDateInput.setText("MM/dd/yyyy");
				final JTextField priceInput = new JTextField(40);
				final JTextField musicianRoyaltiesInput = new JTextField(40);
				final JTextField producerRoyalriesInput = new JTextField(40);
				final JComboBox producerList = new JComboBox(musicianService.getMuscianNames().toArray());				 
				JPanel inputPanel = new JPanel();
				inputPanel.setBackground(Color.WHITE);
				inputPanel.setBorder(new EmptyBorder(0, 50, 0, 30));
				inputPanel.setLayout(new GridLayout(15, 1,15,15));
				inputPanel.add(titleInput);
				inputPanel.add(recorDateInput);
				inputPanel.add(priceInput);
				inputPanel.add(musicianRoyaltiesInput);
				inputPanel.add(producerList);
				inputPanel.add(producerRoyalriesInput);
				inputPanel.add(buildAddSongsButton());
				inputPanel.add(saveBtn);
				
				songsPanel = new JPanel();
				songsPanel.setBackground(Color.WHITE);
				songsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
				
				createAlbumFormPanel.add(labelPanel);
				createAlbumFormPanel.add(inputPanel);
				createAlbumFormPanel.add(songsPanel);
				
				cancelBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						mainPanel.removeAll();
						JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
						JPanel albumListPanel = buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, 0, null);
						JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
						mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
						mainPanel.add(albumListPanel, BorderLayout.CENTER);
						mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});
				
				saveBtn.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						String title = titleInput.getText().trim();
						String recordDate = recorDateInput.getText().trim();
						String price = priceInput.getText().trim();
						String musicianRoyalties = musicianRoyaltiesInput.getText().trim();
						String producerRoyalties = producerRoyalriesInput.getText().trim();
						String producerName = (String)producerList.getSelectedItem();
						
						if(title == null || title.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Title' is required");
						} else if(price == null || price.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Price' is required");
						} else {
							Album a = new Album();
							try {
								a.setTitle(title);
								a.setMusicianRoyalties(Double.parseDouble(musicianRoyalties));
								a.setProducerRoyalties(Double.parseDouble(producerRoyalties));
								a.setPrice(Double.parseDouble(price));
								a.setProducerFk(musicianService.getByName(producerName).getId());
								DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); 
								a.setRecordDate((Date)formatter.parse(recordDate));
								albumService.createAlbum(a);
								
								mainPanel.removeAll();
								JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
								JPanel albumListPanel = buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, 0, null);
								JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);
								
								mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
								mainPanel.add(albumListPanel, BorderLayout.CENTER);
								mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);
								
								mainPanel.revalidate();
								mainPanel.repaint();
							} catch(Exception ex) {
								System.out.println(ex);
								JOptionPane.showMessageDialog(null, "Make sure you have provided valid data.");
							}
						} 
					}
				});
				
				mainPanel.add(createAlbumFormPanel, BorderLayout.CENTER);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		createAlbumPanel.add(addAlbumBtn);
		return createAlbumPanel;
	}
	
	private JButton buildAddSongsButton() {
		JButton btn = new JButton("Add song");
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				List<Song> songs = songService.get(10000, 0, null);
				
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
			      
		        model.setColumnIdentifiers(new Object[] { "Song Title", "",});
		        JTable table = new JTable(model);
		        table.setCellSelectionEnabled(false);
		        table.setRowSelectionAllowed(false);
		        table.getColumnModel().getColumn(0).setPreferredWidth(5*songsPanel.getSize().width/6);
		        table.getColumnModel().getColumn(1).setPreferredWidth(songsPanel.getSize().width/6);
		        
		        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		        table.getColumnModel().getColumn(1).setCellRenderer(buttonRenderer);
		        if(songs.size() == 0) {
		        	model.insertRow(0, new Object[]{"No songs" , null });
		        } else {
		        	rowHeight = songsPanel.getSize().height/22;
		        	for (int i = 0; i < songs.size(); ++i){
		        		JButton addSongToAlbumBtn = buildAddSongToAlbumBtn(songs.get(i));
		                model.insertRow(i, new Object[]{ songs.get(i).getTitle() , addSongToAlbumBtn});
		                table.setRowHeight(i, rowHeight);
		            }
		        }
		        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
		        table.getTableHeader().setSize(table.getSize().width, 20);
		        table.getTableHeader().setBackground(Color.WHITE);
		        songsPanel.add(table.getTableHeader());
		        songsPanel.add(table);
		        mainPanel.setBackground(Color.WHITE);
		        mainPanel.revalidate();
		        mainPanel.repaint();
			}
		});
		
		return btn;
	}
	
	private JButton buildAddSongToAlbumBtn(final Song song) {
		JButton btn = new JButton(SwingUtils.createImageIcon("/icons/add.png", "Add song to Album"));
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				songsToAddToAlbum.add(song);
			}
		});
		
		return btn;
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
				mainPanel.add(buildAlbumListPanel(albumListPanelPreferredSize, albumsPerPage, (currentPage-1)*albumsPerPage, currentFilterQuery),BorderLayout.CENTER);
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
