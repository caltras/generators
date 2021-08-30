package <%= application.package %>.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
public class AppConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${build.version:0.0.1-SNAPSHOT}")
    private String buildVersion;

}
