package edu.progmatic.messageapp.modell;

import java.time.LocalDateTime;

public class Message {

    private Long id;
    private String author;
    private String text;
    private LocalDateTime creationDate;

    public Message() {
    }

    public Message(String author, String text, LocalDateTime creationDate) {
        this.author = author;
        this.text = text;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
