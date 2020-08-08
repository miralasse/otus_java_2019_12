package ru.otus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.handlers.GetUserDataRequestHandler;
import ru.otus.db.hibernate.dao.UserDaoHibernate;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.db.services.DbServiceUserImpl;
import ru.otus.dto.UserData;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

public class MSMain {
    private static final Logger logger = LoggerFactory.getLogger(MSMain.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    public static void main(String[] args) throws InterruptedException {
        MessageSystem messageSystem = new MessageSystemImpl();
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(
                new DbServiceUserImpl(
                        new UserDaoHibernate(
                                new SessionManagerHibernate()))));
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(callbackRegistry));

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);

        frontendService.getUserData(1, new MessageCallback<UserData>() {
            @Override
            public void accept(UserData data) {
                logger.info("got data:{}", data);
            }
        });
        frontendService.getUserData(2, new MessageCallback<UserData>() {
            @Override
            public void accept(UserData data) {
                logger.info("got data:{}", data);
            }
        });

        Thread.sleep(100);
        messageSystem.dispose();
        logger.info("done");
    }
}
