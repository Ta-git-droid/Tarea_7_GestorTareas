package com.example.tarea_7_gestortareas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class NuevaTareaDialogFragment extends DialogFragment {

    private EditText campoTitulo;        // Campo para ingresar el título de la tarea
    private EditText campoDescripcion;   // Campo para ingresar la descripción de la tarea
    private EditText campoFechaEntrega;  // Campo para seleccionar la fecha de entrega
    private EditText campoHoraEntrega;   // Campo para seleccionar la hora de entrega
    private Spinner spinnerAsignatura;   // Spinner para seleccionar la asignatura
    private NuevaTareaDialogFragment.OnTareaGuardadaListener listener; // Listener para manejar la acción de guardar la tarea
    private Tarea tareaAEditar;          // Tarea a editar, si existe

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Asegurarse de que getActivity() no sea null antes de crear el diálogo
        if (getActivity() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        // Crear el builder para el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Inflar el layout del diálogo con el archivo XML
        LayoutInflater layoutInflater = requireActivity().getLayoutInflater();
        View vista = layoutInflater.inflate(R.layout.agregar_tarea, null);
        // Inicializar los campos del formulario con las vistas correspondientes
        campoTitulo = vista.findViewById(R.id.titulo );
        campoDescripcion = vista.findViewById(R.id.descripcion );
        campoFechaEntrega = vista.findViewById(R.id.fecha );
        campoHoraEntrega = vista.findViewById(R.id.hora );
        spinnerAsignatura = vista.findViewById(R.id.Spinner );
        // Configurar el campo de fecha de entrega para que al hacer clic se muestre el DatePickerDialog
        campoFechaEntrega.setOnClickListener(v -> mostrarDatePickerDialog());
        // Configurar el campo de hora de entrega para que al hacer clic se muestre el TimePickerDialog
        campoHoraEntrega.setOnClickListener(v -> mostrarTimePickerDialog());
        // Si estamos editando una tarea existente, llenamos los campos con los datos de la tarea
        if (getArguments() != null) {
            tareaAEditar = getArguments().getParcelable("homework");
            if (tareaAEditar != null) {
                campoTitulo.setText(tareaAEditar.getTitulo());  // Asignamos el título de la tarea
                campoDescripcion.setText(tareaAEditar.getDescripcion()); // Asignamos la descripción
                campoFechaEntrega.setText(tareaAEditar.getFechaEntrega()); // Asignamos la fecha de entrega
                campoHoraEntrega.setText(tareaAEditar.getHoraEntrega()); // Asignamos la hora de entrega
                // Configuramos el spinner para que seleccione la asignatura correcta
                spinnerAsignatura.setSelection(getIndice(spinnerAsignatura, tareaAEditar.getAsignatura()));
            }
        }
        // Configuramos los botones de acción: Guardar y Cancelar
        Button botonGuardar = vista.findViewById(R.id.guardar );
        Button botonCancelar = vista.findViewById(R.id.cancelar );
        // Acción de guardar la tarea
        botonGuardar.setOnClickListener(v -> {
            if (validarEntradas()) { // Validamos las entradas antes de guardar
                // Crear una nueva tarea con los datos del formulario
                Tarea tarea = new Tarea(
                        spinnerAsignatura.getSelectedItem().toString(), // Asignatura seleccionada en el spinner
                        campoTitulo.getText().toString(),  // Título de la tarea
                        campoDescripcion.getText().toString(), // Descripción de la tarea
                        campoFechaEntrega.getText().toString(), // Fecha de entrega
                        campoHoraEntrega.getText().toString(), // Hora de entrega
                        false // Estado de la tarea (inicialmente no completada)
                );
                // Llamar al listener para guardar la tarea
                if (listener != null) {
                    listener.onTareaGuardada(tarea);
                }
                dismiss(); // Cerrar el diálogo
            }
        });
        // Acción de cancelar, simplemente cierra el diálogo
        botonCancelar.setOnClickListener(v -> dismiss());
        builder.setView(vista); // Establece la vista inflada en el diálogo
        return builder.create(); // Retorna el diálogo creado
    }

    // Método para obtener el índice de la asignatura seleccionada en el spinner
    private int getIndice(Spinner spinnerAsignatura, String asignatura) {
        for (int i = 0; i < spinnerAsignatura.getCount(); i++) {
            if (spinnerAsignatura.getItemAtPosition(i).toString().equalsIgnoreCase(asignatura)) {
                return i; // Retorna el índice de la asignatura seleccionada
            }
        }
        return 0;  // Retorna el primer índice si no se encuentra la asignatura
    }

    // Interfaz para manejar el evento de guardado de la tarea
    public interface OnTareaGuardadaListener {
        void onTareaGuardada(Tarea tarea); // Método que se invoca cuando la tarea es guardada
    }

    // Establecer el listener para el evento de tarea guardada
    public void setOnTareaGuardadaListener(NuevaTareaDialogFragment.OnTareaGuardadaListener listener) {
        this.listener = listener;
    }

    // Método para mostrar el DatePickerDialog cuando se hace clic en el campo de fecha
    private void mostrarDatePickerDialog() {
        Calendar calendar = Calendar.getInstance(); // Obtener la fecha actual
        // Asegurarse de que getContext() no sea null antes de mostrar el DatePicker
        if (getContext() == null) return;
        new DatePickerDialog (
                getContext(),
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Formateamos la fecha seleccionada y la mostramos en el campo de fecha
                    String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                    campoFechaEntrega.setText(fecha);
                },
                calendar.get(Calendar.YEAR), // Año actual
                calendar.get(Calendar.MONTH), // Mes actual
                calendar.get(Calendar.DAY_OF_MONTH) // Día actual
        ).show(); // Mostrar el DatePicker
    }

    // Método para mostrar el TimePickerDialog cuando se hace clic en el campo de hora
    private void mostrarTimePickerDialog() {
        Calendar calendar = Calendar.getInstance(); // Obtener la hora actual
        // Asegurarse de que getContext() no sea null antes de mostrar el TimePicker
        if (getContext() == null) return;
        new TimePickerDialog (
                getContext(),
                (TimePicker view, int hourOfDay, int minute) -> {
                    // Formateamos la hora seleccionada y la mostramos en el campo de hora
                    String hora = String.format("%02d:%02d", hourOfDay, minute);
                    campoHoraEntrega.setText(hora);
                },
                calendar.get(Calendar.HOUR_OF_DAY), // Hora actual
                calendar.get(Calendar.MINUTE), // Minuto actual
                true // Usar formato de 24 horas
        ).show(); // Mostrar el TimePicker
    }

    // Método para validar los campos antes de guardar
    private boolean validarEntradas() {
        // Verificamos que los campos no estén vacíos
        if (TextUtils.isEmpty(campoTitulo.getText())) {
            campoTitulo.setError("El título es obligatorio");
            return false;
        }
        if (TextUtils.isEmpty(campoDescripcion.getText())) {
            campoDescripcion.setError("La descripción es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(campoFechaEntrega.getText())) {
            campoFechaEntrega.setError("La fecha de entrega es obligatoria");
            return false;
        }
        if (TextUtils.isEmpty(campoHoraEntrega.getText())) {
            campoHoraEntrega.setError("La hora de entrega es obligatoria");
            return false;
        }
        return true; // Si todos los campos están completos, devolvemos true
    }
}