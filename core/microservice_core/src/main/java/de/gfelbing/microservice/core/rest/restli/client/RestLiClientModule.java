package de.gfelbing.microservice.core.rest.restli.client;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.linkedin.d2.balancer.D2Client;
import com.linkedin.restli.client.ParSeqRestClient;
import com.linkedin.restli.client.RestClient;

/**
 * Module for rest.li clients.
 * <p>
 * With this module you are able to inject:
 * - A #{RestClient} client based on a #{D2Client}
 * - A #{ParSeqRestClient} based on the #{RestClient}
 * <p>
 * In order to use this module, you have to provide a implementation of a #link{D2Client}
 *
 * @author gfelbing@github.com on 06.05.15.
 */
public class RestLiClientModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Inject
    RestClient restClient(final D2Client d2Client) {
        return new RestClient(d2Client, "d2://");
    }

    @Provides
    @Singleton
    @Inject
    ParSeqRestClient parSeqRestClient(final RestClient restClient) {
        return new ParSeqRestClient(restClient);
    }
}
