/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package oct.soft;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.Charset;
import java.util.Properties;
import oct.soft.model.ValidationKind;
import oct.soft.model.ValidationResult;
import oct.soft.validator.ValidateXML;

/**
 *
 * @author Octavian
 */
public class OctSoftSchematron {

    public static void main(String[] args) throws Exception {   
        Properties appProp = new Properties();
        appProp.load(new FileInputStream("config/app.properties"));
        int port = Integer.valueOf(appProp.getProperty("port")).intValue();
        Javalin app = Javalin.create(config->{
            config.defaultContentType="application/json";              
         });
        ValidateXML xmlValidator = new ValidateXML(); 
        app.get("/", ctx->{
        StringBuilder  sb = new StringBuilder();
        try{
         OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();
        sb.append("<p>Operating system:"
                    + "<br/>name: " + operatingSystemBean.getName()
                    + "<br/>version: " + operatingSystemBean.getVersion()
                    + "<br/>architecture: " + operatingSystemBean.getArch()+"</p>");

            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        sb.append("<p>Java runtime:"
                    + "<br/>name: " + runtimeBean.getVmName()
                    + "<br/>vendor: " + runtimeBean.getVmVendor()
                    + "<br/>version: " + runtimeBean.getSpecVersion()+"</p>");

            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            sb.append("<p>Memory limit:"
                    + "<br/>heap: " + memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024) + "mb"
                    + "<br/>snon-heap: " + memoryBean.getNonHeapMemoryUsage().getMax() / (1024 * 1024) + "mb"+"</p>");

            sb.append("<p>Character encoding: "
                    + System.getProperty("file.encoding") + " charset: " + Charset.defaultCharset()+"</p>");
        }
            catch(Exception ex)
                    {
                    sb.append("<p>Failded to get system info</p>");
                    }
        ctx.html(sb.toString());
        });
        app.post("/validate/{tipValidare}",(Context ctx)->{
            String tipValidare = ctx.pathParam("tipValidare");
            String xmlContent = ctx.body();           
            ValidationResult validationResult = new ValidationResult();
            switch (tipValidare) {
                case "efactura":
                    validationResult = xmlValidator.validateXml(xmlContent,ValidationKind.E_FACTURA);
                    break;
                case "etransport":
                    validationResult = xmlValidator.validateXml(xmlContent,ValidationKind.E_TRANSPORT);
                    break;
                default:
                    validationResult.setStatus(ValidationResult.NOT_OK);
                    validationResult.getErrors().add("Nu exista validare de tip: "+tipValidare);
                    break;
            }
            ctx.json(validationResult);
        });
        app.start(port);
    }
}
