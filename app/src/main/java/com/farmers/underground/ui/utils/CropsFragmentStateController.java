package com.farmers.underground.ui.utils;

/**
 * Created by omar
 * on 10/2/15.
 */
public class CropsFragmentStateController {
    private int fragmentCount;
    private int currentCreatedFragments;
    private boolean isAllCreated;


    public CropsFragmentStateController(int fragmentCount) {
        this.fragmentCount = fragmentCount;
    }


    public boolean isAllCreated() {
        return isAllCreated;
    }


    public void addCreated(){
        if(fragmentCount > currentCreatedFragments)
            currentCreatedFragments++;
        if(fragmentCount == currentCreatedFragments)
            isAllCreated = true;
    }

    public void removeCreated(){
        if( currentCreatedFragments > 0)
            currentCreatedFragments--;
            isAllCreated = false;
    }

}
