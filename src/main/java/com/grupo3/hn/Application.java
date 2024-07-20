package com.grupo3.hn;

import com.grupo3.hn.data.DatosRepository;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation to make the application installable on phones, tablets,
 * and some desktop browsers.
 */
@SpringBootApplication
@Theme(value = "sistema-empleados", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
            SqlInitializationProperties properties) {
        // This bean ensures the database is only initialized when empty
        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties);
    }

    @Bean
    public boolean initializeDatabase(SqlDataSourceScriptDatabaseInitializer initializer, DatosRepository repository) {
        if (repository.count() == 0L) {
            initializer.initializeDatabase();
            return true;
        }
        return false;
    }
}

