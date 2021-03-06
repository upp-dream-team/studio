package dreamteamapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import eventprocessors.*;

@Component("appStarter")
public class AppStarter {

	@Autowired
	private AlbumEventProcessorImpl albumEventProcessor;
	
	@Autowired
	private MusicianEventProcessor muscianEventProcessor;
	
	@Autowired
	private SongEventProcessor songEventProcessor;
	
	@Autowired
	private FinancialAffairsEventProcessor financialAffairsEventProcessor;

	@Autowired
	private InstrumentEventProcessor instrumentEventProcessor;

	public AppStarter() {

		JFrame frame = new JFrame("Studio");
		//frame.setResizable(false);
		//frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setSize(new Dimension(1200, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel container = new JPanel();
		// container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.setLayout(new BorderLayout());

		final JPanel contentPanel = new JPanel();

		final int margin = 20;
		EmptyBorder eb = new EmptyBorder(margin, margin, margin, margin);
		JPanel menuPanel = new JPanel();
		menuPanel.setBorder(eb);
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

		final float alignmentX = JComponent.CENTER_ALIGNMENT;

		MenuButton albums = new MenuButton("Albums", alignmentX);
		MenuButton musicians = new MenuButton("Musicians", alignmentX);
		MenuButton songs = new MenuButton("Songs", alignmentX);
		MenuButton instruments = new MenuButton("Instruments", alignmentX);
		MenuButton financial = new MenuButton("Financial Affairs", alignmentX);

		albums.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				contentPanel.add(albumEventProcessor.process(contentPanel.getSize()));
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});
		
		musicians.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				JPanel musicianPanel = muscianEventProcessor.process(contentPanel.getSize());
				musicianPanel.setPreferredSize(contentPanel.getSize());
				contentPanel.add(musicianPanel);
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});
		
		
		songs.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				JPanel songsPanel = songEventProcessor.process(contentPanel.getSize());
				songsPanel.setPreferredSize(contentPanel.getSize());
				contentPanel.add(songsPanel);
				contentPanel.revalidate();
				contentPanel.repaint();
			}
			
		});
		
		financial.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				JPanel faPanel = financialAffairsEventProcessor.process(contentPanel.getSize());
				faPanel.setPreferredSize(contentPanel.getSize());
				contentPanel.add(faPanel);
				contentPanel.revalidate();
				contentPanel.repaint();
			}
			
		});

		instruments.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				JPanel faPanel = instrumentEventProcessor.process(contentPanel.getSize());
				faPanel.setPreferredSize(contentPanel.getSize());
				contentPanel.add(faPanel);
				contentPanel.revalidate();
				contentPanel.repaint();
			}

		});
		
		container.add(menuPanel, BorderLayout.WEST);
		container.add(contentPanel, BorderLayout.EAST);
		container.setVisible(true);


		menuPanel.add(albums);
		menuPanel.add(musicians);
		menuPanel.add(songs);
		menuPanel.add(instruments);
		menuPanel.add(financial);

		JPanel contentPanelWrapper = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		contentPanelWrapper.setLayout(new BorderLayout());
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBackground(new Color(100, 100, 100));
		separator.setForeground(contentPanelWrapper.getBackground());
		contentPanelWrapper.add(separator, BorderLayout.WEST);
		contentPanelWrapper.add(contentPanel, BorderLayout.CENTER);

		container.add(menuPanel, BorderLayout.WEST);
		container.add(contentPanelWrapper, BorderLayout.CENTER);
		container.setVisible(true);

		frame.setContentPane(container);
		frame.setVisible(true);
	}


	public class MenuButton extends JButton {

		private static final int WIDTH = 200;
		private static final int HEIGHT = 40;

		private Color hoverBackgroundColor = Color.white;
		private Color pressedBackgroundColor = Color.green;

		public MenuButton(String title, float alignmentX) {
			super(title);
			Dimension d = new Dimension(WIDTH, HEIGHT);
			setMinimumSize(d);
			setMaximumSize(d);
			setPreferredSize(d);
			// setBorderPainted(false);
			setFocusPainted(false);
			setContentAreaFilled(false);
			setAlignmentX(alignmentX);
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (getModel().isRollover()) {
				g.setColor(Color.lightGray);
				setBorder(new LineBorder(new Color(100, 100, 100)));
			} else {
				g.setColor(getBackground());
				setBorder(new LineBorder(new Color(130, 130, 130)));
			}
			g.fillRect(0, 0, getWidth(), getHeight());
			super.paintComponent(g);
		}

	}
}
