package otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otus.core.dao.UserDaoException;
import otus.jdbc.DbExecutorImpl;
import otus.jdbc.dao.UserDaoJdbc;
import otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcMapperImpl.
 *
 * @author Evgeniya_Yanchenko
 */
public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbc.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<T> dbExecutor;
    private final SQLCreator<T> sqlCreator;
    private final ReflectionHelper<T> reflectionHelper;

    public JdbcMapperImpl(SessionManagerJdbc sessionManager, DbExecutorImpl<T> dbExecutor, ReflectionHelper<T> reflectionHelper) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.reflectionHelper = reflectionHelper;
        this.sqlCreator = new SQLCreator<>(reflectionHelper);
    }

    @Override
    public void insert(T objectData) {
        //поскольку в sqlCreator используется этот же reflectionHelper, список полей из reflectionHelper и в sql запросе совпадает
        try {
            List<Field> allFields = reflectionHelper.getAllFields();
            List<Object> fieldValues = new ArrayList<>();
            for (Field field : allFields) {
                field.setAccessible(true);
                Object o = field.get(objectData);
                fieldValues.add(o);
            }
            dbExecutor.executeInsert(getConnection(), sqlCreator.getInsertSql(), fieldValues);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(T objectData) {
        //поскольку в sqlCreator используется этот же reflectionHelper, список полей из reflectionHelper и в sql запросе совпадает
        try {
            List<Field> fieldsWithoutId = reflectionHelper.getFieldsWithoutId();
            List<Object> values = new ArrayList<>();
            for (Field field : fieldsWithoutId) {
                field.setAccessible(true);
                Object o = field.get(objectData);
                values.add(o);
            }
            Field idField = reflectionHelper.getIdField();
            idField.setAccessible(true);
            Long idValue = idField.getLong(objectData);
            values.add(idValue);
            dbExecutor.executeUpdate(getConnection(), sqlCreator.getUpdateSql(), values);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            Field idField = reflectionHelper.getIdField();
            idField.setAccessible(true);
            Long idValue = idField.getLong(objectData);
            if (findById(idValue, (Class<T>) objectData.getClass()) != null) {
                update(objectData);
            } else {
                insert(objectData);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public T findById(long id, Class<T> clazz) {
        try {
            return dbExecutor.executeSelect(getConnection(), sqlCreator.getSelectByIdSql(), id,
                    rs -> {
                        try {
                            if (rs.next()) {

                                Constructor<T> constructor = reflectionHelper.getConstructor();
                                constructor.setAccessible(true);
                                T instance = (T) constructor.newInstance();

                                List<Field> allFields = reflectionHelper.getAllFields();
                                for (Field field : allFields) {
                                    field.setAccessible(true);
                                    String fieldName = field.getName();
                                    Object value = rs.getObject(fieldName);
                                    field.set(instance, value);
                                }

                                return instance;
                            }
                        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    }).orElse(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
