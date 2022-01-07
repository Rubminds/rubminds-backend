package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class AdminDeleteException extends InvalidValueException{
    public AdminDeleteException(){
        super("팀장은 추방할 수 없습니다.");
    }
}
