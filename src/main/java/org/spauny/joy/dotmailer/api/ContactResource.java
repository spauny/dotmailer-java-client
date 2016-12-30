package org.spauny.joy.dotmailer.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;
import static org.spauny.joy.dotmailer.api.AbstractResource.DEFAULT_MAX_SELECT;
import org.spauny.joy.dotmailer.util.CsvUtil;
import org.spauny.joy.dotmailer.util.DefaultEndpoints;
import org.spauny.joy.dotmailer.util.PersonalizedContactsProcessFunction;
import org.spauny.joy.dotmailer.vo.api.AddressBook;
import org.spauny.joy.dotmailer.vo.api.Contact;
import org.spauny.joy.dotmailer.vo.api.JobReport;
import org.spauny.joy.dotmailer.vo.api.JobStatus;
import org.spauny.joy.dotmailer.vo.api.PersonalisedContact;
import org.spauny.joy.dotmailer.vo.api.SuppressedContact;
import org.spauny.joy.dotmailer.vo.internal.DMAccessCredentials;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 *
 * @author iulian
 */
@Slf4j
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
        return list(createdSince, withFullData, limit, true);
    }

    /**
     * Gets a list of all contacts in the account created since the passed date
     * @param createdSince
     * @param withFullData
     * @param limit
     * @param roundToDate
     * @return
     */
    public Optional<List<Contact>> list(Date createdSince, boolean withFullData, int limit, boolean roundToDate) {
        String dateTemplate = roundToDate ? DM_DATE_FORMAT : DM_DATE_TIME_FORMAT;
        String rootPath = pathWithParam(DefaultEndpoints.CONTACTS_SINCE_DATE.getPath(), new DateTime(createdSince).toString(dateTemplate));
        String path = addAttrAndValueToPath(rootPath, WITH_FULL_DATA_ATTR, BooleanUtils.toStringTrueFalse(withFullData));
        
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
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, 
            Boolean withFullData, int limit) {
        
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false", "false"));
        
        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
        return sendAndGetFullList(initialPath, clazz, jsonDeserializer, typeToken, maxSelect, limit);
    }
    
    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before and a type token that wraps all this structure, used for type inference.
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This request has no limit set.
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, Boolean withFullData) {
        return listPersonalizedContacts(clazz, jsonDeserializer, typeToken, withFullData, 0);
    }
    
    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before and a type token that wraps all this structure, used for type inference.
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This will automatically return with full data and has no limit set.
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @return
     */
    public <T> Optional<List<PersonalisedContact<T>>> listPersonalizedContacts(Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken) {
        return listPersonalizedContacts(clazz, jsonDeserializer, typeToken, true, 0);
    }
    
    
    /**
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for each bulk list of contacts.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all this structure, used for type inference and provide a process function (callback method) that will be called for each personalized contact
     * Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This will automatically process contacts with full data and has no limit set.
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @return
     */
    public <T> void processFullList(Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, PersonalizedContactsProcessFunction<T> processFunction) {
        processFullList(clazz, jsonDeserializer, typeToken, true, 0, processFunction);
    }
    
    
    /**
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for each bulk list of contacts.
     * To be able to do this, you have to create a details class for your contact, a JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all this structure, used for type inference and provide a process function (callback method) that will be called for each personalized contact
     * Please check github repo's wiki for more info on how to use this amazing method!
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @param processFunction
     * @return
     */
    public <T> void processFullList(Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, boolean withFullData, int limit,
            PersonalizedContactsProcessFunction<T> processFunction) {
        
        // unfortunately there is no way to get a count of account contacts. We'll have to process until we hit bottom :)
        log.info("STARTING TO PROCESS ALL CONTACTS");
        
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false"));
        
        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
        
        int skip = 0;
        
        Optional<List<PersonalisedContact<T>>> contacts;
        do {
            contacts = sendAndGetFullList(initialPath, clazz, jsonDeserializer, typeToken, maxSelect, MAX_CONTACTS_TO_PROCESS_PER_STEP, skip);
            if (contacts.isPresent()) {
                processFunction.accept(contacts.get());
            }
            skip += MAX_CONTACTS_TO_PROCESS_PER_STEP;
        } while (contacts.isPresent() && !contacts.get().isEmpty());
    }
    
    
    
    
    /**
     * Gets a list of unsubscribed contacts who unsubscribed after a given date
     * DEFAULT ATTRS: no limit
     * @param since
     * @return
     */
    public Optional<List<SuppressedContact>> listUnsubscribed(Date since) {
        return listUnsubscribed(since, 0);
    }
    
    /**
     * Gets a list of unsubscribed contacts who unsubscribed after a given date
     * @param since
     * @param limit
     * @return
     */
    public Optional<List<SuppressedContact>> listUnsubscribed(Date since, int limit) {
        String rootPath = pathWithParam(DefaultEndpoints.CONTACTS_UNSUBSCRIBED_SINCE_DATE.getPath(), new DateTime(since).toString(DM_DATE_FORMAT));
        
        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(rootPath, new TypeToken<List<SuppressedContact>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(rootPath, new TypeToken<List<SuppressedContact>>() {});
    }
    
    
    public Optional<List<SuppressedContact>> listSuppressed(Date since) {
        return listSuppressed(since, 0);
    }
    
    
    /**
     * Gets a list of suppressed contacts after a given date along with the reason for suppression.
     * 
     * The possible suppression reasons can be:
        'Unsubscribed' - The contact is unsubscribed from your communications
        'SoftBounced' - The contact’s address is temporarily unavailable, possibly because their mailbox is too full, or their mail server won’t accept a message of the size sent, or their server is having temporary issues accepting any mail
        'HardBounced' - The contact’s address is permanently unreachable, most likely because they, or the server they were hosted on, does not exist
        'IspComplained' - The contact has submitted a spam complaint to us via their internet service provider
        'MailBlocked' - The mail server indicated that it didn’t want to receive the mail. No reason was given
        'DirectComplaint' - The contact has complained directly to either us, a hosting facility or possibly even a blacklist about receiving your communications
        'Suppressed' - The contact that has been actively suppressed by you in your account
        'NotAllowed' - The contact's email address is fully blocked from our system
        'DomainSuppression' - The contact’s email domain is on your domain suppression list
        'NoMxRecord' - The contact’s email domain does not have an MX DNS record. A mail exchange record provides the address of the mail server for that domain.
     * 
     * @param since
     * @param limit
     * @return
     */
    public Optional<List<SuppressedContact>> listSuppressed(Date since, int limit) {
        return listSuppressed(since, limit, true);
    }


    /**
     * Gets a list of suppressed contacts after a given date along with the reason for suppression.
     *
     * The possible suppression reasons can be:
     'Unsubscribed' - The contact is unsubscribed from your communications
     'SoftBounced' - The contact’s address is temporarily unavailable, possibly because their mailbox is too full, or their mail server won’t accept a message of the size sent, or their server is having temporary issues accepting any mail
     'HardBounced' - The contact’s address is permanently unreachable, most likely because they, or the server they were hosted on, does not exist
     'IspComplained' - The contact has submitted a spam complaint to us via their internet service provider
     'MailBlocked' - The mail server indicated that it didn’t want to receive the mail. No reason was given
     'DirectComplaint' - The contact has complained directly to either us, a hosting facility or possibly even a blacklist about receiving your communications
     'Suppressed' - The contact that has been actively suppressed by you in your account
     'NotAllowed' - The contact's email address is fully blocked from our system
     'DomainSuppression' - The contact’s email domain is on your domain suppression list
     'NoMxRecord' - The contact’s email domain does not have an MX DNS record. A mail exchange record provides the address of the mail server for that domain.
     *
     * @param since
     * @param limit
     * @param roundToDate
     * @return
     */
    public Optional<List<SuppressedContact>> listSuppressed(Date since, int limit, boolean roundToDate) {
        String dateTemplate = roundToDate ? DM_DATE_FORMAT : DM_DATE_TIME_FORMAT;
        String rootPath = pathWithParam(DefaultEndpoints.CONTACTS_SUPPRESSED_SINCE_DATE.getPath(), new DateTime(since).toString(dateTemplate));

        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(rootPath, new TypeToken<List<SuppressedContact>>() {}, maxSelect, limit);
        }
        return sendAndGetFullList(rootPath, new TypeToken<List<SuppressedContact>>() {});
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
    
    /**
     * Bulk creates, or bulk updates, contacts.
     * @param <T>
     * @param customContactObjects
     * @return
     */
    public <T> Optional<JobStatus> importList(List<T> customContactObjects) {
        Optional<String> csvFilePath = CsvUtil.writeCsv(customContactObjects);
        if (!csvFilePath.isPresent()) {
            return Optional.empty();
        }
        return postFileAndGet(DefaultEndpoints.CONTACTS_IMPORT.getPath(), csvFilePath.get(), JobStatus.class);
    }
    
    public <T> Optional<JobStatus> importList(List<T> customContactObjects, List<String> csvHeaders) {
        Optional<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders);
        if (!csvFilePath.isPresent()) {
            return Optional.empty();
        }
        return postFileAndGet(DefaultEndpoints.CONTACTS_IMPORT.getPath(), csvFilePath.get(), JobStatus.class);
    }
    
    public <T> Optional<JobStatus> importList(List<T> customContactObjects, List<String> csvHeaders, List<String> fieldNames) {
        Optional<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders, fieldNames);
        if (!csvFilePath.isPresent()) {
            return Optional.empty();
        }
        return postFileAndGet(DefaultEndpoints.CONTACTS_IMPORT.getPath(), csvFilePath.get(), JobStatus.class);
    }
    
    public <T> Optional<JobStatus> importList(List<T> customContactObjects, List<String> csvHeaders, List<String> fieldNames, CellProcessor[] cellProcessors) {
        Optional<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders, fieldNames, cellProcessors);
        if (!csvFilePath.isPresent()) {
            return Optional.empty();
        }
        return postFileAndGet(DefaultEndpoints.CONTACTS_IMPORT.getPath(), csvFilePath.get(), JobStatus.class);
    }
    
    public Optional<JobStatus> getImportStatus(String guid) {
        return sendAndGet(pathWithParam(DefaultEndpoints.CONTACTS_IMPORT_STATUS.getPath(), guid), JobStatus.class);
    }
    
    public Optional<JobReport> getImportReport(String guid) {
        return sendAndGet(pathWithParam(DefaultEndpoints.CONTACTS_IMPORT_REPORT.getPath(), guid), JobReport.class);
    }
}
