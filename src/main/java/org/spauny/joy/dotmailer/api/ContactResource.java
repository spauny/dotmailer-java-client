package org.spauny.joy.dotmailer.api;

import com.google.common.reflect.TypeToken;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import static org.spauny.joy.dotmailer.api.AbstractResource.DEFAULT_MAX_SELECT;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.vo.api.AddressBook;
import org.spauny.joy.dotmailer.vo.api.Contact;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;

/**
 *
 * @author iulian
 */
public class ContactResource extends AbstractResource {
    
    public ContactResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
    }
    
    public Optional<Contact> get(Long id) {
        return sendAndGet(pathWithId(DefaultEndpoints.CONTACT.getPath(), id), Contact.class);
    }
    
    public Optional<Contact> get(String email) {
        return sendAndGet(pathWithParam(DefaultEndpoints.CONTACT.getPath(), email), Contact.class);
    }
    
    /**
     * Gets a list of all contacts in the account
     * DEFAULT ATTR: No limit set and without full data
     * @return
     */
    public Optional<List<Contact>> list() {
        return list(false, 0);
    }
    
    /**
     * Gets a list of all contacts in the account
     * DEFAULT ATTR: No limit set
     * @param withFullData
     * @return
     */
    public Optional<List<Contact>> list(boolean withFullData) {
        return list(withFullData, 0);
    }
    
    public Optional<List<Contact>> list(boolean withFullData, int limit) {
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toStringTrueFalse(withFullData));
        
        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(initialPath, new TypeToken<List<Contact>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(initialPath, new TypeToken<List<Contact>>() {});
    }
    
    /**
     * Gets a list of all contacts in the account created since the passed date
     * DEFAULT ATTR: No limit set and without full data
     * @param createdSince
     * @return
     */
    public Optional<List<Contact>> list(Date createdSince) {
        return list(createdSince, false, 0);
    }
    
    /**
     * Gets a list of all contacts in the account created since the passed date
     * DEFAULT ATTR: No limit set
     * @param createdSince
     * @param withFullData
     * @return
     */
    public Optional<List<Contact>> list(Date createdSince, boolean withFullData) {
        return list(createdSince, withFullData, 0);
    }
    
    /**
     * Gets a list of all contacts in the account created since the passed date
     * @param createdSince
     * @param withFullData
     * @param limit
     * @return
     */
    public Optional<List<Contact>> list(Date createdSince, boolean withFullData, int limit) {
        String rootPath = pathWithParam(DefaultEndpoints.CONTACTS_SINCE_DATE.getPath(), new DateTime(createdSince).toString(DM_DATE_FORMAT));
        String path = addAttrAndValueToPath(rootPath, WITH_FULL_DATA_ATTR, BooleanUtils.toStringTrueFalse(withFullData));
        
        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(path, new TypeToken<List<Contact>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(path, new TypeToken<List<Contact>>() {});
    }
    
    /**
     * Gets any address books that a contact is in.
     * DEFAULT ATTR: No limit set
     * @param contactId
     * @return
     */
    public Optional<List<AddressBook>> listAddressBooks(Long contactId) {
        return listAddressBooks(contactId, 0);
    }
    
    /**
     * Gets any address books that a contact is in.
     * @param contactId
     * @param limit
     * @return
     */
    public Optional<List<AddressBook>> listAddressBooks(Long contactId, int limit) {
        String path = pathWithId(DefaultEndpoints.CONTACT_ADDRESS_BOOKS.getPath(), contactId);
        
        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(path, new TypeToken<List<AddressBook>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(path, new TypeToken<List<AddressBook>>() {});
    }
    
    
    public Optional<Contact> create(Contact newContact) {
        return postAndGet(DefaultEndpoints.CONTACTS.getPath(), newContact);
    }
    
    public Optional<Contact> update(Contact updatedContact) {
        return putAndGet(DefaultEndpoints.CONTACTS.getPath(), updatedContact);
    }
    
    public boolean delete(Long contactId) {
        return delete(pathWithId(DefaultEndpoints.CONTACT.getPath(), contactId));
    }
}
