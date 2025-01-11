package com.example.tarea_7_gestortareas;

import android.os.Parcel;
import android.os.Parcelable;

// Clase Tarea: representa una tarea que puede ser asignada y completada.
// Implementa Parcelable para permitir que se envíen objetos Tarea entre componentes de Android (como Activities y Fragments).
public class Tarea implements Parcelable {

    // Atributos de la clase Tarea:
    private String asignatura;  // Nombre de la asignatura (por ejemplo, PMDM, AD, etc.).
    private String titulo;      // Título o nombre de la tarea.
    private String descripcion; // Descripción detallada de la tarea.
    private String fechaEntrega; // Fecha en la que la tarea debe entregarse (formato: dd/MM/yyyy).
    private String horaEntrega; // Hora en la que la tarea debe entregarse (formato: HH:mm).
    private boolean estaCompletada; // Indica si la tarea está completada (true) o pendiente (false).


    // Constructor: se utiliza para crear un objeto Tarea con todos sus atributos.
    public Tarea(String asignatura, String titulo, String descripcion, String fechaEntrega, String horaEntrega, boolean estaCompletada) {
        this.asignatura = asignatura;       // Inicializamos el nombre de la asignatura.
        this.titulo = titulo;               // Inicializamos el título de la tarea.
        this.descripcion = descripcion;     // Inicializamos la descripción de la tarea.
        this.fechaEntrega = fechaEntrega;   // Inicializamos la fecha de entrega.
        this.horaEntrega = horaEntrega;     // Inicializamos la hora de entrega.
        this.estaCompletada = estaCompletada; // Inicializamos el estado de la tarea.
    }


    // Métodos "getter" y "setter" para cada atributo:
    // Los "getters" permiten obtener el valor de un atributo.
    // Los "setters" permiten modificar el valor de un atributo.

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


    // Parcelable: permite "empaquetar" objetos Tarea para enviarlos entre Activities o Fragments.

    // Constructor protegido que crea un objeto Tarea a partir de un Parcel.
    // Un Parcel es una forma de "empaquetar" datos para transferirlos entre diferentes partes de la aplicación.
    protected Tarea(Parcel in) {
        asignatura = in.readString();  // Leemos la asignatura desde el Parcel.
        titulo = in.readString();      // Leemos el título desde el Parcel.
        descripcion = in.readString(); // Leemos la descripción desde el Parcel.
        fechaEntrega = in.readString(); // Leemos la fecha de entrega desde el Parcel.
        horaEntrega = in.readString(); // Leemos la hora de entrega desde el Parcel.
        estaCompletada = in.readByte() != 0; // Leemos el estado como byte y lo convertimos a booleano.
    }


    // Este objeto CREATOR es necesario para la implementación de Parcelable.
    // Permite crear nuevos objetos Tarea a partir de un Parcel.
    public static final Creator<Tarea> CREATOR = new Creator<Tarea>() {
        @Override
        public Tarea createFromParcel(Parcel in) {
            return new Tarea(in); // Crea un objeto Tarea a partir de los datos en el Parcel.
        }

        @Override
        public Tarea[] newArray(int size) {
            return new Tarea[size]; // Crea un array de objetos Tarea del tamaño especificado.
        }
    };


    // Método obligatorio para Parcelable. No es necesario usarlo en la mayoría de los casos.
    @Override
    public int describeContents() {
        return 0; // Retorna 0 porque no hay tipos especiales de contenido en este caso.
    }


    // Método que "escribe" los datos de la tarea en un Parcel.
    // Esto es lo que permite que se transfieran los datos entre componentes.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(asignatura);   // Escribe la asignatura en el Parcel.
        dest.writeString(titulo);       // Escribe el título en el Parcel.
        dest.writeString(descripcion);  // Escribe la descripción en el Parcel.
        dest.writeString(fechaEntrega); // Escribe la fecha de entrega en el Parcel.
        dest.writeString(horaEntrega);  // Escribe la hora de entrega en el Parcel.
        dest.writeByte((byte) (estaCompletada ? 1 : 0)); // Convierte el estado booleano a byte (1 para true, 0 para false).
    }
}