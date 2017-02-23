package com.xtel.ivipbusiness.model.entity;

/**
 * Created by Vulcl on 2/23/2017.
 */

public class Area {
    private boolean isSelected;
    private String name;

    public Area(boolean isSelected, String name) {
        this.isSelected = isSelected;
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
