package com.example.saqayatask.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    // Override the getErrorAttributes method to customize error attributes
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        // Call the superclass method to get the default error attributes
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        // Add custom error attributes
        errorAttributes.put("locale", webRequest.getLocale().toString());
        errorAttributes.put("success", Boolean.FALSE);
        errorAttributes.put("status", errorAttributes.get("error"));
        errorAttributes.put("exception", errorAttributes.get("message"));
        errorAttributes.put("details", Arrays.asList(errorAttributes.get("message")));

        // Remove unnecessary error attributes
        errorAttributes.remove("error");
        errorAttributes.remove("path");
        errorAttributes.remove("trace");

        // Return the modified error attributes
        return errorAttributes;
    }
}