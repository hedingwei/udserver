/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.plugins.fstserialization;

//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryo.io.ByteBufferOutput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author 定巍
 */
public class FstSerializationEncoder extends ProtocolEncoderAdapter {

    private int maxObjectSize = Integer.MAX_VALUE; // 2GB

//    private static Kryo kryo = new Kryo();

//    private ByteBufferOutput output = new ByteBufferOutput(1024, maxObjectSize);

    private ByteBuffer buffer = null;
//
//    static {
//        kryo.setRegistrationRequired(false);
//    }

    /**
     * Creates a new instance.
     */
    public FstSerializationEncoder() {
        // Do nothing
    }

    /**
     * Returns the allowed maximum size of the encoded object. If the size of
     * the encoded object exceeds this value, this encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     */
    public int getMaxObjectSize() {
        return maxObjectSize;
    }

    /**
     * Sets the allowed maximum size of the encoded object. If the size of the
     * encoded object exceeds this value, this encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     */
    public void setMaxObjectSize(int maxObjectSize) {
        if (maxObjectSize <= 0) {
            throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
        }

        this.maxObjectSize = maxObjectSize;
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        if (!(message instanceof Serializable)) {
            throw new NotSerializableException();
        }

        IoBuffer buf = IoBuffer.allocate(64);
        buf.setAutoExpand(true);
        int oldPos = buf.position();
        buf.skip(4);
        FSTObjectOutput oo = new FSTObjectOutput(buf.asOutputStream());
        oo.writeObject(message);
        oo.flush();
        int newPos = buf.position();
        buf.position(oldPos);
        buf.putInt(newPos - oldPos - 4);
        buf.position(newPos);

        int objectSize = buffer.position() - 4;
        if (objectSize > maxObjectSize) {
            throw new IllegalArgumentException("The encoded object is too big: " + objectSize + " (> " + maxObjectSize
                    + ')');
        }
        out.write(buf);
    }

}
