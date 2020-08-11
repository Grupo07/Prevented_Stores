package com.grupo07.preventedstores.model.measuresFactory;

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
        String[] measures = {"Touchless payment promotion",
                "Physical barrier between cashier and customer"};
        return measures;
    }
}
