package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class EvaluateException extends InvalidValueException {
    public EvaluateException(){
        super("이미 평가를 완료한 팀유저입니다.");
    }
}
