package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class TeamNotFoundException extends EntityNotFoundException {
    public TeamNotFoundException(){
        super("존재하지 않는 팀입니다.");
    }
}