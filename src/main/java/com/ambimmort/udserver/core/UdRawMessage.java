/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.udserver.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class UdRawMessage {

    private long id;
    private byte[] header = new byte[16];
    private byte[] body = null;
    private byte packetType;
    private byte packetSubType;
    private String sessionId;
    private String ip;
    private byte[] ipBytes;
    private int ipInt;

    public int getIpInt() {
        return ipInt;
    }

    public void setIpInt(int ipInt) {
        this.ipInt = ipInt;
    }

    public byte[] getIpBytes() {
        return ipBytes;
    }

    public void setIpBytes(byte[] ipBytes) {
        this.ipBytes = ipBytes;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPacketSubType(byte packetSubType) {
        this.packetSubType = packetSubType;
    }

    public void setPacketType(byte packetType) {
        this.packetType = packetType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void normalize() {
        setPacketType(header[8]);
        setPacketSubType(header[9]);
    }

    public byte getVersion() {
        return header[0];
    }

    public String getProtoSignature() {
        byte[] d = new byte[3];
        d[0] = header[1];
        d[1] = header[2];
        d[2] = header[3];
        return new String(d);
    }

    public byte[] getHeader() {
        return header;
    }

    public long getMessageLength() {

        return (header[15] & 0xFF | (header[14] & 0xFF) << 8
                | (header[13] & 0xFF) << 16 | (header[12] & 0xFF) << 24);
    }

    public byte getPacketType() {
        return header[8];
    }

    public byte getPacketSubType() {
        return header[9];
    }

    public long getLengthNeeded() {
        return getMessageLength() - 16;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(header);
            os.write(body);
        } catch (IOException ex) {
            Logger.getLogger(UdRawMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return os.toByteArray();
    }

    public ByteBuffer getByteBuffer() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            os.write(header);
            os.write(body);
        } catch (IOException ex) {
            Logger.getLogger(UdRawMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ByteBuffer.wrap(os.toByteArray());
    }

}
