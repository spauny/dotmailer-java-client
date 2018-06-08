package com.lindar.dotmailer.vo.internal;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameValue {
    @SerializedName("Name")
    private String name;

    @SerializedName("Value")
    private String value;
}
