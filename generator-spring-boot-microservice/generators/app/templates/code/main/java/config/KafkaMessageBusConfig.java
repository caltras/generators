package <%= application.package %>.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaMessageBusConfig {

    private final KafkaAppProperties kafkaAppProperties;

    KafkaMessageBusConfig(KafkaAppProperties kafkaAppProperties) {
        this.kafkaAppProperties = kafkaAppProperties;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
    batchAndManualCommitFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }


    private ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProperties());
    }

    private Map<String, Object> getConsumerProperties() {
        Map<String, Object> props = getConnectionProperties();

        props.put("specific.avro.reader", kafkaAppProperties.getAvroReader());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaAppProperties.getGroup());
        if (isConfigNotNullOrEmpty(kafkaAppProperties.getSchemaRegistryUrl())) {
            props.put("schema.registry.url", kafkaAppProperties.getSchemaRegistryUrl());
        }
        if (isConfigNotNullOrEmpty(kafkaAppProperties.getSchemaRegistryAuthCredSource())) {
            props.put("basic.auth.credentials.source", kafkaAppProperties.getSchemaRegistryAuthCredSource());
        }
        if (isConfigNotNullOrEmpty(kafkaAppProperties.getSchemaRegistryUserInfo())) {
            props.put("schema.registry.basic.auth.user.info", kafkaAppProperties.getSchemaRegistryUserInfo());
        }
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaAppProperties.getConsumerKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaAppProperties.getConsumerValueDeserializer());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaAppProperties.getAutoOffsetReset());

        return props;
    }

    private Map<String, Object> getConnectionProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put("sasl.mechanism", kafkaAppProperties.getSaslMechanism());
        props.put("bootstrap.servers", kafkaAppProperties.getBootstrapServer());
        if (isConfigNotNullOrEmpty(kafkaAppProperties.getSaslJaasConfig())) {
            props.put("sasl.jaas.config", kafkaAppProperties.getSaslJaasConfig());
        }
        if (isConfigNotNullOrEmpty(kafkaAppProperties.getSecurityProtocol())) {
            props.put("security.protocol", kafkaAppProperties.getSecurityProtocol());
        }

        return props;
    }

    private boolean isConfigNotNullOrEmpty(String config) {
        return !config.isEmpty() && config != null;
    }
}
