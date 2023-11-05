package com.example.slangworddictionary;

import javafx.beans.property.SimpleStringProperty;

public class SlangDefinition {
    private String slang;
    private String definition;

    public SlangDefinition(String slang, String definition) {
        this.slang = slang;
        this.definition = definition;
    }

    public String getSlang() {
        return slang;
    }

    public String getDefinition() {
        return definition;
    }
}

