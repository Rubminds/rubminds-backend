package com.rubminds.api.user.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class DuplicateNicknameException extends InvalidValueException {
    public DuplicateNicknameException() { super("이미 사용중인 닉네임입니다."); }
}
