package dreamteamapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import eventprocessors.*;  

@Component("appStarter")
public class AppStarter {
	
	@Autowired
	private AlbumEventProcessorImpl albumEventProcessor;
	
	@Autowired
	private MusicianEventProcessor muscianEventProcessor;
	
	public AppStarter(){  
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame();
		frame.setSize(screenSize.width, screenSize.height);
		frame.setBackground(Color.ORANGE);
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		final JPanel contentPanel = new JPanel();
		contentPanel.setBackground(Color.GREEN);
		contentPanel.setPreferredSize(new Dimension(3*frame.getSize().width/4, frame.getSize().height));
		
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(Color.MAGENTA);
		menuPanel.setPreferredSize(new Dimension(frame.getSize().width/4, frame.getSize().height));
		
		JButton b=new JButton("Click"); 
		b.setBounds(130,100,200, 40);  
		b.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				contentPanel.add(albumEventProcessor.process());
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		});
		
		JButton musiciansBtn =new JButton("Muscians"); 
		//musiciansBtn.setBounds(130,100,200, 40);  
		musiciansBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				JPanel musicianPanel = muscianEventProcessor.process(contentPanel.getSize());
				musicianPanel.setPreferredSize(contentPanel.getSize());
				contentPanel.add(musicianPanel);
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		}); 
		
		menuPanel.add(b);
		menuPanel.add(musiciansBtn);

		
		container.add(menuPanel, BorderLayout.WEST);
		container.add(contentPanel, BorderLayout.EAST);
		container.setVisible(true);
		
		frame.add(container);
		frame.setExtendedState(JFrame.EXIT_ON_CLOSE);
		//frame.pack();
		//frame.setUndecorated(true); 
		frame.setVisible(true);  
	}
}
