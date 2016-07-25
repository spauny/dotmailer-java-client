package org.spauny.joy.dotmailer.api;

import java.util.Optional;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.vo.api.AccountInfo;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;

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
