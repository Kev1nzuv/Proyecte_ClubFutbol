/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.VistaClub.GestioJugador;


import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.milaifontanals.CapaModel_P1.Equip;
import org.milaifontanals.CapaModel_P1.ExceptionClub;
import org.milaifontanals.CapaModel_P1.Jugador;
import org.milaifontanals.InterficiePersistencia_P1.ExceptionClubDB;
import org.milaifontanals.InterficiePersistencia_P1.IGestorDB;

public class V_JugadorCercat extends JFrame implements ActionListener {
    private Jugador jugador;
    private IGestorDB gDB;
    private JComboBox<String> comboFiltre; // Para filtrar por temporada
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblFoto, lblEscut, lblNom, lblNif, lblDataNaix,lblrevisio,lblSexe;

    public V_JugadorCercat(IGestorDB gDB, Jugador jugador) throws ExceptionClubDB {
        this.jugador = jugador;
        this.gDB = gDB;

        // Configuración de la ventana
        setTitle("Fitxa del jugador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Panel izquierdo (Información del jugador)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBounds(10, 10, 250, 550);
        panelIzquierdo.setLayout(null);
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Informació del jugador"));
        add(panelIzquierdo);

        // Foto del jugador
        lblFoto = new JLabel();
        lblFoto.setBounds(75, 20, 100, 100);
        lblFoto.setIcon(new ImageIcon(jugador.getFoto()));
        panelIzquierdo.add(lblFoto);

        // Nombre y Apellidos
        lblNom = new JLabel(jugador.getNom() + " " + jugador.getCognom());
        lblNom.setBounds(20, 140, 200, 30);
        lblNom.setHorizontalAlignment(SwingConstants.CENTER);
        lblNom.setFont(new Font("Arial", Font.BOLD, 14));
        panelIzquierdo.add(lblNom);

        // NIF
        lblNif = new JLabel("NIF: " + jugador.getNif());
        lblNif.setBounds(20, 180, 200, 30);
        lblNif.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblNif);
        
        
        //Sexe
        String sexe="";
        switch(jugador.getSexe()){
                case 'H':sexe="Home";break;
                case 'D':sexe="Dona";break;               
        }    
        lblSexe = new JLabel("Sexe: "+ sexe);
        lblSexe.setBounds(10, 200, 200, 30);
        lblSexe.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblSexe);

