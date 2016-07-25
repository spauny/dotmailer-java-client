package org.spauny.joy.dotmailer.vo.api;

import java.util.List;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class NewContact {
    private String email;
    private String optInType;
    private String emailType;
    
    private List<KeyVal> dataFields;
}
