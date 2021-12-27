package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class AlreadyExitException extends EntityNotFoundException {
    public AlreadyExitException(){
        super("이미 존재하는 회원입니다.");
    }
}