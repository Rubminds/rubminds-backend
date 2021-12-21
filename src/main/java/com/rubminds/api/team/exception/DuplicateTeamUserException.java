package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class DuplicateTeamUserException extends InvalidValueException {
    public DuplicateTeamUserException(){
        super("이미 존재하는 팀유저입니다.");
    }
}