package com.lazyants.filecessor.configuration;

import com.mongodb.MongoClientURI;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
@Data
@ConfigurationProperties(prefix="mongo")
public class MongoConfiguration {

    private String host;

    private String port = "27017";

    private String database;

    private String authenticationDatabase;

    private String username;

    private String passwd;

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(String.format(
                "mongodb://%s:%s@%s:%s/%s?authSource=%s", username, passwd, host, port, database, authenticationDatabase)));
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        MappingMongoConverter converter =
                new MappingMongoConverter(mongoDbFactory(), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return new MongoTemplate(mongoDbFactory(), converter);
    }
}
