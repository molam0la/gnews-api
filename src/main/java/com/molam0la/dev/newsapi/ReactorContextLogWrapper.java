package com.molam0la.dev.newsapi;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ReactorContextLogWrapper {

    public static <T> Consumer<Signal<T>> logOnComplete(Runnable r) {
        return doLog(Signal::isOnComplete, (s) -> r.run());
    }

    public static <T> Consumer<Signal<T>> logOnError(Consumer<Throwable> c) {
        return doLog(Signal::isOnError, (s) -> c.accept(s.getThrowable()));
    }

    private static <T> Consumer<Signal<T>> doLog(Function<Signal<T>, Boolean> signalFilter, Consumer<Signal<T>> r) {
        return (signal) -> {
            if (!signalFilter.apply(signal)) {
                return;
            }

            Map<String, String> copy = setupMdc(signal.getContext());

            try {
                r.accept(signal);
            } finally {
                restoreMdc(copy);
            }
        };
    }

    private static Map<String, String> setupMdc(Context context) {

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        MDC.clear();

        context.stream()
                .filter(e -> e.getKey() instanceof String && e.getValue() instanceof String)
                .forEach((e) -> MDC.put((String) e.getKey(), (String) e.getValue()));

        return copyOfContextMap;
    }

    private static void restoreMdc(Map<String, String> mdcContextMap) {

        if (mdcContextMap == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(mdcContextMap);
        }
    }
}
