package acme.warehouse.demo.config.beans;

import lombok.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;

@Component
@Scope(value = SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CurrentUser {
    public Optional<String> getCurrentUser(){
        try {
            return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
