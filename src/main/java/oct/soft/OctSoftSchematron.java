/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package oct.soft;

import ch.qos.logback.core.CoreConstants;
import io.javalin.Javalin;
import io.javalin.http.Context;
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
        ValidateXML xmlValidator = new ValidateXML();           
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
        app.start(9090);
    }
}
