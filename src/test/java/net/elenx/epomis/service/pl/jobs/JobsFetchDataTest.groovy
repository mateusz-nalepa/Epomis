package net.elenx.epomis.service.pl.jobs

import com.google.api.client.http.HttpHeaders
import net.elenx.connection5.ConnectionResponse5
import spock.lang.Specification

class JobsFetchDataTest extends Specification {
    void "test hasLoggedSuccessfully"() {
        given:

        JobsConnector jobsConnector = Mock()
        HttpHeaders headers = new HttpHeaders()
        headers.setLocation("https://www.jobs.pl/strefa-kandydata")
        JobsFetchData jobsFetchData = new JobsFetchData(jobsConnector)
        
        when:

        boolean result = jobsFetchData.hasLoggedSuccessfully(headers)

        then:

        result
    }

    void "test extractJobId"() {
        given:

        JobsConnector jobsConnector = Mock()
        String url = "http://www.jobs.pl/mobile-solution-architect-9android0-oferta-1445365";

        JobsFetchData jobsFetchData = new JobsFetchData(jobsConnector)

        when:

        String result = jobsFetchData.extractJobId(url)

        then:

        result == "1445365"
    }


    void "test extractLoginCookie"() {
        given:

        JobsConnector jobsConnector = Mock()
        HttpHeaders headers = new HttpHeaders()
        headers.set("set-cookie", "[sid=5420c74852a8f5f1757885c6abbaa87ffa4c836f; path=/; domain=.jobs.pl; httponly, sid=023fc01ca2cb3ec342c25b3ff1fbed2d24ad797e; path=/; domain=.jobs.pl; httponly]")

        JobsFetchData jobsFetchData = new JobsFetchData(jobsConnector)

        when:

        String result = jobsFetchData.extractLoginCookie(headers)

        then:

        result == "sid=5420c74852a8f5f1757885c6abbaa87ffa4c836f; path=/; domain=.jobs.pl; httponly"
    }

    void "test hasAppliedSuccessfully"() {
        given:

        JobsConnector jobsConnector = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        jobsConnector.apply(*_) >> connectionResponse5

        HttpHeaders headers = new HttpHeaders()
        headers.setLocation("potwierdzenie-aplikacji")
        connectionResponse5.getHeaders() >> headers

        JobsFetchData jobsFetchData = new JobsFetchData(jobsConnector)

        when:
        boolean result = jobsFetchData.hasAppliedSuccessfully("jobID", "loginCookie", "username")

        then:

        result
    }


}