package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static <T> T readJsonToObject(Class<T> clazz, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(LZJsonModule.LZ_MODULE);

        try {
            T result = mapper.readValue(json, clazz);
            return result;
        } catch (Exception e) {
        }
        return null;
    }


    public static <T> T readJsonToObject(TypeReference<T> type, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> List<T> readJsonToObjectList(Class<T> clazz, String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(LZJsonModule.LZ_MODULE);

        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> result = mapper.readValue(json, type);
            return result;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map readJsonToMap(String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        try {
            Map result = mapper.readValue(json, Map.class);
            if (result == null) result = new HashMap();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap();
        }

    }

    //-----------------------------------------------------------------
    public static <T> T readJsonToObject(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            T result = mapper.readValue(json, clazz);
            return result;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readJsonToObject(TypeReference<T> type, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            T result = mapper.readValue(json, type);
            return result;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> readJsonToObjectList(Class<T> clazz, String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }

        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            List<T> result = mapper.readValue(json, type);
            return result;
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map readJsonToMap(String json, PropertyNamingStrategy pns) {
        if (StringUtils.isBlank(json)) {
            return new HashMap();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(LZJsonModule.LZ_MODULE);
        if (pns != null) {
            mapper.setPropertyNamingStrategy(pns);
        }
        try {
            Map result = mapper.readValue(json, Map.class);
            if (result == null) result = new HashMap();
            return result;
        } catch (Exception e) {
            return new HashMap();
        }
    }

    public static String writeObjectToJson(Object object) {
        return writeObjectToJson(object, false);
    }

    public static void writeObjectToStream(OutputStream os, Object object, boolean excludeNull) {
        writeObject(object, os, null, excludeNull);
    }

    public static void writeObjectToWriter(Writer writer, Object object, boolean excludeNull) {
        writeObject(object, null, writer, excludeNull);
    }

    public static String writeObjectToJson(Object object, boolean excludeNull) {
        return writeObject(object, null, null, excludeNull);
    }

    private static String writeObject(Object object, OutputStream os, Writer writer, boolean excludeNull) {
        if (object == null) return null;
        if (object instanceof Map) {
            try {
                ((Map) object).remove(null);
            } catch (Exception e) {
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        //不自动关闭流
        mapper.getFactory().disable(Feature.AUTO_CLOSE_TARGET);
        if (excludeNull) {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        try {
            mapper.registerModule(LZJsonModule.LZ_MODULE);
            if (os != null) {
                mapper.writeValue(os, object);
            } else if (writer != null) {
                mapper.writeValue(writer, object);
            } else {
                String data = mapper.writeValueAsString(object);
                return data;
            }
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String writeMapToJson(Map<String, String> dataMap) {
        if (dataMap == null) return null;
        if (dataMap instanceof HashMap) {
            try {
                dataMap.remove(null);
            } catch (Exception e) {
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
        try {
            String data = mapper.writeValueAsString(dataMap);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addJsonKeyValue(String json, String key, String value) {
        Map info = readJsonToMap(json);
        info.put(key, value);
        return writeMapToJson(info);
    }

    public static String removeJsonKeyValue(String json, String key) {
        Map info = readJsonToMap(json);
        info.remove(key);
        return writeMapToJson(info);
    }

    public static String getJsonValueByKey(String json, String key) {
        Map<String, String> info = readJsonToMap(json);
        return info.get(key);
    }

}
