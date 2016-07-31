package org.spauny.joy.dotmailer;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.spauny.joy.dotmailer.api.AccountInfoResource;
import org.spauny.joy.dotmailer.api.AddressBookResource;
import org.spauny.joy.dotmailer.api.CampaignResource;
import org.spauny.joy.dotmailer.api.ContactResource;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.util.InvalidAccountException;
import org.spauny.joy.dotmailer.vo.api.AccountInfo;
import org.spauny.joy.dotmailer.vo.api.DMProperty;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;

/**
 * Dotmailer API Client Facade
 *
 * @author Iulian Dafinoiu
 */
@Slf4j
public class Dotmailer {

    private static final String API_ENDPOINT_PROP = "ApiEndpoint";
    private static final String INVALID_ACCOUNT_MSG = "No account info could be found using the provided credentials. Please check and try again";

    private DMAccessCredentials accessCredentials;

    private AccountInfoResource accountInfoResource;
    private AddressBookResource addressBookResource;
    private ContactResource contactResource;
    private CampaignResource campaignResource;

    private Dotmailer(DMAccessCredentials accessCredentials) {
        this.accessCredentials = accessCredentials;
        this.accountInfoResource = new AccountInfoResource(accessCredentials);
        this.addressBookResource = new AddressBookResource(accessCredentials);
        this.contactResource = new ContactResource(accessCredentials);
        this.campaignResource = new CampaignResource(accessCredentials);
    }

    /**
     * Builds Dotmailer API Client Facade using default API url and version. Default API URL:
     * https://r1-api.dotmailer.com Default API version: /v2
     * <b>PLEASE NOTE:</b> If you do not know your API url then use the
     * <b>autobuild</b> method to automatically detect your account's regional API
     *
     * @param username
     * @param password
     * @return
     */
    public final static Dotmailer build(String username, String password) {
        DMAccessCredentials dMAccessCredentials = new DMAccessCredentials();
        dMAccessCredentials.setApiUrl(DefaultEndpoints.API_URL.getPath());
        dMAccessCredentials.setVersion(DefaultEndpoints.VERSION.getPath());
        dMAccessCredentials.setUsername(username);
        dMAccessCredentials.setPassword(password);
        return new Dotmailer(dMAccessCredentials);
    }

    /**
     * Builds Dotmailer API Client Facade using account info to automatically detect your regional API url. Uses /v2 as
     * default API version
     * <b>PLEASE NOTE:</b> If you already know your API url use the appropriate
     * <b>build</b> method: Use standard build for default URL: https://r1-api.dotmailer.com or build with custom API
     * URL
     *
     * @param username
     * @param password
     * @return
     * @throws org.spauny.joy.dotmailer.util.InvalidAccountException
     */
    public final static Dotmailer autobuild(String username, String password) throws InvalidAccountException {
        Dotmailer dotmailer = Dotmailer.build(username, password);
        Optional<AccountInfo> accountInfo = dotmailer.accountInfo().get();
        if (accountInfo.isPresent()) {
            Optional<DMProperty> apiEndpointProp = accountInfo.get().getProperties().stream()
                    .filter(prop -> API_ENDPOINT_PROP.equals(prop.getName()))
                    .findAny();
            if (apiEndpointProp.isPresent() && StringUtils.isNotBlank(apiEndpointProp.get().getValue())) {
                dotmailer.accessCredentials.setApiUrl(apiEndpointProp.get().getValue());
                return dotmailer;
            }
            throw new InvalidAccountException(INVALID_ACCOUNT_MSG);
        }
        throw new InvalidAccountException(INVALID_ACCOUNT_MSG);
    }

    /**
     * Builds Dotmailer API Client Facade using custom API url and default version. Default API version: /v2
     *
     * @param username
     * @param password
     * @param customAPIUrl
     * @return
     */
    public final static Dotmailer build(String username, String password, String customAPIUrl) {
        DMAccessCredentials dMAccessCredentials = new DMAccessCredentials();
        dMAccessCredentials.setApiUrl(customAPIUrl);
        dMAccessCredentials.setVersion(DefaultEndpoints.VERSION.getPath());
        dMAccessCredentials.setUsername(username);
        dMAccessCredentials.setPassword(password);
        return new Dotmailer(dMAccessCredentials);
    }

    /**
     * Builds Dotmailer API Client Facade using custom API url and custom version.
     *
     * @param username
     * @param password
     * @param customAPIUrl
     * @param customVersion
     * @return
     */
    public final static Dotmailer build(String username, String password, String customAPIUrl, String customVersion) {
        DMAccessCredentials dMAccessCredentials = new DMAccessCredentials();
        dMAccessCredentials.setApiUrl(customAPIUrl);
        dMAccessCredentials.setVersion(customVersion);
        dMAccessCredentials.setUsername(username);
        dMAccessCredentials.setPassword(password);
        return new Dotmailer(dMAccessCredentials);
    }

    /**
     * Returns an account info resource that allows you to interact with all account info endpoints
     *
     * @return
     */
    public AccountInfoResource accountInfo() {
        return accountInfoResource;
    }

    /**
     * returns an account info resource that allows you to interact with all account info endpoints
     *
     * @return
     */
    public AddressBookResource addressBook() {
        return addressBookResource;
    }

    /**
     * returns a contact resource that allows you to interact with all contact endpoints
     *
     * @return
     */
    public ContactResource contact() {
        return contactResource;
    }

    /**
     * returns a campaign resource that allows you to interact with all campaign endpoints
     *
     * @return
     */
    public CampaignResource campaign() {
        return campaignResource;
    }

}
