package com.farmers.underground.remote.models;

/**
 * Created by omar on 10/3/15.
 */
public class SearchHint {

    private String name;

    public enum HintType{
        FROM_HISTORY,
        FROM_CROPS_LIST
    }

    private HintType type;


    public SearchHint(String name, HintType type) {
        this.name = name;
        this.type = type;
    }

    public HintType getType() {
        return type;
    }

    public void setType(HintType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchHint)) return false;

        SearchHint that = (SearchHint) o;

        return getName().equals(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
