package com.lindar.dotmailer.vo.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailTriggeredCampaignRequest {
    private List<String> toAddresses;
    private int campaignId;
    private Map<String, String> personalizationValues;
}
