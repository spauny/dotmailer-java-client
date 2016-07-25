package org.spauny.joy.dotmailer.vo.api;

import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class CampaignInfo {

    private Long id;
    private String name;
    private String subject;
    private String fromName;
    private FromAddress fromAddress;
    private String htmlContent;
    private String plainTextContent;
    private String replyAction;
    private String replyToAddress;
    private Boolean isSplitTest;
    private String status;
}
