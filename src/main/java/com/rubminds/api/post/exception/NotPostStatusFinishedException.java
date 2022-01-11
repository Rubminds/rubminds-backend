package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.BusinessException;

public class NotPostStatusFinishedException extends BusinessException {
    public NotPostStatusFinishedException() {
        super("아직 게시글의 상태가 finish가 아닙니다!.");
    }
}
