package uz.zaytun.zaytunuserms.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Getter
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private final Keycloak keycloak = new Keycloak();

    @Getter
    @Setter
    @ToString
    public static class Keycloak {
        private String realm;
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
        private String grantType;
        private String scope;
        private String serverUrl;
        private String tokenUrl;
        private String refreshGrantType;
    }
}
