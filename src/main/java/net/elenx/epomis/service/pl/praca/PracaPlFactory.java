package net.elenx.epomis.service.pl.praca;

import net.elenx.connection5.DataEntry;
import net.elenx.epomis.provider.cv.CVProvider;
import org.atmosphere.config.service.Singleton;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Singleton
@Component
class PracaPlFactory {

    private static final String NAME = "data[OgloszenieKandydat][imie]";
    private static final String EXAMPLE_NAME = "name";
    private static final String MAIL = "data[OgloszenieKandydat][email]";
    private static final String EXAMPLE_MAIL = "mail@spam.net";
    private static final String CREATE_ACCOUNT = "data[Consent][createAccount]";
    private static final String NO = "0";
    private static final String YES = "1";
    private static final String FILE = "data[OgloszenieKandydatPlik][]";
    private static final String FILENAME = "cv.pdf";
    private static final String CONTENT = "data[OgloszenieKandydat][tresc]";
    private static final String EXAMPLE_CONTENT = "data[OgloszenieKandydat][tresc]";
    private static final String REGULATION_ACCEPT = "data[Consent][regulations]";
    private static final String MARKETING_ACCEPT = "data[Consent][marketing]";
    private static final String APPLICATION_FORM = "application-form";
    private static final String SUBMIT_BUTTON = "Wyślij do pracodawcy";
    private final List<DataEntry> templateDataEntries;

    PracaPlFactory(CVProvider cvProvider) {
        templateDataEntries = Arrays.asList
                (
                        new DataEntry(NAME, EXAMPLE_NAME),
                        new DataEntry(MAIL, EXAMPLE_MAIL),
                        new DataEntry(CREATE_ACCOUNT, NO),
                        new DataEntry(FILE, FILENAME, cvProvider.asInputStream()),
                        new DataEntry(CONTENT, EXAMPLE_CONTENT),
                        new DataEntry(REGULATION_ACCEPT, YES),
                        new DataEntry(MARKETING_ACCEPT, NO),
                        new DataEntry(APPLICATION_FORM, SUBMIT_BUTTON)
                );
    }

    List<DataEntry> create() {
        return templateDataEntries;
    }
}
