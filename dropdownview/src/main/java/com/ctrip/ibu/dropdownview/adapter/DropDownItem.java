package com.ctrip.ibu.dropdownview.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * DropDown
 * Created by tangjie on 01,三月,2018
 */

public class DropDownItem implements Serializable{

    @Expose
    @SerializedName("label")
    private String label;

    @Expose
    @SerializedName("canDelete")
    private boolean canDelete;

    public DropDownItem(String label, boolean canDelete) {
        this.label = label;
        this.canDelete = canDelete;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) return false;

        DropDownItem item = (DropDownItem) obj;
        return label.equals(item.getLabel());
    }

    @Override
    public int hashCode() {
        int hashCode = label.hashCode() * 31;
        return hashCode;
    }

    @Override
    public String toString() {
        return label;
    }
}
