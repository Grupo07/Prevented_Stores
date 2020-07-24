package com.grupo07.preventedstores.objects;

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
        String[] measures = {"shop option 1", "shop option 2"};
        return measures;
    }
}