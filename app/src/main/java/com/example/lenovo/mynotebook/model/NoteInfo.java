package com.example.lenovo.mynotebook.model;

import java.io.Serializable;

/*
笔记的数据类
实现了Serializable，可以添加到Bundle数据包里
 */
public class NoteInfo implements Serializable{
    private String name=null;
    private String content=null;
    private String Date=null;
    private String Type=null;//笔记的样式：“图文”或者“文字”
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return Date;
    }

    public String getType() {
        return Type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setType(String type) {
        Type = type;
    }
}
