package net.elenx.epomis.service.com.stackoverflow

import spock.lang.Specification

class StackOverflowCvUploaderTest extends Specification {
    void "test sendCV"() {
        given:
        StackOverflowConnector stackOverflowConnector = Mock()
        StackOverflowCvUploadResponse stackOverflowCvUploadResponse = Mock()

        stackOverflowConnector.sendCV(*_) >> stackOverflowCvUploadResponse
        StackOverflowCvUploader uploader = new StackOverflowCvUploader(stackOverflowConnector)

        when:
        uploader.sendCV("job", "csrf", ["a": "a"])

        then:
        1 * stackOverflowConnector.sendCV(*_)
    }
}