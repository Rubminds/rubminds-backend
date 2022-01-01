package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class TeamOutOfBoundException extends InvalidValueException {
    public TeamOutOfBoundException(){
        super("팀 정원초과입니다.");
    }
}
