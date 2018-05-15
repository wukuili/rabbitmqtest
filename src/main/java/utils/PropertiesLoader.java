package utils;



import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 属性配置读取工具
 *
 * @author guo
 * 2018年2月28日11:44:27
 */
public class PropertiesLoader {

    public static Logger logger = Logger.getLogger(PropertiesLoader.class);

    private static Map<String, String> map = new HashMap<String, String>();

    private PropertiesLoader() {
    }

    private static class SingletonHolder {
        public static PropertiesLoader instance = new PropertiesLoader();
    }

    public static PropertiesLoader newInstance() {
        return SingletonHolder.instance;
    }

    public Properties getPros(String... resourcesPaths) {
        Properties pros = new Properties();
        for (String path : resourcesPaths) {
            try {
                pros.load(new BufferedReader(new InputStreamReader(PropertiesLoader.class.getClassLoader().getResourceAsStream(path), "UTF-8")));
                addToMap(pros,path);
            } catch (Exception e) {
                logger.error("load properties [" + path + "] error", e);
            } finally {
            }
        }

        return pros;
    }

    private void addToMap(Properties pros, String path) {
        StringBuffer sb = new StringBuffer();
        sb.append("----->成功从[");
        sb.append(path);
        sb.append( "]中加载到");
        sb.append( pros.size());
        sb.append("项配置属性-----------");
        sb.append("\n").append("{").append("\n");;
        for (String key : pros.stringPropertyNames()) {
            map.put(key, pros.getProperty(key));
            sb.append("\t").append(key).append("=").append(pros.getProperty(key)).append("\n");
        }
        sb.append("}");
        logger.info(sb.toString());
    }


    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private static String getValue(String key) {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) {
            return systemProperty;
        }
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return "";
    }

    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     */
    public static String getProperty(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * 取出String类型的Property，但以System的Property优先.如果都为Null则返回Default值.
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Integer getInteger(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Integer.valueOf(value);
    }

    /**
     * 取出Integer类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        String value = getValue(key);
        return StringUtils.isNotEmpty(value) ? Integer.valueOf(value) : defaultValue;
    }


    /**
     * 保留原来的getInt方法
     *
     * @param key
     * @return
     */
    public static int getIntProperty(String key) {
        return getInteger(key).intValue();
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null或内容错误则抛出异常.
     */
    public static Double getDouble(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Double.valueOf(value);
    }

    /**
     * 取出Double类型的Property，但以System的Property优先.如果都为Null则返回Default值，如果内容错误则抛出异常
     */
    public static Double getDouble(String key, Integer defaultValue) {
        String value = getValue(key);
        return StringUtils.isNotEmpty(value) ? Double.valueOf(value) : defaultValue;
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null抛出异常,如果内容不是true/false则返回false.
     */
    public static Boolean getBoolean(String key) {
        String value = getValue(key);
        if (value == null) {
            throw new NoSuchElementException();
        }
        return Boolean.valueOf(value);
    }

    /**
     * 取出Boolean类型的Property，但以System的Property优先.如果都为Null则返回Default值,如果内容不为true/false则返回false.
     */
    public static Boolean getBoolean(String key, boolean defaultValue) {
        String value = getValue(key);
        return StringUtils.isNotEmpty(value) ? Boolean.valueOf(value) : defaultValue;
    }


    public static void printAllPros() {
        StringBuffer sb = new StringBuffer();
        sb.append("-------全局加载到");
        sb.append( map.size());
        sb.append("项配置属性-----------");
        sb.append("\n").append("{").append("\n");
        Set<String> keyset = map.keySet();
        for (String key : keyset) {
            logger.info(key + "=" + map.get(key));
            sb.append("\t").append(key).append("=").append(map.get(key)).append("\n");
        }
        sb.append("}");
        logger.info(sb.toString());
    }

}
