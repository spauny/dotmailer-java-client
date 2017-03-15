package com.lindar.dotmailer.vo.api;

import java.util.List;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class Contact {
    
    private String id;
    private String status;
    
    private String email;
    private String optInType;
    private String emailType;
    
    private List<KeyVal> dataFields;
}
