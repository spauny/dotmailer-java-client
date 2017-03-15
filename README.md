# dotmailer-java-client

Dotmailer API client written in Java.

Some Examples (more to come): 

** Get Account Info **
``` java
Optional<AccountInfo> accountInfo = Dotmailer.build(username, pass).accountInfo().get();
```


** List Address Books or Get a specific Address Book**
``` java
Optional<List<AddressBook>> addressBooks = Dotmailer.build(username, pass).addressBook().list();
Optional<AddressBook> addressBook = Dotmailer.build(username, pass).addressBook().get(addressBookId);
```

** List Address Books Contacts**
``` java
Optional<List<Contact>> addressBooks = Dotmailer.build(username, pass).addressBook().listContacts(addressBookId);
```

Note: Almost all methods return an empty optional object when the account doesn't exist or dotmailer's API couldn't be contacted.
