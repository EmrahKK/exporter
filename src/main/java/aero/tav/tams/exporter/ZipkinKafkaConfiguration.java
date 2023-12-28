package aero.tav.tams.exporter;

import aero.tav.tams.exporter.properties.ZipkinKafkaBrokerProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.kafka.KafkaSender;

import java.util.Properties;

/**
 * @author mehmetkorkut
 * created 6.03.2023 16:17
 * package - aero.tav.tams.exporter
 * project - tams-exporter
 */
@Configuration
public class ZipkinKafkaConfiguration {

    private final ZipkinKafkaBrokerProperties zipkinKafkaBrokerProperties;

    public ZipkinKafkaConfiguration(ZipkinKafkaBrokerProperties zipkinKafkaBrokerProperties) {
        this.zipkinKafkaBrokerProperties = zipkinKafkaBrokerProperties;
    }

    @Bean("zipkinSender")
    public KafkaSender kafkaSender() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, zipkinKafkaBrokerProperties.getZipkin().getKeySerializer());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, zipkinKafkaBrokerProperties.getZipkin().getValueSerializer());
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, zipkinKafkaBrokerProperties.getBootstrapServers());
        return KafkaSender.newBuilder().topic(zipkinKafkaBrokerProperties.getZipkin().getTopic()).overrides(properties).build();
    }

}
