package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.handlers.GetAllUsersRequestHandler;
import ru.otus.db.handlers.SaveUserRequestHandler;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.front.services.FrontendService;
import ru.otus.front.services.FrontendServiceImpl;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

/**
 * MessageSystemConfig.
 *
 * @author Evgeniya_Yanchenko
 */
@Configuration
public class MessageSystemConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";


    @Bean(name = "requestHandlerDatabaseStore")
    public HandlersStore requestHandlerDatabaseStore(GetAllUsersRequestHandler getAllUsersRequestHandler,
                                                     SaveUserRequestHandler saveUserRequestHandler) {

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.GET_ALL_USERS, getAllUsersRequestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.CREATE_USER, saveUserRequestHandler);
        return requestHandlerDatabaseStore;
    }


    @Bean(name = "databaseMsClient")
    public MsClient databaseMsClient(MessageSystem messageSystem,
                                     HandlersStore requestHandlerDatabaseStore,
                                     CallbackRegistry callbackRegistry) {

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }


    @Bean(name = "requestHandlerFrontendStore")
    public HandlersStore requestHandlerFrontendStore(GetUserDataResponseHandler responseHandler) {

        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.GET_ALL_USERS, responseHandler);  //другой хендлер
        requestHandlerFrontendStore.addHandler(MessageType.CREATE_USER, responseHandler);   //другой хендлер
        return requestHandlerFrontendStore;
    }


    @Bean(name = "frontendMsClient")
    public MsClient frontendMsClient(MessageSystem messageSystem,
                                     HandlersStore requestHandlerFrontendStore,
                                     CallbackRegistry callbackRegistry) {

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

    @Bean
    FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }

}
