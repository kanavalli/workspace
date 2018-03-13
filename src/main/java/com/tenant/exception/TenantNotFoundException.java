package com.tenant.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)

public class TenantNotFoundException extends RuntimeException {

	private String resourceName;
    private String fieldName;
    private Object fieldValue;

	
	
	public TenantNotFoundException(String exception) {
		super(exception);
	}
	
	public TenantNotFoundException( String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s %s ", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
