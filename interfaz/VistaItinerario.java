package interfaz;

import modelo.Actividad;
import modelo.DiaDeViaje;
import modelo.Viaje;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VistaItinerario extends JPanel {

    private VentanaPrincipal ventana;
    private Viaje viajeActual;

    private JLabel labelTitulo;
    private JTextArea areaItinerario;
    private JScrollPane scroll;
    private JButton botonVerReporte;
    private JButton botonNuevoViaje;
    private JButton botonCambiarActividad;

    public VistaItinerario(VentanaPrincipal ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setBackground(new Color(245, 245, 250));

        labelTitulo = new JLabel("Itinerario de tu viaje");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitulo.setForeground(new Color(41, 98, 160));

        areaItinerario = new JTextArea();
        areaItinerario.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaItinerario.setEditable(false);
        areaItinerario.setBackground(new Color(255, 255, 255));
        areaItinerario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scroll = new JScrollPane(areaItinerario);
        scroll.setPreferredSize(new java.awt.Dimension(600, 320));

        botonVerReporte = new JButton("Ver Reporte de Costos");
        botonVerReporte.setFont(new Font("Arial", Font.BOLD, 13));
        botonVerReporte.setBackground(new Color(41, 98, 160));
        botonVerReporte.setForeground(Color.WHITE);
        botonVerReporte.setFocusPainted(false);

        botonNuevoViaje = new JButton("Planificar otro viaje");
        botonNuevoViaje.setFont(new Font("Arial", Font.PLAIN, 13));
        botonNuevoViaje.setBackground(new Color(200, 200, 200));
        botonNuevoViaje.setFocusPainted(false);

        botonCambiarActividad = new JButton("Cambiar una actividad");
        botonCambiarActividad.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCambiarActividad.setBackground(new Color(200, 200, 200));
        botonCambiarActividad.setFocusPainted(false);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelTitulo, gbc);

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);

        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(botonVerReporte, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(botonNuevoViaje, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCambiarActividad, gbc);
    }

    private void configurarEventos() {
        botonVerReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.mostrarPanel("reporte");
            }
        });

        botonNuevoViaje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.getFormularioPerfil().precargarPerfil();
                ventana.mostrarPanel("perfil");
            }
        });

        botonCambiarActividad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manejarCambiarActividad();
            }
        });
    }

    private void manejarCambiarActividad() {
        if (viajeActual == null) { return; }

        ArrayList<DiaDeViaje> dias = viajeActual.getDias();
        String[] nombresDias = new String[dias.size()];
        for (int i = 0; i < dias.size(); i++) {
            nombresDias[i] = "Dia " + (i + 1) + " - " + dias.get(i).getFecha();
        }

        String diaElegido = (String) JOptionPane.showInputDialog(
                ventana, "Elige el dia:", "Cambiar actividad",
                JOptionPane.PLAIN_MESSAGE, null, nombresDias, nombresDias[0]);
        if (diaElegido == null) { return; }

        int indiceDia = 0;
        for (int i = 0; i < nombresDias.length; i++) {
            if (nombresDias[i].equals(diaElegido)) { indiceDia = i; break; }
        }

        DiaDeViaje dia = dias.get(indiceDia);
        ArrayList<Actividad> acts = dia.getActividades();
        if (acts.size() == 0) {
            JOptionPane.showMessageDialog(ventana, "Este dia no tiene actividades.", "Sin actividades", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombresActs = new String[acts.size()];
        for (int i = 0; i < acts.size(); i++) {
            nombresActs[i] = acts.get(i).getNombre() + " ($" + acts.get(i).getCosto() + ")";
        }

        String actElegida = (String) JOptionPane.showInputDialog(
                ventana, "Elige la actividad a cambiar:", "Cambiar actividad",
                JOptionPane.PLAIN_MESSAGE, null, nombresActs, nombresActs[0]);
        if (actElegida == null) { return; }

        int indiceAct = 0;
        for (int i = 0; i < nombresActs.length; i++) {
            if (nombresActs[i].equals(actElegida)) { indiceAct = i; break; }
        }

        Actividad actActual = acts.get(indiceAct);
        double presupuestoDisponible = viajeActual.getPresupuesto() - viajeActual.getCostoTotal() + actActual.getCosto();

        ArrayList<Actividad> todasActs = ventana.getGestorActividades().getActividades();
        ArrayList<String> opcionesAlts = new ArrayList<String>();
        ArrayList<Actividad> alternativas = new ArrayList<Actividad>();

        for (int i = 0; i < todasActs.size(); i++) {
            Actividad candidata = todasActs.get(i);
            if (candidata.getCosto() <= presupuestoDisponible
                    && !candidata.getNombre().equals(actActual.getNombre())
                    && candidata.getCategoria().equals(actActual.getCategoria())) {
                opcionesAlts.add(candidata.getNombre() + " ($" + candidata.getCosto() + ")");
                alternativas.add(candidata);
            }
        }

        if (opcionesAlts.size() == 0) {
            JOptionPane.showMessageDialog(ventana, "No hay alternativas disponibles dentro del presupuesto.", "Sin alternativas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] optsArray = opcionesAlts.toArray(new String[0]);
        String nuevaElegida = (String) JOptionPane.showInputDialog(
                ventana, "Elige la nueva actividad:", "Cambiar actividad",
                JOptionPane.PLAIN_MESSAGE, null, optsArray, optsArray[0]);
        if (nuevaElegida == null) { return; }

        int indiceNueva = 0;
        for (int i = 0; i < optsArray.length; i++) {
            if (optsArray[i].equals(nuevaElegida)) { indiceNueva = i; break; }
        }

        acts.set(indiceAct, alternativas.get(indiceNueva));
        mostrarItinerario(viajeActual, dias);
        JOptionPane.showMessageDialog(ventana, "Actividad cambiada exitosamente.", "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarItinerario(Viaje viaje, ArrayList<DiaDeViaje> itinerario) {
        this.viajeActual = viaje;
        String texto = "";
        texto = texto + "Viaje a: " + viaje.getDestino().toUpperCase() + "\n";
        texto = texto + "Fecha: " + viaje.getFechaInicio() + " al " + viaje.getFechaFin() + "\n";
        texto = texto + "Presupuesto: $" + viaje.getPresupuesto() + "\n";
        texto = texto + "==========================================\n\n";

        for (int i = 0; i < itinerario.size(); i++) {
            DiaDeViaje dia = itinerario.get(i);
            texto = texto + "Dia " + (i + 1) + " - " + dia.getFecha() + "\n";
            texto = texto + "  Costo del dia: $" + dia.getCostoDelDia() + "\n";
            texto = texto + "  Duracion total: " + dia.getDuracionTotal() + " horas\n";
            texto = texto + "  Actividades:\n";

            ArrayList<Actividad> actividades = dia.getActividades();
            if (actividades.size() > 0) {
                for (int j = 0; j < actividades.size(); j++) {
                    texto = texto + "    - " + actividades.get(j).toString() + "\n";
                }
            }
            texto = texto + "\n";
        }

        texto = texto + "==========================================\n";
        texto = texto + "Costo Total del viaje: $" + viaje.getCostoTotal() + "\n";
        texto = texto + "Presupuesto disponible: $" + viaje.getPresupuesto() + "\n";

        areaItinerario.setText(texto);
        areaItinerario.setCaretPosition(0);
    }

    public Viaje getViajeActual() {
        return viajeActual;
    }
}