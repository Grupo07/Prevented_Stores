package com.grupo07.preventedstores.objects;

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
        String[] measures = {"services option 1", "services option 2"};
        return measures;
    }
}