package gov.cipam.gi.model;

import android.view.View;

/**
 * Created by NITANT SOOD on 08-02-2018.
 */

public class SellerAndView {
    private Seller seller;
    private View view;

    public SellerAndView(Seller seller, View view) {
        this.seller = seller;
        this.view = view;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
