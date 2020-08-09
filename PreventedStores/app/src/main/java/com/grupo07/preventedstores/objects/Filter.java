package com.grupo07.preventedstores.objects;

import java.util.ArrayList;

public interface Filter {
    ArrayList<Store> filter(ArrayList<Store> list,String id);
    ArrayList<Store> sort(ArrayList<Store> list);
}
