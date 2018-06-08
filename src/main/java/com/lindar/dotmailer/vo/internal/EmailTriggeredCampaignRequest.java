package com.lindar.dotmailer.vo.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailTriggeredCampaignRequest {
    private List<String> toAddresses;
    private int campaignId;
    private List<NameValue> personalizationValues;
}
