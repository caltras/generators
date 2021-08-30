package functionaltest.<%= application.package %>.testhelper;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

@Component
@Import(ApplicationTestConfig.class)
public class WebTestClientSetup {

    private ApplicationTestConfig configuration;

    public WebTestClientSetup(ApplicationTestConfig configuration) {
        this.configuration = configuration;
    }

    public WebTestClient getClient(int port) {
        return WebTestClient.bindToServer()
                .baseUrl(configuration.getApplicationLoadBalancer() + ":" + port)
                .build();
    }
}
