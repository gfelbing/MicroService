package de.gfelbing.microservice.vertical.foo;

import com.google.inject.Guice;
import de.gfelbing.microservice.core.concurrency.ExecutorServiceModule;
import de.gfelbing.microservice.core.discovery.client.D2ClientModule;
import de.gfelbing.microservice.core.discovery.server.D2ServerModule;
import de.gfelbing.microservice.core.http.jetty.handler.StaticContextHandlerModule;
import de.gfelbing.microservice.core.http.jetty.server.JettyServerModule;
import de.gfelbing.microservice.core.rest.parseq.ParseqModule;
import de.gfelbing.microservice.vertical.foo.config.ConfigurationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entrypoint class.
 *
 * @author gfelbing@github.com on 01.05.15.
 */
public final class Foo {

    private Foo() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(Foo.class);
    private static final String WELCOME_MSG = "\n"
            + "                  <<<<>>>>>>           .----------------------------.      \n"
            + "               _>><<<<>>>>>>>>>       /               _____________)       \n"
            + "      \\|/      \\<<<<<  < >>>>>>>>>   /            _______________)         \n"
            + "-------*--===<=<<           <<<<<<<>/         _______________)             \n"
            + "      /|\\     << @    _/      <<<<</       _____________)                  \n"
            + "             <  \\    /  \\      >>>/      ________)  ____                   \n"
            + "                 |  |   |       </      ______)____((- \\\\\\\\                \n"
            + "                 o_|   /        /      ______)         \\  \\\\\\\\    \\\\\\\\\\\\\\  \n"
            + "                      |  ._    (      ______)           \\  \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n"
            + "                      | /       `----------'    /       /     \\\\\\\\\\\\\\\\\\    \n"
            + "              .______/\\/     /                 /       /          \\\\\\      \n"
            + "             / __.____/    _/         ________(       /\\                   \n"
            + "            / / / ________/`---------'         \\     /  \\_                 \n"
            + "           / /  \\ \\                             \\   \\ \\_  \\                \n"
            + "          ( <    \\ \\                             >  /    \\ \\               \n"
            + "           \\/      \\\\_                          / /       > )              \n"
            + "                    \\_|                        / /       / /               \n"
            + "                                             _//       _//                 \n"
            + "                                            /_|       /_|                  \n";

    /**
     * Starting the Service by creating an injector and initializing a Service-Instance.
     *
     * @param args aren't used yet.
     * @throws java.lang.Exception uncaught from Service in order let the process die if something goes wrong.
     */
    public static void main(final String[] args) throws Exception {
        LOG.info(WELCOME_MSG);
        Guice
                .createInjector(
                        new ConfigurationModule(),
                        new ExecutorServiceModule(),
                        new D2ClientModule(),
                        new D2ServerModule(),
                        new JettyServerModule(),
                        new StaticContextHandlerModule(),
                        new ParseqModule())
                .getInstance(Service.class)
                .start();
    }
}
