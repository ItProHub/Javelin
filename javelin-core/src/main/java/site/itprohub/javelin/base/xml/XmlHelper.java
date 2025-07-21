package site.itprohub.javelin.base.xml;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.StringReader;

public class XmlHelper {
    public static <T> T deserializeFromFile(Class<T> clazz, String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return clazz.cast(unmarshaller.unmarshal(new File(filePath)));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to deserialize XML from file: " + filePath, e);
        }
    }

     // ✅ 从字符串反序列化
    public static <T> T deserializeFromXml(Class<T> clazz, String xml) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return clazz.cast(unmarshaller.unmarshal(new StringReader(xml)));
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to deserialize XML from string.", e);
        }
    }
}
