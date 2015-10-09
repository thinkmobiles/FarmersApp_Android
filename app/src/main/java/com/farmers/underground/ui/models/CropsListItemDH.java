package com.farmers.underground.ui.models;

import com.farmers.underground.remote.models.CropModel;
import com.farmers.underground.ui.adapters.CropsListAdapter;

/**
 * Created by omar on 10/2/15.
 */
public class CropsListItemDH {

    private CropModel model;
    private CropsListFragmentModel.TYPE type;
    private CropsListAdapter.CropsAdapterCallback callback;



    public CropsListItemDH(CropModel model, CropsListFragmentModel.TYPE type, CropsListAdapter.CropsAdapterCallback callback) {
        this.model = model;
        this.type = type;
        this.callback = callback;
    }

    public CropModel getModel() {
        return model;
    }

    public void setModel(CropModel model) {
        this.model = model;
    }

    public CropsListFragmentModel.TYPE getType() {
        return type;
    }

    public void setType(CropsListFragmentModel.TYPE type) {
        this.type = type;
    }

    public CropsListAdapter.CropsAdapterCallback getCallback() {
        return callback;
    }
}
