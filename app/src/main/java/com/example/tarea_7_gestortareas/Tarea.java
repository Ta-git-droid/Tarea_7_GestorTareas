package com.example.tarea_7_gestortareas;

import android.os.Parcel;
import android.os.Parcelable;

public class Tarea implements Parcelable {

    // Atributos de la clase Tarea
    private String asignatura;  // Asignatura (PMDM, AD, etc.)
    private String titulo;      // Título del deber
    private String descripcion; // Descripción del deber
    private String fechaEntrega; // Fecha de entrega en formato dd/MM/yyyy
    private String horaEntrega; // Hora de entrega en formato HH:mm
    private boolean estaCompletada; // Estado del deber (completada o pendiente)

    // Constructor
    public Tarea(String asignatura, String titulo, String descripcion, String fechaEntrega, String horaEntrega, boolean estaCompletada) {
        this.asignatura = asignatura;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.estaCompletada = estaCompletada;
    }

    // Getters y Setters para acceder y modificar los atributos de la tarea
    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public boolean estaCompletada() {
        return estaCompletada;
    }

    public void setEstaCompletada(boolean estaCompletada) {
        this.estaCompletada = estaCompletada;
    }

    // Implementación de la interfaz Parcelable para poder enviar objetos Tarea entre componentes (Activities, Fragments, etc.)
    protected Tarea(Parcel in) {
        asignatura = in.readString();  // Lee la asignatura desde el Parcel
        titulo = in.readString();      // Lee el título desde el Parcel
        descripcion = in.readString(); // Lee la descripción desde el Parcel
        fechaEntrega = in.readString(); // Lee la fecha de entrega desde el Parcel
        horaEntrega = in.readString(); // Lee la hora de entrega desde el Parcel
        estaCompletada = in.readByte() != 0; // Lee el estado de completado (convierte el byte a booleano)
    }

    // Creator es necesario para la implementación de Parcelable y para la creación de nuevos objetos Tarea desde un Parcel
    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in); // Crea un nuevo objeto Tarea a partir del Parcel
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size]; // Crea un nuevo array de objetos Tarea
        }
    };

    @Override
    public int describeContents() {
        return 0; // Método obligatorio de Parcelable, devuelve 0 ya que no se manejan tipos especiales
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(asignatura);   // Escribe la asignatura en el Parcel
        dest.writeString(titulo);       // Escribe el título en el Parcel
        dest.writeString(descripcion);  // Escribe la descripción en el Parcel
        dest.writeString(fechaEntrega); // Escribe la fecha de entrega en el Parcel
        dest.writeString(horaEntrega);  // Escribe la hora de entrega en el Parcel
        dest.writeByte((byte) (estaCompletada ? 1 : 0)); // Escribe el estado de completado como un byte (1 para true, 0 para false)
    }
}