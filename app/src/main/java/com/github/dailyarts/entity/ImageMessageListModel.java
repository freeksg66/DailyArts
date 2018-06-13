package com.github.dailyarts.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by legao005426 on 2018/6/13.
 */

public class ImageMessageListModel {
    private Integer errorCode;
    private String errorMsg;
    private List<ImageModel> data = null;
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

    public List<ImageModel> getData() {
        return data;
    }

    public void setData(List<ImageModel> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
