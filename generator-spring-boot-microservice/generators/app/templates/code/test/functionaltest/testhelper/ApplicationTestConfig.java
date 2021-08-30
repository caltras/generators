package functionaltest.<%= application.package %>.testhelper;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@Getter
public class ApplicationTestConfig {

    @Value("${functionaltest.applicationloadbalancer}")
    private String applicationLoadBalancer;
}
