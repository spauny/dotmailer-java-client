package org.spauny.joy.dotmailer.util;

import lombok.Getter;

/**
 *
 * @author iulian
 */
public enum DefaultEndpoints {
    API_URL ("https://r1-api.dotmailer.com"),
    VERSION ("/v2"),
    
    ACCOUNT_INFO ("/account-info"),
    
    CAMPAIGNS ("/campaigns"),
    CAMPAIGNS_WITH_ACTIVITY_SINCE("/campaigns/with-activity-since/%s"),
    CAMPAIGN_INFO ("/campaigns/%s"),
    CAMPAIGN_SUMMARY ("/campaigns/%s/summary"),
    CAMPAIGN_ACTIVITY ("/campaigns/%s/activities"),
    CAMPAIGN_ACTIVITY_SINCE ("/campaigns/%s/activities/since-date/%s"),

    ADDRESS_BOOKS ("/address-books"),
    ADDRESS_BOOK ("/address-books/%s"),
    ADDRESS_BOOK_CONTACTS ("/address-books/%s/contacts"),
    ADDRESS_BOOK_CONTACTS_UNSUBSCRIBED_SINCE_DATE ("/address-books/%s/contacts/unsubscribed-since/%s"),
    
    CONTACTS ("/contacts"),
    CONTACT ("/contacts/%s"),
    CONTACT_ADDRESS_BOOKS ("/contacts/%s/address-books"),
    CONTACTS_SINCE_DATE ("/contacts/created-since/%s"),
    CONTACTS_UNSUBSCRIBED_SINCE_DATE ("/contacts/unsubscribed-since/%s"),
    CONTACTS_SUPPRESSED_SINCE_DATE ("/contacts/suppressed-since/%s"),
    CONTACTS_IMPORT ("/contacts/import"),
    CONTACTS_IMPORT_STATUS ("/contacts/import/%s"),
    CONTACTS_IMPORT_REPORT ("/contacts/import/%s/report"),
    CONTACTS_IMPORT_REPORT_FAULTS ("/contacts/import/%s/report-faults");
    
    
    @Getter
    private String path;
    
    private DefaultEndpoints(String path) {
        this.path = path;
    }
}
