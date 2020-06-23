package otus.jdbc.mapper;

import static java.util.stream.Collectors.toList;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * EntitySQLMetaDataImpl.
 *
 * @author Evgeniya_Yanchenko
 */
@RequiredArgsConstructor
public class SQLCreator<T> implements EntitySQLMetaData {

    private final ReflectionHelper<T> reflectionHelper;

    private String tableName;
    private List<String> allFields;
    private List<String> fieldsWithoutId;
    private String idFieldName;

    @Override
    public String getSelectAllSql() {
        //select user.* from user

        StringBuilder sb = new StringBuilder("select ");
        tableName = tableName == null ? reflectionHelper.getName() : tableName;
        sb.append(tableName).append(".* from ").append(tableName);
        return sb.toString();
    }

    @Override
    public String getSelectByIdSql() {
        //select id, name, age from user where id = ?

        StringBuilder sb = new StringBuilder("select ");
        appendAllFieldsNamesEnumeration(sb);
        tableName = tableName == null ? reflectionHelper.getName() : tableName;
        idFieldName = idFieldName == null ? reflectionHelper.getIdField().getName() : idFieldName;
        sb.append(" from ").append(tableName).append(" where ").append(idFieldName).append(" = ?");
        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        //insert into user (id, name, age) values (?, ?, ?)

        StringBuilder sb = new StringBuilder("insert into ");
        tableName = tableName == null ? reflectionHelper.getName() : tableName;
        sb.append(tableName).append(" (");
        appendAllFieldsNamesEnumeration(sb);
        sb.append(") values (");
        for (int i = 0; i < allFields.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        //update user set name = ?, age = ? where id = ?

        StringBuilder sb = new StringBuilder("update ");
        tableName = tableName == null ? reflectionHelper.getName() : tableName;
        sb.append(tableName).append(" set ");
        fieldsWithoutId = fieldsWithoutId == null
                ? reflectionHelper.getFieldsWithoutId().stream().map(field -> field.getName().toLowerCase()).collect(toList())
                : fieldsWithoutId;
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(fieldsWithoutId.get(i)).append(" = ?");
        }
        idFieldName = idFieldName == null ? reflectionHelper.getIdField().getName() : idFieldName;
        sb.append(" where ").append(idFieldName).append(" = ?");
        return sb.toString();
    }

    private void appendAllFieldsNamesEnumeration(StringBuilder sb) {
        allFields = allFields == null
                ? reflectionHelper.getAllFields().stream().map(field -> field.getName().toLowerCase()).collect(toList())
                : allFields;
        for (int i = 0; i < allFields.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(allFields.get(i));
        }
    }
}
