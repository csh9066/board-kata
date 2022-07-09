package boardkata.sever.web;

import boardkata.sever.dto.ErrorResponse;
import boardkata.sever.exception.AuthenticationException;
import boardkata.sever.exception.UserEmailDuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailDuplicationException() {
        return new ErrorResponse("중복된 이메일입니다. 다시 한번 확인해주세요.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException() {
        return new ErrorResponse("로그인 인증에 실패했습니다. 이메일이나 패스워드를 다시 한번 확인해주세요.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = new ErrorResponse("올바르지 못한 요청 값입니다.");

        BindingResult bindingResult = e.getBindingResult();

        bindingResult.getFieldErrors()
                .stream()
                .forEach((fieldError -> {
                    response.addMessage(fieldError.getDefaultMessage());
                }));

        return response;
    }
}
