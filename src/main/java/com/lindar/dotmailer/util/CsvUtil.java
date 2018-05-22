package com.lindar.dotmailer.util;

import com.lindar.wellrested.vo.Result;
import com.lindar.wellrested.vo.ResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CsvUtil {
    private static final String GET_METHOD_PREFIX = "get";
    private static final String IS_METHOD_PREFIX = "is";

    private static final String CSV_FILE_NAME = "dotmailerContacts-%s.csv";

    private static final String ERROR_CSV = "ERROR_CSV";



    public static <T> Result<String> writeCsv(final List<T> objectsToWrite) {
        return writeCsv(objectsToWrite, null, null, null);
    }

    public static <T> Result<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders) {
        return writeCsv(objectsToWrite, csvHeaders, null, null);
    }

    public static <T> Result<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders, List<String> fieldNames) {
        return writeCsv(objectsToWrite, csvHeaders, fieldNames, null);
    }

    public static <T> Result<String> writeCsv(final List<T> objectsToWrite, List<String> csvHeaders, List<String> fieldNames, final CellProcessor[] processors) {
        if (objectsToWrite == null || objectsToWrite.isEmpty() || objectsToWrite.get(0) == null) {
            log.warn("Nothing to write. The list of objects is empty or null elements provided");
            return ResultBuilder.failed().msg("List is empty, nothing to write").code(ERROR_CSV).buildAndIgnoreData();
        }

        if (fieldNames == null || fieldNames.isEmpty()) {
            T firstObjectToWrite = objectsToWrite.get(0);
            fieldNames = listAllVariablesWithGettersIgnoreGetClass(firstObjectToWrite);
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
            return ResultBuilder.successful(filePath);
        } catch (IOException e) {
            log.error("Error occured while writing csv file: {}", e);
            return ResultBuilder.failed().msg(e.getMessage()).code(ERROR_CSV).buildAndIgnoreData();
        }
    }
    
    public static <T> List<String> listAllVariablesWithGettersIgnoreGetClass(T object) {
        return listAllVariablesWithGetters(object, Arrays.asList("class"));
    }
    
    public static <T> List<String> listAllVariablesWithGetters(T object, List<String> namesToIgnore) {
        Method[] objMethods = object.getClass().getMethods();
        List<String> names = new ArrayList<>(objMethods.length);
        for (Method firstObjMethod : objMethods) {
            String objMethodName = firstObjMethod.getName();
            String objStrippedMethodName;
            if (objMethodName.startsWith(GET_METHOD_PREFIX)) {
                objStrippedMethodName = objMethodName.substring(objMethodName.indexOf(GET_METHOD_PREFIX) + GET_METHOD_PREFIX.length());
            } else if (objMethodName.startsWith(IS_METHOD_PREFIX)) {
                objStrippedMethodName = objMethodName.substring(objMethodName.indexOf(IS_METHOD_PREFIX) + IS_METHOD_PREFIX.length());
            } else {
                continue;
            }
            String uncapitalizedName = StringUtils.uncapitalize(objStrippedMethodName);
            if (namesToIgnore == null || !namesToIgnore.contains(uncapitalizedName)) {
                names.add(uncapitalizedName);
            }
        }
        return names;
    }

}
