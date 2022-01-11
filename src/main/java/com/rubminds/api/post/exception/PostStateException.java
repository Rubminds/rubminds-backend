package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class PostStateException extends InvalidValueException {
    public PostStateException(){
        super("진행종료 단계가 아닙니다.");
    }
}
