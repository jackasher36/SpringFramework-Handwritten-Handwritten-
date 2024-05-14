import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.myspringframework.core.ClassPathXmlApplication;

import java.io.InputStream;
import java.util.List;


public class MyspringTest {

    @Test
    public void mySpringTest() throws DocumentException {
        ClassPathXmlApplication classPathXmlApplication = new ClassPathXmlApplication("test.xml");
        Object user = classPathXmlApplication.getBean("user");
        System.out.println(user);

        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("test.xml");
        System.out.println(resourceAsStream);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(resourceAsStream);
        List list = document.selectNodes("//bean");
        System.out.println(list);

    }

    @Test
    public void xmlParsingTest() throws DocumentException {
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream("test.xml");
        System.out.println(resourceAsStream);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(resourceAsStream);
        List list = document.selectNodes("//bean");
        System.out.println(list);
    }


}
