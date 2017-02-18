package dreamteamapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import eventprocessors.*;

@Component("appStarter")
public class AppStarter {

	@Autowired
	private EventProcessor albumEventProcessor;

	public AppStarter() {

		JFrame frame = new JFrame("Studio");
		frame.setSize(new Dimension(1000, 500));
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
				contentPanel.add(albumEventProcessor.process());
				contentPanel.revalidate();
				contentPanel.repaint();
			}

		});

		menuPanel.add(albums);
		menuPanel.add(musicians);
		menuPanel.add(songs);
		menuPanel.add(instruments);
		menuPanel.add(financial);

		container.add(menuPanel, BorderLayout.WEST);
		container.add(contentPanel, BorderLayout.CENTER);
		container.setVisible(true);

		frame.setContentPane(container);
		frame.setVisible(true);
	}

	public class MenuButton extends JButton {

		private static final int WIDTH = 170;
		private static final int HEIGHT = 40;

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
	}
}
