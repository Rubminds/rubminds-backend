package com.rubminds.api.post.exception;

import com.rubminds.api.common.exception.BusinessException;

public class NotFullFinishedException extends BusinessException {
    public NotFullFinishedException() {
        super("아직 모든 팀원들이 finish 버튼을 누르지 않았습니다!.");
    }
}
