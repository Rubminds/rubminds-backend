package com.rubminds.api.team.exception;

import com.rubminds.api.common.exception.PermissionException;

public class NotAdminException extends PermissionException {
    public NotAdminException() {
        super();
    }
}
