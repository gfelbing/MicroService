package de.gfelbing.microservice.core.rest.swagger;

import com.google.common.collect.ImmutableList;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.jaxrs.config.ReflectiveJaxrsScanner;
import de.gfelbing.microservice.core.util.GuavaCollect;

import java.util.Set;

/**
 * Wraps multiple ReflectiveJaxrsScanner.
 *
 * @author gfelbing@github.com on 14.05.15.
 */
public final class ReflectiveJaxrsScannerCollection implements Scanner {

    private final ImmutableList.Builder<ReflectiveJaxrsScanner> scanners;

    /**
     * Default constructor.
     */
    public ReflectiveJaxrsScannerCollection() {
        this.scanners = new ImmutableList.Builder<>();
    }

    /**
     * Adds a scanner for the given package.
     * @param resourcePackage the package for which the scanner will be created.
     * @return itself for chaining.
     */
    public ReflectiveJaxrsScannerCollection addPackage(final String resourcePackage) {
        final ReflectiveJaxrsScanner scanner = new ReflectiveJaxrsScanner();
        scanner.setResourcePackage(resourcePackage);
        scanners.add(scanner);
        return this;
    }

    /**
     * Invokes @link{addPackage()} for each element.
     * @param packages the list of packages for which @link{addPackage()} should be invoked.
     * @return itsel for chaining.
     */
    public ReflectiveJaxrsScannerCollection addAllPackages(final ImmutableList<String> packages) {
        packages.forEach(this::addPackage);
        return this;
    }

    /**
     * Collects all classes found by all scanners.
     * @return set of found classes.
     */
    @Override
    public Set<Class<?>> classes() {
        return scanners.build().stream()
                .map(scanner -> scanner.classes())
                .flatMap(set -> set.stream())
                .collect(GuavaCollect.immutableSet());
    }

    /**
     * Check if all scanners have set prettyprint.
     * @return true if all scanners returning @link{getPrettyPrint()}==true.
     */
    @Override
    public boolean getPrettyPrint() {
        return scanners.build().stream().allMatch(ReflectiveJaxrsScanner::getPrettyPrint);
    }

    /**
     * Setting prettyPrint to all scanners.
     * @param shouldPrettyPrint the value prettyPrint should be set to.
     */
    @Override
    public void setPrettyPrint(final boolean shouldPrettyPrint) {
        scanners.build().forEach(scanner -> scanner.setPrettyPrint(shouldPrettyPrint));
    }
}
