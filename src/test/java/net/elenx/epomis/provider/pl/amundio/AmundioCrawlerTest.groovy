package net.elenx.epomis.provider.pl.amundio

import com.google.api.client.http.HttpHeaders
import net.elenx.connection5.ConnectionResponse5
import net.elenx.connection5.ConnectionService5
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import spock.lang.Specification

public class AmundioCrawlerTest extends Specification {


    void "extract JobOffers"() {

        given:
        InputStream inputStream = AmundioCrawlerTest.class.getResourceAsStream("amundio.html")

        ConnectionService5 connectionService5 = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        HttpHeaders httpHeaders = Mock()

        connectionService5.submit(_) >> connectionResponse5

        connectionResponse5.getHeaders() >> httpHeaders
        httpHeaders.getLocation() >> "location"

        connectionResponse5.getResponseAsHtml() >> Optional.of(
                Jsoup.parse(inputStream, "UTF-8", "http://www.amundio.pl/praca/?q=java&l=Warszawa&distance=0.06&lat=52.2296756&lng=21.0122287")
        )

        AmundioCrawler amundioCrawler = new AmundioCrawler(connectionService5)

        when:
        Set<JobOffer> offers = amundioCrawler.offers()

        then:
        offers.size() == 10
    }
}