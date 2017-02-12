package net.elenx.epomis.service.com.stackoverflow;

import com.google.api.client.http.HttpMethods;
import lombok.Data;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionResponse5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.connection5.DataEntry;
import org.apache.http.protocol.HTTP;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Data
class StackOverflowConnector {

    private static final String CV_UPLOAD_UNSUCCESSFULL = "Couldn't upload cv.";
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    private final StackOverflowFactory stackOverflowFactory;
    private final ConnectionService5 connectionService5;

    ConnectionResponse5 connect(String url) {
        ConnectionRequest5 connectionRequest = ConnectionRequest5
                .builder()
                .url(url)
                .method(HttpMethods.GET)
                .build();

        return connectionService5.submit(connectionRequest);
    }

    StackOverflowCvUploadResponse sendCV(String cvUrl, Map<String, String> cookies) {
        ConnectionRequest5 connectionRequest5 = ConnectionRequest5.builder()
                .method(HttpMethods.POST)
                .url(cvUrl)
                .data(stackOverflowFactory.cvUploadData())
                .header(HTTP.CONTENT_TYPE, APPLICATION_OCTET_STREAM)
                .cookies(cookies)
                .build();

        ConnectionResponse5 connectionResponse5 = connectionService5.submit(connectionRequest5);
        Optional<StackOverflowCvUploadResponse> response = connectionResponse5.getResponseAsJson(StackOverflowCvUploadResponse.class);
        return response.orElseThrow(() -> new RuntimeException(CV_UPLOAD_UNSUCCESSFULL));
    }

    Document applySuccessfully(String jobId, String csrf, String url, Map<String, String> cookies) {
        List<DataEntry> dataEntries = stackOverflowFactory.applyDataEntry(jobId, csrf);
        ConnectionRequest5 connectionRequest = ConnectionRequest5
                .builder()
                .url(url)
                .method(HttpMethods.POST)
                .data(dataEntries)
                .cookies(cookies)
                .build();

        ConnectionResponse5 connectionResponse5 = connectionService5.submit(connectionRequest);
        Optional<Document> responseAsHtml = connectionResponse5.getResponseAsHtml();
        return responseAsHtml.orElseThrow(RuntimeException::new);
    }
}
