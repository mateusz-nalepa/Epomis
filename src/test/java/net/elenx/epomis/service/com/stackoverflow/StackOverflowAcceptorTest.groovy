package net.elenx.epomis.service.com.stackoverflow

import net.elenx.connection5.ConnectionResponse5
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import spock.lang.Specification

class StackOverflowAcceptorTest extends Specification {
    void "test acceptor"() {
        given:
        InputStream inputStream = StackOverflowAcceptorTest.class.getResourceAsStream("stackApplySuccessfully.html")

        StackOverflowFetchData stackOverflowFetchData = Mock()
        StackOverflowCvUploader stackOverflowCvUploader = Mock()
        StackOverflowConnector stackOverflowConnector = Mock()
        StackOverflowCvUploadResponse stackOverflowCvUploadResponse = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()

        stackOverflowFetchData.extractJobId(_) >> "110247"
        stackOverflowFetchData.extractCSRF(_) >> "csrf"
        stackOverflowConnector.applySuccessfully(*_) >> Jsoup.parse(inputStream, "UTF-8", "someUrl")

        stackOverflowCvUploadResponse.isSuccess() >> true
        stackOverflowCvUploader.sendCV(*_) >> stackOverflowCvUploadResponse


        stackOverflowConnector.connect(*_) >> connectionResponse5
        connectionResponse5.getCookies(*_) >> ["a": "a"]

        StackOverflowAcceptor stackOverflowAcceptor = new StackOverflowAcceptor(stackOverflowFetchData, stackOverflowCvUploader, stackOverflowConnector)
        JobOffer jobOffer = Mock()

        when:
        stackOverflowAcceptor.accept(jobOffer)

        then:
        true
    }
}