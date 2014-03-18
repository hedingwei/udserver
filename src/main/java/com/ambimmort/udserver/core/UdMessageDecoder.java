/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * @author 定巍
 */
public class UdMessageDecoder extends CumulativeProtocolDecoder {

    @Override
    protected boolean doDecode(IoSession is, IoBuffer ib, ProtocolDecoderOutput pdo) throws Exception {
        UdRawMessage msg = null;
//        System.out.println("dddd:" + is.getAttribute("message"));

        if (is.containsAttribute("message")) {

            msg = (UdRawMessage) is.getAttribute("message");
            if (ib.remaining() >= msg.getLengthNeeded()) {
                msg.setBody(new byte[(int) msg.getLengthNeeded()]);
                ib.get(msg.getBody());
                pdo.write(msg);
                is.setAttribute("message", null);
                if (!ib.hasRemaining()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {

            while (ib.remaining() >= 16) {
                msg = new UdRawMessage();
                is.setAttribute("message", msg);
                ib.get(msg.getHeader());
//                System.out.println("msg-length:" + msg.getMessageLength());
                if (ib.remaining() >= msg.getLengthNeeded()) {
                    msg.setBody(new byte[(int) msg.getLengthNeeded()]);
                    ib.get(msg.getBody());
                    pdo.write(msg);
//                    System.out.println("msg.length=" + msg.getMessageLength());
                    is.setAttribute("message", null);
                    if (!ib.hasRemaining()) {
//                        System.out.println("yes1: remain:" + ib.remaining());
                        return true;
                    }
//                    else {
//                        System.out.println("yes: remain:" + ib.remaining());
//                        return false;
//                    }
                } else {
//                    System.out.println("yes2: remain:" + ib.remaining());
                    return false;
                }
            }
            {
//                System.out.println("yes4: remain:" + ib.remaining());
                return false;
            }
        }

    }

}
