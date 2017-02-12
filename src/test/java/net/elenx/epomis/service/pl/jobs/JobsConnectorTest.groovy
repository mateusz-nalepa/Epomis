package net.elenx.epomis.service.pl.jobs

import com.google.api.client.http.HttpHeaders
import net.elenx.connection5.ConnectionResponse5
import net.elenx.connection5.ConnectionService5
import net.elenx.connection5.DataEntry
import spock.lang.Specification

class JobsConnectorTest extends Specification {
    void "test requestLogIn"() {
        given:
        ConnectionService5 connectionService5 = Mock()
        JobsFactory jobsFactory = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        DataEntry dataEntry = Mock()
        HttpHeaders headers = Mock()

        jobsFactory.loginFormData(*_) >> [dataEntry]
        connectionService5.submit(_) >> connectionResponse5
        connectionResponse5.getHeaders() >> headers

        JobsConnector jobsConnector = new JobsConnector(connectionService5, jobsFactory)

        when:

        jobsConnector.requestLogIn("username", "password")

        then:

        1 * connectionResponse5.getHeaders()
    }

    void "test sendCookie"() {
        given:

        ConnectionService5 connectionService5 = Mock()
        JobsFactory jobsFactory = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        connectionService5.submit(_) >> connectionResponse5

        JobsConnector jobsConnector = new JobsConnector(connectionService5, jobsFactory)

        when:

        jobsConnector.sendCookie("jobId", "loginCookie")

        then:

        1 * connectionService5.submit(_)
    }

    void "test apply"() {
        given:

        ConnectionService5 connectionService5 = Mock()
        JobsFactory jobsFactory = Mock()
        ConnectionResponse5 connectionResponse5 = Mock()
        DataEntry dataEntry = Mock()

        jobsFactory.applyFormData(*_) >> [dataEntry]
        connectionService5.submit(_) >> connectionResponse5

        JobsConnector jobsConnector = new JobsConnector(connectionService5, jobsFactory)

        when:

        jobsConnector.apply("jobId", "loginCookie", "username")

        then:

        1 * connectionService5.submit(_)
    }
}