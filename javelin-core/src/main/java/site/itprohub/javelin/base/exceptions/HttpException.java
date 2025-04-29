package site.itprohub.javelin.base.exceptions;

public class HttpException extends Exception {
    public int statusCode;

    public int getErrorCode() {
        return statusCode;
    }

    public HttpException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode; 
    }
    
}
