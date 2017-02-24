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
import javax.swing.table.TableColumnModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eventprocessorhelpers.JTableButtonMouseListener;
import eventprocessorhelpers.JTableButtonRenderer;
import eventprocessorhelpers.SwingUtils;
import models.Song;
import services.SongService;

@Component
public class SongEventProcessorImpl implements SongEventProcessor {

	@Autowired
	private SongService songService;

	private JPanel mainPanel;
	private String currentFilterQuery;
	final int songsPerPage = 10;
	int currentPage = 1;
	int rowHeight;
	private Dimension listPanelPreferredSize;
	private Dimension paginationPanelPreferredSize;
	private Dimension searchAndCreatePanelPreferredSize;

	public JPanel process(Dimension sizeOfParentElement) {
		System.out.println("in SongEventProcessor");
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(sizeOfParentElement);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(new BorderLayout());

		searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,
				mainPanel.getPreferredSize().height / 7);
		listPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,
				(int) (4 * mainPanel.getPreferredSize().height) / 7);
		paginationPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width,
				mainPanel.getPreferredSize().height / 7);

		JPanel searchPanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
		JPanel tablePanel = buildSongList(listPanelPreferredSize, songsPerPage, 0, "");
		JPanel paginationPanel = buildPaginationPanel(paginationPanelPreferredSize);

		mainPanel.add(searchPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(paginationPanel, BorderLayout.SOUTH);

		return mainPanel;
	}

	private JPanel buildSearchAndCreatePanel(Dimension dimension) {
		JPanel res = new JPanel();

		res.setBackground(Color.WHITE);
		res.setPreferredSize(dimension);
		res.setLayout(new BorderLayout());

		JPanel searchPanel = buildSearchPanel(
				new Dimension(3 * res.getPreferredSize().width / 4, res.getPreferredSize().height));
		JPanel createPanel = buildCreatePanel(
				new Dimension(res.getPreferredSize().width / 4, res.getPreferredSize().height));

		res.add(searchPanel, BorderLayout.WEST);
		res.add(createPanel, BorderLayout.EAST);
		return res;
	}

	private JPanel buildCreatePanel(Dimension preferredSize) {
		JPanel res = new JPanel();
		res.setBackground(Color.WHITE);
		res.setPreferredSize(preferredSize);

		Icon addIcon = SwingUtils.createImageIcon("/icons/add.png", "Add");

		JButton addBtn = new JButton("Add New Song", addIcon);
		addBtn.setBackground(Color.WHITE);
		addBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));

				JPanel form = new JPanel();
				form.setBorder(new EmptyBorder(10, 10, 30, 10));
				form.setSize(searchAndCreatePanelPreferredSize);
				form.setLayout(new GridLayout(3, 2));

				JLabel titleL = new JLabel("Title");
				JLabel authorL = new JLabel("Author");

				final JTextField titleInput = new JTextField(40);
				final JTextField authorInput = new JTextField(40);

				JButton cancelBtn = new JButton("Cancel");
				JButton saveBtn = new JButton("Save");

				cancelBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
						mainPanel.add(buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize), BorderLayout.NORTH);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});

				saveBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						String title = titleInput.getText().trim();
						String author = authorInput.getText().trim();

						if (title == null || title.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Title' is required");
						} else if (author == null || author.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Author' is required");
						} else {
							Song s = new Song();
							s.setTitle(title);
							s.setAuthor(author);
							try {
								songService.add(s);
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to save the song");
							}

							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel songListPanel = buildSongList(listPanelPreferredSize, songsPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);

							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(songListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

							mainPanel.revalidate();
							mainPanel.repaint();
						}
					}
				});

				form.add(titleL);
				form.add(titleInput);
				form.add(authorL);
				form.add(authorInput);
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
				mainPanel.add(buildSongList(listPanelPreferredSize, songsPerPage, (currentPage - 1) * songsPerPage,
						currentFilterQuery), BorderLayout.CENTER);
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

	private JPanel buildSongList(Dimension preferredSize, int limit, int start, String query) {
		JPanel res = new JPanel(new BorderLayout());
		res.setBackground(Color.BLUE);
		res.setPreferredSize(preferredSize);
		rowHeight = mainPanel.getPreferredSize().height / 20;

		List<Song> songs = songService.get(start, start + limit, query);
		JTable recordTable = buildRecordTable(songs, preferredSize);

		res.add(recordTable.getTableHeader(), BorderLayout.NORTH);
		res.add(recordTable, BorderLayout.CENTER);

		return res;
	}

	private JTable buildRecordTable(List<Song> songs, Dimension preferredSize) {
		int numSongs = songs.size();

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

		model.setColumnIdentifiers(new Object[] { "#", "Title", "Author", "Album", "", "" });
		JTable table = new JTable(model);
		TableColumnModel columnModel = table.getColumnModel();

		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		columnModel.getColumn(4).setCellRenderer(buttonRenderer);
		columnModel.getColumn(5).setCellRenderer(buttonRenderer);

		columnModel.getColumn(0).setPreferredWidth(preferredSize.width / 9);
		columnModel.getColumn(1).setPreferredWidth(2 * preferredSize.width / 9);
		columnModel.getColumn(2).setPreferredWidth(2 * preferredSize.width / 9);
		columnModel.getColumn(3).setPreferredWidth(2 * preferredSize.width / 9);
		columnModel.getColumn(4).setPreferredWidth(preferredSize.width / 9);
		columnModel.getColumn(5).setPreferredWidth(preferredSize.width / 9);

		table.setRowHeight(rowHeight);

		for (int i = 0; i < numSongs; i++) {
			Song s = songs.get(i);
			Object[] data = new Object[8];
			data[0] = Integer.toString((currentPage - 1) * songsPerPage + i + 1);
			data[1] = s.getTitle();
			data[2] = s.getAuthor();
			if (s.getAlbum() != null){
				data[3] = s.getAlbum().getTitle();
			} else {
				data[3] = " - ";
			}
			
			data[4] = buildEditButton(s);
			data[5] = buildDeleteButton(s);
			model.insertRow(i, data);
		}
		table.getTableHeader().setBackground(Color.WHITE);
		table.setBackground(Color.WHITE);
		table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
		return table;
	}

	private JButton buildEditButton(final Song s) {
		Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png", "Edit");
		JButton b = new JButton(editIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				BorderLayout layout = (BorderLayout) mainPanel.getLayout();
				mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));

				JPanel editFormPanel = new JPanel();
				editFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
				editFormPanel.setSize(searchAndCreatePanelPreferredSize);
				editFormPanel.setLayout(new GridLayout(3, 2));

				JLabel titleL = new JLabel("Title");
				JLabel authorL = new JLabel("Author");

				final JTextField titleInput = new JTextField(40);
				titleInput.setText(s.getTitle());
				final JTextField authorInput = new JTextField(40);
				authorInput.setText(s.getAuthor());

				JButton cancelBtn = new JButton("Cancel");
				JButton saveBtn = new JButton("Save");

				cancelBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
						mainPanel.add(buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize), BorderLayout.NORTH);
						mainPanel.revalidate();
						mainPanel.repaint();
					}
				});

				saveBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						String title = titleInput.getText().trim();
						String author = authorInput.getText().trim();

						if (title == null || title.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Title' is required");
						} else if (author == null || author.isEmpty()) {
							JOptionPane.showMessageDialog(null, "Field 'Author' is required");
						} else {
							s.setTitle(title);
							s.setAuthor(author);
							try {
								songService.update(s);
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(null, "Failed to update record");
							}

							mainPanel.removeAll();
							JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
							JPanel songListPanel = buildSongList(listPanelPreferredSize, songsPerPage, 0, null);
							JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);

							mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
							mainPanel.add(songListPanel, BorderLayout.CENTER);
							mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

							mainPanel.revalidate();
							mainPanel.repaint();
						}
					}
				});

				editFormPanel.add(titleL);
				editFormPanel.add(titleInput);
				editFormPanel.add(authorL);
				editFormPanel.add(authorInput);
				editFormPanel.add(cancelBtn);
				editFormPanel.add(saveBtn);

				mainPanel.add(editFormPanel, BorderLayout.NORTH);
				mainPanel.revalidate();
				mainPanel.repaint();
			}
		});
		return b;
	}

	private JButton buildDeleteButton(final Song s) {
		Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png", "Delete");
		JButton b = new JButton(deleteIcon);
		b.setBackground(Color.WHITE);
		b.setBorderPainted(false);

		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this song?");
				if (dialogResult == JOptionPane.YES_OPTION) {
					songService.delete(s.getId());

					mainPanel.removeAll();
					currentPage = 1;
					JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
					JPanel songListPanel = buildSongList(listPanelPreferredSize, songsPerPage, 0, null);
					JPanel paginationBarPanel = buildPaginationPanel(paginationPanelPreferredSize);

					mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
					mainPanel.add(songListPanel, BorderLayout.CENTER);
					mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}

		});

		return b;
	}

	private JPanel buildPaginationPanel(final Dimension preferredSize) {
		int n = songService.getNumOfSongs(currentFilterQuery);
		JPanel paginationPanel = new JPanel();
		paginationPanel.setBackground(Color.WHITE);
		paginationPanel.setLayout(new FlowLayout());
		paginationPanel.setBorder(new EmptyBorder(50, 0, 50, 0));

		if (n > songsPerPage) {
			int i = 0;
			int numOfPages = n / songsPerPage + (n % songsPerPage == 0 ? 0 : 1);
			while (i < numOfPages) {
				final int index = i;
				final JButton pageBtn = new JButton("" + (++i));
				pageBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						currentPage = index + 1;
						BorderLayout layout = (BorderLayout) mainPanel.getLayout();
						mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
						mainPanel.add(buildSongList(preferredSize, songsPerPage, (currentPage - 1) * songsPerPage,
								currentFilterQuery), BorderLayout.CENTER);
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
