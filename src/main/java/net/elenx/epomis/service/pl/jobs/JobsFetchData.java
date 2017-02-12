package net.elenx.epomis.service.pl.jobs;

import com.google.api.client.http.HttpHeaders;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Data
class JobsFetchData {
    private static final String ERROR = "There wasn't jobId in given href: ";
    private static final String SET_COOKIE = "set-cookie";
    private static final String COMMA = ",";
    private static final String DASH = "-";
    private static final String SQUARE_BRACKET_OPEN = "[";
    private static final String EMPTY_STRING = "";
    private static final String URL_FOR_LOGGED_USERS = "https://www.jobs.pl/strefa-kandydata";
    private static final String CONFIRMATION_APPLY = "potwierdzenie-aplikacji";
    private static final String JOB_ID_REGEX = "oferta-[0-9]+";
    private final Pattern pattern = Pattern.compile(JOB_ID_REGEX);
    private final JobsConnector jobsConnector;

    boolean hasLoggedSuccessfully(HttpHeaders responseHeaders) {
        return responseHeaders
                .getLocation()
                .contains(URL_FOR_LOGGED_USERS);
    }

    String extractJobId(String url) {
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException(ERROR + url);
        }
        return matcher
                .group(0)
                .split(DASH)[1];
    }

    String extractLoginCookie(HttpHeaders headers) {
        return headers
                .get(SET_COOKIE)
                .toString()
                .split(COMMA)[0]
                .replace(SQUARE_BRACKET_OPEN, EMPTY_STRING);
    }

    boolean hasAppliedSuccessfully(String jobId, String loginCookie, String username) {
        return jobsConnector
                .apply(jobId, loginCookie, username)
                .getHeaders()
                .getLocation()
                .contains(CONFIRMATION_APPLY);
    }
}
