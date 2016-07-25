package org.spauny.joy.dotmailer.vo.api;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class PersonalisedContact<T> {
    
    private String id;
    private String email;
    private String optInType;
    private String emailType;
    private String status;
    
    @SerializedName("dataFields")
    private T details;
}
