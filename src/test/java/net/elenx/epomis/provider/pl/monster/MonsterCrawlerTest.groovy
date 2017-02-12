package net.elenx.epomis.provider.pl.monster

import net.elenx.connection5.ConnectionResponse5
import net.elenx.connection5.ConnectionService5
import net.elenx.epomis.entity.JobOffer
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.integration.support.json.Jackson2JsonObjectMapper
import spock.lang.Specification

class MonsterCrawlerTest extends Specification {
    void "extract JobOffers"() {
        given:
        InputStream inputStream = MonsterCrawlerTest.class.getResourceAsStream("monster.html")

        ConnectionService5 connectionService5 = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        Jackson2JsonObjectMapper jackson2JsonObjectMapper = new Jackson2JsonObjectMapper()

        Optional<Document> responseAsHtml = Optional.of(Jsoup.parse(inputStream, "UTF-8",
                "http://www.monsterpolska.pl/praca/q/?q=java&where=" +
                        "Warszawa%2C+mazowieckie&cy=pl&intcid=swoop_HeroSearch_PL"))

        connectionService5.submit(_) >> connectionResponse5
        connectionResponse5.getResponseAsHtml() >> responseAsHtml


        MonsterCrawler monsterCrawler = new MonsterCrawler(connectionService5, jackson2JsonObjectMapper)

        when:
        Set<JobOffer> offers = monsterCrawler.offers()

        then:
        offers.size() == 25
    }
}
