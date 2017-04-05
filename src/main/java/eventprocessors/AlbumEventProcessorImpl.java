package eventprocessors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
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
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.RozpodilDao;
import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Album;
import models.Musician;
import models.Rozpodil;
import models.Song;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import services.AlbumService;
import services.LicenseService;
import services.MusicianService;
import services.RecordService;
import services.SongService;

@Component
public class AlbumEventProcessorImpl implements AlbumEventProcessor {

	@Autowired
	private AlbumService albumService;
	@Autowired
	private MusicianService musicianService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private LicenseService licenseService;
	@Autowired
	private RozpodilDao rozpodilDao;
	@Autowired
	private SongService songService;
	private JPanel mainPanel;
	private JPanel allSongListPanel;
	private JPanel albumSongsPanel;
	private JPanel upperAlbumSongsSubPanel;
	private JPanel lowerAlbumSongsSubPanel;
	private ButtonsBuilder btns;
	private String currentFilterQuery;
	private int currentPage;
	private int albumsPerPage = 10;
	private Dimension albumListPanelPreferredSize;
	private Dimension paginationBarPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;
	private int rowHeight;
	private List<Song> songsToAddToAlbum; 

	public JPanel process(Dimension sizeOfParentElement) {
		btns = new ButtonsBuilder();
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
		
		DefaultTableModel model = SwingUtils.getDefaultTableModel();
        model.setColumnIdentifiers(new Object[] { "#", "Назва", "К-ть ліцензій" , "К-ть альбомів", "", "" });
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(1).setPreferredWidth(5*albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(2).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(3).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(4).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        table.getColumnModel().getColumn(5).setPreferredWidth(albumListPanel.getPreferredSize().width/10);
        
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(4).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(buttonRenderer);
        if(albums.size() == 0) {
        	model.insertRow(0, new Object[]{ "", "Немає результатів" , null , null });
        } else {
        	rowHeight = albumListPanel.getPreferredSize().height/12;
        	for (int i = 0; i < albums.size(); ++i){
        		JButton editBtn = buildEditAlbumButton(albums.get(i));
        		JButton deleteBtn = buildDeleteAlbumButton(albums.get(i));
                model.insertRow(i, new Object[]{ ""+((currentPage-1)*albumsPerPage+i+1), albums.get(i).getTitle(), 
                		licenseService.getNumOfSoldLicensesByAlbumId(albums.get(i).getId()), 
                		recordService.getNumOfSoldRecordsByAlbumId(albums.get(i).getId()), editBtn, deleteBtn });
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
				songsToAddToAlbum = album.getSongs();
				System.out.println("In edit form");
				for(Song s : songsToAddToAlbum) {
					System.out.println(""+s.getId()+"  "+s.getTitle());
				}
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
				labelPanel.add(new JLabel("", SwingConstants.RIGHT));
				labelPanel.add(cancelBtn);
				
				JButton saveBtn = new JButton("Save");
				final JTextField titleInput = new JTextField(40);
				titleInput.setText(album.getTitle());
				UtilDateModel model = new UtilDateModel(album.getRecordDate());
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField priceInput = new JTextField(40);
				priceInput.setText(album.getPrice()+"");
				final JTextField musicianRoyaltiesInput = new JTextField(40);
				musicianRoyaltiesInput.setText(""+album.getMusicianRoyalties());
				final JTextField producerRoyalriesInput = new JTextField(40);
				producerRoyalriesInput.setText(""+album.getProducerRoyalties());
				List<String> musicians = musicianService.getMuscianNames();
				System.out.println("Producer: " + album.getProducer().getName());
				int indexOfSelectedItem = musicians.indexOf(album.getProducer().getName());
				System.out.println("Producer index: " + indexOfSelectedItem);
				final JComboBox producerList = new JComboBox(musicians.toArray());
				producerList.setSelectedIndex(indexOfSelectedItem);
				JPanel inputPanel = new JPanel();
				inputPanel.setBackground(Color.WHITE);
				inputPanel.setBorder(new EmptyBorder(0, 50, 0, 30));
				inputPanel.setLayout(new GridLayout(15, 1,15,15));
				inputPanel.add(titleInput);
				inputPanel.add(datePicker);
				inputPanel.add(priceInput);
				inputPanel.add(musicianRoyaltiesInput);
				inputPanel.add(producerList);
				inputPanel.add(producerRoyalriesInput);
				inputPanel.add(buildAddSongsButton());
				inputPanel.add(saveBtn);
				
				allSongListPanel = new JPanel();
				allSongListPanel.setBackground(Color.WHITE);
				allSongListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
				
				createAlbumFormPanel.add(labelPanel);
				createAlbumFormPanel.add(inputPanel);
				createAlbumFormPanel.add(allSongListPanel);
				
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
						Date date = (Date) datePicker.getModel().getValue();
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
								a.setId(album.getId());
								a.setTitle(title);
								a.setMusicianRoyalties(Double.parseDouble(musicianRoyalties));
								a.setProducerRoyalties(Double.parseDouble(producerRoyalties));
								a.setPrice(Double.parseDouble(price));
								a.setProducerFk(musicianService.getByName(producerName).getId());
								a.setRecordDate(date);
								a.setSongs(songsToAddToAlbum);
								albumService.updateAlbum(a);
								
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
				createAlbumFormPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
				createAlbumFormPanel.setSize(searchAndCreatePanelPreferredSize);
				createAlbumFormPanel.setLayout(new GridLayout(1,3));
				
				JButton cancelBtn = new JButton("Відмінити");
				JPanel formPanel = new JPanel();
				formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
				JButton saveBtn = new JButton("Зберегти");
				final JTextField titleInput = new JTextField(40);
				UtilDateModel model = new UtilDateModel();
				model.setSelected(true);
				final JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(model));
				final JTextField priceInput = new JTextField(40);
				final JTextField musicianRoyaltiesInput = new JTextField(40);
				final JTextField producerRoyalriesInput = new JTextField(40);
				final JComboBox producerList = new JComboBox(musicianService.getMuscianNames().toArray());				 
				formPanel.setBackground(Color.WHITE);
				formPanel.setLayout(new GridLayout(15, 1,15,15));
				formPanel.add(new JLabel("Title", SwingConstants.LEFT));
				formPanel.add(titleInput);
				formPanel.add(new JLabel("Record Date",SwingConstants.LEFT));
				formPanel.add(datePicker);
				formPanel.add(new JLabel("Price", SwingConstants.LEFT));
				formPanel.add(priceInput);
				formPanel.add(new JLabel("Musician Royalties", SwingConstants.LEFT));
				formPanel.add(musicianRoyaltiesInput);
				formPanel.add(new JLabel("Producer", SwingConstants.LEFT));
				formPanel.add(producerList);
				formPanel.add(new JLabel("Producer Royalties", SwingConstants.LEFT));
				formPanel.add(producerRoyalriesInput);
				formPanel.add(buildAddSongsButton());
				formPanel.add(saveBtn);
				formPanel.add(cancelBtn);
				allSongListPanel = new JPanel();
				allSongListPanel.setBackground(Color.WHITE);
				allSongListPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				
				upperAlbumSongsSubPanel = new JPanel();
				lowerAlbumSongsSubPanel = new JPanel();
				
				albumSongsPanel = new JPanel();
				albumSongsPanel.setLayout(new GridLayout(2, 1));
				albumSongsPanel.add(upperAlbumSongsSubPanel);
				albumSongsPanel.add(lowerAlbumSongsSubPanel);
				
				
				createAlbumFormPanel.add(formPanel);
				createAlbumFormPanel.add(allSongListPanel);
				createAlbumFormPanel.add(albumSongsPanel);
				
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
						Date date = (Date) datePicker.getModel().getValue();
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
								a.setRecordDate(date);
								a.setSongs(songsToAddToAlbum);
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
		final JButton btn = new JButton("Пісні");
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				btn.setEnabled(false);
				List<Song> songs = songService.get(50, 0, null);
				DefaultTableModel model = SwingUtils.getDefaultTableModel();
		        model.setColumnIdentifiers(new Object[] { "Назва пісні", "",});
		        JTable table = new JTable(model);
		        table.setCellSelectionEnabled(false);
		        table.setRowSelectionAllowed(false);
		        table.getColumnModel().getColumn(0).setPreferredWidth(8*(allSongListPanel.getSize().width-60)/10);
		        table.getColumnModel().getColumn(1).setPreferredWidth(2*(allSongListPanel.getSize().width-60)/10);
		        
		        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		        table.getColumnModel().getColumn(1).setCellRenderer(buttonRenderer);
		        if(songs.size() == 0) {
		        	model.insertRow(0, new Object[]{"Немає пісень" , null });
		        } else {
		        	rowHeight = allSongListPanel.getSize().height/22;
		        	for (int i = 0; i < songs.size(); ++i){
		        		String btnText = "+";
		        		
		        		List<Integer> ids = new ArrayList<Integer>();
		        		for(Song s : songsToAddToAlbum) {
		        			ids.add(s.getId());
		        		}
		        		
		        		if(ids.contains(songs.get(i).getId())) {
		        			System.out.print("is in album");
		        			btnText = "-";
		        		}
		        		JButton addSongToAlbumBtn = buildAddSongToAlbumBtn(songs.get(i),btnText);
		                model.insertRow(i, new Object[]{ songs.get(i).getTitle() , addSongToAlbumBtn});
		                table.setRowHeight(i, rowHeight);
		            }
		        }
		        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
		        table.getTableHeader().setSize(table.getSize().width, 20);
		        table.getTableHeader().setBackground(Color.WHITE);
		        
		        /*
		        JPanel auxPanel = new JPanel();
		        auxPanel.setLayout(new GridLayout());
		        auxPanel.setPreferredSize(new Dimension(songsPanel.getSize().width-50, songsPanel.getSize().height-50));
		        auxPanel.add(table);
		        */
		        
		        allSongListPanel.setLayout(new GridLayout());
		        
		        JScrollPane sp = new JScrollPane(table);
		        //sp.getViewport().setPreferredSize(auxPanel.getPreferredSize());
		        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		        
		        allSongListPanel.add(sp);
		        allSongListPanel.revalidate();
		        allSongListPanel.repaint();
		        mainPanel.setBackground(Color.WHITE);
		        mainPanel.revalidate();
		        mainPanel.repaint();
			}
		});
		
