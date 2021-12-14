package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class TeamUserNotFoundException extends EntityNotFoundException {
    public TeamUserNotFoundException(){
        super("존재하지 않는 팀원입니다.");
    }
}