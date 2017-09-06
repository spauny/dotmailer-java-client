package com.lindar.dotmailer.api;

import com.lindar.dotmailer.util.DefaultEndpoints;
import com.lindar.dotmailer.vo.api.AccountInfo;
import com.lindar.dotmailer.vo.internal.DMAccessCredentials;
import com.lindar.wellrested.vo.Result;

import java.util.Optional;

public class AccountInfoResource extends AbstractResource {
    
    private String path;
    
    public AccountInfoResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
        this.path = DefaultEndpoints.ACCOUNT_INFO.getPath();
    }
    
    public Result<AccountInfo> get() {
        return sendAndGet(path, AccountInfo.class);
    }
}
