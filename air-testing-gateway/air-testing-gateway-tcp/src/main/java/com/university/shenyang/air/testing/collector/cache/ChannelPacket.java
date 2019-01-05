package com.university.shenyang.air.testing.collector.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chenjc on 2017/05/03.
 */
public class ChannelPacket {
    private static ChannelPacket instance = new ChannelPacket();
    private ChannelPacket(){}
    public static ChannelPacket getInstance (){
        return instance;
    }

    /**
     * key :channel : remoteAddress
     * value: byte[] : 不完整数据包
     */
    private static Map<String, byte[]> packets = new ConcurrentHashMap<>();

    public void add(String remoteAddress, byte[] bytes){
        packets.put(remoteAddress, bytes);
    }

    public byte[] get(String remoteAddress){
        return packets.get(remoteAddress);
    }

    public void remove(String remoteAddress){
        packets.remove(remoteAddress);
    }
}
