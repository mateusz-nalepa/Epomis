package net.elenx.epomis.service.com.stackoverflow;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import net.elenx.connection5.ConnectionResponse5;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.service.JobOfferService;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Data
@Service
@Log
class StackOverflowAcceptor implements JobOfferService {

    private static final String BASE_URL = "http://stackoverflow.com/jobs/apply/";
    private static final String WIDGET_SUCCESS = "widget-success";
    private static final String AWESOME = "Awesome!";
    private static final String APPLICATION_UNSCUCCESSFULL_BEGIN = "Couldn't apply for: ";
    private static final String APPLICATION_UNSCUCCESSFULL_END = " Possible reasons: 1. Not all fields are filled. 2. You have tried to apply on this offer during last " +
            "30days. 3. There are changes of fields name on the provider side.";
    private static final String CV_UPLOAD_UNSUCCESSFULL = "Couldn't upload CV. Probably no file has been selected.";
    private static final String FORMAT = "%s%s%s";
    private final StackOverflowFetchData stackOverflowFetchData;
    private final StackOverflowCvUploader stackOverflowCvUploader;
    private final StackOverflowConnector stackOverflowConnector;

    @Override
    @SneakyThrows
    public void accept(JobOffer jobOffer) {
        String jobId = stackOverflowFetchData.extractJobId(jobOffer.getHref());
        String url = BASE_URL + jobId;
        ConnectionResponse5 connectionResponse5 = stackOverflowConnector.connect(url);
        String csrf = stackOverflowFetchData.extractCSRF(connectionResponse5);
        Map<String, String> cookies = connectionResponse5.getCookies();
        StackOverflowCvUploadResponse stackOverflowCvUploadResponse = stackOverflowCvUploader.sendCV(jobId, csrf, cookies);

        if (!stackOverflowCvUploadResponse.isSuccess()) {
            log.info(CV_UPLOAD_UNSUCCESSFULL);
            return;
        }

        if (!hasAppliedSuccessfully(jobId, csrf, url, cookies)) {
            log.info(String.format(FORMAT, APPLICATION_UNSCUCCESSFULL_BEGIN, url, APPLICATION_UNSCUCCESSFULL_END));
        }
    }

    private boolean hasAppliedSuccessfully(String jobId, String csrf, String url, Map<String, String> cookies) {
        Document document = stackOverflowConnector.applySuccessfully(jobId, csrf, url, cookies);
        return Optional.of(document)
                .map(d -> d.getElementsByClass(WIDGET_SUCCESS))
                .map(e -> e.get(0))
                .map(Element::html)
                .map(html -> html.contains(AWESOME))
                .orElse(false);
    }

}