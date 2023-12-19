package com.myapps.note_it;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String content;
    com.google.firebase.Timestamp timestamp;

    public Note() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
