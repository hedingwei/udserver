/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.plugins.fstserialization;

import de.ruedigermoeller.serialization.FSTObjectInput;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 *
 * @author 定巍
 */
public class FstSerializationDecoder extends CumulativeProtocolDecoder {

    private final ClassLoader classLoader;

    private int maxObjectSize = 1048576; // 1MB

    /**
     * Creates a new instance with the {@link ClassLoader} of the current
     * thread.
     */
    public FstSerializationDecoder() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a new instance with the specified {@link ClassLoader}.
     */
    public FstSerializationDecoder(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader");
        }
        this.classLoader = classLoader;
    }

    /**
     * Returns the allowed maximum size of the object to be decoded. If the size
     * of the object to be decoded exceeds this value, this decoder will throw a
     * {@link BufferDataException}. The default value is <tt>1048576</tt> (1MB).
     */
    public int getMaxObjectSize() {
        return maxObjectSize;
    }

    /**
     * Sets the allowed maximum size of the object to be decoded. If the size of
     * the object to be decoded exceeds this value, this decoder will throw a
     * {@link BufferDataException}. The default value is <tt>1048576</tt> (1MB).
     */
    public void setMaxObjectSize(int maxObjectSize) {
        if (maxObjectSize <= 0) {
            throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
        }

        this.maxObjectSize = maxObjectSize;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (!in.prefixedDataAvailable(4, maxObjectSize)) {
            return false;
        }
        int length = in.getInt();
        int oldLimit = in.limit();
        in.limit(in.position() + length);
        try {
            FSTObjectInput os = new FSTObjectInput(in.asInputStream());
            out.write(os.readObject());
            return true;
        } finally {
            in.limit(oldLimit);
        }
        
    }
}
