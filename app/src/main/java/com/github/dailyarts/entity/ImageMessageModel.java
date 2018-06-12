package com.github.dailyarts.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class ImageMessageModel {
    private Integer errorCode;
    private String errorMsg;
    private ImageModel data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ImageModel getData() {
        return data;
    }

    public void setImageModel(ImageModel data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
