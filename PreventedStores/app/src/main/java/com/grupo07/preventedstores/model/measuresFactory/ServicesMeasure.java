package com.grupo07.preventedstores.model.measuresFactory;

/**
 * Sanitary measure of a services store
 */
public class ServicesMeasure implements SanitaryMeasure {

    /**
     * Returns a set of sanitary measures exclusive for services stores
     * @return set of sanitary measures
     */
    @Override
    public String[] getMeasures() {
        String[] measures = {"Physical barrier between employee and customer",
                "Interpolated wait spaces reserved for the virus"};
        return measures;
    }
}