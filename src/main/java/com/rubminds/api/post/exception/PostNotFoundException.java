package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class PostNotFoundException extends EntityNotFoundException {
    public PostNotFoundException() {
        super("존재하지 않는 게시글입니다.");
    }
}
