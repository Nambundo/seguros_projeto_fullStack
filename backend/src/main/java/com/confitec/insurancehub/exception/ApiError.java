package com.confitec.insurancehub.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

/** Formato único de erro devolvido pela API. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(int status, String erro, Map<String, String> campos) {

    public static ApiError de(int status, String erro) {
        return new ApiError(status, erro, null);
    }
}
