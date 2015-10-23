package com.farmers.underground.remote.models.base;

/**
 * Created by tZpace
 * on 06-Oct-15.
 */
public class MarketeerBase {
    private String _marketeer;
    private String fullName;
    private String location;
    private Boolean newMarketeer;
    private Boolean canChangeMarketeer;

    public String get_marketeer() {
        return _marketeer;
    }

    public void set_marketeer(String _marketeer) {
        this._marketeer = _marketeer;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getNewMarketeer() {
        return newMarketeer;
    }

    public void setNewMarketeer(Boolean newMarketeer) {
        this.newMarketeer = newMarketeer;
    }

    public Boolean getCanChangeMarketeer() {
        return canChangeMarketeer;
    }

    public void setCanChangeMarketeer(Boolean canChangeMarketeer) {
        this.canChangeMarketeer = canChangeMarketeer;
    }
}
