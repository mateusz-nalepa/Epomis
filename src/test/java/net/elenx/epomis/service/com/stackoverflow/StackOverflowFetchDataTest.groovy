package net.elenx.epomis.service.com.stackoverflow

import net.elenx.connection5.ConnectionResponse5
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class StackOverflowFetchDataTest extends Specification {
    void "test extractJobId"() {
        given:
        String url = "http://stackoverflow.com/jobs/110247/software-developer-c-plus-plus-java-python-goldman-sachs";
        StackOverflowFetchData stackOverflowFetchData = new StackOverflowFetchData();

        when:
        String jobId = stackOverflowFetchData.extractJobId(url);

        then:
        jobId == "110247"
    }

    void "test extractCsrf"() {
        given:
        InputStream inputStream = StackOverflowFetchDataTest.class.getResourceAsStream("stackJobOffer.html")
        Optional<Document> document = Optional.of(
                Jsoup.parse(inputStream, "UTF-8", "someUrl")
        )
        ConnectionResponse5 connectionResponse5 = Mock()
        connectionResponse5.getResponseAsHtml() >> document

        StackOverflowFetchData stackOverflowFetchData = new StackOverflowFetchData();

        when:
        String csrf = stackOverflowFetchData.extractCSRF(connectionResponse5);

        then:
        csrf == "1bc6ebdd8d1f3d782c5cb05c64800954"
    }

}