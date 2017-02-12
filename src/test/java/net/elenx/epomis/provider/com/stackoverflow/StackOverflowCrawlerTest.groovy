package net.elenx.epomis.provider.com.stackoverflow

import net.elenx.connection2.ConnectionResponse
import net.elenx.connection2.ConnectionService2
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import spock.lang.Specification

class StackOverflowCrawlerTest extends Specification {
    void "extract JobOffers"() {
        given:
        InputStream inputStream = StackOverflowCrawlerTest.class.getResourceAsStream("stackOverflow.html")

        ConnectionService2 connectionService2 = Mock()
        ConnectionResponse connectionResponse = Mock()
        connectionService2.submit(_) >> connectionResponse
        connectionResponse.getDocument() >> Jsoup.parse(inputStream, "UTF-8",
                "http://stackoverflow" +
                ".com/jobs?searchTerm=java&location=warsaw&range=20")

        StackOverflowCrawler stackOverflowCrawler = new StackOverflowCrawler(connectionService2)

        when:
        Set<JobOffer> offers = stackOverflowCrawler.offers()

        then:
        offers.size() == 11
    }
}
