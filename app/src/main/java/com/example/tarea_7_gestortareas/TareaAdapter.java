package com.example.tarea_7_gestortareas;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private final List<Tarea> listaTareas; // Lista de tareas que se va a mostrar
    private final OnTareaClickListener listener; // Interfaz para manejar los clics en las tareas

    // Constructor
    public TareaAdapter(List<Tarea> listaTareas, OnTareaClickListener listener) {
        this.listaTareas = listaTareas; // Asigna la lista de tareas que se mostrará
        this.listener = listener; // Asigna el listener para manejar los clics
    }

    // Método para crear el ViewHolder. Es llamado cuando se necesita un nuevo item para la lista.
    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout para un item de tarea
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view); // Creamos un nuevo ViewHolder con la vista inflada
    }

    // Método que enlaza los datos con las vistas de cada item en la lista.
    @SuppressLint("SetTextI18n") // Suprime la advertencia sobre el uso de concatenación en texto
    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        // Obtenemos la tarea en la posición actual
        Tarea tarea = listaTareas.get(position);
        // Asignamos los valores de la tarea a los TextViews del ViewHolder
        holder.asignaturaTextView.setText(tarea.getAsignatura());
        holder.tituloTextView.setText(tarea.getTitulo());
        holder.descripcionTextView.setText(tarea.getDescripcion());
        holder.fechaEntregaTextView.setText("Fecha de entrega: " + tarea.getFechaEntrega());
        holder.horaEntregaTextView.setText("Hora de entrega: " + tarea.getHoraEntrega());
        // Establecemos el estado de la tarea como "Completada" o "Pendiente"
        holder.estadoTextView.setText(tarea.estaCompletada() ? "Completada" : "Pendiente");

        // Establecemos un OnClickListener para que el usuario pueda interactuar con el item
        holder.itemView.setOnClickListener(v -> listener.onTareaClick(tarea));
    }

    // Método que devuelve el número de tareas en la lista
    @Override
    public int getItemCount() {
        return listaTareas.size(); // Devuelve el tamaño de la lista de tareas
    }

    // ViewHolder es una clase interna que mantiene las vistas de cada item
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        // Definimos los TextViews que se van a mostrar en cada item
        TextView asignaturaTextView;
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView fechaEntregaTextView;
        TextView horaEntregaTextView;
        TextView estadoTextView;
        // Constructor de ViewHolder, que recibe la vista de un item
        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asociamos las vistas a los TextViews definidos
            asignaturaTextView = itemView.findViewById(R.id.asignaturaTextView);
            tituloTextView = itemView.findViewById(R.id.tituloTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionTextView);
            fechaEntregaTextView = itemView.findViewById(R.id.fechaEntregaTextView);
            horaEntregaTextView = itemView.findViewById(R.id.horaEntregaTextView);
            estadoTextView = itemView.findViewById(R.id.estadoTextView);
        }
    }

    // Interfaz que define el comportamiento al hacer clic en una tarea
    public interface OnTareaClickListener {
        void onTareaClick(Tarea tarea); // Método que se invoca cuando el usuario hace clic en una tarea
    }
}