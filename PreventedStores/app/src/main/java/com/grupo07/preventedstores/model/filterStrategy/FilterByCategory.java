package com.grupo07.preventedstores.model.filterStrategy;

import com.grupo07.preventedstores.model.database.Store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FilterByCategory implements Filter {
    /**
     * filter a list of stores by category
     * @param list arraylist of stores to get filtered
     * @param id name of category
     * @return arraylist of stores filtered
     */
    @Override
    public ArrayList<Store> filter(ArrayList<Store> list, String id) {
        ArrayList<Store> result = new ArrayList<Store>();
        for(Store store:list){
            if(store.getCategory().contains(id)){
                result.add(store);
            }
        }
        return result;
    }

    /**
     * sort a lsit of stores by category
     * @param list arraylist of stores to get sorted
     * @return arralist of stores sorted
     */
    @Override
    public ArrayList<Store> sort(ArrayList<Store> list) {
        Collections.sort(list, new Comparator<Store>(){
            @Override
            public int compare(Store store1, Store store2) {
                return store1.getCategory().compareTo(store2.getCategory());
            }
        });
        return list;
    }
}
