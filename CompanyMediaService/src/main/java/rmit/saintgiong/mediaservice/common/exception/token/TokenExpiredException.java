package rmit.saintgiong.mediaservice.common.exception.token;

// Exception thrown when a token has expired.
public class TokenExpiredException extends RuntimeException {
    
    public TokenExpiredException(String message) {
        super(message);
    }
    
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
