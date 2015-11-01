package com.banshi.model.dto;

import com.google.gson.Gson;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Dto公共类
 */
public class BaseDTO implements Serializable {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
