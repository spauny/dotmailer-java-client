package com.lindar.dotmailer.api;

import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import com.lindar.dotmailer.vo.internal.DMAccessCredentials;
import com.lindar.wellrested.WellRestedRequest;
import com.lindar.wellrested.vo.WellRestedResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractResource {

    protected static final String WITH_FULL_DATA_ATTR = "withFullData";
    protected static final String DM_DATE_FORMAT = "yyyy-MM-dd";
    protected static final String DM_DATE_TIME_FORMAT = "yyyy-MM-dd%20HH:mm:ss";

    private static final String DM_SELECT_SKIP_ATTRIBUTES = "select=%s&skip=%s";
    private static final String AND = "&";
    private static final String QUESTION = "?";
    private static final String EQUAL = "=";
    protected static final int DEFAULT_MAX_SELECT = 1000;
    protected static final int MAX_CONTACTS_TO_PROCESS_PER_STEP = 50000;

    @Getter
    private long recordsSynced = 0l;

    @Getter
    private final DMAccessCredentials accessCredentials;

    public AbstractResource(DMAccessCredentials accessCredentials) {
        this.accessCredentials = accessCredentials;
    }

    protected String dotmailerUrl() {
        return accessCredentials.getApiUrl() + accessCredentials.getVersion();
    }

    protected WellRestedRequest buildRequestFromResourcePath(String resourcePath) {
        String url = validatePath(dotmailerUrl() + resourcePath);
        return WellRestedRequest.build(url, accessCredentials.getUsername(), accessCredentials.getPassword());
    }

    protected String validatePath(String path) {
        String newPath = path.replaceAll("//", "/");
        return newPath.replaceFirst("/", "//");
    }

    private String validatePathWithAttributes(String path) {
        String newPath = validatePath(path);
        if (newPath.endsWith("/")) {
            newPath = newPath.substring(0, newPath.length() - 1);
            newPath += QUESTION;
        } else if (newPath.contains(QUESTION)) {
            newPath += AND;
        } else {
            newPath += QUESTION;
        }
        return newPath.replaceAll(AND + AND, AND);
    }

    protected <T> Optional<T> sendAndGet(String resourcePath, Class<T> clazz) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.get();
        if (validResponse(response)) {
            return Optional.of(response.fromJson().castTo(clazz));
        }
        return Optional.empty();
    }

    protected <T> Optional<T> postAndGet(String resourcePath, T objectToPost) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.post(objectToPost);
        if (validResponse(response)) {
            return Optional.of(response.fromJson().castTo(((Class<T>) objectToPost.getClass())));
        }
        return Optional.empty();
    }

    protected <T> Optional<T> postAndGet(String resourcePath, Object objectToPost, Class<T> responseClass) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.post(objectToPost);
        if (validResponse(response)) {
            return Optional.of(response.fromJson().castTo(responseClass));
        }
        return Optional.empty();
    }

    protected int post(String resourcePath, Object objectToPost) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.post(objectToPost);
        return response.getStatusCode();
    }

    protected <T> Optional<T> postFileAndGet(String resourcePath, String filePath, Class<T> responseClass) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);

        File postedFile = new File(filePath);
        WellRestedResponse response = request.post(postedFile);
        postedFile.delete();

        if (validResponse(response)) {
            return Optional.of(response.fromJson().castTo(responseClass));
        }
        return Optional.empty();
    }

    protected <T> Optional<T> putAndGet(String resourcePath, T objectToPost) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.put(objectToPost);
        if (validResponse(response)) {
            return Optional.of(response.fromJson().castTo((Class<T>) objectToPost.getClass()));
        }
        return Optional.empty();
    }

    protected boolean delete(String resourcePath) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.delete();
        return validBlankResponse(response);
    }

    protected <T> Optional<List<T>> sendAndGetFullList(String resourcePath, TypeToken<List<T>> typeToken) {
        return sendAndGetFullList(resourcePath, null, null, typeToken, DEFAULT_MAX_SELECT, 0);
    }

    protected <T> Optional<List<T>> sendAndGetFullList(String resourcePath, TypeToken<List<T>> typeToken, int maxSelect) {
        return sendAndGetFullList(resourcePath, null, null, typeToken, maxSelect, 0);
    }

    protected <T> Optional<List<T>> sendAndGetFullList(String resourcePath, TypeToken<List<T>> typeToken, int maxSelect, int limit) {
        return sendAndGetFullList(resourcePath, null, null, typeToken, maxSelect, limit);
    }

    protected <T, K> Optional<List<T>> sendAndGetFullList(String resourcePath, Class<K> clazz, JsonDeserializer<K> jsonDeserializer, TypeToken<List<T>> typeToken,
            int maxSelect, int limit) {
        return sendAndGetFullList(resourcePath, clazz, jsonDeserializer, typeToken, maxSelect, limit, 0);
    }

    protected <T, K> Optional<List<T>> sendAndGetFullList(String resourcePath, Class<K> clazz, JsonDeserializer<K> jsonDeserializer, TypeToken<List<T>> typeToken,
            int maxSelect, int limit, int initialSkip) {

        if (resourcePath.contains("select") && resourcePath.contains("skip")) {
            log.error("Please remove select and skip attributes from the URL if you want to select the full list. Otherwise try the single list method");
            return Optional.empty();
        }
        String baseUrl = validatePathWithAttributes(dotmailerUrl() + resourcePath);

        int skip = -maxSelect + initialSkip;
        int newResultsSize = 0;
        List<T> allResults = new ArrayList<>(DEFAULT_MAX_SELECT);
        long recordsSynced = 0;
        long lastResultSize = 0;
        do {
            try {
                skip += maxSelect;
                String url = baseUrl + String.format(DM_SELECT_SKIP_ATTRIBUTES, maxSelect, skip);
                log.info(url);
                WellRestedRequest request = WellRestedRequest.build(url, accessCredentials.getUsername(), accessCredentials.getPassword());
                WellRestedResponse response = request.get();
                if (validResponse(response)) {
                    List<T> newResults;
                    if (clazz != null && jsonDeserializer != null) {
                        newResults = response.fromJson().registerDeserializer(clazz, jsonDeserializer).castToList(typeToken);
                    } else {
                        newResults = response.fromJson().castToList(typeToken);
                    }
                    allResults.addAll(newResults);
                    newResultsSize = newResults.size();
                    recordsSynced += allResults.size();

                    if (newResults.size() < maxSelect) {
                        break;
                    }
                }
            } catch (Exception ex) {
                log.error("sendAndGetFullList: error occured: {}", ex);
                newResultsSize = 0;
            }
        } while (newResultsSize != 0 && (limit <= 0 || allResults.size() < limit));
        return Optional.of(allResults);
    }

    protected <T> Optional<List<T>> sendAndGetSingleList(String resourcePath, TypeToken<List<T>> typeToken) {
        WellRestedRequest request = buildRequestFromResourcePath(resourcePath);
        WellRestedResponse response = request.get();
        if (validResponse(response)) {
            return Optional.of(response.fromJson().castToList(typeToken));
        }
        return Optional.empty();
    }

    protected String pathWithId(String path, Long id) {
        return String.format(path, id);
    }

    protected String pathWithParam(String path, String param) {
        return String.format(path, param);
    }

    protected String pathWithIdAndParam(String path, Long id, String param) {
        return String.format(path, id, param);
    }

    protected String addAttrAndValueToPath(String path, String attrName, String value) {
        String newPath = validatePathWithAttributes(path);
        return newPath + attrName + EQUAL + value;
    }

    protected boolean validResponse(WellRestedResponse response) {
        return validBlankResponse(response) && StringUtils.isNotBlank(response.getServerResponse());
    }

    protected boolean validBlankResponse(WellRestedResponse response) {
        return response != null && (response.getStatusCode() == 200 || response.getStatusCode() == 201 || response.getStatusCode() == 202);
    }

}
