package eventprocessors;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import org.springframework.beans.factory.annotation.Autowired;

import dao.RozpodilDao;
import eventprocessorhelpers.SwingUtils;
import models.Rozpodil;

public  class ButtonsBuilder {
	
	@Autowired
	private RozpodilDao rozpodilDao;

	public JButton saveRozpodilBtn(final int musicianId, final int songId, final double chastka)
	{
		JButton btn = new JButton("Додати виконаця");
		btn.setBackground(Color.WHITE);
		btn.setBorderPainted(false);
		btn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Rozpodil r = new Rozpodil();
				r.setChastka(chastka);
				r.setSongId(songId);
				r.setMusicianId(musicianId);
				rozpodilDao.insert(r);
			}
		});
		
		return btn;
	}
}
