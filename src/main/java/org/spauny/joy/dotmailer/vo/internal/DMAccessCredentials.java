package org.spauny.joy.dotmailer.vo.internal;

import lombok.Data;

/**
 *
 * @author iulian
 */
@Data
public class DMAccessCredentials {
    private String apiUrl;
    private String version;
    
    private String username;
    private String password;
}
