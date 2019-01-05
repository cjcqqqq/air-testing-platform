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
    private int commandId;
    private int answerId;
    private String uniqueMark;// 设备标识码，长度17字节
    private int encrypt;
    private int contentPosition;
    private int contentCapacity;
    private byte[] content = new byte[0];
    private Map<Object, Object> parameters;

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
            this.contentCapacity = capacity;
        }
    }

    public Packet appendContent(byte[] bytes) {
        if (bytes.length + this.contentPosition > this.contentCapacity) {
            throw new RuntimeException(
                    "Packet Content Capacity is not enough .");
        }
        ArraysUtils.arrayappend(this.content, this.contentPosition, bytes);
        this.contentPosition += bytes.length;
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
        if (this.parameters == null) {
            this.parameters = new ConcurrentHashMap<Object, Object>();
        }
        this.parameters.put(pKey, pValue);
        return this;
    }

    public Object getParamter(Object pKey) {
        if (this.parameters != null) {
            return this.parameters.get(pKey);
        }
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
