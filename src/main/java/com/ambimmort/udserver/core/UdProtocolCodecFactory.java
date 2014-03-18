/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 *
 * @author 定巍
 */
public class UdProtocolCodecFactory implements ProtocolCodecFactory {

    ProtocolDecoder decoder = null;

    public UdProtocolCodecFactory() {
        decoder = new UdMessageDecoder();
    }

    public ProtocolEncoder getEncoder(IoSession is) throws Exception {
        return null;
    }

    public ProtocolDecoder getDecoder(IoSession is) throws Exception {
        return decoder;
    }

}
