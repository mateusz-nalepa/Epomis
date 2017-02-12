package net.elenx.epomis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.OAuth2Operations;

@Configuration
public class FacebookOAuth2OperationConfiguration {
    @Value("${spring.social.facebook.app-secret}")
    private String APP_SECRET;
    @Value("${spring.social.facebook.app-id}")
    private String APP_ID;

    @Bean
    OAuth2Operations oAuth2Operations() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(APP_ID, APP_SECRET);
        return connectionFactory.getOAuthOperations();
    }
}
