package dreamteamapp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import eventprocessors.*;  

@Component("appStarter")
public class AppStarter {
	
	@Autowired
	private EventProcessor albumEventProcessor;  
	
	public AppStarter(){  
		
		JFrame frame = new JFrame();
		
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

		final JPanel contentPanel = new JPanel();
		
		JPanel menuPanel = new JPanel();
		JButton b=new JButton("Click"); 
		b.setBounds(130,100,100, 40);  
		b.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				contentPanel.removeAll();
				contentPanel.add(albumEventProcessor.process());
				contentPanel.revalidate();
				contentPanel.repaint();
			}
		}); 
		menuPanel.add(b);

		
		container.add(menuPanel);
		container.add(contentPanel);
		container.setVisible(true);
		
		frame.add(container);
		frame.setSize(400,500); 
		frame.setVisible(true);  
	}
}
