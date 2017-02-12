package net.elenx.epomis.service.com.stackoverflow;

import net.elenx.connection5.DataEntry;
import net.elenx.epomis.provider.cv.CVProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
class StackOverflowFactory {

    private static final String EMPTY_STRING = "";
    private static final String FORMAT = "%s%s%s%s%s%s%s%s";
    private static final String BEGIN_MAIL = "spam";
    private static final String END_MAIL = "@somemail.com";
    private static final String FKEY = "fkey";
    private static final String SID = "sid";
    private static final String SID_VALUE = "0";
    private static final String JOB_ID = "JobId";
    private static final String CANDIDATE_NAME = "CandidateName";
    private static final String EXAMPLE_NAME = "Jaden korr";
    private static final String CANDIDATE_PHONE_NUMBER = "CandidatePhoneNumber";
    private static final String CANDIDATE_EMAIL = "CandidateEmail";
    private static final String CANDIDATE_LOCATION = "CandidateLocation";
    private static final String COVER_LETTER = "CoverLetter";
    private static final String SHOW_COVER_LETTER_EXAMPLES = "ShowCoverLetterExamples";
    private static final String EXAMPLE_COVER_LETTER = "this is spam for only test project";
    private static final String QQFILE = "qqfile";
    private static final String CV_PDF = "cv.pdf";
    private final List<DataEntry> templateDataEntries;
    private final List<DataEntry> templateCVUploadEntries;

    public StackOverflowFactory(CVProvider cvProvider) {
        LocalDateTime ldt = LocalDateTime.now();
        String mail = String.format(FORMAT, BEGIN_MAIL, ldt.getYear(), ldt.getMonth(), ldt.getDayOfYear(), ldt.getHour(), ldt.getMinute(), ldt.getSecond(), END_MAIL);
        templateDataEntries = Arrays.asList(
                new DataEntry(SID, SID_VALUE),
                new DataEntry(CANDIDATE_NAME, EXAMPLE_NAME),
                new DataEntry(CANDIDATE_PHONE_NUMBER, EMPTY_STRING),
                new DataEntry(CANDIDATE_EMAIL, mail),
                new DataEntry(CANDIDATE_LOCATION, EMPTY_STRING),
                new DataEntry(COVER_LETTER, EXAMPLE_COVER_LETTER),
                new DataEntry(SHOW_COVER_LETTER_EXAMPLES, EMPTY_STRING)
        );
        templateCVUploadEntries = Collections.singletonList(
                new DataEntry(QQFILE, CV_PDF, cvProvider.asInputStream())
        );
    }

    List<DataEntry> applyDataEntry(String id, String csrf) {
        List<DataEntry> dataEntries = new LinkedList<>(templateDataEntries);
        dataEntries.add(new DataEntry(FKEY, csrf));
        dataEntries.add(new DataEntry(JOB_ID, id));
        return dataEntries;
    }

    List<DataEntry> cvUploadData() {
        return templateCVUploadEntries;
    }
}
