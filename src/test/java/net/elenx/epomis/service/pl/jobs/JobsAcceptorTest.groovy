package net.elenx.epomis.service.pl.jobs

import com.google.api.client.http.HttpHeaders
import net.elenx.epomis.entity.JobOffer
import spock.lang.Specification

class JobsAcceptorTest extends Specification {
    void "test acceptor"() {
        given:

        JobsFetchData jobsFetchData = Mock()
        JobsConnector jobsConnector = Mock()
        HttpHeaders httpHeaders = Mock()

        jobsConnector.requestLogIn(*_) >> httpHeaders
        jobsFetchData.hasLoggedSuccessfully(_) >> true
        jobsFetchData.extractLoginCookie(_) >> "loginCookie"
        jobsFetchData.extractJobId(_) >> "jobId"
        jobsFetchData.hasAppliedSuccessfully(*_) >> true

        JobsAcceptor jobsAcceptor = new JobsAcceptor(jobsConnector, jobsFetchData)
        JobOffer jobOffer = Mock()

        when:

        jobsAcceptor.accept(jobOffer)

        then:

        true
    }
}