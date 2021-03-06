package dreamteamapp;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import dao.AlbumDao;
import dao.AlbumDaoImpl;

@Configuration
@ComponentScan(basePackages= {"eventprocessors","dreamteamapp","dao","services"})
public class AppConfig {
	
	@Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://194.44.143.138:3306/projman9");
        dataSource.setUsername("projman9");
        dataSource.setPassword("!projman_9");
        //dataSource.setUrl("jdbc:mysql://localhost:3306/projman9");
        //dataSource.setUsername("root");
        //dataSource.setPassword("12345");
        return dataSource;
    }
 
    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

}
