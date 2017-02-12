package net.elenx.epomis.service.com.stackoverflow;

import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
class StackOverflowCvUploader {

    private static final String FORMAT = "%s%s%s%s%s";
    private static final String BASE_URL = "http://stackoverflow.com/jobs/apply/upload-resume?jobId=";
    private static final String FKEY = "&fkey=";
    private static final String QQFILE = "&qqfile=cv.pdf";
    private final StackOverflowConnector stackOverflowConnector;

    @SneakyThrows
    StackOverflowCvUploadResponse sendCV(String jobId, String csrf, Map<String, String> cookies) {
        String cvUrl = cvUrl(jobId, csrf);
        return stackOverflowConnector.sendCV(cvUrl, cookies);
    }

    private String cvUrl(String jobId, String csrf) {
        return String.format(FORMAT, BASE_URL, jobId, FKEY, csrf, QQFILE);
    }

}
