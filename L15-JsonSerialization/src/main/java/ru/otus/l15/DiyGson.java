package ru.otus.l15;


import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ru.otus.l15.DiyGson.
 *
 * @author Evgeniya_Yanchenko
 */
public class DiyGson {

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }

        if (isKnownType(object)) {
            return createJsonElement(object);
        } else {
            return processFields(object);
        }
    }


    private boolean isKnownType(Object object) {
        return isArrayOrCollection(object)
                || isMap(object)
                || isCharacterOrString(object)
                || isNumberOrBoolean(object);
    }


    private String createJsonElement (Object object) {
        if (object == null) {
            return null;
        }

        if (isNumberOrBoolean(object)) {
            return object.toString();
        }

        if (isCharacterOrString(object)) {
            return wrapWithQuoteMarks(object.toString());
        }

        if (isArrayOrCollection(object)) {
            if (object instanceof Collection) {
                object = ((Collection<Object>)object).toArray();
            }
            int length = Array.getLength(object);
            List<String> arrayElements = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                Object arrayElement = Array.get(object, i);
                arrayElements.add(createJsonElement(arrayElement));
            }
            return wrapWithBrackets(String.join(",", arrayElements));
        }

        if (isMap(object)) {
            Map<Object, Object> map = (Map)object;
            List<String> mapElements = new ArrayList<>(map.size());

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                String key = wrapWithQuoteMarks(entry.getKey().toString());
                String value = wrapWithQuoteMarks(entry.getValue().toString());
                mapElements.add(key + ":" + value);
            }
            return wrapWithBraces(String.join(",", mapElements));
        }

        return processFields(object);
    }


    @SneakyThrows
    private String processFields(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        if (fields.length == 0) {
            return null;
        }

        List<String> fieldsList = new ArrayList<>(fields.length);

        for (Field field : fields) {
            field.setAccessible(true);
            int modifiers = field.getModifiers();
            if (!(Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))) {
                Object fieldValue = field.get(object);
                String key = wrapWithQuoteMarks(field.getName());
                String value = createJsonElement(fieldValue);
                fieldsList.add(key + ":" + value);
            }
        }
        return wrapWithBraces(String.join(",", fieldsList));
    }


    private boolean isNumberOrBoolean(Object object) {
        return object instanceof Byte
                || object instanceof Short
                || object instanceof Integer
                || object instanceof Long
                || object instanceof Float
                || object instanceof Double
                || object instanceof Boolean;
    }


    private boolean isCharacterOrString(Object object) {
        return object instanceof Character || object instanceof String;
    }


    private boolean isArrayOrCollection(Object object) {
        return object.getClass().isArray() || object instanceof Collection;

    }


    private boolean isMap(Object object) {
        return object instanceof Map;
    }


    private String wrapWithBraces(String value) {
        return "{" + value + "}";
    }


    private String wrapWithBrackets(String value) {
        return "[" + value + "]";
    }


    private String wrapWithQuoteMarks(String value) {
        return "\"" + value + "\"";
    }

}
