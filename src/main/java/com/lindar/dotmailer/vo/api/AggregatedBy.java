package com.lindar.dotmailer.vo.api;

import com.google.gson.annotations.SerializedName;

public enum AggregatedBy {
    @SerializedName("AllTime") ALL_TIME("AllTime"),
    @SerializedName("Month") MONTH("Month"),
    @SerializedName("Week") WEEK("Week"),
    @SerializedName("Day") DAY("Day");

    private final String value;

    AggregatedBy(String value) {
        this.value = value;
    }
}
