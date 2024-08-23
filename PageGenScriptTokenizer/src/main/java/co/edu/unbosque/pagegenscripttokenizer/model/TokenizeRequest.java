package co.edu.unbosque.pagegenscripttokenizer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenizeRequest {

    @JsonProperty("dsl_content")
    private String dslContent;
}
