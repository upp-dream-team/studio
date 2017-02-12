package dreamteamapp;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import dao.AlbumDao;

public class AppMain {

	public static void main(String[] args){
		
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		
		AppStarter appStarter = (AppStarter) context.getBean("appStarter");
	}
}
