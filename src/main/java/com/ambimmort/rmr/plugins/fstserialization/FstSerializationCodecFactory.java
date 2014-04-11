/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.rmr.plugins.fstserialization;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.serialization.ObjectSerializationDecoder;
import org.apache.mina.filter.codec.serialization.ObjectSerializationEncoder;

/**
 *
 * @author 定巍
 */
public class FstSerializationCodecFactory implements ProtocolCodecFactory {

    private final ObjectSerializationEncoder encoder;

    private final ObjectSerializationDecoder decoder;

    /**
     * Creates a new instance with the {@link ClassLoader} of the current
     * thread.
     */
    public FstSerializationCodecFactory() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a new instance with the specified {@link ClassLoader}.
     */
    public FstSerializationCodecFactory(ClassLoader classLoader) {
        encoder = new ObjectSerializationEncoder();
        decoder = new ObjectSerializationDecoder(classLoader);
    }

    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }

    /**
     * Returns the allowed maximum size of the encoded object. If the size of
     * the encoded object exceeds this value, the encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     * <p>
     * This method does the same job with
     * {@link ObjectSerializationEncoder#getMaxObjectSize()}.
     */
    public int getEncoderMaxObjectSize() {
        return encoder.getMaxObjectSize();
    }

    /**
     * Sets the allowed maximum size of the encoded object. If the size of the
     * encoded object exceeds this value, the encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     * <p>
     * This method does the same job with
     * {@link ObjectSerializationEncoder#setMaxObjectSize(int)}.
     */
    public void setEncoderMaxObjectSize(int maxObjectSize) {
        encoder.setMaxObjectSize(maxObjectSize);
    }

    /**
     * Returns the allowed maximum size of the object to be decoded. If the size
     * of the object to be decoded exceeds this value, the decoder will throw a
     * {@link BufferDataException}. The default value is <tt>1048576</tt> (1MB).
     * <p>
     * This method does the same job with
     * {@link ObjectSerializationDecoder#getMaxObjectSize()}.
     */
    public int getDecoderMaxObjectSize() {
        return decoder.getMaxObjectSize();
    }

    /**
     * Sets the allowed maximum size of the object to be decoded. If the size of
     * the object to be decoded exceeds this value, the decoder will throw a
     * {@link BufferDataException}. The default value is <tt>1048576</tt> (1MB).
     * <p>
     * This method does the same job with
     * {@link ObjectSerializationDecoder#setMaxObjectSize(int)}.
     */
    public void setDecoderMaxObjectSize(int maxObjectSize) {
        decoder.setMaxObjectSize(maxObjectSize);
    }
}
