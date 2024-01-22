/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oct.soft.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Octavian
 * Clasa pt. reprezentarea rapsunsului validarii
 */
public class ValidationResult implements Serializable {
    public final static String OK = "ok";
    public final static String NOT_OK = "nok";
    private String status;
    private List<String> errors;

    public ValidationResult() {
        status=ValidationResult.OK;
        errors=new ArrayList<>();
    }
    
    public ValidationResult(String status, List<String> errros) {
        this.status = status;
        this.errors = errros;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrors() {
        if(errors==null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    public void setErrors(List<String> errros) {
        this.errors = errros;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValidationResult other = (ValidationResult) obj;
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return Objects.equals(this.errors, other.errors);
    }

    @Override
    public String toString() {
        return "ValidationResult{" + "status=" + status + ", errros=" + errors + '}';
    }
    
}
