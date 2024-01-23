/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package oct.soft;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oct.soft.model.ValidationKind;
import oct.soft.model.ValidationResult;
import oct.soft.validator.ValidateXML;

/**
 *
 * @author Octavian
 */
public class OctSoftSchematron {

    public static void main(String[] args) throws Exception {                 
        Javalin app = Javalin.create(config->{
            config.defaultContentType="application/json";                 
         });
        app.attribute("xmlValidator", new ValidateXML());                        
        app.before("/validate/*", ctx-> {
            List<DayOfWeek> dowList = new ArrayList<>(Arrays.asList(DayOfWeek.MONDAY,DayOfWeek.WEDNESDAY,DayOfWeek.FRIDAY));
            LocalDate ld = LocalDate.now();
             ValidateXML xmlValidator =(ValidateXML) ctx.appAttribute("xmlValidator");
             if(xmlValidator == null || dowList.contains(ld.getDayOfWeek())){
                 xmlValidator = new ValidateXML();
                 app.attribute("xmlValidator",xmlValidator);
             } 
        });
        app.post("/validate/{tipValidare}",(Context ctx)->{
            String tipValidare = ctx.pathParam("tipValidare");
            String xmlContent = ctx.body();
            ValidateXML xmlValidator =(ValidateXML) ctx.appAttribute("xmlValidator");            
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
        app.start(9090);
    }
}
