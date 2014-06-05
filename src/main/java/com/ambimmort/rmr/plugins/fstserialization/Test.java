/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.plugins.fstserialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author 定巍
 */
public class Test {

    public static void main(String[] args) {
        IoBuffer in = IoBuffer.allocate(1024 * 1024);
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(User.class);

        User u = new User();
        Output output = new Output(1024,Integer.MAX_VALUE);
        kryo.writeClassAndObject(output, u);

        System.out.println(output);
//        System.out.println(output.getByteBuffer());
//        in.putInt(output.getByteBuffer().position());
//        in.put(output.getByteBuffer());

        System.out.println(in);

        System.out.println(output.position());
        
      
        Input input = new Input(output.getBuffer());
        Object o = kryo.readClassAndObject(input);
        System.out.println(o);
        
        kryo.writeClassAndObject(output, u);
        input = new Input(output.getBuffer());
        o = kryo.readClassAndObject(input);
        System.out.println(o);

    }
}
