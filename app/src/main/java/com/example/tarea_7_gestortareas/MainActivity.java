package com.example.tarea_7_gestortareas;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // Vista para mostrar una lista de tareas
    private TareaAdapter adaptador;     // Adaptador para conectar la lista de tareas con la vista
    private List<Tarea> listaTareas;    // Lista que contiene las tareas a mostrar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Configurar diseño sin bordes (opcional)
        setContentView(R.layout.activity_main);

        // Configurar la disposición de la actividad principal para adaptarse a los bordes del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (vista, insets) -> {
            Insets bordesSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            vista.setPadding(bordesSistema.left, bordesSistema.top, bordesSistema.right, bordesSistema.bottom);
            return insets;
        });

        // Inicializar el RecyclerView y el botón flotante
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton botonFlotante = findViewById(R.id.fab);

        // Crear una lista vacía y un adaptador para las tareas
        listaTareas = new ArrayList<>();
        adaptador = new TareaAdapter(listaTareas, this::mostrarOpcionesTarea);

        // Configurar el RecyclerView con un diseño vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        // Configurar el botón flotante para agregar nuevas tareas
        botonFlotante.setOnClickListener(v -> mostrarDialogoTarea(null));

        // Agregar tareas de ejemplo y ordenarlas
        agregarTareasEjemplo();
        ordenarTareas();
    }


    /**
     * Agregar tareas de ejemplo a la lista.
     */
    private void agregarTareasEjemplo() {
        List<Tarea> tareasEjemplo = new ArrayList<>();
        tareasEjemplo.add(new Tarea("PMDM", "Tarea 1", "Ejercicio práctico", "10/01/2024", "10:00", false));
        tareasEjemplo.add(new Tarea("AD", "Tarea 2", "Estudiar conceptos básicos", "11/01/2024", "14:00", false));
        listaTareas.addAll(tareasEjemplo);
    }


    /**
     * Ordenar la lista de tareas por asignatura y luego por fecha y hora de entrega.
     */
    private void ordenarTareas() {
        listaTareas.sort((t1, t2) -> {
            int compararAsignatura = t1.getAsignatura().compareTo(t2.getAsignatura());
            if (compararAsignatura != 0) return compararAsignatura;

            try {
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha1 = formatoFecha.parse(t1.getFechaEntrega());
                Date fecha2 = formatoFecha.parse(t2.getFechaEntrega());
                return fecha1.compareTo(fecha2);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        });
        adaptador.notifyDataSetChanged(); // Actualizar la vista
    }


    /**
     * Mostrar un cuadro de diálogo para agregar o editar una tarea.
     */
    private void mostrarDialogoTarea(Tarea tareaAEditar) {
        NuevaTareaDialogFragment dialogo = new NuevaTareaDialogFragment();

        if (tareaAEditar != null) {
            Bundle argumentos = new Bundle();
            argumentos.putParcelable("homework", tareaAEditar);
            dialogo.setArguments(argumentos);
        }

        dialogo.setOnTareaGuardadaListener(tarea -> {
            if (tareaAEditar == null) listaTareas.add(tarea); // Agregar nueva tarea
            else listaTareas.set(listaTareas.indexOf(tareaAEditar), tarea); // Editar tarea existente

            ordenarTareas(); // Reordenar tareas y actualizar vista
        });

        dialogo.show(getSupportFragmentManager(), "DialogoTarea");
    }


    /**
     * Mostrar opciones (editar, eliminar o completar) para una tarea seleccionada.
     */
    private void mostrarOpcionesTarea(Tarea tarea) {
        BottomSheetDialog dialogoOpciones = new BottomSheetDialog(this);
        View vistaOpciones = getLayoutInflater().inflate(R.layout.tareas_opciones, null);

        vistaOpciones.findViewById(R.id.editOption).setOnClickListener(v -> {
            dialogoOpciones.dismiss();
            mostrarDialogoTarea(tarea);
        });

        vistaOpciones.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            dialogoOpciones.dismiss();
            confirmarEliminacion(tarea);
        });

        vistaOpciones.findViewById(R.id.completeOption).setOnClickListener(v -> {
            dialogoOpciones.dismiss();
            tarea.setEstaCompletada(true);
            adaptador.notifyDataSetChanged();
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
        });

        dialogoOpciones.setContentView(vistaOpciones);
        dialogoOpciones.show();
    }


    /**
     * Confirmar la eliminación de una tarea con un cuadro de diálogo.
     */
    private void confirmarEliminacion(Tarea tarea) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar", (dialogo, cual) -> {
                    listaTareas.remove(tarea);
                    ordenarTareas();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}