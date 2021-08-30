package <%= application.package %>.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class KafkaAppProperties {

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.bootstrap.servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.properties.sasl.jaas.config:}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.security.protocol:}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.specific.avro.reader}")
    private String avroReader;

    @Value("${spring.kafka.properties.basic.auth.credentials.source:}")
    private String schemaRegistryAuthCredSource;

    @Value("${spring.kafka.properties.basic.auth.user.info:}")
    private String schemaRegistryUserInfo;

    @Value("${spring.kafka.properties.schema.registry.url:}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String consumerKeyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String consumerValueDeserializer;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${io.confluent.config.group}")
    private String group;
}
