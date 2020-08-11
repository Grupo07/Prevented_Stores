package com.grupo07.preventedstores.model.measuresFactory;

/**
 * Sanitary measure of a shop store
 */
public class ShopMeasure implements SanitaryMeasure {

    /**
     * Returns a set of sanitary measures exclusive for shop stores
     * @return set of sanitary measures
     */
    @Override
    public String[] getMeasures() {
        String[] measures = {"Physical barrier between cashier and customer",
                "Spaces for self-cleaning shopping carts"};
        return measures;
    }
}