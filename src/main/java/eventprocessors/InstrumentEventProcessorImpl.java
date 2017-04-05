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
import models.Instrument;
import services.InstrumentService;

@Component
public class InstrumentEventProcessorImpl implements InstrumentEventProcessor {

    @Autowired
    private InstrumentService instrumentService;
    private JPanel mainPanel;
    private String currentFilterQuery;
    private int currentPage;
    private int instrumentsPerPage = 10;
    private Dimension instrumentListPanelPreferredSize;
    private Dimension paginationBarPanelPreferredSize;
    private Dimension searchAndCreatePanelPreferredSize;
    private int rowHeight;

    public JPanel process(Dimension sizeOfParentElement) {
        currentPage = 1;

        mainPanel = new JPanel();
        mainPanel.setPreferredSize(sizeOfParentElement);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BorderLayout());

        searchAndCreatePanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height / 7);
        JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
        instrumentListPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, (int) (4.5 * mainPanel.getPreferredSize().height) / 7);
        JPanel instrumentListPanel = buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, 0, null);
        paginationBarPanelPreferredSize = new Dimension(mainPanel.getPreferredSize().width, mainPanel.getPreferredSize().height / 7);
        JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);

        mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
        mainPanel.add(instrumentListPanel, BorderLayout.CENTER);
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

        int count = instrumentService.getNumOfInstruments(currentFilterQuery);

        if (count > instrumentsPerPage) {
            int i = 0;
            int numOfPages = count / instrumentsPerPage;
            if (count % instrumentsPerPage != 0)
                ++numOfPages;
            while (i < numOfPages) {
                final int index = i;
                final JButton pageBtn = new JButton("" + (++i));
                pageBtn.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        currentPage = index + 1;
                        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
                        mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        mainPanel.add(buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, (currentPage - 1) * instrumentsPerPage, currentFilterQuery), BorderLayout.CENTER);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });
                paginationPanel.add(pageBtn);
            }
        }

        return paginationPanel;
    }

    private JPanel buildInstrumentListPanel(Dimension panelDimension, int limit, int offset, String filterQuery) {
        JPanel instrumentListPanel = new JPanel();
        instrumentListPanel.setPreferredSize(panelDimension);
        instrumentListPanel.setBackground(Color.WHITE);

        java.util.List<Instrument> instruments = instrumentService.get(offset, offset + limit, filterQuery);

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

        model.setColumnIdentifiers(new Object[]{"#", "Ім'я", "", ""});
        JTable table = new JTable(model);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(instrumentListPanel.getPreferredSize().width / 10);
        table.getColumnModel().getColumn(1).setPreferredWidth(7 * instrumentListPanel.getPreferredSize().width / 10);
        table.getColumnModel().getColumn(2).setPreferredWidth(instrumentListPanel.getPreferredSize().width / 10);
        table.getColumnModel().getColumn(3).setPreferredWidth(instrumentListPanel.getPreferredSize().width / 10);

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(buttonRenderer);
        if (instruments.size() == 0) {
            model.insertRow(0, new Object[]{"", "Нічого не знайдено", null, null});
        } else {
            rowHeight = instrumentListPanel.getPreferredSize().height / 12;
            for (int i = 0; i < instruments.size(); ++i) {
                JButton editBtn = buildEditInstrumentButton(instruments.get(i));
                JButton deleteBtn = buildDeleteInstrumentButton(instruments.get(i));
                model.insertRow(i, new Object[]{"" + ((currentPage - 1) * instrumentsPerPage + i + 1), instruments.get(i).getName(), editBtn, deleteBtn});
                table.setRowHeight(i, rowHeight);
            }
        }
        table.addMouseListener(new JTableButtonMouseListener(table, rowHeight));
        table.getTableHeader().setSize(table.getSize().width, 20);
        table.getTableHeader().setBackground(Color.WHITE);
        instrumentListPanel.add(table.getTableHeader());
        instrumentListPanel.add(table);

        return instrumentListPanel;
    }

    private JButton buildDeleteInstrumentButton(final Instrument instrument) {
        Icon deleteIcon = SwingUtils.createImageIcon("/icons/delete.png", "Видалити інструмент");
        JButton deleteBtn = new JButton(deleteIcon);
        deleteBtn.setBackground(Color.WHITE);
        deleteBtn.setBorderPainted(false);
        deleteBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int dialogResult = JOptionPane.showConfirmDialog(null, "Видалити інструмент '" + instrument.getName() + "' ?");
                if (dialogResult == JOptionPane.YES_OPTION) {
                    instrumentService.delete(instrument.getId());
                    mainPanel.removeAll();
                    JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
                    JPanel instrumentListPanel = buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, 0, null);
                    JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);

                    mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
                    mainPanel.add(instrumentListPanel, BorderLayout.CENTER);
                    mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        });
        return deleteBtn;
    }

    private JButton buildEditInstrumentButton(final Instrument instrument) {
        Icon editIcon = SwingUtils.createImageIcon("/icons/edit.png", "Редагувати інструмент");
        JButton editBtn = new JButton(editIcon);
        editBtn.setBackground(Color.WHITE);
        editBtn.setBorderPainted(false);
        editBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BorderLayout layout = (BorderLayout) mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));

                JPanel editInstrumentFormPanel = new JPanel();
                editInstrumentFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
                editInstrumentFormPanel.setSize(searchAndCreatePanelPreferredSize);
                editInstrumentFormPanel.setLayout(new GridLayout(3, 2));

                JLabel nameLabel = new JLabel("Ім'я");
                final JTextField nameInput = new JTextField(40);
                nameInput.setText(instrument.getName());
                JButton cancelBtn = new JButton("Відміна");
                JButton saveBtn = new JButton("Зберегти зміни");

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
                        String name = nameInput.getText().trim();

                        if (name == null || name.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "'Ім'я' - обов'зякове поле!");
                        } else {
                            instrument.setName(name);
                            try {
                                instrumentService.update(instrument);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Неможливо зберегти зміни до бази данних.");
                            }

                            mainPanel.removeAll();
                            JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
                            JPanel instrumentListPanel = buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, 0, null);
                            JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);

                            mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
                            mainPanel.add(instrumentListPanel, BorderLayout.CENTER);
                            mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

                            mainPanel.revalidate();
                            mainPanel.repaint();
                        }
                    }
                });

                editInstrumentFormPanel.add(nameLabel);
                editInstrumentFormPanel.add(nameInput);
                editInstrumentFormPanel.add(cancelBtn);
                editInstrumentFormPanel.add(saveBtn);

                mainPanel.add(editInstrumentFormPanel, BorderLayout.NORTH);

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

        JPanel searchPanel = buildSearchPanel(new Dimension(3 * searchAndCreatePanel.getPreferredSize().width / 4, searchAndCreatePanel.getPreferredSize().height));
        JPanel createInstrumentPanel = buildCreateInstrumentPanel(new Dimension(searchAndCreatePanel.getPreferredSize().width / 4, searchAndCreatePanel.getPreferredSize().height));

        searchAndCreatePanel.add(searchPanel, BorderLayout.WEST);
        searchAndCreatePanel.add(createInstrumentPanel, BorderLayout.EAST);
        return searchAndCreatePanel;
    }

    private JPanel buildCreateInstrumentPanel(Dimension preferredSize) {
        JPanel createInstrumentPanel = new JPanel();
        createInstrumentPanel.setBackground(Color.WHITE);
        createInstrumentPanel.setPreferredSize(preferredSize);

        Icon addIcon = SwingUtils.createImageIcon("/icons/add.png", "Додати інструмент");

        JButton addInstrumentBtn = new JButton("Додати інструмент", addIcon);
        addInstrumentBtn.setBackground(Color.WHITE);
        addInstrumentBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BorderLayout layout = (BorderLayout) mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));

                JPanel createInstrumentFormPanel = new JPanel();
                createInstrumentFormPanel.setBorder(new EmptyBorder(10, 10, 30, 10));
                createInstrumentFormPanel.setSize(searchAndCreatePanelPreferredSize);
                createInstrumentFormPanel.setLayout(new GridLayout(3, 2));

                JLabel nameLabel = new JLabel("Ім'я");
                final JTextField nameInput = new JTextField(40);
                JButton cancelBtn = new JButton("Відмінити");
                JButton saveBtn = new JButton("Зберегти");

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
                        String name = nameInput.getText().trim();

                        if (name == null || name.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "'Ім'я' - обов'язкове поле!");
                        } else {
                            Instrument m = new Instrument();
                            m.setName(name);
                            try {
                                instrumentService.add(m);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "Неможливо зберегти до бази данних.");
                            }

                            mainPanel.removeAll();
                            JPanel searchAndCreatePanel = buildSearchAndCreatePanel(searchAndCreatePanelPreferredSize);
                            JPanel instrumentListPanel = buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, 0, null);
                            JPanel paginationBarPanel = buildPaginationBarPanel(paginationBarPanelPreferredSize);

                            mainPanel.add(searchAndCreatePanel, BorderLayout.NORTH);
                            mainPanel.add(instrumentListPanel, BorderLayout.CENTER);
                            mainPanel.add(paginationBarPanel, BorderLayout.SOUTH);

                            mainPanel.revalidate();
                            mainPanel.repaint();
                        }
                    }
                });

                createInstrumentFormPanel.add(nameLabel);
                createInstrumentFormPanel.add(nameInput);
                createInstrumentFormPanel.add(cancelBtn);
                createInstrumentFormPanel.add(saveBtn);

                mainPanel.add(createInstrumentFormPanel, BorderLayout.NORTH);

                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        createInstrumentPanel.add(addInstrumentBtn);
        return createInstrumentPanel;
    }

    private JPanel buildSearchPanel(Dimension preferredSize) {
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setPreferredSize(preferredSize);

        final JTextField filterQueryInput = SwingUtils.createTextFIeldWithPlaceholder(40, "Пошук...");

        JButton searchBtn = new JButton("Пошук");
        searchBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                currentFilterQuery = filterQueryInput.getText();
                currentPage = 1;
                BorderLayout layout = (BorderLayout) mainPanel.getLayout();
                mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                mainPanel.add(buildInstrumentListPanel(instrumentListPanelPreferredSize, instrumentsPerPage, (currentPage - 1) * instrumentsPerPage, currentFilterQuery), BorderLayout.CENTER);
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
