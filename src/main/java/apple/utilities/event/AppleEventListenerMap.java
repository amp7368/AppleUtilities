package apple.utilities.event;

import java.util.*;

public class AppleEventListenerMap {
    private final Map<AppleEventType<?>, List<AppleListenerWrapper<?>>> map = new HashMap<>();

    public <Event extends AppleEvent> void put(AppleEventType<Event> eventType, AppleListener<Event> listener, AppleListenerPriority priority) {
        synchronized (map) {
            List<AppleListenerWrapper<?>> listeners = map.computeIfAbsent(eventType, (e) -> new ArrayList<>());
            listeners.add(new AppleListenerWrapper<>(listener, priority));
            listeners.sort(AppleListenerWrapper::comparePriority);
        }
    }

    public <Event extends AppleEvent> void onEvent(AppleEventType<Event> eventType, Event event) {
        List<AppleListenerWrapper<?>> listeners = map.get(eventType);
        if (listeners == null) return;
        for (AppleListenerWrapper<?> wrapperGeneric : listeners) {
            // this cast is okay because put() enforces this
            AppleListenerWrapper<Event> wrapper = (AppleListenerWrapper<Event>) wrapperGeneric;
            wrapper.listener().onEvent(event);
            if (event.isCanceled()) {
                return;
            }
        }
    }

    private static final class AppleListenerWrapper<Event> {
        private final AppleListener<Event> listener;
        private final AppleListenerPriority priority;

        private AppleListenerWrapper(AppleListener<Event> listener, AppleListenerPriority priority) {
            this.listener = listener;
            this.priority = priority;
        }

        public int comparePriority(AppleListenerWrapper<?> other) {
            return this.priority.compare(other.priority);
        }

        public AppleListener<Event> listener() {
            return listener;
        }

        public AppleListenerPriority priority() {
            return priority;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (AppleListenerWrapper) obj;
            return Objects.equals(this.listener, that.listener) &&
                    Objects.equals(this.priority, that.priority);
        }

        @Override
        public int hashCode() {
            return Objects.hash(listener, priority);
        }

        @Override
        public String toString() {
            return "AppleListenerWrapper[" +
                    "listener=" + listener + ", " +
                    "priority=" + priority + ']';
        }

    }
}
