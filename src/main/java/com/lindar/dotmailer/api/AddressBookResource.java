package com.lindar.dotmailer.api;

import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.lindar.dotmailer.util.CsvUtil;
import com.lindar.dotmailer.util.DefaultEndpoints;
import com.lindar.dotmailer.util.PersonalizedContactsProcessFunction;
import com.lindar.dotmailer.vo.api.AddressBook;
import com.lindar.dotmailer.vo.api.Contact;
import com.lindar.dotmailer.vo.api.JobStatus;
import com.lindar.dotmailer.vo.api.PersonalisedContact;
import com.lindar.dotmailer.vo.api.SuppressedContact;
import com.lindar.dotmailer.vo.internal.DMAccessCredentials;
import com.lindar.wellrested.vo.Result;
import com.lindar.wellrested.vo.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.joda.time.DateTime;
import org.supercsv.cellprocessor.ift.CellProcessor;

import java.util.Date;
import java.util.List;

@Slf4j
public class AddressBookResource extends AbstractResource {

    public AddressBookResource(DMAccessCredentials accessCredentials) {
        super(accessCredentials);
    }

    public Result<List<AddressBook>> list() {
        return sendAndGetFullList(DefaultEndpoints.ADDRESS_BOOKS.getPath(), new TypeToken<List<AddressBook>>() {});
    }

    public Result<AddressBook> get(Long addressBookId) {
        String path = pathWithId(DefaultEndpoints.ADDRESS_BOOK.getPath(), addressBookId);
        return sendAndGet(path, AddressBook.class);
    }

    public Result<AddressBook> create(AddressBook addressBook) {
        String path = DefaultEndpoints.ADDRESS_BOOKS.getPath();
        return postAndGet(path, addressBook);
    }

