package org.spauny.joy.dotmailer.vo.api;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class CampaignSummary {

    private Date dateSent;
    private Integer numUniqueOpens;
    private Integer numUniqueTextOpens;
    private Integer numTotalUniqueOpens;
    private Integer numOpens;
    private Integer numTextOpens;
    private Integer numTotalOpens;
    private Integer numClicks;
    private Integer numTextClicks;
    private Integer numTotalClicks;
    private Integer numPageViews;
    private Integer numTotalPageViews;
    private Integer numTextPageViews;
    private Integer numForwards;
    private Integer numTextForwards;
    private Integer numEstimatedForwards;
    private Integer numTextEstimatedForwards;
    private Integer numTotalEstimatedForwards;
    private Integer numReplies;
    private Integer numTextReplies;
    private Integer numTotalReplies;
    private Integer numHardBounces;
    private Integer numTextHardBounces;
    private Integer numTotalHardBounces;
    private Integer numSoftBounces;
    private Integer numTextSoftBounces;
    private Integer numTotalSoftBounces;
    private Integer numUnsubscribes;
    private Integer numTextUnsubscribes;
    private Integer numTotalUnsubscribes;
    private Integer numIspComplaints;
    private Integer numTextIspComplaints;
    private Integer numTotalIspComplaints;
    private Integer numMailBlocks;
    private Integer numTextMailBlocks;
    private Integer numTotalMailBlocks;
    private Integer numSent;
    private Integer numTextSent;
    private Integer numTotalSent;
    private Integer numRecipientsClicked;
    private Integer numDelivered;
    private Integer numTextDelivered;
    private Integer numTotalDelivered;
    private Integer percentageDelivered;
    private Integer percentageUniqueOpens;
    private Double percentageOpens;
    private Double percentageUnsubscribes;
    private Double percentageReplies;
    private Double percentageHardBounces;
    private Double percentageSoftBounces;
    private Double percentageUsersClicked;
    private Double percentageClicksToOpens;
}
