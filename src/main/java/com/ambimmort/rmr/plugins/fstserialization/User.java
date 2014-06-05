/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.rmr.plugins.fstserialization;

import java.io.Serializable;

/**
 *
 * @author 定巍
 */
public class User implements Serializable{
    private String id = "dddd";
    private int age = 10;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
}
