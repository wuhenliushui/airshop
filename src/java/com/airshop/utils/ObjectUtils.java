package com.airshop.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtils {
	
	private static Log logger = LogFactory.getLog(ObjectUtils.class);
	/**
     * ���л� 
     * @param object
     * @return
     */
	public static <T> byte[] serialize(T object) {
    	byte[] bytes = null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            // ���л�
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            bytes = bos.toByteArray();         
        } catch (Exception e) {
        	logger.error("���л�ʧ�ܣ�\n"+e.getMessage());
        	return null;
        } finally {
        	try {
				oos.close();
				bos.close();
			} catch (IOException e) {}        	
        }
        return bytes;
    }

    /**
     * �����л�
     * @param bytes
     * @return
     */
	@SuppressWarnings("unchecked")
	public static <T> T unserialize(byte[] bytes) {
        T obj = null;
        ObjectInputStream ois = null;
        try {
            // �����л�
        	ois = new ObjectInputStream(
        			new ByteArrayInputStream(bytes));
        	obj = (T) ois.readObject();
        } catch (Exception e) {
        	logger.error("�����л�ʧ�ܣ�\n"+e.getMessage());
        	return null;
        } finally {
        	try {
				ois.close();
			} catch (IOException e) {}
        }
        return obj;
    }

}
