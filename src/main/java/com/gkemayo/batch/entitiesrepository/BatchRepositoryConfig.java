package com.gkemayo.batch.entitiesrepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories
@RequiredArgsConstructor
public class BatchRepositoryConfig extends AbstractJdbcConfiguration {

	private final DataSource dataSource;
	
	@Bean
    NamedParameterJdbcOperations operations() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

//    @Bean
//    PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dataSource);
//	}

//    @Bean
//    DataSource dataSource(){
//        return new EmbeddedDatabaseBuilder()
//                .generateUniqueName(true)
//                .setType(EmbeddedDatabaseType.HSQL)
//                .addScript("create-customer-schema.sql")
//                .build();
//    }
    
}
