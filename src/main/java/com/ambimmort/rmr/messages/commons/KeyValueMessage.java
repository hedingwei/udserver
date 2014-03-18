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
public class KeyValueMessage implements Serializable {

    private Serializable key;
    private Serializable value;

    public KeyValueMessage(Serializable key, Serializable value) {
        this.key = key;
        this.value = value;
    }

    public Serializable getKey() {
        return key;
    }

    public void setKey(Serializable key) {
        this.key = key;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

}
