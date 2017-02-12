package net.elenx.epomis.service.com.stackoverflow

import net.elenx.connection5.ConnectionResponse5
import net.elenx.connection5.ConnectionService5
import net.elenx.connection5.DataEntry
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class StackOverflowConnectorTest extends Specification {

    void "test sendCV"() {
        given:
        StackOverflowFactory stackOverflowFactory = Mock()
        ConnectionService5 connectionService5 = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        DataEntry dataEntry = Mock()

        stackOverflowFactory.cvUploadData() >> [dataEntry]

        connectionService5.submit(*_) >> connectionResponse5

        StackOverflowCvUploadResponse response = new StackOverflowCvUploadResponse()
        connectionResponse5.getResponseAsJson(*_) >> Optional.of(response)

        StackOverflowConnector stackOverflowConnector = new StackOverflowConnector(stackOverflowFactory, connectionService5)

        when:
        StackOverflowCvUploadResponse stackOverflowCvUploadResponse = stackOverflowConnector.sendCV("cvurl", ["a": "a"])

        then:
        stackOverflowCvUploadResponse != null
    }

    void "test apply Successfully"() {
        given:
        InputStream inputStream = StackOverflowConnector.class.getResourceAsStream("stackApplySuccessfully.html")
        StackOverflowFactory stackOverflowFactory = Mock()
        ConnectionService5 connectionService5 = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()

        DataEntry dataEntry = Mock()

        Document mockDocument = Jsoup.parse(inputStream, "UTF-8", "someUrl")
        Optional<Document> responseAsHtml = Optional.of(mockDocument)
        connectionResponse5.getResponseAsHtml() >> responseAsHtml
        connectionService5.submit(_) >> connectionResponse5

        stackOverflowFactory.applyDataEntry(*_) >> [dataEntry]

        StackOverflowConnector stackOverflowConnector = new StackOverflowConnector(stackOverflowFactory, connectionService5)

        when:
        Document document = stackOverflowConnector.applySuccessfully("jobid", "csrf", "url", ["a": "a"])

        then:
        document == mockDocument
    }
}