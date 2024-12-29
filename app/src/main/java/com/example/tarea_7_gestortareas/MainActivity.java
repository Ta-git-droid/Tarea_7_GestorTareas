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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;  // RecyclerView para mostrar la lista de tareas
    private TareaAdapter adapter;       // Adaptador para manejar la lista de tareas en el RecyclerView
    private List<Tarea> tareaList;      // Lista de tareas que se mostrará en el RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        EdgeToEdge.enable ( this );
        setContentView ( R.layout.activity_main );
        ViewCompat.setOnApplyWindowInsetsListener ( findViewById ( R.id.activity_main ) , (v , insets) -> {
            Insets systemBars = insets.getInsets ( WindowInsetsCompat.Type.systemBars () );
            v.setPadding ( systemBars.left , systemBars.top , systemBars.right , systemBars.bottom );
            return insets;
        } );

        // Inicializar el RecyclerView y el FloatingActionButton
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Inicializar la lista de tareas y el adaptador
        tareaList = new ArrayList<> ();
        adapter = new TareaAdapter(tareaList, this::showBottomSheet);

        // Configurar el RecyclerView con un LinearLayoutManager (para un diseño de lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager (this));
        recyclerView.setAdapter(adapter); // Establecer el adaptador en el RecyclerView

        // Configurar el botón flotante para agregar una nueva tarea
        fab.setOnClickListener(v -> showAddHomeworkDialog(null));

        // Cargar las tareas iniciales y ordenarlas por asignatura y fecha
        agregarTareasIniciales();
        cargarYOrdenarTareas();
    }

    private void agregarTareasIniciales() {
        // Crear tareas predeterminadas y agregarlas a la lista
        List<Tarea> tareasIniciales = new ArrayList<>();
        tareasIniciales.add(new Tarea("PMDM", "Tarea 1", "Tarea de programación", "10/01/2024", "10:00", false));
        tareasIniciales.add(new Tarea("AD", "Tarea 2", "Estudiar conceptos de programación", "11/01/2024", "14:00", false));
        tareasIniciales.add(new Tarea("PMDM", "Tarea 3", "Investigar sobre la arquitectura de sistemas", "12/01/2024", "16:00", false));
        tareasIniciales.add(new Tarea("PDP", "Tarea 4", "Realizar el informe de prácticas", "13/01/2024", "11:00", false));
        tareasIniciales.add(new Tarea("DI", "Tarea5", "Preparar la presentación de la asignatura", "14/01/2024", "09:00", false));
        // Agregar las tareas iniciales a la lista principal
        tareaList.addAll(tareasIniciales);
    }

    private void cargarYOrdenarTareas() {
        // Ordenar la lista de tareas primero por asignatura y luego por fecha y hora de entrega
        tareaList.sort(new Comparator<Tarea> () {
            @Override
            public int compare(Tarea t1, Tarea t2) {
                // Comparar por asignatura
                int asignaturaComparison = t1.getAsignatura().compareTo(t2.getAsignatura());
                if (asignaturaComparison != 0) {
                    return asignaturaComparison;
                }
                // Si las asignaturas son iguales, comparar por fecha de entrega
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date fechaTarea1 = dateFormat.parse(t1.getFechaEntrega());
                    Date fechaTarea2 = dateFormat.parse(t2.getFechaEntrega());
                    // Si las fechas son iguales, comparar por hora de entrega
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Date horaTarea1 = timeFormat.parse(t1.getHoraEntrega());
                    Date horaTarea2 = timeFormat.parse(t2.getHoraEntrega());
                    // Comparar por hora, si es necesario
                    int horaComparison = horaTarea1.compareTo(horaTarea2);
                    if (horaComparison != 0) {
                        return horaComparison;
                    }
                    // Comparar por fecha
                    return fechaTarea1.compareTo(fechaTarea2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // En caso de error al parsear la fecha
                }
            }
        });
        // Notificar al adaptador que la lista de tareas ha cambiado
        adapter.notifyDataSetChanged();
    }

    private void showAddHomeworkDialog(Tarea tareaToEdit) {
        // Mostrar el diálogo para agregar o editar una tarea
        NuevaTareaDialogFragment dialog = new NuevaTareaDialogFragment();
        if (tareaToEdit != null) {
            // Si estamos editando una tarea, pasar los datos de la tarea a editar al diálogo
            Bundle args = new Bundle();
            args.putParcelable("homework", tareaToEdit);
            dialog.setArguments(args);
        }
        // Configurar el listener para guardar la tarea después de agregarla o editarla
        dialog.setOnTareaGuardadaListener(tarea -> {
            if (tareaToEdit == null) {
                // Si no estamos editando, agregar la nueva tarea a la lista
                tareaList.add(tarea);
            } else {
                // Si estamos editando, actualizar la tarea existente en la lista
                tareaList.set(tareaList.indexOf(tareaToEdit), tarea);
            }
            // Reordenar las tareas después de agregar o editar y actualizar el RecyclerView
            cargarYOrdenarTareas();
        });
        // Mostrar el diálogo
        dialog.show(getSupportFragmentManager(), "AddHomeworkDialog");
    }

    private void showBottomSheet(Tarea tarea) {
        // Mostrar un BottomSheet con opciones para editar, eliminar o marcar como completada
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.tareas_opciones, null);
        // Configurar la opción de editar tarea
        view.findViewById(R.id.editOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showAddHomeworkDialog(tarea); // Mostrar el diálogo para editar la tarea
        });
        // Configurar la opción de eliminar tarea
        view.findViewById(R.id.deleteOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            showDeleteConfirmation(tarea); // Mostrar confirmación de eliminación
        });
        // Configurar la opción de marcar tarea como completada
        view.findViewById(R.id.completeOption).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            tarea.setEstaCompletada(true); // Marcar la tarea como completada
            adapter.notifyDataSetChanged(); // Notificar al adaptador para actualizar la lista
            Toast.makeText(this, "Tarea marcada como completada", Toast.LENGTH_SHORT).show();
        });
        // Establecer el contenido del BottomSheet y mostrarlo
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showDeleteConfirmation(Tarea tarea) {
        // Mostrar un AlertDialog para confirmar la eliminación de la tarea
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar este deber?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    tareaList.remove(tarea); // Eliminar la tarea de la lista
                    cargarYOrdenarTareas(); // Reordenar y actualizar la lista de tareas
                })
                .setNegativeButton("Cancelar", null) // Cancelar la eliminación
                .show();
    }
}