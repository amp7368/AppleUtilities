package apple.utilities.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppleEventListenerMap {

    private final Map<AppleEventType<?>, List<AppleListenerWrapper<?>>> map = new HashMap<>();

    public <Event extends AppleEvent> void put(AppleEventType<Event> eventType, AppleListener<Event> listener,
        AppleListenerPriority priority) {
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
            @SuppressWarnings("unchecked")
            AppleListenerWrapper<Event> wrapper = (AppleListenerWrapper<Event>) wrapperGeneric;
            wrapper.listener().onEvent(event);
            if (event.isCanceled()) {
                return;
            }
        }
    }

    private record AppleListenerWrapper<Event>(AppleListener<Event> listener, AppleListenerPriority priority) {

        public int comparePriority(AppleListenerWrapper<?> other) {
            return this.priority.compare(other.priority);
        }
    }
}
