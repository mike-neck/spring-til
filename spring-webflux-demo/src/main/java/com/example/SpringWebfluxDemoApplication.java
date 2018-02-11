package com.example;

import com.example.user.User;
import com.example.user.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = { CassandraDataAutoConfiguration.class })
@EnableWebFlux
@EnableWebFluxSecurity
@Slf4j
public class SpringWebfluxDemoApplication {

    private final ReactiveCassandraTemplate cassandraTemplate;
    private final UserRepository userRepository;

    public SpringWebfluxDemoApplication(final ReactiveCassandraTemplate cassandraTemplate,
            final UserRepository userRepository) {
        this.cassandraTemplate = cassandraTemplate;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringWebfluxDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            final List<User> users = createUser(
                    Arrays.asList(
                            User.createNew("foo", "bar"),
                            User.createNew("baz", "qux")
                    ))
                    .<List<User>>collect(ArrayList::new, List::add)
                    .log("user-creation")
                    .block();
            log.info("created users: {}", users);
            cassandraTemplate.selectOne(Query.query(Criteria.where("username").is("foo")), User.class)
                    .log("user-creation")
                    .block();
        };
                
    }

    @Transactional
    Flux<User> createUser(final Iterable<User> users) {
        return userRepository.saveAll(users);
    }

    @Bean
    CodecCustomizer addJacksonJsr310Support() {
        return configurer -> configurer.customCodecs()
                .encoder(new Jackson2JsonEncoder(objectMapper(), MimeTypeUtils.APPLICATION_JSON));
    }

    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                        .modules(new JavaTimeModule().addSerializer(OffsetDateTime.class, new CustomOffsetDateTimeSerializer()))
                        .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                        .serializationInclusion(JsonInclude.Include.NON_NULL)
                        .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                        .build();
    }

    @JsonComponent
    public static class CustomOffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
        @Override
        public void serialize(final OffsetDateTime value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
            gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
        }
    }
}
