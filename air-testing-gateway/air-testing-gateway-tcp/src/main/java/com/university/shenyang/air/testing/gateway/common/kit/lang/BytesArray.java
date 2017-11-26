package com.university.shenyang.air.testing.gateway.common.kit.lang;

import java.util.ArrayList;
import java.util.List;

public class BytesArray {
	private List<byte[]> arrays;
	private int arraysLength;

	public BytesArray() {
		if(this.arrays == null){
			this.arrays = new ArrayList<byte[]>();
			this.arraysLength = 0;
		}
	}
	public BytesArray append(byte[] bytes){
		this.arrays.add(bytes);
		this.arraysLength +=bytes.length;
		return this;
	}
	public byte[] get(){
		byte[] bytes = new byte[this.arraysLength];
		int index = 0;
		for (byte[] b : this.arrays) {
			ArraysUtils.arrayappend(bytes, index, b);
			index += b.length;
		}
		return bytes;
	}
}
