package net.elenx.epomis.provider.com.stackoverflow;

import lombok.Data;
import net.elenx.connection2.ConnectionRequest;
import net.elenx.connection2.ConnectionService2;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.model.ProviderType;
import net.elenx.epomis.provider.JobOfferProvider;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
class StackOverflowCrawler implements JobOfferProvider
{
    private final ConnectionService2 connectionService2;
    
    @Override
    public Set<JobOffer> offers()
    {
        ConnectionRequest connectionRequest = ConnectionRequest
            .builder()
            .url("http://stackoverflow.com/jobs?searchTerm=java&location=warsaw&range=20")
            .method(Connection.Method.GET)
            .build();
        
        return connectionService2
            .submit(connectionRequest)
            .getDocument()
            .getElementsByClass("-job")
            .stream()
            .map(this::extractOffer)
            .collect(Collectors.toSet());
    }
    
    private JobOffer extractOffer(Element jobOffer)
    {
        Element element = jobOffer.getElementsByClass("job-link").get(0);
        
        return JobOffer
            .builder()
            .providerType(ProviderType.STACK_OVERFLOW)
            .title(element.text())
            .company(jobOffer.getElementsByClass("employer").get(0).text())
            .location(jobOffer.getElementsByClass("location").get(0).text())
            .href(element.attr("href"))
            .build();
    }
}
