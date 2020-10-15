package restfulapi.spring.web.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(Long id) {
        super("Maaf Order dengan kode "+id+": Tidak ditemukan");
    }
}
