package acme.warehouse.demo.web.messages;

import acme.warehouse.demo.eventstream.products.events.ProductRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionModificationRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionRejectedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
class MessageStore {

    private final ConcurrentHashMap<String, String> messages = new ConcurrentHashMap<>();

    @EventListener
    public void handle(PositionModificationRejectedEvent e){
        messages.put(e.getMessageTo(), getMessage(e.getMessageTo()) + e.getMessage());
    }

    @EventListener
    public void handle(PositionRejectedEvent e){
        messages.put(e.getMessageTo(), getMessage(e.getMessageTo()) + e.getMessage());
    }

    @EventListener
    public void handle(ProductRejectedEvent e){
        messages.put(e.getMessageTo(), getMessage(e.getMessageTo()) + e.getMessage());
    }

    private String getMessage(String to) {
        String s = messages.get(to);
        String old = s != null ? s+"\n" : "";
        return old;
    }

    String getMessagesForUser(String username){
        String message = messages.get(username);
        messages.remove(username);
        return message;
    }
}
