package com.grupo07.preventedstores.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FilterByName implements Filter {
    @Override
    public ArrayList<Store> filter(ArrayList<Store> list, String id) {
        return null;
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
