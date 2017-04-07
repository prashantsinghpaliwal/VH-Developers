package com.example.hp_pc.vhdevelopers;

/**
 * Created by HP-PC on 11/9/2016.
 */

public class Feed {
    String title,desc,image;

    public Feed() {
    }

    public Feed(String desc, String image, String title) {

        this.desc = desc;
        this.image = image;
        this.title = title;
    }

    public String getDesc() {

        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
