package com.brogrammers.tutionbd.beans;

import java.io.Serializable;

public class Slider implements Serializable {
    String link;

    public Slider() {
    }

    public Slider(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
