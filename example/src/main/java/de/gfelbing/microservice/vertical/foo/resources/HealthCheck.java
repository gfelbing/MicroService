package de.gfelbing.microservice.vertical.foo.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import de.gfelbing.microservice.core.common.LifeCycle;
import de.gfelbing.microservice.vertical.foo.Service;
import de.gfelbing.microservice.vertical.foo.api.HealthState;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Creates a HealthCheck.
 *
 * @author gfelbing@github.com on 15.05.15.
 */
@Path("/")
@Api(description = "BaseResource is used as a HealthCheck.")
@Singleton
public final class HealthCheck {

    private final Service service;

    @Inject HealthCheck(final Service service) {
        this.service = service;
    }

    /**
     * See annotations.
     * @return see annotations.
     */
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Create a HealthCheck",
            notes = "It fails early, meaing if one LifeCycle is in State ERROR, the HealthCheck will be ERROR.",
            position = 1,
            response = HealthState.class)
    public Response createHealthCheck() {
        LifeCycle.State state = service.getState();
        return Response.ok().entity(new HealthState().setStatus(state)).build();
    }

    private static Response.Status map(final LifeCycle.State state) {
        switch (state) {
            case ERROR:
                return Response.Status.INTERNAL_SERVER_ERROR;
            case UP:
                return Response.Status.OK;
            default:
                return Response.Status.SERVICE_UNAVAILABLE;
        }
    }
}