		return btn;
	}
	
	private JButton buildAddSongToAlbumBtn(final Song song, String btnText) {
		final JButton btn = new JButton(btnText);
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if(btn.getText().equals("-")) {
					songsToAddToAlbum.remove(song);
					btn.setText("+");
				} else {
					songsToAddToAlbum.add(song);
					btn.removeAll();
					btn.setText("-");
				}
				allSongListPanel.revalidate();
				allSongListPanel.repaint();
				repaintUpperAlbumSongsSubPanel();
			}
		});
		
		return btn;
	}
	
	private void repaintUpperAlbumSongsSubPanel()
	{
		upperAlbumSongsSubPanel.removeAll();

		DefaultTableModel model = SwingUtils.getDefaultTableModel();
        model.setColumnIdentifiers(new Object[] { "Пісні  в Альбомі", "%", ""});
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(10*(upperAlbumSongsSubPanel.getSize().width-80)/12);
        table.getColumnModel().getColumn(1).setPreferredWidth(1*(upperAlbumSongsSubPanel.getSize().width-80)/12);
        table.getColumnModel().getColumn(2).setPreferredWidth(1*(upperAlbumSongsSubPanel.getSize().width-80)/12);
        
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(buttonRenderer);
        if(songsToAddToAlbum.size() == 0) {
        	model.insertRow(0, new Object[]{"Ще немає пісень в альбомі" , null, null });
        } else {
        	for(int i = 0; i < songsToAddToAlbum.size(); ++i ) {
	        	rowHeight = upperAlbumSongsSubPanel.getSize().height/11;
	            model.insertRow(i, new Object[]{ songsToAddToAlbum.get(i).getTitle() , 
	            		rozpodilDao.getRozpodilForSong(songsToAddToAlbum.get(i).getId()), 
	            		buildChangeRozpodilButton(songsToAddToAlbum.get(i).getId(), songsToAddToAlbum.get(i).getTitle())});
	            table.setRowHeight(i, rowHeight);
        	}
        }
        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);

        upperAlbumSongsSubPanel.setLayout(new GridLayout());
        JScrollPane sp = new JScrollPane(table);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        upperAlbumSongsSubPanel.add(sp);
        upperAlbumSongsSubPanel.revalidate();
        upperAlbumSongsSubPanel.repaint();
	}
	
	private JButton buildChangeRozpodilButton(final int songId, final String songTitle) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/details.png","Edit Rozpodil");
		JButton editBtn = new JButton(editIcon);
		editBtn.setSize(editIcon.getIconWidth(), editIcon.getIconHeight()+10);
		editBtn.setBackground(Color.WHITE);
		editBtn.setBorderPainted(false);
		editBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				repaintLowerAlbumSongsSubPanel(songId, songTitle);
			}	
		});
		return editBtn;
	}
	
	private void repaintLowerAlbumSongsSubPanel(int songId, String songTitle) {
		lowerAlbumSongsSubPanel.removeAll();
		
		DefaultTableModel model = SwingUtils.getDefaultTableModel();
        model.setColumnIdentifiers(new Object[] { songTitle, "%", "", ""});
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(9*(albumSongsPanel.getSize().width*2-80)/12);
        table.getColumnModel().getColumn(1).setPreferredWidth(1*(albumSongsPanel.getSize().width*2-80)/12);
        table.getColumnModel().getColumn(2).setPreferredWidth(1*(albumSongsPanel.getSize().width*2-80)/12);
        table.getColumnModel().getColumn(3).setPreferredWidth(1*(albumSongsPanel.getSize().width*2-80)/12);
        
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(buttonRenderer);
        List<Rozpodil> rozp = rozpodilDao.getRozpodilsBySongIdIncludingMusician(songId);
        List<String> musicianNames = musicianService.getMuscianNames();
        if(rozp.size() == 0) {
        	model.insertRow(0, new Object[]{"Ще немає виконавців" , null, null, null });
        } else {
        	for(int i = 0; i < rozp.size(); ++i ) {
	        	rowHeight = albumSongsPanel.getSize().height/22;
	            model.insertRow(i, new Object[]{ rozp.get(i).getMusician().getName(), 
	            		rozp.get(i).getChastka(), 
	            		buildEditRozpodilBtn(rozp.get(i).getSongId(), rozp.get(i).getMusicianId(), songTitle),
	            		buildDeleteRozpodilBtn(rozp.get(i).getSongId(), rozp.get(i).getMusicianId(), songTitle)});
	            table.setRowHeight(i, rowHeight);
	            musicianNames.remove(rozp.get(i).getMusician().getName());
        	}
        }
        
        table.addMouseListener(new JTableButtonMouseListener(table,rowHeight));
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);
        
        lowerAlbumSongsSubPanel.setLayout(new BorderLayout() );
        JScrollPane sp = new JScrollPane(table);
        sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        lowerAlbumSongsSubPanel.add(sp, BorderLayout.CENTER);
        
        JPanel musicianPanel = new JPanel();
        musicianPanel.setLayout(new GridLayout(2,1));
        
        JComboBox musicianList = new JComboBox(musicianNames.toArray());
        musicianPanel.add(musicianList);
        musicianPanel.add(buildAddMusicianBtn(songId, songTitle, musicianList));
        
        musicianPanel.setBorder(new EmptyBorder(10, 0, 10, 0 ));
        
        lowerAlbumSongsSubPanel.add(musicianPanel, BorderLayout.NORTH);
        lowerAlbumSongsSubPanel.revalidate();
        lowerAlbumSongsSubPanel.repaint();
	}
	
	private JButton buildDeleteRozpodilBtn(final int songId, final int musicianId, final String songTitle) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete-small.png","Delete");
		JButton btn = new JButton(deleteIcon);
		btn.setBackground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				rozpodilDao.delete(musicianId, songId);
				repaintLowerAlbumSongsSubPanel(songId, songTitle);
				repaintUpperAlbumSongsSubPanel();
			}
		});
		
		return btn;
	}
	
	private JButton buildEditRozpodilBtn(final int songId, final int musicianId, final String songTitle) {
		Icon icon = SwingUtils.createImageIcon("/icons/edit-small.jpg","Edit");
		JButton btn = new JButton(icon);
		btn.setBackground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				String input = JOptionPane.showInputDialog ( "Нова частка від винагородження:" ); 
				double chastka = Double.valueOf(input);
				Rozpodil r = new Rozpodil();
				r.setSongId(songId);
				r.setMusicianId(musicianId);
				r.setChastka(chastka);
				rozpodilDao.update(r);
				
				repaintLowerAlbumSongsSubPanel(songId, songTitle); 
				repaintUpperAlbumSongsSubPanel();
			}
		});
		
		return btn;
	}

	private JButton buildAddMusicianBtn(final int songId, final String songTitle, final JComboBox musicianList) {
		JButton btn = new JButton("Додати виконавця");
		
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String name = (String)musicianList.getSelectedItem();
				int musicianId = musicianService.getByName(name).getId();
				Rozpodil r = new Rozpodil();
				r.setSongId(songId);
				r.setMusicianId(musicianId);
				rozpodilDao.insert(r);
				repaintUpperAlbumSongsSubPanel();
				repaintLowerAlbumSongsSubPanel(songId, songTitle);
			}
		});
		
		return btn;
	}

	private JPanel buildSearchPanel(Dimension preferredSize) {
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
