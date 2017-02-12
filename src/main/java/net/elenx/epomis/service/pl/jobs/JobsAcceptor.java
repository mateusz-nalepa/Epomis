package net.elenx.epomis.service.pl.jobs;


import com.google.api.client.http.HttpHeaders;
import lombok.Data;
import lombok.extern.java.Log;
import net.elenx.epomis.entity.JobOffer;
import net.elenx.epomis.service.JobOfferService;
import org.springframework.stereotype.Service;

@Data
@Service
@Log
class JobsAcceptor implements JobOfferService {

    private static final String USERNAME = "anlqdkiz@sharklasers.com";
    private static final String PASS = "Qwerty123";
    private static final String ERROR_LOGIN = "Wrong username or password.";
    private static final String APPLICATION_UNSCUCCESSFULL_BEGIN = "Couldn't apply for: ";
    private static final String APPLICATION_UNSCUCCESSFULL_END = " Possible reasons: 1. Not all fields are filled. " +
            "2. There are changes of fields name on the provider side.";
    private static final String FORMAT = "%s%s%s";

    private final JobsConnector jobsConnector;
    private final JobsFetchData jobsFetchData;

    @Override
    public void accept(JobOffer jobOffer) {
        HttpHeaders responseHeaders = jobsConnector.requestLogIn(USERNAME, PASS);

        if (!jobsFetchData.hasLoggedSuccessfully(responseHeaders)) {
            log.info(ERROR_LOGIN);
            return;
        }

        String url = jobOffer.getHref();
        String loginCookie = jobsFetchData.extractLoginCookie(responseHeaders);
        String jobId = jobsFetchData.extractJobId(url);

        jobsConnector.sendCookie(jobId, loginCookie);

        if (!jobsFetchData.hasAppliedSuccessfully(jobId, loginCookie, USERNAME)) {
            log.info(String.format(FORMAT, APPLICATION_UNSCUCCESSFULL_BEGIN, url, APPLICATION_UNSCUCCESSFULL_END));
        }
    }

}
