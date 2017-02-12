package net.elenx.epomis.provider.pl.monster;

import com.google.api.client.http.HttpMethods;
import lombok.Data;
import lombok.SneakyThrows;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
class MonsterCrawler implements JobOfferProvider {

    private static final String URL = "https://www.monsterpolska.pl/praca/q/?q=java&where=Warszawa%2C+mazowieckie&cy=pl&intcid=swoop_HeroSearch_PL";
    private static final String TYPE = "type";
    private static final String APPLICATION_LD_JSON = "application/ld+json";
    private final ConnectionService5 connectionService5;
    private final Jackson2JsonObjectMapper jsonObjectMapper;

    @Override
    public Set<JobOffer> offers() {
        return elementsWithJobOffers(URL)
                .stream()
                .skip(1)
                .map(this::extractJobOffer)
                .collect(Collectors.toSet());
    }

    private Elements elementsWithJobOffers(String url) {
        ConnectionRequest5 connectionRequest5 = ConnectionRequest5
                .builder()
                .url(url)
                .method(HttpMethods.GET)
                .build();

        return connectionService5
                .submit(connectionRequest5)
                .getResponseAsHtml()
                .orElseThrow(RuntimeException::new)
                .getElementsByAttributeValue(TYPE, APPLICATION_LD_JSON);
    }

    @SneakyThrows
    private JobOffer extractJobOffer(Element offer) {
        MonsterJobOffer monsterJobOffer = jsonObjectMapper.fromJson(offer.data(), MonsterJobOffer.class);

        return JobOffer
                .builder()
                .providerType(ProviderType.MONSTER)
                .title(monsterJobOffer.getTitle())
                .company(monsterJobOffer.getHiringOrganization().getName())
                .location(monsterJobOffer.getJobLocation().getAddress().getAddressLocality())
                .href(monsterJobOffer.getUrl())
                .build();
    }
}
