package com.grupo07.preventedstores.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FilterByCategory implements Filter {
    @Override
    public ArrayList<Store> filter(ArrayList<Store> list, String category) {
        ArrayList<Store> result = new ArrayList<Store>();
        for(Store store:list){
            if(store.getCategory().contains(category)){
                result.add(store);
            }
        }
        return result;
    }

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
