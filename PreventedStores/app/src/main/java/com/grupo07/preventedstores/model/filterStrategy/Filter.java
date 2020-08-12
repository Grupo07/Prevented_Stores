package com.grupo07.preventedstores.model.filterStrategy;

import com.grupo07.preventedstores.model.database.Store;

import java.util.ArrayList;

/**
 * Interface of strategy pattern
 */
public interface Filter {
    ArrayList<Store> filter(ArrayList<Store> list, String id);
    ArrayList<Store> sort(ArrayList<Store> list);
}
