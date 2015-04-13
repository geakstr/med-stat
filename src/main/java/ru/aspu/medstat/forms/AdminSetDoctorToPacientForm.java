package ru.aspu.medstat.forms;

public class AdminSetDoctorToPacientForm extends Form {
    private long pacient;
    private long doctor;

    public long getPacient() {
        return pacient;
    }
    public void setPacient(long pacient) {
        this.pacient = pacient;
    }
    public long getDoctor() {
        return doctor;
    }
    public void setDoctor(long doctor) {
        this.doctor = doctor;
    }
}