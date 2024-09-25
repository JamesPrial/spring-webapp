package jpja.webapp.daos.datasourceconfig;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {
    
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSourceProperties getDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean
    public DataSource getDataSource(){
        return getDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedJdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
