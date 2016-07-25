package org.spauny.joy.dotmailer.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.util.PersonalizedContactsProcessFunction;
import org.spauny.joy.dotmailer.vo.api.AddressBook;
import org.spauny.joy.dotmailer.vo.api.Contact;
import org.spauny.joy.dotmailer.vo.api.PersonalisedContact;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;

/**
 *
 * @author iulian
 */
@Slf4j
public class AddressBookResource extends AbstractResource {
    private static final int MAX_CONTACTS_TO_PROCESS_PER_STEP = 50000;
    
    public AddressBookResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
    }
    
    public Optional<List<AddressBook>> list() {
        return sendAndGetFullList(DefaultEndpoints.ADDRESS_BOOKS.getPath(), new TypeToken<List<AddressBook>>() {});
    }
    
    public Optional<AddressBook> get(Long addressBookId) {
        String path = pathWithId(DefaultEndpoints.ADDRESS_BOOK.getPath(), addressBookId);
        return sendAndGet(path, AddressBook.class);
    }
    
    public Optional<List<Contact>> listContacts(Long addressBookId, Boolean withFullData) {
        return listContacts(addressBookId, withFullData, 0);
    }
    
    public Optional<List<Contact>> listContacts(Long addressBookId, Boolean withFullData, int limit) {
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false", "false"));
        String path = pathWithId(initialPath, addressBookId);
        
        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(path, new TypeToken<List<Contact>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(path, new TypeToken<List<Contact>>() {});
    }
    
    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before and a type token that wraps all this structure, used for type inference.
     * Please check github repo's wiki for more info on how to use this amazing method!
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, 
            Boolean withFullData, int limit) {
        
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false", "false"));
        String path = pathWithId(initialPath, addressBookId);
        
        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
        return sendAndGetFullList(path, clazz, jsonDeserializer, typeToken, maxSelect, limit);
    }
    
    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before and a type token that wraps all this structure, used for type inference.
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This request has no limit set.
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, Boolean withFullData) {
        return listPersonalizedContacts(addressBookId, clazz, jsonDeserializer, typeToken, withFullData, 0);
    }
    
    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before and a type token that wraps all this structure, used for type inference.
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This will automatically return with full data and has no limit set.
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken) {
        return listPersonalizedContacts(addressBookId, clazz, jsonDeserializer, typeToken, true, 0);
    }
    
    
    /**
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all this structure, used for type inference and provide a process function (callback method) that will be called for each personalized contact
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This will automatically process contacts with full data and has no limit set.
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @return
     */
    public <T> void processFullList(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, PersonalizedContactsProcessFunction<T> processFunction) {
        processFullList(addressBookId, clazz, jsonDeserializer, typeToken, true, 0, processFunction);
    }
    
    
    /**
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all this structure, used for type inference and provide a process function (callback method) that will be called for each personalized contact
     * Please check github repo's wiki for more info on how to use this amazing method!
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @return
     */
    public <T> void processFullList(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, boolean withFullData, int limit,
            PersonalizedContactsProcessFunction<T> processFunction) {
        AddressBook addressBook = get(addressBookId).orElseThrow(() -> new IllegalArgumentException("Address book doesn't exist!"));
        int nrOfContacts = addressBook.getContacts();
        
        log.info("STARTING TO PROCESS A TOTAL OF {} CONTACTS", nrOfContacts);
        
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false"));
        String path = pathWithId(initialPath, addressBookId);
        
        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
        
        int skip = 0;
        
        do {
            Optional<List<PersonalisedContact<T>>> contacts = sendAndGetFullList(path, clazz, jsonDeserializer, typeToken, maxSelect, MAX_CONTACTS_TO_PROCESS_PER_STEP, skip);
            if (contacts.isPresent()) {
                processFunction.accept(contacts.get());
            }
            skip += MAX_CONTACTS_TO_PROCESS_PER_STEP;
        } while (nrOfContacts > skip);
    }
    
}
