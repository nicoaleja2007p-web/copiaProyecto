package interfaz;

import modelo.HistorialViaje;
import modelo.Viaje;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MenuPrincipal extends JPanel {

    private VentanaPrincipal ventana;

    private JLabel labelBienvenida;
    private JTextArea areaResumenViajes;
    private JScrollPane scroll;

    private JButton botonPlanificar;
    private JButton botonModificar;
    private JButton botonConfirmar;
    private JButton botonVerDetalles;
    private JButton botonVerHistorial;
    private JButton botonVerReporte;
    private JButton botonCerrarSesion;

    public MenuPrincipal(VentanaPrincipal ventana) {
        this.ventana = ventana;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }

    private void inicializarComponentes() {
        setBackground(new Color(245, 245, 250));

        labelBienvenida = new JLabel("Bienvenido al Planificador de Viajes");
        labelBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        labelBienvenida.setForeground(new Color(41, 98, 160));

        areaResumenViajes = new JTextArea();
        areaResumenViajes.setFont(new Font("Courier New", Font.PLAIN, 13));
        areaResumenViajes.setEditable(false);
        areaResumenViajes.setBackground(Color.WHITE);
        areaResumenViajes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scroll = new JScrollPane(areaResumenViajes);
        scroll.setPreferredSize(new java.awt.Dimension(600, 300));

        botonPlanificar = new JButton("Planificar nuevo viaje");
        botonPlanificar.setFont(new Font("Arial", Font.BOLD, 13));
        botonPlanificar.setBackground(new Color(41, 98, 160));
        botonPlanificar.setForeground(Color.WHITE);
        botonPlanificar.setFocusPainted(false);

        botonModificar = new JButton("Modificar viaje");
        botonModificar.setFont(new Font("Arial", Font.PLAIN, 13));
        botonModificar.setBackground(new Color(200,200,200));
        botonModificar.setFocusPainted(false);

        botonConfirmar = new JButton("Confirmar viaje");
        botonConfirmar.setFont(new Font("Arial", Font.BOLD, 13));
        botonConfirmar.setBackground(new Color(46,125,50));
        botonConfirmar.setForeground(Color.WHITE);
        botonConfirmar.setFocusPainted(false);

        botonVerDetalles = new JButton("Ver Itinerario");
        botonVerDetalles.setFont(new Font("Arial", Font.PLAIN, 13));
        botonVerDetalles.setBackground(new Color(200,200,200));
        botonVerDetalles.setFocusPainted(false);

        botonVerHistorial = new JButton("Ver historial completo");
        botonVerHistorial.setFont(new Font("Arial", Font.PLAIN, 13));
        botonVerHistorial.setBackground(new Color(200, 200, 200));
        botonVerHistorial.setFocusPainted(false);

        botonVerReporte = new JButton("Ver reporte de costos");
        botonVerReporte.setFont(new Font("Arial", Font.PLAIN, 13));
        botonVerReporte.setBackground(new Color(200, 200, 200));
        botonVerReporte.setFocusPainted(false);

        botonCerrarSesion = new JButton("Cerrar Sesion");
        botonCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 13));
        botonCerrarSesion.setBackground(new Color(180, 50, 50));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.setFocusPainted(false);
    }

    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(labelBienvenida, gbc);

        gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);

        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        add(botonPlanificar, gbc);

        gbc.gridx = 1;
        add(botonModificar, gbc);

        gbc.gridx = 2;
        add(botonConfirmar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(botonVerDetalles, gbc);

        gbc.gridx = 1;
        add(botonVerHistorial, gbc);

        gbc.gridx = 2;
        add(botonVerReporte, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(botonCerrarSesion, gbc);

        gbc.gridx = 1;
        add(botonVerReporte, gbc);

        gbc.gridx = 2;
        add(botonVerDetalles, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 3;
        add(botonCerrarSesion, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botonCerrarSesion, gbc);
    }

    private void configurarEventos() {
        botonPlanificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.getFormularioPerfil().precargarPerfil();
                ventana.mostrarPanel("perfil");
            }
        });

        botonModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Viaje ultimo = obtenerUltimoViaje();
                if(ultimo==null){
                    javax.swing.JOptionPane.showMessageDialog(
                            ventana, "No existen viajes.");
                    return;
                }
                if(ultimo.isConfirmado()){
                    javax.swing.JOptionPane.showMessageDialog(
                            ventana, "Los viajes confirmados ya no pueden modificarse.");
                    return;
                }
                ventana.getFormularioPlanificador().cargarViaje(ultimo);
                ventana.mostrarPanel("planificador");
            }

        });

        botonConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Viaje ultimo = obtenerUltimoViaje();
                if(ultimo==null){
                    javax.swing.JOptionPane.showMessageDialog(
                            ventana, "No existen viajes para confirmar.");
                    return;
                }
                ultimo.setConfirmado(true);
                javax.swing.JOptionPane.showMessageDialog(
                        ventana, "Viaje confirmado correctamente.");
                actualizar();
            }
        });

        botonVerDetalles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Viaje ultimo = obtenerUltimoViaje();
                if(ultimo==null){
                    javax.swing.JOptionPane.showMessageDialog(
                            ventana, "No existen viajes.");
                    return;
                }
                ventana.getVistaItinerario().mostrarItinerario(
                        ultimo,
                        ultimo.getDias());
                ventana.mostrarPanel("itinerario");
            }
        });

        botonVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.getVistaHistorial().mostrarHistorial(
                        ventana.getGestorUsuario().obtenerHistorial());
                ventana.mostrarPanel("historial");
            }
        });

        botonVerReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Viaje ultimo = obtenerUltimoViaje();
                if (ultimo == null) {
                    javax.swing.JOptionPane.showMessageDialog(ventana,
                            "Aun no tienes viajes planificados.",
                            "Sin viajes", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                ventana.getVistaReporte().mostrarReporte(ultimo);
                ventana.mostrarPanel("reporte");
            }
        });

        botonCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ventana.getGestorUsuario().logout();
                ventana.mostrarPanel("login");
            }
        });
    }

    private Viaje obtenerUltimoViaje() {
        HistorialViaje historial = ventana.getGestorUsuario().obtenerHistorial();
        ArrayList<Viaje> viajes = historial.obtenerViajes();
        if (viajes.size() == 0) { return null; }
        return viajes.get(viajes.size() - 1);
    }

    public void actualizar() {
        String nombreUsuario = "";
        if (ventana.getGestorUsuario().getUsuarioActivo() != null) {
            nombreUsuario = ventana.getGestorUsuario().getUsuarioActivo().getNombre();
        }
        labelBienvenida.setText("Bienvenido, " + nombreUsuario);

        HistorialViaje historial = ventana.getGestorUsuario().obtenerHistorial();
        ArrayList<Viaje> viajes = historial.obtenerViajes();

        if (viajes.size() == 0) {
            areaResumenViajes.setText("Todavia no tienes viajes planificados.\nUsa el boton 'Planificar nuevo viaje' para comenzar.");
            return;
        }

        String texto = "=== TUS VIAJES ===\n\n";
        for (int i = 0; i < viajes.size(); i++) {
            Viaje v = viajes.get(i);
            texto += "=====================================\n";
            texto += "Viaje #" + (i+1) + "\n";
            texto += "Destino: " + v.getDestino() + "\n";
            texto = texto + "  Fechas:      " + v.getFechaInicio() + " al " + v.getFechaFin() + "\n";
            texto = texto + "  Duracion:    " + v.calcularDuracion() + " dias\n";
            texto = texto + "  Personas:    " + v.getNumeroPersonas() + "\n";
            texto = texto + "  Presupuesto: $" + v.getPresupuesto() + "\n";
            texto += "  Personas: " + v.getNumeroPersonas() + "\n";
            texto = texto + "  Costo total: $" + v.getCostoTotal() + "\n";
            if (v.isConfirmado()) {
                texto += "  Estado:      Confirmado\n";
            } else {
                texto += "  Estado:      Pendiente\n";
            }
            texto = texto + "\n";
        }
        areaResumenViajes.setText(texto);
        areaResumenViajes.setCaretPosition(0);
    }
}