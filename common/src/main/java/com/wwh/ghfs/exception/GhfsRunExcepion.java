package com.wwh.ghfs.exception;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class GhfsRunExcepion extends RuntimeException {
    private String code;
    private Exception e;

    public GhfsRunExcepion(String code,Exception cause){
        super(code, cause);
    }

    public GhfsRunExcepion(String error){
        super(error);
    }

}
