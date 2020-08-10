package com.grupo07.preventedstores.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FilterByName implements Filter {
    @Override
    public ArrayList<Store> filter(ArrayList<Store> list, String id) {
        ArrayList<Store> result = new ArrayList<Store>();
        for(Store store:list){
            if(store.getName().contains(id)){
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
                    return store1.getName().compareTo(store2.getName());
                }
            });
            return list;
    }
}
