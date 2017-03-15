package com.lindar.dotmailer.vo.api;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class CampaignContactActivity {

    private Long contactId;
    private String email;
    private Integer numOpens;
    private Integer numPageViews;
    private Integer numClicks;
    private Integer numForwards;
    private Integer numEstimatedForwards;
    private Integer numReplies;
    private Date dateSent;
    private Date dateFirstOpened;
    private Date dateLastOpened;
    private String firstOpenIp;
    private Boolean unsubscribed;
    private Boolean softBounced;
    private Boolean hardBounced;
}
