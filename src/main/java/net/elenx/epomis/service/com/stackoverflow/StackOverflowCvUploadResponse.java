package net.elenx.epomis.service.com.stackoverflow;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class StackOverflowCvUploadResponse extends GenericJson {

    @Key
    private boolean success;
    @Key
    private String filename;

}