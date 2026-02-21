package beans;

import java.time.LocalDate;
import org.json.JSONObject;

public class StudentBean implements java.io.Serializable {
    private String nume = null;
    private String prenume = null;
    private int varsta = 0;

    private boolean populated = false;

    public StudentBean() {
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public boolean getPopulated(){
        return this.populated;
    }

    public void setPopulated(boolean populated){
        this.populated = populated;
    }

    @Override
    public String toString(){
        return "Nume: " + this.nume + 
        "; Prenume: " + this.prenume + 
        "; Varsta: " + Integer.toString(this.varsta) + 
        "; An nastere: " + Integer.toString(LocalDate.now().getYear() - this.varsta);
    }

    public JSONObject toJson(){
        var json = new JSONObject();
        try{
            json.put("nume", this.nume);
            json.put("prenume", this.prenume);
            json.put("varsta", this.varsta);
            json.put("an_nastere",LocalDate.now().getYear() - this.varsta);
        }
        catch(Exception e){
            throw new RuntimeException("Json conversion failed",e);
        }
        return json;
    }

}