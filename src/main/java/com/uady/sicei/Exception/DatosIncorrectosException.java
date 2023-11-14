package com.uady.sicei.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DatosIncorrectosException {
    private final String message;
    private final String details;
}
