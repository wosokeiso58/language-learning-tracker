package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AppData(List<SessionManagerData> sessionManagers) {

    public AppData(@JsonProperty("sessionManagers") List<SessionManagerData> sessionManagers) {
        this.sessionManagers = sessionManagers;
    }

}
