package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.BusinessException;

public class NotFullFinishedException extends BusinessException {
    public NotFullFinishedException() {
        super("아직 모든 팀원들이 평가를 완료하지 않았습니다!.");
    }
}
