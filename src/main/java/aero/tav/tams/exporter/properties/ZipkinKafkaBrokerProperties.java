package aero.tav.tams.exporter.properties;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * @author mehmetkorkut
 * created 10.08.2022 10:17
 * package - aero.tav.tams.exporter.properties
 * project - tams-exporter
 */
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Getter
@Setter
public class ZipkinKafkaBrokerProperties {

    private List<String> bootstrapServers = Collections.singletonList("localhost:9092");

    @NestedConfigurationProperty
    private ZipkinConfiguration zipkin;

    @Getter
    @Setter
    public static class ZipkinConfiguration {
        private String topic = "zipkin";
        private String keySerializer = ByteArraySerializer.class.getName();
        private String valueSerializer = ByteArraySerializer.class.getName();
    }
}
