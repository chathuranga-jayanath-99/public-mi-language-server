package org.eclipse.lemminx.customservice.synapse.parser;

import org.eclipse.lsp4j.Range;

public class PropertyDetails {

    private String name;
    private String value;
    private Range range;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    public Range getRange() {

        return range;
    }

    public void setRange(Range range) {

        this.range = range;
    }
}
