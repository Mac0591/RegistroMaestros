package com.registro.registromaestros.model;

public class Profesor {

    private String csp;
    private String nombreProfesor;
    private String region;
    private String delegacionSindical;
    private String cartera;
    private String folio;
    private final int numFinal = 10;


    public Profesor() {
    }

    public Profesor(String csp,
                    String nombreProfesor,
                    String region,
                    String delegacionSindical,
                    String cartera,
                    String folio) {
        this.cartera = cartera;
        this.csp = csp;
        this.delegacionSindical = delegacionSindical;
        this.folio = folio;
        this.nombreProfesor = nombreProfesor;
        this.region = region;
    }

    public String getCartera() {
        return cartera;
    }

    public void setCartera(String cartera) {
        this.cartera = cartera;
    }

    public String getCsp() {
        return csp;
    }

    public void setCsp(String csp) {
        this.csp = csp;
    }

    public String getDelegacionSindical() {
        return delegacionSindical;
    }

    public void setDelegacionSindical(String delegacionSindical) {
        this.delegacionSindical = delegacionSindical;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public int getNumFinal() {
        return numFinal;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Profesor profesor = (Profesor) obj;
        return csp.equals(profesor.csp); // Comparar por ID (puedes cambiar esto si lo deseas)
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(Integer.parseInt(csp)); // Asegúrate de que hashCode esté sobreescrito también
    }
}
