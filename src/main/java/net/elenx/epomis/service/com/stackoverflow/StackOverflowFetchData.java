package net.elenx.epomis.service.com.stackoverflow;

import lombok.Data;
import net.elenx.connection5.ConnectionResponse5;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Data
class StackOverflowFetchData {

    private static final String FKEY = "fkey";
    private static final String ERROR = "There wasn't jobId in given href: ";
    private static final String ERROR_COOKIES = "There wasn't cookies in connectionResponse.";
    private static final String JOB_ID_REGEX = ".\\/([0-9]+)\\/.";
    private final Pattern pattern = Pattern.compile(JOB_ID_REGEX);

    String extractJobId(String url) {
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new RuntimeException(ERROR + url);
    }

    String extractCSRF(ConnectionResponse5 connectionResponse5) {
        Optional<Document> responseAsHtml = connectionResponse5.getResponseAsHtml();
        Document document = responseAsHtml.orElseThrow(RuntimeException::new);
        return document.getElementById(FKEY).val();
    }

}
