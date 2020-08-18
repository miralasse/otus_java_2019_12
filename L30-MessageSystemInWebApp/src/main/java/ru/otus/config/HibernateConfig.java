package ru.otus.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.sessionmanager.SessionManagerException;
import ru.otus.model.User;

/**
 * HibernateConfig.
 *
 * @author Evgeniya_Yanchenko
 */
@Configuration
public class HibernateConfig {

    private static final String HIBERNATE_CFG_XML_FILE_RESOURCE = "hibernate.cfg.xml";

    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_XML_FILE_RESOURCE, User.class);
        if (sessionFactory == null) {
            throw new SessionManagerException("SessionFactory is null");
        }
        return sessionFactory;
    }
}
