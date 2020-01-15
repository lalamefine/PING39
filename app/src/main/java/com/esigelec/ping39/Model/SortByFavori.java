package com.esigelec.ping39.Model;

import java.util.Comparator;

public class SortByFavori implements Comparator<Bateau> {

    @Override
    public int compare(Bateau b1, Bateau b2) {
        if (b1.isFavori() && !b2.isFavori())
            return -1;
        if (!b1.isFavori() && b2.isFavori())
            return 1;
        return 0;
    }
}
