package org.spauny.joy.dotmailer.vo.api;

import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class AddressBook {
    
    private long id;
    private String name;
    private String visibility;
    private int contacts;
}
