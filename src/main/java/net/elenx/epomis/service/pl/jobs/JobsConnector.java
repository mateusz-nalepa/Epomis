package net.elenx.epomis.service.pl.jobs;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import lombok.Data;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionResponse5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.connection5.RequestContentType;
import org.springframework.stereotype.Component;

@Component
@Data
class JobsConnector {
    private static final String LOGIN = "https://www.jobs.pl/logowanie";
    private static final String REQUIRED_SUBMIT = "http://www.jobs.pl/aplikuj-";
    private static final String APPLY = "http://www.jobs.pl/formularz-aplikacyjny-";
    private static final String COOKIE = "Cookie";
    private final ConnectionService5 connectionService5;
    private final JobsFactory jobsFactory;

    HttpHeaders requestLogIn(String username, String password) {
        ConnectionRequest5 request5 = ConnectionRequest5
                .builder()
                .url(LOGIN)
                .method(HttpMethods.POST)
                .data(jobsFactory.loginFormData(username, password))
                .contentType(RequestContentType.MULTIPART)
                .shouldRedirect(false)
                .build();

        return connectionService5
                .submit(request5)
                .getHeaders();
    }

    void sendCookie(String jobId, String loginCookie) {
        ConnectionRequest5 request5 = ConnectionRequest5
                .builder()
                .url(REQUIRED_SUBMIT + jobId)
                .method(HttpMethods.POST)
                .header(COOKIE, loginCookie)
                .shouldRedirect(false)
                .build();

        connectionService5.submit(request5);
    }

    ConnectionResponse5 apply(String jobId, String loginCookie, String username) {
        ConnectionRequest5 request5 = ConnectionRequest5
                .builder()
                .url(APPLY + jobId)
                .method(HttpMethods.POST)
                .data(jobsFactory.applyFormData(username))
                .header(COOKIE, loginCookie)
                .shouldRedirect(false)
                .build();

        return connectionService5.submit(request5);
    }
}
