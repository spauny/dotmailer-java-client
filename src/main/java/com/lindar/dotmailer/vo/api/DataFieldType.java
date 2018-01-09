package com.lindar.dotmailer.vo.api;

import com.google.gson.annotations.SerializedName;

public enum DataFieldType {
    @SerializedName("String") STRING,
    @SerializedName("Numeric") NUMERIC,
    @SerializedName("Date") DATE,
    @SerializedName("Boolean") BOOLEAN;
}
