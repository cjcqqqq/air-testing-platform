package com.university.shenyang.air.testing.gateway.common.kit;


import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用数据传输对象
 *
 * @author lgw
 */
public class Packet implements Serializable {
    private static final long serialVersionUID = 1L;
    private long vehicleId;
    private int commandId;
    private int answerId;
    private String uniqueMark;// 设备标识码，长度17字节
    private int encrypt;
    private byte[] originalPacket;
    private int _content_position;
    private int _content_capacity;
    private byte[] content = new byte[0];
    private Map<Object, Object> parameters;
    private int times;// 该包当前转发次数，用于重发计数
    private long timestamp;// 该包上一次转发的时间，用于判断重发间隔

    public Packet() {
        super();
    }

    /**
     * 指定容量
     *
     * @param capacity {@link Integer}
     */
    public Packet(int capacity) {
        if (capacity > 0) {
            this.content = new byte[capacity];
            this._content_capacity = capacity;
        }
    }

    public Packet appendContent(byte[] bytes) {
        if (bytes.length + this._content_position > this._content_capacity) {
            throw new RuntimeException(
                    "Packet Content Capacity is not enough .");
        }
        ArraysUtils.arrayappend(this.content, this._content_position, bytes);
        this._content_position += bytes.length;
        return this;
    }

    /**
     * 添加一个自定义参数
     *
     * @param pKey   {@link String} Key
     * @param pValue {@link Object} Value
     * @return
     */
    public Packet addParameter(Object pKey, Object pValue) {
        if (this.parameters == null)
            this.parameters = new ConcurrentHashMap<Object, Object>();
        this.parameters.put(pKey, pValue);
        return this;
    }

    public Object getParamter(Object pKey) {
        if (this.parameters != null)
            return this.parameters.get(pKey);
        return null;
    }

    public String getParamterForString(Object pKey) {
        if (this.parameters != null) {
            Object value = this.parameters.get(pKey);
            return String.valueOf(value);
        }
        return null;
    }

    /**
     * 获取指令号的16进制形式
     */
    public String getCommandIdForHex() {
        return Convert.decimalToHexadecimal(this.commandId, 2);
    }
    public String getAnswerIdForHex() {
        return Convert.decimalToHexadecimal(this.answerId, 2);
    }

    /**
     * 获取数据内容体的16进制形式
     */
    public String getContentForHex() {
        return Convert.bytesToHexString(this.content);
    }

    public long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public int getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(int encrypt) {
        this.encrypt = encrypt;
    }

    public byte[] getOriginalPacket() {
        return originalPacket;
    }

    public void setOriginalPacket(byte[] originalPacket) {
        this.originalPacket = originalPacket;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static Packet copyPacket(Packet packet){
        Packet newPacket = new Packet();
        newPacket.setVehicleId(packet.getVehicleId());
        newPacket.setCommandId(packet.getCommandId());
        newPacket.setAnswerId(packet.getAnswerId());
        newPacket.setUniqueMark(packet.getUniqueMark());
        newPacket.setEncrypt(packet.getEncrypt());
        newPacket.setOriginalPacket(packet.getOriginalPacket());
        newPacket.setContent(packet.getContent());
        newPacket.setTimes(packet.getTimes());
        newPacket.setTimestamp(packet.getTimestamp());

        return newPacket;
    }

}
