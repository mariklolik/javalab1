package com.javalabs;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.text.ParseException;

public class JDUPromptData extends PromptDataStorage {

    public JDUPromptData() {
        this.integerParamsMap.put("depth", 3);
        this.integerParamsMap.put("limit", Integer.MAX_VALUE);

        this.booleanParamsMap.put("symlinks", Boolean.FALSE);

        this.stringParamsMap.put("dir", System.getProperty("user.dir"));
    }

    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public JDUPromptData(String[] paramList) throws ParseException {
        this();
        for (int i = 0; i < paramList.length; ++i) {
            String param = paramList[i];
            if (param.startsWith("-")) {
                switch (param) {
                    case "--depth" -> this.setValue("depth", Integer.parseInt(paramList[++i]));
                    case "--limit" -> this.setValue("limit", Integer.parseInt(paramList[++i]));
                    case "-L" -> this.setValue("symlinks", Boolean.TRUE);
                    default -> throw new ParseException("Parameter " + param + " not found!", i);
                }
            } else {
                if (isValidPath(param)) {
                    this.setValue("dir", param);
                } else {
                    throw new ParseException("Wrong path!", i);
                }
            }
        }
    }

    private boolean isValidKey(String key) {
        return integerParamsMap.containsKey(key) || stringParamsMap.containsKey(key) || booleanParamsMap.containsKey(key);
    }

    @Override
    public boolean setValue(String key, Object value) throws ClassCastException {
        if (!this.isValidKey(key)) {
            return false;
        }
        if (value instanceof Integer) {
            this.integerParamsMap.put(key, (Integer) value);
        } else if (value instanceof Boolean) {
            this.booleanParamsMap.put(key, (Boolean) value);
        } else if (value instanceof String) {
            this.stringParamsMap.put(key, (String) value);
        } else {
            throw new ClassCastException();

        }
        return true;
    }

    @Override
    public Object getValue(String key) {
        if (!this.isValidKey(key)) {
            return null;
        }
        if (integerParamsMap.containsKey(key)) {
            return integerParamsMap.get(key);
        }

        if (booleanParamsMap.containsKey(key)) {
            return booleanParamsMap.get(key);
        }

        if (stringParamsMap.containsKey(key)) {
            return stringParamsMap.get(key);
        }
        return null;
    }


    @Override
    public boolean setValue(Object key, Object value) {
        return false;
    }

    @Override
    public boolean checkValue(Object key) {
        return false;
    }

    @Override
    public Object getValue(Object key) {
        return null;
    }
}
