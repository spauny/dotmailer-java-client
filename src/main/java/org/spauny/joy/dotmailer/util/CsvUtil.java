package org.spauny.joy.dotmailer.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.spauny.joy.dotmailer.vo.api.CsvContact;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author iulian
 */
@Slf4j
public class CsvUtil {

    private static final String ROOT_PATH = "./";
    private static final String CSV_FILE_NAME = "dotmailerContacts-%s.csv";

    public static <T> Optional<String> writeCsv(final List<T> objectsToWrite) {
        return writeCsv(objectsToWrite, null, null, null);
    }

    public static <T> Optional<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders) {
        return writeCsv(objectsToWrite, csvHeaders, null, null);
    }

    public static <T> Optional<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders, List<String> fieldNames) {
        return writeCsv(objectsToWrite, csvHeaders, fieldNames, null);
    }

    /**
     * An example of writing using CsvBeanWriter.
     */
    public static <T> Optional<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders, List<String> fieldNames, final CellProcessor[] processors) {
        if (objectsToWrite == null || objectsToWrite.isEmpty() || objectsToWrite.get(0) == null) {
            log.warn("Nothing to write. The list of objects is empty or null elements provided");
            return Optional.empty();
        }

        if (fieldNames == null || fieldNames.isEmpty()) {
            T firstObjectToWrite = objectsToWrite.get(0);
            fieldNames = ObjectsUtil.listAllVariablesWithGettersIgnoreGetClass(firstObjectToWrite);
        }

        if (csvHeaders == null || csvHeaders.isEmpty()) {
            csvHeaders = fieldNames;
        }

        // generate a unique file name everytime for parallel processing
        String filePath = System.getProperty("user.home") + File.separator + String.format(CSV_FILE_NAME, RandomStringUtils.random(10, true, true));
        try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(filePath), CsvPreference.STANDARD_PREFERENCE)) {

            // write the header
            beanWriter.writeHeader(csvHeaders.toArray(new String[]{}));

            String[] fieldNamesArray = fieldNames.toArray(new String[]{});

            // write the beans
            for (final T obj : objectsToWrite) {
                if (processors != null) {
                    beanWriter.write(obj, fieldNamesArray, processors);
                } else {
                    beanWriter.write(obj, fieldNamesArray);
                }
            }
            return Optional.of(filePath);
        } catch (IOException e) {
            log.error("Error occured while writing csv file: {}", e);
        }
        return Optional.empty();
    }

    public static void main(String args[]) throws Exception {
        // create the customer beans
        final CsvContact john = new CsvContact();
        john.setId("jhfasjkfkgaskgjkas");
        john.setEmail("john@gmail.com");
        john.setRef("gjkshjds");

        final CsvContact bob = new CsvContact();
        bob.setId("f564as");
        bob.setEmail("bob@gmail.com");
        bob.setRef("523564654dsa");

        final List<CsvContact> contacts = Lists.newArrayList(john, bob);

        for (int i = 0; i < 50000; i++) {
            CsvContact generated = new CsvContact();
            generated.setId(RandomStringUtils.random(50, true, true));
            generated.setEmail(RandomStringUtils.random(30, true, true));
            generated.setRef(RandomStringUtils.random(30, true, true));
            contacts.add(generated);
        }

        // the header elements are used to map the bean values to each column (names must match)
//        final List<String> headers = Lists.newArrayList("ID", "EMAIL", "REF_NAME");
//        final List<String> fieldNames = Lists.newArrayList("id", "email", "ref");

        final CellProcessor[] processors = new CellProcessor[]{
            new UniqueHashCode(), // customerNo (must be unique)
            new NotNull(), // firstName
            new NotNull(), // lastName
        //            new FmtDate("dd/MM/yyyy"), // birthDate
        //            new NotNull(), // mailingAddress
        //            new Optional(new FmtBool("Y", "N")), // married
        //            new Optional(), // numberOfKids
        //            new NotNull(), // favouriteQuote
        //            new NotNull(), // email
        //            new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
        };

//        Optional<String> writtenFile = writeCsv(contacts);
//        if (writtenFile.isPresent() && writtenFile.get().exists()) {
//            //writtenFile.get().delete();
//        }
    }
}
