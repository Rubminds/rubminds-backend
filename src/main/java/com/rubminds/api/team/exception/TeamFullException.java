package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class TeamFullException extends InvalidValueException {
    public TeamFullException(){
        super("팀 정원초과입니다.");
    }
}
