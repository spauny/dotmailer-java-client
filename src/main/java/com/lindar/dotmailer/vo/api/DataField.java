package com.lindar.dotmailer.vo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of = "name")
@Data
public class DataField <T> {
    private String name;
    private DataFieldType type;
    private DataFieldVisibility visibility = DataFieldVisibility.PRIVATE;
    private T defaultValue;

    public DataField(String name, DataFieldType type){
        this.name = name;
        this.type = type;
    }

    public DataField(String name, DataFieldType type, DataFieldVisibility visibility, T defaultValue){
        this.name = name;
        this.type = type;
        this.visibility = visibility;
        this.defaultValue = defaultValue;
    }
}
