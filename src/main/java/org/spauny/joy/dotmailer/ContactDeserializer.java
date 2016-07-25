package org.spauny.joy.dotmailer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.apache.commons.lang3.StringUtils;
import org.spauny.joy.dotmailer.vo.internal.ContactDetails;

/**
 *
 * @author iulian
 */
public class ContactDeserializer implements JsonDeserializer<ContactDetails> {
    
    @Override
    public ContactDetails deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonArray jArray = (JsonArray) json;
        ContactDetails details = new ContactDetails();
        for (int i = 1; i < jArray.size(); i++) {
            JsonObject jObject = (JsonObject) jArray.get(i);
            if (StringUtils.isBlank(details.getUid())) {
                details.setUid(getValueForKey("UNIQUEID", jObject));
            }
            if (StringUtils.isBlank(details.getFirstName())) {
                details.setFirstName(getValueForKey("FIRSTNAME", jObject));
            }
            if (StringUtils.isBlank(details.getLastName())) {
                details.setLastName(getValueForKey("LASTNAME", jObject));
            }
            if (StringUtils.isBlank(details.getFullName())) {
                details.setFullName(getValueForKey("FULLNAME", jObject));
            }
            if (StringUtils.isBlank(details.getUsername())) {
                details.setUsername(getValueForKey("USERNAME", jObject));
            }
            if (StringUtils.isBlank(details.getPostcode())) {
                details.setPostcode(getValueForKey("POSTCODE", jObject));
            }
        }
        return details;
    }
    
    private String getValueForKey(String keyName, JsonObject jObject) {
        JsonElement keyElement = jObject.get("key");
        if (!keyElement.isJsonNull()) {
            String key = keyElement.getAsString();
            if (StringUtils.isNotBlank(key) && key.equals(keyName)) {
                JsonElement valueElement = jObject.get("value");
                if (!valueElement.isJsonNull()) {
                    return valueElement.getAsString();
                }
            }
        }
        return StringUtils.EMPTY;
    }
    
}
