package com.javalabs;

import java.util.HashMap;

public abstract class PromptDataStorage implements DataStorage {
    protected HashMap<String, Integer> integerParamsMap = new HashMap<String, Integer>();
    protected HashMap<String, Boolean> booleanParamsMap = new HashMap<String, Boolean>();

    protected HashMap<String, String> stringParamsMap = new HashMap<String, String>();
    public PromptDataStorage() {
    }
    public PromptDataStorage(String[] paramList) {
    }

    public abstract boolean setValue(String key, Object value);
    public abstract Object getValue(String key);

}
