package com.rubminds.api.file.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class FileExtensionException extends InvalidValueException {
    public FileExtensionException(String fileName) { super(String.format("잘못된 형식의 파일입니다. (%s）", fileName)); }
}
