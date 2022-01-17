package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class PostStateException extends InvalidValueException {
    public PostStateException(){
        super("게시글을 삭제할 수 없는 상태입니다.");
    }
}
