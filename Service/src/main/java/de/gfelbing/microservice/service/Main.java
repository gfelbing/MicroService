package de.gfelbing.microservice.service;

import com.google.inject.Guice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entrypoint class.
 *
 * @author gfelbing@github.com on 01.05.15.
 */
public final class Main {

    private Main() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
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
                .createInjector(new ServiceModule())
                .getInstance(Service.class)
                .start();
    }
}
