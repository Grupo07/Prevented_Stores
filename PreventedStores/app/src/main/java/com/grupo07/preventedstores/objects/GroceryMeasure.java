package com.grupo07.preventedstores.objects;

/**
 * Sanitary measure of a grocery store
 */
public class GroceryMeasure implements SanitaryMeasure {

    /**
     * Returns a set of sanitary measures exclusive for grocery stores
     * @return set of sanitary measures
     */
    @Override
    public String[] getMeasures() {
        String[] measures = {"grocery option 1", "grocery option 2"};
        return measures;
    }
}
