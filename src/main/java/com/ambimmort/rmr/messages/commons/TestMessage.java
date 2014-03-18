/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.messages.commons;

import java.io.Serializable;

/**
 *
 * @author 定巍
 */
public class TestMessage implements Serializable {

    private String key = "dd";
    private String value = "ddd";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TestMessage{" + "key=" + key + ", value=" + value + '}';
    }

}