        // Fecha de nacimiento
        lblDataNaix = new JLabel("<html>Data naixement:<br>" + jugador.getDataNaix() + "</html>");
        lblDataNaix.setBounds(20, 210, 200, 100);
        lblDataNaix.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblDataNaix);
        
        lblrevisio = new JLabel("<html>Revisió medica:<br> " + jugador.getRivisioMedica()+ "</html>");
        lblrevisio.setBounds(20, 230, 200, 160);
        lblrevisio.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblrevisio);

        // Escudo del equipo
        lblEscut = new JLabel();
        lblEscut.setBounds(75, 330, 100, 100);
        lblEscut.setIcon(new ImageIcon("escudo.png")); 
        panelIzquierdo.add(lblEscut);

        // Panel derecho (Tabla y filtro)
        JPanel panelDret = new JPanel();
        panelDret.setBounds(270, 10, 500, 550);
        panelDret.setLayout(null);
        add(panelDret);

        // ComboBox para el filtro
        JLabel lblFiltre = new JLabel("Filtrar per Temporada");
        lblFiltre.setBounds(20, 20, 140, 30);
        panelDret.add(lblFiltre);

        List<Integer> llistatTemporades = gDB.LlistatTemporades();

        String[] opcionsCombo = new String[llistatTemporades.size()];

        int index = 0; 
        for (Integer temporada : llistatTemporades) {
            opcionsCombo[index] = String.valueOf(temporada); 
            index++;
        }

        comboFiltre = new JComboBox<>(opcionsCombo);
        comboFiltre.setBounds(160, 20, 150, 25);
        comboFiltre.addActionListener(this);      
        panelDret.add(comboFiltre);
        
        
        JButton btnVolver = new JButton("Tornar");
        btnVolver.setBounds(10, 500, 100, 30); // Posición en la parte inferior izquierda
        btnVolver.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                try {
                    new V_GestioJugador(gDB).setVisible(true);
                } catch (ExceptionClubDB ex) {
                    Logger.getLogger(V_JugadorCercat.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        panelIzquierdo.add(btnVolver);

        // Tabla
        tableModel = new DefaultTableModel(new String[]{"Equip", "Sexe", "Categoria", "Titular"}, 0);
        table = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                // Cambia el índice de 2 a 3 para que se maneje como Boolean en la columna "Titular"
                return column == 3 ? Boolean.class : String.class; // Checkbox en la cuarta columna (índice 3)
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo la columna de checkboxes es editable
                return column == 3;
            }
        };
        table.getColumnModel().getColumn(3).setCellRenderer(new CustomCheckboxRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new CustomCheckboxEditor());
       // Añadir el TableModelListener
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Verificar si se editó la columna "Titular" (índice 3)
            if (column == 3) {
                Boolean isChecked = (Boolean) tableModel.getValueAt(row, column);

                try {
                    // Crear el objeto Equip basado en la temporada seleccionada y el nombre del equipo
                    Equip eq = new Equip(Integer.parseInt((String) comboFiltre.getSelectedItem()), 
                                         (String) tableModel.getValueAt(row, 0));

                    // Si el checkbox ha sido desmarcado, no hacer nada
                    if (!isChecked) {
                        return; // Solo no hacer nada si el checkbox se desmarca
                    }

                    // Si el checkbox ha sido marcado, proceder con la validación
                    Equip equip = gDB.cercaEquipNom(eq);

                    // Verificar que el equipo sea de la misma categoría
                    if (!this.jugador.getCategoriaJugador().equals(equip.getCategoria())) {
                        JOptionPane.showMessageDialog(this,
                                "Error: El jugador no pot ser titular d'un equip de diferent categoria.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        tableModel.setValueAt(false, row, column); // Desmarcar el checkbox
                        return; // Salir del método si la validación falla
                    }
                    

                    // Si la validación es exitosa, actualizamos el titular del jugador
                    this.jugador.setTitular(equip);
                    gDB.modificarTitularitat(this.jugador);
                    gDB.confirmarCanvis();
                    // Desmarcar todos los demás checkboxes, excepto el marcado
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        if (i != row) {
                            tableModel.setValueAt(false, i, column);
                        }
                    }
                } catch (ExceptionClub ex) {
                    tableModel.setValueAt(false, row, column); // Desmarcar si ocurre un error
                    JOptionPane.showMessageDialog(this,
                            "Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ExceptionClubDB ex) {
                    tableModel.setValueAt(false, row, column); // Desmarcar si ocurre un error en la base de datos
                    JOptionPane.showMessageDialog(this,
                            "Error en la base de datos: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 60, 450, 470);
        panelDret.add(scrollPane);
        
        
        
        
        int anyActual = LocalDate.now().getYear();
        comboFiltre.setSelectedItem(String.valueOf(anyActual));
        /*
        // Cargar datos iniciales
        try {
            cargarDadesTaula(String.valueOf(anyActual));
        } catch (ExceptionClubDB ex) {
            Logger.getLogger(ex.getMessage());
        }*/
        setVisible(true);
        
        
      
    }

    private void cargarDadesTaula(String filtre) throws ExceptionClubDB {
        tableModel.setRowCount(0); // Limpiar tabla
        this.jugador.natejar();
        int temporadaSeleccionada = Integer.parseInt(filtre);
        List<Equip> equips = gDB.llistatEquipsDelJugadorFiltreTemp(this.jugador, temporadaSeleccionada);
        // Iterar sobre los equipos obtenidos
        for (Equip equip : equips) {
            // Verificar si el jugador es titular en este equipo
            boolean esTitular = equip.equals(jugador.getTitular()); // Comparar el equipo con el titular del jugador

            // Obtener los datos del equipo
            String equipName = equip.getNom();  // Nombre del equipo
            String equipSexe ="";
            String categoria = equip.getCategoria().name(); // Categoría del equipo
            switch(equip.getTipus()){
                case 'H':equipSexe="Masculi";break;
                case 'D':equipSexe="Femeni";break;
                case 'M':equipSexe="Mixta";break;
            }
            // Agregar los datos a la tabla
            Object[] fila = new Object[4];
            fila[0] = equipName;    // Equip
            fila[1] = equipSexe;         // Sexe
            fila[2] = categoria;    // Categoria
            fila[3] = esTitular;    // Titularidad (checkbox)

            // Añadir la fila a la tabla
            tableModel.addRow(fila);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboFiltre) {      
            // Obtener el valor seleccionado del JComboBox
            String temporadaSeleccionada = (String) comboFiltre.getSelectedItem();
            try {
                cargarDadesTaula(temporadaSeleccionada);
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_JugadorCercat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    class CustomCheckboxRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JCheckBox checkbox = new JCheckBox();
            checkbox.setSelected((Boolean) value);
            checkbox.setHorizontalAlignment(SwingConstants.CENTER);
            checkbox.setEnabled(false); // Siempre deshabilitado en el renderizado
            return checkbox;
        }
    }

    class CustomCheckboxEditor extends AbstractCellEditor implements TableCellEditor {
        private final JCheckBox checkbox;

        public CustomCheckboxEditor() {
            checkbox = new JCheckBox();
            checkbox.setHorizontalAlignment(SwingConstants.CENTER);

            // Escuchar clics en el checkbox
            checkbox.addActionListener(e -> {
                // Notificar que el valor ha cambiado
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkbox.setSelected((Boolean) value);

            // Habilitar o deshabilitar el checkbox según su estado
            boolean isTitular = (Boolean) value;
            checkbox.setEnabled(!isTitular); // Solo habilitar si no es titular actual

            return checkbox;
        }

        @Override
        public Object getCellEditorValue() {
            return checkbox.isSelected();
        }
    }


    
}
