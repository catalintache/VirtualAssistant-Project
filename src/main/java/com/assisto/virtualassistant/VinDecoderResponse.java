package com.assisto.virtualassistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class VinDecoderResponse {
    private List<Decode> decode;

    public List<Decode> getDecode() {
        return decode;
    }

    public void setDecode(List<Decode> decode) {
        this.decode = decode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Decode {
        private String label;

        @JsonDeserialize(using = ValueDeserializer.class)
        private Object value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}
