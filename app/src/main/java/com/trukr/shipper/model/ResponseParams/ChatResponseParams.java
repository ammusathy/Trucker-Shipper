package com.trukr.shipper.model.ResponseParams;

/**
 * Created by nijamudhin on 7/11/2016.
 */
public class ChatResponseParams  {
    public String ChatId;
    public String UserId;
    public String message;
    public String image;
    public String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getChatId() {
        return ChatId;
    }

    public void setChatId(String chatId) {
        ChatId = chatId;
    }


}
