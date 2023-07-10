package kz.sberbank.service;

public class ResponseMessage<T> {

    public ResponseMessage(String code) {
        this.code = code;
    }

    private String code;
    private String message;
    private T responseObject;
}
