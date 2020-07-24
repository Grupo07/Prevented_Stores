package com.grupo07.preventedstores.objects;

/**
 * Sanitary measure of a food store
 */
public class FoodMeasure implements SanitaryMeasure {

    /**
     * Returns a set of sanitary measures exclusive for food stores
     * @return set of sanitary measures
     */
    @Override
    public String[] getMeasures() {
        String[] measures = {"food option 1", "food option 2"};
        return measures;
    }
}