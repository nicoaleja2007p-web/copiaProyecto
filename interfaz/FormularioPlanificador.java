package interfaz;

import negocio.GestorActividades;
import negocio.Planificador;
import modelo.DiaDeViaje;
import modelo.PerfilPreferencias;
import modelo.Viaje;
import excepciones.PresupuestoExcedidoException;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FormularioPlanificador extends JPanel {

    private VentanaPrincipal ventana;
    private GestorActividades gestorActividades;
    private Viaje viajeEditar = null;

    private JLabel labelTitulo;
    private JLabel labelDestino;
    private JLabel labelFechaInicio;
    private JLabel labelFechaFin;
    private JLabel labelPresupuesto;
    private JLabel labelPersonas;
    private JTextField campoDestino;
    private JTextField campoFechaInicio;
    private JTextField campoFechaFin;
    private JTextField campoPresupuesto;
    private JTextField campoPersonas;
    private JButton botonGenerar;
    private JButton botonHistorial;
    private JButton botonCerrarSesion;
    private JLabel labelMensaje;

    public FormularioPlanificador(VentanaPrincipal ventana, GestorActividades gestorActividades) {
        this.ventana = ventana;
        this.gestorActividades = gestorActividades;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setBackground(new Color(245, 245, 250));

        labelTitulo = new JLabel("Planifica tu viaje");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setForeground(new Color(41, 98, 160));

        labelDestino = new JLabel("Destino:");
        labelFechaInicio = new JLabel("Fecha inicio (dd/mm/aaaa):");
        labelFechaFin = new JLabel("Fecha fin (dd/mm/aaaa):");
        labelPresupuesto = new JLabel("Presupuesto total ($):");
        labelPersonas = new JLabel("Numero de personas:");

        campoDestino = new JTextField(15);
        campoFechaInicio = new JTextField(15);
        campoFechaFin = new JTextField(15);
        campoPresupuesto = new JTextField(15);
        campoPersonas = new JTextField("1",15);

        botonGenerar = new JButton("Generar Itinerario");
        botonGenerar.setFont(new Font("Arial", Font.BOLD, 13));
        botonGenerar.setBackground(new Color(41, 98, 160));
        botonGenerar.setForeground(Color.WHITE);
        botonGenerar.setFocusPainted(false);

        botonHistorial = new JButton("Ver Historial");
        botonHistorial.setFont(new Font("Arial", Font.PLAIN, 13));
        botonHistorial.setBackground(new Color(200, 200, 200));
        botonHistorial.setFocusPainted(false);

        botonCerrarSesion = new JButton("Cerrar Sesion");
        botonCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCerrarSesion.setBackground(new Color(180, 50, 50));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setFocusPainted(false);

        labelMensaje = new JLabel(" ");
        labelMensaje.setFont(new Font("Arial", Font.ITALIC, 12));
        labelMensaje.setForeground(Color.RED);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        agregarFila(gbc, labelDestino, campoDestino, 1);
        agregarFila(gbc, labelFechaInicio, campoFechaInicio, 2);
        agregarFila(gbc, labelFechaFin, campoFechaFin, 3);
        agregarFila(gbc, labelPresupuesto, campoPresupuesto, 4);
        agregarFila(gbc, labelPersonas, campoPersonas, 5);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelMensaje, gbc);

        gbc.gridy = 7; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(botonGenerar, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(botonHistorial, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCerrarSesion, gbc);
    }

    private void agregarFila(GridBagConstraints gbc, JLabel label, JTextField campo, int fila) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.anchor = GridBagConstraints.EAST;
        add(label, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        add(campo, gbc);
    }

    private void configurarEventos() {
        botonGenerar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generarItinerario();
            }
        });

        botonHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.getVistaHistorial().mostrarHistorial(ventana.getGestorUsuario().obtenerHistorial());
                ventana.mostrarPanel("historial");
            }
        });

        botonCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        ventana,
                        "¿Estas seguro que deseas cerrar sesion?",
                        "Cerrar Sesion",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirmacion == JOptionPane.YES_OPTION) {
                    ventana.getGestorUsuario().logout();
                    campoDestino.setText("");
                    campoFechaInicio.setText("");
                    campoFechaFin.setText("");
                    campoPresupuesto.setText("");
                    labelMensaje.setText(" ");
                    ventana.mostrarPanel("login");
                }
            }
        });
    }

    private void generarItinerario() {
        String destino = campoDestino.getText().trim();
        String fechaInicio = campoFechaInicio.getText().trim();
        String fechaFin = campoFechaFin.getText().trim();
        String presupuestoTexto = campoPresupuesto.getText().trim();
        String personasTexto = campoPersonas.getText().trim();

        if (destino.equals("") || fechaInicio.equals("") || fechaFin.equals("") || presupuestoTexto.equals("")) {
            labelMensaje.setText("Error: Todos los campos son obligatorios.");
            return;
        }

        if (!esDestinoValido(destino)) {
            labelMensaje.setText("Error: El destino no debe contener numeros.");
            return;
        }

        double presupuesto = 0.0;
        int personas = 1;

        try {
            presupuesto = Double.parseDouble(presupuestoTexto);
        } catch (NumberFormatException ex) {
            labelMensaje.setText("Error: El presupuesto debe ser un numero valido.");
            return;
        }

        if (presupuesto <= 0) {
            labelMensaje.setText("Error: El presupuesto debe ser mayor a cero.");
            return;
        }

        try{
            personas = Integer.parseInt(personasTexto);
        }catch(NumberFormatException ex){
            labelMensaje.setText("Numero de personas invalido.");
            return;
        }

        if(personas<=0){
            labelMensaje.setText("Debe viajar al menos una persona.");
            return;
        }

        ArrayList<String> fechas = generarFechas(fechaInicio, fechaFin);
        if (fechas.size() == 0) {
            labelMensaje.setText("Error: Las fechas no son validas o la fecha fin es anterior a la inicio.");
            return;
        }

        int duracionDias = fechas.size();

        if (duracionDias >= 1 && duracionDias <= 5 && presupuesto < 300) {
            labelMensaje.setText("Error: Para viajes de 1 a 5 dias el presupuesto minimo es $300.");
            return;
        }
        if (duracionDias >= 6 && duracionDias <= 10 && presupuesto < 600) {
            labelMensaje.setText("Error: Para viajes de 6 a 10 dias el presupuesto minimo es $600.");
            return;
        }
        if (duracionDias > 10 && presupuesto < 1000) {
            labelMensaje.setText("Error: Para viajes de mas de 10 dias el presupuesto minimo es $1000.");
            return;
        }

        PerfilPreferencias perfil = ventana.getPerfilActual();
        Viaje viaje;

        if (viajeEditar == null) {
            viaje = new Viaje(1, destino, fechaInicio, fechaFin, presupuesto);
        } else {
            viaje = viajeEditar;
            viaje.setDestino(destino);
            viaje.setFechaInicio(fechaInicio);
            viaje.setFechaFin(fechaFin);
            viaje.setPresupuesto(presupuesto);
        }
        viaje.setNumeroPersonas(personas);
        Planificador planificador = new Planificador(viaje, gestorActividades.getActividades(), perfil);

        try {
            planificador.ajustarPresupuesto(presupuesto);
        } catch (PresupuestoExcedidoException ex) {
            labelMensaje.setText("Error: " + ex.getMessage());
            return;
        }

        ArrayList<DiaDeViaje> itinerario = planificador.generarItinerario(fechas);
        if (viajeEditar == null) {
            ventana.getGestorUsuario().agregarViajeAlHistorial(viaje);
        }
        ventana.getVistaItinerario().mostrarItinerario(viaje, itinerario);
        ventana.getVistaReporte().mostrarReporte(viaje);
        labelMensaje.setText(" ");
        ventana.mostrarPanel("itinerario");
        campoDestino.setText("");
        campoFechaInicio.setText("");
        campoFechaFin.setText("");
        campoPresupuesto.setText("");
        campoPersonas.setText("1");

        viajeEditar = null;
    }

    private boolean esDestinoValido(String destino) {
        return !destino.matches(".*\\d.*");
    }

    private ArrayList<String> generarFechas(String inicio, String fin) {
        ArrayList<String> fechas = new ArrayList<String>();
        try {
            String[] parteInicio = inicio.split("/");
            String[] parteFin = fin.split("/");

            if (parteInicio.length != 3 || parteFin.length != 3) {
                return fechas;
            }

            int diaI = Integer.parseInt(parteInicio[0]);
            int mesI = Integer.parseInt(parteInicio[1]);
            int anioI = Integer.parseInt(parteInicio[2]);

            int diaF = Integer.parseInt(parteFin[0]);
            int mesF = Integer.parseInt(parteFin[1]);
            int anioF = Integer.parseInt(parteFin[2]);

            if (diaI < 1 || diaI > 31 || diaF < 1 || diaF > 31) { return fechas; }
            if (mesI < 1 || mesI > 12 || mesF < 1 || mesF > 12) { return fechas; }
            if (anioI < 2000 || anioF < 2000) { return fechas; }

            int totalInicio = anioI * 10000 + mesI * 100 + diaI;
            int totalFin = anioF * 10000 + mesF * 100 + diaF;

            if (totalFin <= totalInicio) {
                labelMensaje.setText("Error: La fecha de regreso debe ser posterior a la de salida.");
                return fechas;
            }

            int[] diasPorMes = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

            int dia = diaI;
            int mes = mesI;
            int anio = anioI;
            int contador = 0;

            while (contador < 60) {
                fechas.add(dia + "/" + mes + "/" + anio);

                if (dia == diaF && mes == mesF && anio == anioF) {
                    break;
                }

                dia = dia + 1;
                if (dia > diasPorMes[mes]) {
                    dia = 1;
                    mes = mes + 1;
                }
                if (mes > 12) {
                    mes = 1;
                    anio = anio + 1;
                }
                contador = contador + 1;
            }

        } catch (NumberFormatException ex) {
            return new ArrayList<String>();
        }
        return fechas;
    }

    public void cargarViaje(Viaje viaje) {

        viajeEditar = viaje;

        campoDestino.setText(viaje.getDestino());
        campoFechaInicio.setText(viaje.getFechaInicio());
        campoFechaFin.setText(viaje.getFechaFin());
        campoPresupuesto.setText(String.valueOf(viaje.getPresupuesto()));
        campoPersonas.setText(String.valueOf(viaje.getNumeroPersonas()));

    }
}