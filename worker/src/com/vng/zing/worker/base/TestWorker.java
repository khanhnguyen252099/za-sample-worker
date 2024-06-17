package com.vng.zing.worker.base;

import com.vng.wmb.worker.AbstractZaloFunction;
import com.vng.wmb.worker.client.IZaloJobResult;
import com.vng.wmb.worker.packet.IZaloPacket;
import com.vng.wmb.worker.packet.ZaloPacketHeader;
import com.vng.wmb.worker.packet.ZaloPacketImpl;
import com.vng.wmb.worker.packet.ZaloPacketMessageType;
import com.vng.wmb.worker.packet.ZaloPacketSourceDestType;
import com.vng.zing.zgroupmedialib.common.utils.GzipHelper;
import com.vng.zing.zgroupmedialib.common.utils.ZBufferWrapper;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.mutable.MutableInt;

/**
 *
 * @author dungln
 * @param <H>
 */
public class TestWorker<H extends AbstractZaloFunction> {

	private final ArrayList<Class<? extends Object>> _prototype;
	private final Class<H> _hClass;
	private final String ByteBufferType = "java.nio.HeapByteBuffer";
	private final String _strNULL = "";
	private WorkerHeader _hdr = new WorkerHeader((short) 1, (byte) 2);

	public TestWorker(Class<H> hClass, Class<? extends Object>... types) {
		// init struct for input
		_hClass = hClass;
		_prototype = new ArrayList<Class<? extends Object>>();
		_prototype.addAll(Arrays.asList(types));
	}

	public void setHeader(WorkerHeader hdr) {
		_hdr = hdr;
	}

	private byte[] _buildPacket(Object... params) {
		MutableInt size = new MutableInt(0);
		// compute buffer size
		for (Object obj : params) {
			if (!updateSizeForBuffer(obj, size)) {
				return null;
			}
		}

		// build buffer
		byte[] bPacket = new byte[size.intValue()];
		ZBufferWrapper bwPacket = new ZBufferWrapper(bPacket);
		for (Object obj : params) {
			if (!updateBWPacket(obj, bwPacket)) {
				return null;
			}
		}
		return bPacket;
	}

	public String call(Object... params) {
                
		// travel for check type
		if (_prototype.size() != params.length) {
			System.out.println("Call params incorrect size by prototype");
			return _strNULL;
		}
		for (int idx = 0; idx < _prototype.size(); idx++) {
			Object a = params[idx];
			String aName = a.getClass().getName();
			String pName = _prototype.get(idx).getName();
			Class paramClass = _prototype.get(idx);

			if (pName.equals(ByteBuffer.class.getName()) && aName.equals(ByteBufferType)) {
				continue;
			}
			if (!aName.equals(pName) && !paramClass.isInstance(a)) {
				System.out.println(String.format(" param[%d] incorrect type: %s != %s",
						idx, aName, pName));
				return _strNULL;
			}
		}

		ZaloPacketHeader header = new ZaloPacketHeader(
				_hdr.ver, ZaloPacketMessageType.NULL, _hdr.seqId, _hdr.srcId,
				ZaloPacketSourceDestType.NULL, _hdr.cmd, _hdr.subCommand, _hdr.dstId,
				ZaloPacketSourceDestType.NULL, _hdr.globalMsgId, _hdr.i5);
		byte[] bPacket = _buildPacket(params);
		ZaloPacketImpl packet = new ZaloPacketImpl(header, bPacket);

		try {
			H handler = _hClass.newInstance();
			handler.setPacket(packet);
			IZaloJobResult result = handler.executeFunction();

			// decode result
			List<IZaloPacket> packetResult = result.getPacketResult();
			byte[] fData = packetResult.get(0).getData();
			// some trick to split data
			if (fData.length < 27) {
				System.out.println("Trick failed");
				return _strNULL;
			}
			byte[] data = Arrays.copyOfRange(fData, 27, fData.length);
			String comData = GzipHelper.decompress(data);
			//System.out.println("comData: " + comData);
			return comData;
		} catch (Exception ex) {
			System.out.println("call: " + ex.toString());
		}
		return _strNULL;
	}

	private boolean updateSizeForBuffer(Object obj, MutableInt size) {
		if (obj.getClass().equals(Integer.class
		)) {
			size.add(4);//Integer.BYTES;

		} else if (obj.getClass().equals(Short.class
		)) {
			size.add(2);//Short.BYTES;

		} else if (obj.getClass().equals(Byte.class
		)) {
			size.add(1);//Byte.BYTES;

		} else if (obj.getClass().equals(Long.class
		)) {
			size.add(8);// Long.BYTES;

		} else if (obj.getClass().equals(Double.class
		)) {
			size.add(8); //Double.BYTES;

		} else if (obj.getClass().equals(String.class
		)) {
			byte[] bObj = ((String) obj).getBytes();
			size.add(bObj.length + 4);
		} else if (obj instanceof List) {
			List<Object> objList = (List<Object>) obj;
			for (int i = 0; i < objList.size(); i++) {
				if (!updateSizeForBuffer(objList.get(i), size)) {
					return false;
				}
			}
		} else if (obj.getClass().getName().equals(ByteBufferType)) {
			//<!> care
			ByteBuffer data = (ByteBuffer) obj;
			size.add(data.remaining());
		} else {
			System.out.println("_buildPacket: Unknow type: " + obj.getClass().getName());
			return false;
		}
		return true;

	}

	private boolean updateBWPacket(Object obj, ZBufferWrapper bwPacket) {
		if (obj.getClass().equals(Integer.class
		)) {
			bwPacket.writeI32((Integer) obj);

		} else if (obj.getClass().equals(Short.class
		)) {
			bwPacket.writeI16((Short) obj);

		} else if (obj.getClass().equals(Byte.class
		)) {
			bwPacket.writeI8((Byte) obj);

		} else if (obj.getClass().equals(Long.class
		)) {
			bwPacket.writeI64((Long) obj);

		} else if (obj.getClass().equals(Double.class
		)) {
			bwPacket.writeDouble((Double) obj);

		} else if (obj.getClass().equals(String.class
		)) {
			String data = (String) obj;
			bwPacket.writeStringS4(data);
		} else if (obj instanceof List) {
			List<Object> objList = (List<Object>) obj;
			for (int i = 0; i < objList.size(); i++) {
				if (!updateBWPacket(objList.get(i), bwPacket)) {
					return false;
				}
			}
		} else if (obj.getClass().getName().equals(ByteBufferType)) {
			// <!>
			ByteBuffer data = (ByteBuffer) obj;
			byte[] bytes = new byte[data.remaining()];
			data.get(bytes);
			bwPacket.writeBytes(bytes);
		} else {
			System.out.println("_buildPacket: Unknow type: " + obj.getClass().getName());
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		List<Integer> a = new ArrayList();
		a.add(1);
		System.out.println("type " + a.getClass().equals(ArrayList.class
		));
		Object[] b = a.toArray();
		System.out.println("type " + b[0].getClass());

	}
}
