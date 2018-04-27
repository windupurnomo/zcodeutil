package id.zcode.util.model;

public class Response {
    private Object data;
    private String message;
    private boolean isError;

    public Response(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public Response(Object data, String message, boolean isError) {
        this.data = data;
        this.message = message;
        this.isError = isError;
    }

    public Response(String message) {
        this.message = message;
        this.setError(true);
    }

    public Response(Object data) {
        this.data = data;
    }

    public Response() {
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
