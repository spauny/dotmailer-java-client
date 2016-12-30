package org.spauny.joy.dotmailer.api;

import com.google.common.reflect.TypeToken;
import org.joda.time.DateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.vo.api.Campaign;
import org.spauny.joy.dotmailer.vo.api.CampaignContactActivity;
import org.spauny.joy.dotmailer.vo.api.CampaignInfo;
import org.spauny.joy.dotmailer.vo.api.CampaignSummary;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;

/**
 *
 * @author iulian
 */
public class CampaignResource extends AbstractResource {
    
    public CampaignResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
    }
    
    /**
     * Returns a list of Campaigns including only the base information and <b>without email html content</b>.
     * If you require all the information about each campaign then use the <b>listComprehensive</b> method
     * 
     * @return
     */
    public Optional<List<CampaignInfo>> list() {
        return sendAndGetFullList(DefaultEndpoints.CAMPAIGNS.getPath(), new TypeToken<List<CampaignInfo>>() {});
    }
    
    /**
     * Returns a list of Campaigns including only the base information and <b>without email html content</b>.
     * If you require all the information about each campaign then use the <b>listComprehensive</b> method
     * 
     * @return
     */
    public Optional<List<CampaignInfo>> listWithActivitySince(Date startDate) {
        return sendAndGetFullList(pathWithParam(DefaultEndpoints.CAMPAIGNS_WITH_ACTIVITY_SINCE.getPath(), new DateTime(startDate).toString(DM_DATE_FORMAT)), new TypeToken<List<CampaignInfo>>() {});
    }

    public Optional<List<CampaignInfo>> listWithActivitySince(Date startDate, boolean roundToDate) {
        String dateTemplate = roundToDate ? DM_DATE_FORMAT : DM_DATE_TIME_FORMAT;
        return sendAndGetFullList(pathWithParam(DefaultEndpoints.CAMPAIGNS_WITH_ACTIVITY_SINCE.getPath(), new DateTime(startDate).toString(dateTemplate)), new TypeToken<List<CampaignInfo>>() {});
    }
    
    /**
     * Returns a list of Campaigns including html content, summary and all the activities. 
     * If you require only the base information about each campaign then use the <b>list</b> method
     * 
     * @return
     */
    public Optional<List<Campaign>> listComprehensive() {
        Optional<List<CampaignInfo>> campaigns = sendAndGetFullList(DefaultEndpoints.CAMPAIGNS.getPath(), new TypeToken<List<CampaignInfo>>() {});
        if (!campaigns.isPresent() || campaigns.get().isEmpty()) {
            return Optional.empty();
        }
        List<Campaign> comprehensiveCampaigns = campaigns.get().stream()
                .map(baseCampaign -> get(baseCampaign.getId()).orElse(new Campaign(baseCampaign)))
                .collect(Collectors.toList());
        
        return Optional.of(comprehensiveCampaigns);
    }
    
    /**
     * Returns the full information about a campaign, including html content, summary and all the activities. 
     * In order to do this, the client will make at least 3 requests (or more depending on the activities), 
     * for each type of information and stitch the data together in one Campaign object.
     * 
     * Please note: if you only need the info, the summary or the activity then use the specific methods allocated. Enjoy!
     * 
     * @param id
     * @return
     */
    public Optional<Campaign> get(Long id) {
        Optional<CampaignInfo> info = info(id);
        if (!info.isPresent()) {
            return Optional.empty();
        }
        Campaign campaign = new Campaign(info.get());
        
        Optional<CampaignSummary> summary = summary(id);
        if (summary.isPresent()) {
            campaign.setSummary(summary.get());
        }
        
        Optional<List<CampaignContactActivity>> activities = activities(id);
        if (activities.isPresent()) {
            campaign.setActivities(activities.get());
        }
        
        return Optional.of(campaign);
    }
    
    public Optional<CampaignInfo> info(Long id) {
        String path = pathWithId(DefaultEndpoints.CAMPAIGN_INFO.getPath(), id);
        return sendAndGet(path, CampaignInfo.class);
    }
    
    public Optional<CampaignSummary> summary(Long id) {
        String path = pathWithId(DefaultEndpoints.CAMPAIGN_SUMMARY.getPath(), id);
        return sendAndGet(path, CampaignSummary.class);
    }
    
    public Optional<List<CampaignContactActivity>> activities(Long id) {
        String path = pathWithId(DefaultEndpoints.CAMPAIGN_ACTIVITY.getPath(), id);
        return sendAndGetFullList(path, new TypeToken<List<CampaignContactActivity>>() {});
    }
    

    public Optional<List<CampaignContactActivity>> activitiesSince(Long id, Date startDate, boolean roundToDate) {
        String dateTemplate = roundToDate ? DM_DATE_FORMAT : DM_DATE_TIME_FORMAT;
        String path = pathWithIdAndParam(DefaultEndpoints.CAMPAIGN_ACTIVITY_SINCE.getPath(), id, new DateTime(startDate).toString(dateTemplate));
        return sendAndGetFullList(path, new TypeToken<List<CampaignContactActivity>>() {});
    }

    public Optional<List<CampaignContactActivity>> activitiesSince(Long id, Date startDate) {
        return activitiesSince(id, startDate, true);
    }
}
