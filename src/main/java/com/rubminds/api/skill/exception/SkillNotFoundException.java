package com.rubminds.api.skill.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class SkillNotFoundException extends EntityNotFoundException {
    public SkillNotFoundException(){
        super("존재하지 않는 기술입니다.");
    }
}
