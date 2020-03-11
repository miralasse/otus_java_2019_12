package ru.otus.l08;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
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
        private final Set<MethodSignature> annotatedMethods;

        DemoInvocationHandler(Logging instance) {
            this.instance = instance;
            annotatedMethods = Arrays.stream(instance.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .map(method -> new MethodSignature(method.getName(), method.getParameterTypes()))
                    .collect(toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodSignature methodSignature = new MethodSignature(method.getName(), method.getParameterTypes());
            if (annotatedMethods.contains(methodSignature)) {
                System.out.println(String.format("executed method: %s, params: %s", method.getName(), Arrays.toString(args)));
            }
            return method.invoke(instance, args);
        }
    }

    @Getter
    static class MethodSignature {
        private final String methodName;
        private final Class<?>[] parameterTypes;

        public MethodSignature(String methodName, Class<?>[] parameterTypes) {
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MethodSignature that = (MethodSignature) o;
            return Objects.equals(methodName, that.methodName) &&
                    Arrays.equals(parameterTypes, that.parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(methodName);
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }
    }
}
