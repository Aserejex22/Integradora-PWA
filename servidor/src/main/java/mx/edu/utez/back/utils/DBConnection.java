package mx.edu.utez.back.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DBConnection {
    @Value("${db.url}")
    private String URL;

    @Value("${db.user}")
    private String USER;

    @Value("${db.password}")
    private String PASSWORD;

    @Bean
    public DataSource getConnection() {
        DriverManagerDataSource s = new DriverManagerDataSource();
        s.setDriverClassName("com.mysql.cj.jdbc.Driver");
        s.setUrl(URL);
        s.setUsername(USER);
        s.setPassword(PASSWORD);

        return s;
    }
}
