package net.elenx.epomis.service.pl.jobs;

import net.elenx.connection5.DataEntry;
import net.elenx.epomis.provider.cv.CVProvider;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
class JobsFactory {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String APPLY_MSG = "apply_msg";
    private static final String EXAMPLE_APPLY_MSG = "Sorry for spam";
    private static final String USER_FILE_1_TYPE = "user_file_1_type";
    private static final String THREE = "3";
    private static final String USER_FILE_1 = "user_file_1";
    private static final String USER_FILE_2 = "user_file_2";
    private static final String USER_FILE_3 = "user_file_3";
    private static final String USER_FILE_4 = "user_file_4";
    private static final String EMPTY_STRING = "";
    private static final String CV_PDF = "cv.pdf";
    private static final String ONE = "1";
    private static final String CREATE_DATE = "profile_docs[0][create_date]";
    private static final String TYPE = "profile_docs[0][type]";
    private static final String NAME = "profile_docs[0][name]";
    private static final String FILEPATH = "profile_docs[0][filepath]";
    private static final String FILENAME = "profile_docs[0][filename]";
    private static final String RULE_ACCEPT = "rule_accept";
    private static final String ON = "on";

    private final List<DataEntry> templateCVUploadData;

    JobsFactory(CVProvider cvProvider) {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        templateCVUploadData = Collections.unmodifiableList(
                Arrays.asList(
                        new DataEntry(APPLY_MSG, EXAMPLE_APPLY_MSG),
                        new DataEntry(USER_FILE_1_TYPE, THREE),
                        new DataEntry(USER_FILE_1, CV_PDF, cvProvider.asInputStream()),
                        new DataEntry(USER_FILE_2, EMPTY_STRING, emptyInputStream),
                        new DataEntry(USER_FILE_3, EMPTY_STRING, emptyInputStream),
                        new DataEntry(USER_FILE_4, EMPTY_STRING, emptyInputStream),
                        new DataEntry(CREATE_DATE, EMPTY_STRING),
                        new DataEntry(TYPE, ONE),
                        new DataEntry(NAME, EMPTY_STRING),
                        new DataEntry(FILEPATH, EMPTY_STRING),
                        new DataEntry(FILENAME, EMPTY_STRING),
                        new DataEntry(RULE_ACCEPT, ON)
                )
        );
    }

    List<DataEntry> loginFormData(String username, String password) {
        return Arrays.asList(
                new DataEntry(USERNAME, username),
                new DataEntry(PASSWORD, password)
        );
    }

    List<DataEntry> applyFormData(String username) {
        LinkedList<DataEntry> cvUploadData = new LinkedList<>(templateCVUploadData);
        cvUploadData.add(new DataEntry(USERNAME, username));
        return templateCVUploadData;
    }
}
