package com.yuyh.library.bean;

/**
 * @author yuyh
 * @date 2016/12/24
 */
public class Message {

    private String messagetext;

    private int textSize = -1;

    public Message(String messagetext) {
        this.setMessagetext(messagetext);
    }

    public Message(String messagetext, int textSize) {
        this.setMessagetext(messagetext);
        this.setTextSize(textSize);
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
