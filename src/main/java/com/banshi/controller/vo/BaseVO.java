package com.banshi.controller.vo;

import com.google.gson.Gson;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * VO公共类
 */
public class BaseVO implements Serializable {

    public static final String RET_CODE_SUCCESS = "00";
    public static final String RET_CODE_CHECK_ERROR = "01";
    public static final String RET_CODE_LOGIC_ERROR = "02";
    public static final String RET_CODE_DB_ERROR = "80";
    public static final String RET_CODE_SYS_ERROR = "99";

    public String retCode;
    public String retMsg;
    public Map<String, Object> retErrorMap;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public Map<String, Object> getRetErrorMap() {
        return retErrorMap;
    }

    public void setRetErrorMap(Map<String, Object> retErrorMap) {
        this.retErrorMap = retErrorMap;
    }
}
