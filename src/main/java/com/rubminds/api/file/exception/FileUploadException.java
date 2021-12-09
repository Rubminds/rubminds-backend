package com.rubminds.api.file.exception;

import com.rubminds.api.common.exception.InvalidValueException;

public class FileUploadException extends InvalidValueException {
    public FileUploadException() { super("파일업로드 중 예외가 발생하였습니다.");  }
}
