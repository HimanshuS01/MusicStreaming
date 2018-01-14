package com.agarwal.vinod.govindkigali.models;

/**
 * Created by anirudh on 28/12/17.
 */

public class Thought {

    int id;

    public Thought() {
    }

    String text;

    public Thought(int id, String text) {

        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
