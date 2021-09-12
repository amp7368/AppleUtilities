package apple.utilities.event;

public abstract class AppleHandler {
    private final AppleEventListenerMap listeners = new AppleEventListenerMap();

    public <Event extends AppleEvent> void onEvent(AppleEventType<Event> eventType, Event event) {
        listeners.onEvent(eventType, event);
    }

    public <Event extends AppleEvent> void addListener(AppleEventType<Event> eventType, AppleListener<Event> listener) {
        listeners.put(eventType, listener, getDefaultPriority());
    }

    public <Event extends AppleEvent> void addListener(AppleEventType<Event> eventType, AppleListener<Event> listener, AppleListenerPriority priority) {
        listeners.put(eventType, listener, priority);
    }

    public AppleListenerPriority getDefaultPriority() {
        return AppleListenerPrioritySimple.PRIORITY;
    }
}
