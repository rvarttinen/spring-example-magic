package se.autocorrect.springexample.util;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * A simple class to extract the current port set in the web server it starts up.
 */
@Service
public class ServerPortExtractor {

    // TODO: check out: https://www.baeldung.com/spring-boot-running-port

    private int port;

    public int getPort() {
        return port;
    }

    /**
     * Await the web server to start so we can get the current port used, whether fixed by setting or random.
     *
     * @param event the web server init event
     */
    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        port = event.getWebServer().getPort();
    }
}
