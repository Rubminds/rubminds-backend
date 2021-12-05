package com.rubminds.api.user.exception;

import com.rubminds.api.common.exception.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
