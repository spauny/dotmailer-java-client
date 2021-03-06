package com.lindar.dotmailer.vo.internal;

import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class ContactDetails implements ContactDetailsInterface {
    
    private String ref;
    private String uid;
    private String firstName;
    private String lastName;
    private String fullName;
    private String username;
    private String postcode;
}
