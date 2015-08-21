package com.polycom.vega.fundamental;

/**
 * Created by xwcheng on 8/21/2015.
 */
public class OptionObject {
    private String title;
    private int iconId;
    private int fragmentId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public OptionObject(int iconId, String title, int fragmentId) {
        this.iconId = iconId;
        this.title = title;
        this.fragmentId = fragmentId;
    }
}
