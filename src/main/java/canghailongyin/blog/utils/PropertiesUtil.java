package canghailongyin.blog.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Properties Util class.
 * Tom Zhuo update in 2017.3.27 to enable constant substitution in property values <br/>
 * by constructing XProperties instance for <code>private static Properties properties</code>
 */
public class PropertiesUtil {

    /**
     * path of default configure properties file
     */
    public static String CONFIG_PROPERTIES = "/conf.properties";

    private static Properties properties = new XProperties();

    static {
        //load configure property file
        try {
            properties.load(properties.getClass().getResourceAsStream(CONFIG_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
            properties = null;
        }
    }

    /**
     * get value by key with a default value.
     * @param key key String to index property
     * @param defaultValue default value to return if key do not match
     * @return property value
     */
    public static String getValue(String key, String defaultValue) {
        if (properties != null)
            return properties.getProperty(key, defaultValue);
        return "";
    }

}