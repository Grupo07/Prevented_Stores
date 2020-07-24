package com.grupo07.preventedstores.objects;

/**
 * Creates sanitary measures of a set of store categories
 */
public class SanitaryMeasuresFactory {

    /**
     * Makes a specific type of SanitaryMeasure object given the store category
     * @param storeCategory category of the story
     * @return sanitary measures of the store category
     */
    public static SanitaryMeasure makeMeasures(String storeCategory) {
        switch(storeCategory) {
            case "food":
                return new FoodMeasure();

            case "grocery":
                return new GroceryMeasure();

            case "shop":
                return new ShopMeasure();

            case "services":
                return new ServicesMeasure();

            default:
                return null;
        }
    }
}
