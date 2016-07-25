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
    CAMPAIGN_INFO ("/campaigns/%s"),
    CAMPAIGN_SUMMARY ("/campaigns/%s/summary"),
    CAMPAIGN_ACTIVITY ("/campaigns/%s/activities"),
    
    ADDRESS_BOOKS ("/address-books"),
    ADDRESS_BOOK ("/address-books/%s"),
    ADDRESS_BOOK_CONTACTS ("/address-books/%s/contacts"),
    
    CONTACTS ("/contacts"),
    CONTACT ("/contacts/%s"),
    CONTACT_ADDRESS_BOOKS ("/contacts/%s/address-books"),
    CONTACTS_SINCE_DATE ("/contacts/created-since/%s");
    
    
    @Getter
    private String path;
    
    private DefaultEndpoints(String path) {
        this.path = path;
    }
}
