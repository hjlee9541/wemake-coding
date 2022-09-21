package com.wemake.codingtest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Output {
    //output
    private int quotient;
    private int remainder;

    //error ( 추후 biz exception )
    private String errorCode;
    private String errorMessage;
}
