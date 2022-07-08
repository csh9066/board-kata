package boardkata.sever.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s가 %s인 %s를 찾을 수 없습니다", fieldName, fieldValue, resourceName));
    }
}
