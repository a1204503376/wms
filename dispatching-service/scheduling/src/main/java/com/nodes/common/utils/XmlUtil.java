package com.nodes.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class XmlUtil {

    /**
     * JavaBean对象转换xml字符串
     */
    public static String objectToXML(Object object) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller m = context.createMarshaller();
            // 设置格式化输出
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            Writer w = new StringWriter();
            m.marshal(object, w);
            return w.toString();
        } catch (JAXBException e) {
            return StringPool.EMPTY;
        }
    }

    /**
     * JavaBean对象转换xml文件
     */
    public static File objectToXML(Object object, String fileName) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller m = context.createMarshaller();
        // 设置格式化输出
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        File file = new File(fileName);
        m.marshal(object, file);
        return file;
    }

    /**
     * xml字符串转换为JavaBean对象
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            StringReader sr = new StringReader(xml);
            return (T) um.unmarshal(sr);
        } catch (JAXBException e) {
            return null;
        }
    }

    /**
     * xml文件转换为JavaBean对象
     */
    public static <T> T xmlToBean(File file, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller um = context.createUnmarshaller();
        return (T) um.unmarshal(file);
    }

}
