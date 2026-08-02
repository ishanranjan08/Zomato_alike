package com.first.foodo.first_foodo.Exception;

public class ImageErrorException extends RuntimeException
{
    public ImageErrorException(String message) {
        super(message);
    }
    public ImageErrorException() {
        super("Error In file uploading");
    }
}
