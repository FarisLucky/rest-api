package restfulapi.spring.web.model;

public class WebResponse<T> {

    private int code;
    private String status;
    private T data;
}
