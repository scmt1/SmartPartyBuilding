package com.yami.shop.security.api.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {
    private static Gson gson = new Gson();
    private static JsonParser parser = new JsonParser();

    public JsonUtils() {
    }

    public static String toJson(Object data) {
        return gson.toJson(data);
    }

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
        List<T> list = new ArrayList();
        Iterator var4 = jsonArray.iterator();

        while(var4.hasNext()) {
            JsonElement jsonElement = (JsonElement)var4.next();
            list.add(gson.fromJson(jsonElement, clazz));
        }

        return list;
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        Gson gson = (new GsonBuilder()).registerTypeAdapter(Date.class, new DateDeserializer()).setDateFormat(1).create();
        return gson.fromJson(json, typeOfT);
    }

    static class DateDeserializer implements JsonDeserializer<Date> {
        DateDeserializer() {
        }

        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Date(jsonElement.getAsJsonPrimitive().getAsLong());
        }
    }
}
