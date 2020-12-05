package acme.warehouse.demo.web.messages;

import acme.warehouse.demo.config.beans.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
@RequiredArgsConstructor
public class MessagesController {

    private final MessageStore messageStore;
    private final CurrentUser user;

    @GetMapping
    private String getMessages(){
        return user.getCurrentUser().map(messageStore::getMessagesForUser).orElse("");
    }
}
