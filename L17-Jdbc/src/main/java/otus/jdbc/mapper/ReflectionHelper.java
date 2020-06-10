package otus.jdbc.mapper;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import otus.core.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * EntityClassMetaDataImpl.
 *
 * @author Evgeniya_Yanchenko
 */
public class ReflectionHelper<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    private String className;
    private Constructor<T> defaultConstructor;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;
    private Field idField;

    public ReflectionHelper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        className = className == null ? clazz.getSimpleName().toLowerCase() : className;
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (defaultConstructor == null) {
            Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            defaultConstructor = (Constructor<T>) Arrays.stream(constructors)
                    .filter(c -> c.getGenericParameterTypes().length == 0)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(format("No default constructor found in class %s", clazz.getSimpleName())));
        }
        return defaultConstructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            idField = Arrays.stream(clazz.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(format("No @Id field found in class %s", clazz.getSimpleName())));
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        allFields = allFields == null ? asList(clazz.getDeclaredFields()) : allFields;
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                    .peek(field -> field.setAccessible(true))
                    .filter(field -> !field.isAnnotationPresent(Id.class))
                    .collect(toList());
        }
        return fieldsWithoutId;
    }
}
