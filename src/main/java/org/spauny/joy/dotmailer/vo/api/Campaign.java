package org.spauny.joy.dotmailer.vo.api;

import java.util.List;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class Campaign {

    private CampaignInfo info;
    private CampaignSummary summary;
    private List<CampaignContactActivity> activities;
    
    public Campaign(CampaignInfo info) {
        this.info = info;
    }
}
