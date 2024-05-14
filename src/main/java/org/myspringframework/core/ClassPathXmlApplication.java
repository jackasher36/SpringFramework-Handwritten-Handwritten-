package org.myspringframework.core;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplication implements ApplicationContext {
    private static final Logger logger = LoggerFactory.getLogger(ClassPathXmlApplication.class);

    private Map<String, Object> singletonObjects = new HashMap<>();
    /**
     * 解析spring配置文件,初始化所有bean对象
     * @param configLocation spring配置文件路径
     */
    public ClassPathXmlApplication(String configLocation) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        InputStream resourceAsStream = ClassLoader.getSystemClassLoader().getResourceAsStream(configLocation);
        Document document = saxReader.read(resourceAsStream);
        List nodes = document.selectNodes("//bean");
        nodes.forEach(node -> {
            System.out.println("-------------------------------------------------------------------");

            try {
                Element element = (Element) node;
                String id = element.attributeValue("id");
                String aClass = element.attributeValue("class");
                logger.info("beanName = " + id);
                logger.info("beanClass = " + aClass);
                Class<?> aClass1 = Class.forName(aClass);
                Constructor<?> declaredConstructor = aClass1.getDeclaredConstructor();
                Object bean = declaredConstructor.newInstance();
                singletonObjects.put(id, bean);

                logger.info(singletonObjects.toString());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        nodes.forEach(node -> {
            System.out.println("--------------------------------------------------------------------------------------------------------------------");

            try {
                Element element = (Element) node;
                String id = element.attributeValue("id");
                String className = element.attributeValue("class");
                Class<?> aClass = Class.forName(className);
                List propertynodes = element.selectNodes("properties");
                propertynodes.forEach(property ->{

                    try {
                        Element propertyElement = (Element) property;
                        String name = propertyElement.attributeValue("name");
                        logger.info("属性名:" + name);
                        String setMethodName = "set" + name.toUpperCase().charAt(0) + name.substring(1);
                        Field declaredField = aClass.getDeclaredField(name);

                        logger.info("declaredFileld:" + declaredField);
                        Class<?> declaredFieldType = declaredField.getType();
                        logger.info("declaredFileldType:" + declaredFieldType);

                        logger.info("set赋值方法:" + setMethodName);
                        Method setMethod = aClass.getDeclaredMethod(setMethodName,declaredFieldType );
                        logger.info("set方法参数:" + declaredFieldType);

                        String value = propertyElement.attributeValue("value");
                        String ref = propertyElement.attributeValue("ref");

                        Object actualValue = null;
                        if (value != null) {

                            String simpleName = declaredField.getType().getSimpleName();
                            switch(simpleName) {
                                case "byte":
                                    actualValue = Byte.parseByte(value);
                                    break;
                                case "short":
                                    actualValue = Short.parseShort(value);
                                    break;
                                case "int":
                                    actualValue = Integer.parseInt(value);
                                    break;
                                case "long":
                                    actualValue = Long.parseLong(value);
                                    break;
                                case "float":
                                    actualValue = Float.parseFloat(value);
                                    break;
                                case "double":
                                    actualValue = Double.parseDouble(value);
                                    break;
                                case "boolean":
                                    actualValue = Boolean.parseBoolean(value);
                                    break;
                                case "char":
                                    if (value.length() == 1) {
                                        actualValue = value.charAt(0);
                                    } else {
                                        // 处理字符长度不为1的情况，这里简化为取第一个字符
                                        actualValue = value.charAt(0);
                                        // 或者抛出异常，视情况而定
                                        // throw new IllegalArgumentException("Invalid character value: " + value);
                                    }
                                    break;
                                case "Byte":
                                    actualValue = Byte.valueOf(value);
                                    break;
                                case "Short":
                                    actualValue = Short.valueOf(value);
                                    break;
                                case "Integer":
                                    actualValue = Integer.valueOf(value);
                                    break;
                                case "Long":
                                    actualValue = Long.valueOf(value);
                                    break;
                                case "Float":
                                    actualValue = Float.valueOf(value);
                                    break;
                                case "Double":
                                    actualValue = Double.valueOf(value);
                                    break;
                                case "Boolean":
                                    actualValue = Boolean.valueOf(value);
                                    break;
                                case "Character":
                                    if (value.length() == 1) {
                                        actualValue = value.charAt(0);
                                    }
                                    break;
                                case "String":
                                    actualValue = value;
                                    break;
                                default:
                                    // 处理未知数据类型的情况，这里简单地抛出异常
                                    throw new IllegalArgumentException("Unsupported data type: " + simpleName);
                            }

                            setMethod.invoke(singletonObjects.get(id),actualValue);
                        }
                        if (ref != null) {
                            setMethod.invoke(singletonObjects.get(id),singletonObjects.get(ref));
                        }


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }

    @Override
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }
}
