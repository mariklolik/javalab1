package com.javalabs;

public interface DataStorage {
    boolean setValue(Object key, Object value);

    boolean checkValue(Object key);

    Object getValue(Object key);
}
