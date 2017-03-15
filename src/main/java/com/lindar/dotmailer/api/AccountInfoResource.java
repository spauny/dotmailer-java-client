package com.lindar.dotmailer.api;

import java.util.Optional;
import com.lindar.dotmailer.util.DefaultEndpoints;
import com.lindar.dotmailer.vo.api.AccountInfo;
import com.lindar.dotmailer.vo.internal.DMAccessCredentials;

/**
 *
 * @author iulian
 */
public class AccountInfoResource extends AbstractResource {
    
    private String path;
    
    public AccountInfoResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
        this.path = DefaultEndpoints.ACCOUNT_INFO.getPath();
    }
    
    public Optional<AccountInfo> get() {
        return sendAndGet(path, AccountInfo.class);
    }
}
