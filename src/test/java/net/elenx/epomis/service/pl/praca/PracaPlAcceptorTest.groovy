package net.elenx.epomis.service.pl.praca

import net.elenx.connection5.ConnectionService5
import net.elenx.connection5.DataEntry
import net.elenx.epomis.entity.JobOffer
import spock.lang.Specification

class PracaPlAcceptorTest extends Specification {
    void "accept JobOffer"() {
        given:
        ConnectionService5 connectionService5 = Mock()
        PracaPlFactory pracaPlFactory = Mock()
        JobOffer jobOffer = Mock()
        List<DataEntry> a = new ArrayList<>()

        jobOffer.getHref() >> "url"

        a.add(new DataEntry("key", "value"))
        pracaPlFactory.create() >> a

        PracaPlAcceptor plAcceptor = new PracaPlAcceptor(connectionService5, pracaPlFactory)

        when:
        plAcceptor.accept(jobOffer)

        then:
        1 * connectionService5.submit(_)
    }
}