    public Result<Contact> addContact(Long addressBookId, Contact contact) {
        String path = pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), addressBookId);
        return postAndGet(path, contact);
    }

    public boolean deleteContacts(Long addressBookId, List<Long> contactIds) {
        String path = pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_DELETE.getPath(), addressBookId);
        return post(path, contactIds) == HttpStatus.SC_NO_CONTENT; // http 204 no content response
    }

    /**
     * List address book contacts. DEFAULT ATTRS: No limit and without full data
     */
    public Result<List<Contact>> listContacts(Long addressBookId) {
        return listContacts(addressBookId, false, 0);
    }

    public Result<List<Contact>> listContacts(Long addressBookId, Boolean withFullData) {
        return listContacts(addressBookId, withFullData, 0);
    }

    public Result<List<Contact>> listContacts(Long addressBookId, Boolean withFullData, int limit) {
        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false", "false"));
        String path = pathWithId(initialPath, addressBookId);

        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(path, new TypeToken<List<Contact>>() {
            }, maxSelect, limit);
        }
        return sendAndGetFullList(path, new TypeToken<List<Contact>>() {
        });
    }

    /**
     * Gets a list of contacts who have unsubscribed since a given date from a given address book. No limit
     */
    public Result<List<SuppressedContact>> listUnsubscribedContacts(Long addressBookId, Date since) {
        return listUnsubscribedContacts(addressBookId, since, 0);
    }

    /**
     * Gets a list of contacts who have unsubscribed since a given date from a given address book
     *
     * @param addressBookId
     * @param since
     * @param limit
     * @return
     */
    public Result<List<SuppressedContact>> listUnsubscribedContacts(Long addressBookId, Date since, int limit) {
        String path = pathWithIdAndParam(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_UNSUBSCRIBED_SINCE_DATE.getPath(), addressBookId, new DateTime(since).toString(DM_DATE_FORMAT));

        if (limit > 0) {
            int maxSelect = limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
            return sendAndGetFullList(path, new TypeToken<List<SuppressedContact>>() {
            }, maxSelect, limit);
        }
        return sendAndGetFullList(path, new TypeToken<List<SuppressedContact>>() {});
    }

    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the
     * dataFields map (key, value) returned by DotMailer for each contact. To be able to do this, you have to create a
     * details class for your contact, a JsonDeserializer object where your build the object of the class mentioned
     * before and a type token that wraps all this structure, used for type inference. Please check github repo's wiki
     * for more info on how to use this amazing method!
     *
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @return
     */
    public <T> Result<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken,
            Boolean withFullData, int limit) {

        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toString(withFullData, "true", "false", "false"));
        String path = pathWithId(initialPath, addressBookId);

        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;
        return sendAndGetFullList(path, clazz, jsonDeserializer, typeToken, maxSelect, limit);
    }

    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the
     * dataFields map (key, value) returned by DotMailer for each contact. To be able to do this, you have to create a
     * details class for your contact, a JsonDeserializer object where your build the object of the class mentioned
     * before and a type token that wraps all this structure, used for type inference. Please check github repo's wiki
     * for more info on how to use this amazing method! DEFAULT ATTRS: This request has no limit set.
     *
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @return
     */
    public <T> Result<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, Boolean withFullData) {
        return listPersonalizedContacts(addressBookId, clazz, jsonDeserializer, typeToken, withFullData, 0);
    }

    /**
     * This is a very powerful method that allows you to create a list of personalized contacts by deserializing the
     * dataFields map (key, value) returned by DotMailer for each contact. To be able to do this, you have to create a
     * details class for your contact, a JsonDeserializer object where your build the object of the class mentioned
     * before and a type token that wraps all this structure, used for type inference. Please check github repo's wiki
     * for more info on how to use this amazing method! DEFAULT ATTRS: This will automatically return with full data and
     * has no limit set.
     *
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @return
     */
    public <T> Result<List<PersonalisedContact<T>>> listPersonalizedContacts(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken) {
        return listPersonalizedContacts(addressBookId, clazz, jsonDeserializer, typeToken, true, 0);
    }

    /**
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the
     * dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for
     * each bulk list of contacts. To be able to do this, you have to create a details class for your contact, a
     * JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all
     * this structure, used for type inference and provide a process function (callback method) that will be called for
     * each personalized contact Please check github repo's wiki for more info on how to use this amazing method!
     * DEFAULT ATTRS: This will automatically process contacts with full data and has no limit set.
     *
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
     * This is a very powerful method that allows you to process a list of personalized contacts by deserializing the
     * dataFields map (key, value) returned by DotMailer for each contact and apply a processFunction (callback) for
     * each bulk list of contacts. To be able to do this, you have to create a details class for your contact, a
     * JsonDeserializer object where your build the object of the class mentioned before, a type token that wraps all
     * this structure, used for type inference and provide a process function (callback method) that will be called for
     * each personalized contact Please check github repo's wiki for more info on how to use this amazing method!
     *
     * @param addressBookId
     * @param clazz
     * @param jsonDeserializer
     * @param typeToken
     * @param withFullData
     * @param limit
     * @param processFunction
     * @return
     */
    public <T> Result processFullList(Long addressBookId, Class<T> clazz, JsonDeserializer<T> jsonDeserializer, TypeToken<List<PersonalisedContact<T>>> typeToken, boolean withFullData, int limit,
            PersonalizedContactsProcessFunction<T> processFunction) {

        Result<AddressBook> addressBookResult = get(addressBookId);
        if(!addressBookResult.isSuccessAndNotNull()){
            return ResultBuilder.of(addressBookResult).buildAndIgnoreData();
        }

        AddressBook addressBook = addressBookResult.getData();
        int nrOfContacts = addressBook.getContacts();

        log.info("STARTING TO PROCESS A TOTAL OF {} CONTACTS", nrOfContacts);

        String initialPath = addAttrAndValueToPath(DefaultEndpoints.ADDRESS_BOOK_CONTACTS.getPath(), WITH_FULL_DATA_ATTR, BooleanUtils.toStringTrueFalse(withFullData));
        String path = pathWithId(initialPath, addressBookId);

        int maxSelect = limit <= 0 || limit >= DEFAULT_MAX_SELECT ? DEFAULT_MAX_SELECT : limit;

        int skip = 0;

        do {
            Result<List<PersonalisedContact<T>>> contacts = sendAndGetFullList(path, clazz, jsonDeserializer, typeToken, maxSelect, MAX_CONTACTS_TO_PROCESS_PER_STEP, skip);
            contacts.ifSuccessAndNotNull(processFunction);
            skip += MAX_CONTACTS_TO_PROCESS_PER_STEP;
        } while (nrOfContacts > skip);

        return ResultBuilder.successfulWithoutData("Success");
    }

    /**
     * Bulk creates, or bulk updates, contacts.
     *
     * @param <T>
     * @param addressBookId
     * @param customContactObjects
     * @return
     */
    public <T> Result<JobStatus> importList(Long addressBookId, List<T> customContactObjects) {
        Result<String> csvFilePath = CsvUtil.writeCsv(customContactObjects);
        if (!csvFilePath.isSuccessAndNotNull()) {
            return ResultBuilder.of(csvFilePath).buildAndIgnoreData();
        }
        return postFileAndGet(pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_IMPORT.getPath(), addressBookId), csvFilePath.getData(), JobStatus.class);
    }

    public <T> Result<JobStatus> importList(Long addressBookId, List<T> customContactObjects, List<String> csvHeaders) {
        Result<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders);
        if (!csvFilePath.isSuccessAndNotNull()) {
            return ResultBuilder.of(csvFilePath).buildAndIgnoreData();
        }
        return postFileAndGet(pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_IMPORT.getPath(), addressBookId), csvFilePath.getData(), JobStatus.class);
    }

    public <T> Result<JobStatus> importList(Long addressBookId, List<T> customContactObjects, List<String> csvHeaders, List<String> fieldNames) {
        Result<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders, fieldNames);
        if (!csvFilePath.isSuccessAndNotNull()) {
            return ResultBuilder.of(csvFilePath).buildAndIgnoreData();
        }
        return postFileAndGet(pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_IMPORT.getPath(), addressBookId), csvFilePath.getData(), JobStatus.class);
    }

    public <T> Result<JobStatus> importList(Long addressBookId, List<T> customContactObjects, List<String> csvHeaders, List<String> fieldNames, CellProcessor[] cellProcessors) {
        Result<String> csvFilePath = CsvUtil.writeCsv(customContactObjects, csvHeaders, fieldNames, cellProcessors);
        if (!csvFilePath.isSuccessAndNotNull()) {
            return ResultBuilder.of(csvFilePath).buildAndIgnoreData();
        }
        return postFileAndGet(pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_IMPORT.getPath(), addressBookId), csvFilePath.getData(), JobStatus.class);
    }

    public <T> Result<JobStatus> importList(Long addressBookId, String csvFilePath) {
        return postFileAndGet(pathWithId(DefaultEndpoints.ADDRESS_BOOK_CONTACTS_IMPORT.getPath(), addressBookId), csvFilePath, JobStatus.class);
    }
}
