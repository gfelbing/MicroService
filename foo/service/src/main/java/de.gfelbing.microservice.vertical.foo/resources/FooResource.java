package de.gfelbing.microservice.vertical.foo.resources;


import com.google.common.collect.ImmutableList;
import com.linkedin.restli.server.annotations.RestLiSimpleResource;
import com.linkedin.restli.server.resources.SimpleResourceTemplate;
import de.gfelbing.microservice.vertical.foo.api.Foo;

import java.util.Random;

/**
 * Return a random mood.
 *
 * @author gfelbing@github.com on 05.05.15.
 */
@RestLiSimpleResource(name = "foo")
public final class FooResource extends SimpleResourceTemplate<Foo> {

    private static final ImmutableList<String> MOODS = ImmutableList.of("Happy", "Sad", "Without Coffee");

    @Override
    public Foo get() {
        return new Foo().setMood(MOODS.get(new Random().nextInt() % MOODS.size()));
    }
}
