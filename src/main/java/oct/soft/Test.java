/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oct.soft;

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.error.level.ErrorLevel;
import com.helger.commons.error.level.IErrorLevel;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.svrl.AbstractSVRLMessage;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.transform.stream.StreamSource;
import oct.soft.model.ValidationResult;

/**
 *
 * @author Octavian
 */
public class Test {
    public static void main(String[] args) {
        LocalDate ld = LocalDate.now();
        List<DayOfWeek> dow = new ArrayList<>(Arrays.asList(DayOfWeek.MONDAY,DayOfWeek.WEDNESDAY,DayOfWeek.SUNDAY));
        System.out.println(dow.contains(ld.getDayOfWeek()));
    }
    public static void mainValidate(String[] args) throws Exception {
        SchematronResourcePure  schResPure = SchematronResourcePure.fromFile("etransport/eTransport-validation_v.2.0.1_08022022.sch");
         ValidationResult validationResult = new ValidationResult();
         System.out.println(schResPure.isValidSchematron());
         if (schResPure != null) {
            try {
//                StringReader sr = new StringReader(xmlContent);
                SchematronOutputType oType = schResPure.applySchematronValidationToSVRL(new StreamSource(new File("test.xml")));
                ICommonsList<AbstractSVRLMessage> fAsserts = (ICommonsList<AbstractSVRLMessage>) SVRLHelper.getAllFailedAssertionsAndSuccessfulReports(oType);
                if (!fAsserts.isEmpty()) {
                    
//                    validationResult.setStatus(ValidationResult.NOT_OK);
                    fAsserts.stream().forEach(fa -> {
//                        validationResult.getErrors().add(fa.getText());     
                        IErrorLevel errLevel =  fa.getFlag();
                        if(errLevel.isError()) {
                        System.out.println(errLevel.getID()+" "+fa.getText());
                        }
                    });
                }else{
                    System.out.println("Totul este perfect");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                validationResult.setStatus(ValidationResult.NOT_OK);
                validationResult.getErrors().add("Eroare Tehnica: " + ex.getMessage());
            }
        }         
    }
}
