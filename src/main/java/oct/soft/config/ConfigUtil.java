/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oct.soft.config;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author osantau
 */
public class ConfigUtil {

    public ConfigUtil() {
    }

    public static Properties getConfig() {
        Properties appProp = new Properties();
        try {
            appProp.load(new FileInputStream("config/app.properties"));
        } catch (Exception ex) {
            Logger.getLogger(ConfigUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return appProp;
    }

}
