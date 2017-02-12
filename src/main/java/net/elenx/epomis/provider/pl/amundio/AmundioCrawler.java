package net.elenx.epomis.provider.pl.amundio;

import com.google.api.client.http.HttpMethods;
import lombok.Data;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
class AmundioCrawler implements JobOfferProvider {
    private static final String URL_WITH_JOB_OFFERS = "http://www.amundio.pl/praca/?q=java&l=Warszawa&distance=0.06&lat=52.2296756&lng=21.0122287";
    private static final String TR = "tr";
    private static final String B = "b";
    private static final String A = "a";
    private static final String HREF = "href";
    private static final String SPAN = "span";
    private static final String SCRIPT = "script";
    private static final String FORMAT_LOCATION = "%s/%s";
    private static final String FORMAT_URL = "%s%s";
    private static final String SMALL = "small";
    private static final String BASE_URL = "http://www.amundio.pl";
    private final ConnectionService5 connectionService5;

    @Override
    public Set<JobOffer> offers() {
        ConnectionRequest5 connectionRequest5 = ConnectionRequest5
                .builder()
                .url(URL_WITH_JOB_OFFERS)
                .method(HttpMethods.GET)
                .build();

        return connectionService5
                .submit(connectionRequest5)
                .getResponseAsHtml()
                .orElseThrow(RuntimeException::new)
                .getElementsByTag(TR)
                .stream()
                .filter(this::isJobOffer)
                .map(this::extractOffer)
                .collect(Collectors.toSet());
    }

    private boolean isJobOffer(Element element) {
        return element.getElementsByTag(SCRIPT).isEmpty();
    }

    private JobOffer extractOffer(Element element) {
        Elements a = element.getElementsByTag(A);
        String title = a.html();
        String href = changeTargetUrl(a.attr(HREF));
        String city = element.getElementsByTag(B).html();
        String region = element.getElementsByTag(SMALL).html();
        String company = element.getElementsByTag(SPAN).get(6).html();

        return JobOffer
                .builder()
                .providerType(ProviderType.AMUNDIO)
                .title(title)
                .company(company)
                .location(String.format(FORMAT_LOCATION, city, region))
                .href(href)
                .build();
    }

    private String changeTargetUrl(String href) {
        ConnectionRequest5 connectionRequest5 = ConnectionRequest5
                .builder()
                .url(String.format(FORMAT_URL, BASE_URL, href))
                .method(HttpMethods.GET)
                .build();

        return connectionService5
                .submit(connectionRequest5)
                .getHeaders()
                .getLocation();
    }
}
