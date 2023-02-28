package ru.practicum.shareit.exception.model;

public class ErrorResponse {
    private  String text;

    private  String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String text, String error) {
        this.text = text;
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
