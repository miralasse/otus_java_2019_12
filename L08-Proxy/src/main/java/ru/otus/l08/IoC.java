package ru.otus.l08;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;

/**
 * IoC.
 *
 * @author Evgeniya_Yanchenko
 */
@NoArgsConstructor(access = PRIVATE)
public class IoC {

    static Logging createLoggingClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (Logging) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{Logging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Logging instance;
        private final Set<String> annotatedMethods;

        DemoInvocationHandler(Logging instance) {
            this.instance = instance;
            annotatedMethods = Arrays.stream(instance.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(Method::getName)
                    .collect(toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            if (annotatedMethods.contains(methodName)) {
                System.out.println(String.format("executed method: %s, params: %s", methodName, Arrays.toString(args)));
            }
            return method.invoke(instance, args);
        }

    }
}
