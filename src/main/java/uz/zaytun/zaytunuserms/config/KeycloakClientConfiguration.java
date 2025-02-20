package uz.zaytun.zaytunuserms.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakClientConfiguration {

    private final ApplicationProperties applicationProperties;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .realm(applicationProperties.getKeycloak().getRealm())
                .clientId(applicationProperties.getKeycloak().getClientId())
                .clientSecret(applicationProperties.getKeycloak().getClientSecret())
                .username(applicationProperties.getKeycloak().getUsername())
                .password(applicationProperties.getKeycloak().getPassword())
                .grantType(OAuth2Constants.PASSWORD)
                .serverUrl(applicationProperties.getKeycloak().getServerUrl())
                .build();
    }
}

