package oct.soft.validator;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.IErrorLevel;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicInteger;
import javax.xml.transform.stream.StreamSource;
import oct.soft.model.ValidationKind;
import oct.soft.model.ValidationResult;

/**
 *
 * @author Octavian
 */
public class ValidateXML {
    
    private  ISchematronResource eFacturaResSch = null;
    private  ISchematronResource eTransportResSch = null;
    
    public ValidateXML() {        
        eFacturaResSch = getEFacturaResSch();
        eTransportResSch = getETransportResSch();
    }
    
    public  ValidationResult validateXml(String xmlContent, ValidationKind validationKind) {
        
        ValidationResult validationResult = new ValidationResult();
        ISchematronResource schResPure = null;
        switch (validationKind) {
            case E_FACTURA:
                schResPure = eFacturaResSch;
                break;
            case E_TRANSPORT:
                schResPure = eTransportResSch;
                break;
            default:
                validationResult.setStatus(ValidationResult.NOT_OK);
                validationResult.getErrors().add("Validarea trimisa ca parametru nu exista !");
                break;
        }
        if (schResPure != null) {
            try {
                StringReader sr = new StringReader(xmlContent);
                SchematronOutputType oType = schResPure.applySchematronValidationToSVRL(new StreamSource(sr));
                ICommonsList<AbstractSVRLMessage> fAsserts = (ICommonsList<AbstractSVRLMessage>) SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(oType);
                final AtomicInteger errCnt = new AtomicInteger(0);
                if (!fAsserts.isEmpty()) {                    
                    fAsserts.stream().forEach(fa -> {
                        IErrorLevel errLevel = fa.getFlag();
                        if (errLevel.isError()) {                            
                            validationResult.getErrors().add("codEroare="+fa.getID()+"; localizare="+fa.getLocation() +"; eroare="+fa.getText());
                            errCnt.getAndIncrement();
                        }                        
                    });
                    if (errCnt.get() > 0) {
                        validationResult.setStatus(ValidationResult.NOT_OK);
                    }
                }
                
            } catch (Exception ex) {
                validationResult.setStatus(ValidationResult.NOT_OK);
                validationResult.getErrors().add("Eroare Tehnica: " + ex.getMessage());
            }
        }
        return validationResult;
    }
    
    private  ISchematronResource getEFacturaResSch() {
        System.out.println("Initializez !!!");
        if (eFacturaResSch == null) {            
            eFacturaResSch = SchematronResourcePure.fromFile("efactura/EN16931-CIUS_RO-UBL-validation.sch");            
        }
        return eFacturaResSch;
    }
    
    private  ISchematronResource getETransportResSch() {
        if (eTransportResSch == null) {
            eTransportResSch = SchematronResourcePure.fromFile("etransport/eTransport-validation_v.2.0.1_08022022.sch");            
        }
        return eTransportResSch;
    }
}
