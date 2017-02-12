package net.elenx.epomis.service.pl.praca;

import com.google.api.client.http.HttpMethods;
import lombok.Data;
import net.elenx.connection5.ConnectionRequest5;
import net.elenx.connection5.ConnectionService5;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.service.JobOfferService;
import org.springframework.stereotype.Service;

@Data
@Service
public class PracaPlAcceptor implements JobOfferService {
    private final ConnectionService5 connectionService5;
    private final PracaPlFactory pracaPlFactory;

    @Override
    public void accept(JobOffer jobOffer) {
        ConnectionRequest5 build = ConnectionRequest5
                .builder()
                .data(pracaPlFactory.create())
                .url(jobOffer.getHref())
                .method(HttpMethods.POST)
                .build();

        connectionService5.submit(build);
    }
}
