package com.ofidy.ofidyshoppingbrowser.ofidyExtra.constant;

/**
 * Created by Ari on 01/12/2016.
 */

public class Currency {

    private String id;
    private String name;
    private String code;

    public Currency(String id, String name, String code){
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
