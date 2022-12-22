package com.johnson.employeeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ErrorDetails {
    public Date timestamp;
    private String message;
    private String path;
    private String errorCode;
}
