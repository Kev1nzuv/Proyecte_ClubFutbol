/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.VistaClub.GestioEquips;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.CapaModel_P1.Categoria;
import org.milaifontanals.CapaModel_P1.Equip;
import org.milaifontanals.InterficiePersistencia_P1.ExceptionClubDB;
import org.milaifontanals.InterficiePersistencia_P1.IGestorDB;

/**
 *
 * @author kevin
 */
public class V_GestioEquips extends JFrame implements ActionListener {
    private JTabbedPane tabbedPane;
    private JPanel llistarPanel, crearPanel, editarPanel, eliminarPanel, informePanel;
    private IGestorDB gDB;
    private JComboBox comboFiltre,comboCategoria, comboTemporada;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtNovaTemporada,txtNomEquipCreat;
    private JLabel lblErrorTemporada,lblErrorCrearEquip;
    private JButton btnCrearTemporada,btnCrearEquip,btnResulat;
    private ButtonGroup groupTipus;
    private JRadioButton rbMixta,rbMasculi,rbFemeni;
            
    public V_GestioEquips(IGestorDB gDB) throws ExceptionClubDB {
        this.gDB=gDB;
        
        
        
        // Configuración de la ventana
        setTitle("Gestió d'Equip");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Crear un TabbedPane para las pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 50, 700, 500);

        // Crear los paneles para cada pestaña
        llistarPanel = crearLlistarPanel();
        tabbedPane.addTab("Llistar", llistarPanel);

        crearPanel = crearEquipTemporada();
        tabbedPane.addTab("Crear", crearPanel);

        editarPanel = crearPanelBase("Editar Equip");
        tabbedPane.addTab("Editar Equip", editarPanel);

        eliminarPanel = crearPanelBase("Eliminar Equip");
        tabbedPane.addTab("Eliminar", eliminarPanel);

        informePanel = crearPanelBase("Informe");
        tabbedPane.addTab("Informe", informePanel);

        // Añadir el TabbedPane a la ventana
        add(tabbedPane);
    }
    private JPanel crearLlistarPanel() throws ExceptionClubDB {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Combobox para Temporada
        JLabel lblTemporada = new JLabel("Temporada");
        lblTemporada.setBounds(50, 20, 100, 25);
        panel.add(lblTemporada);

        List<Integer> llistatTemporades = gDB.LlistatTemporades();
        String[] opcionsCombo = new String[llistatTemporades.size()];
        int index = 0; 
        for (Integer temporada : llistatTemporades) {
            opcionsCombo[index] = String.valueOf(temporada); 
            index++;
        }

        comboFiltre = new JComboBox<>(opcionsCombo);
        comboFiltre.setBounds(120, 20, 80, 25);
        comboFiltre.addActionListener(this);      
        panel.add(comboFiltre);

        // Combobox para Categoría
        JLabel lblCategoria = new JLabel("Categoria");
        lblCategoria.setBounds(450, 20, 60, 25);
        panel.add(lblCategoria);

        comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(520, 20, 100, 25);
        comboCategoria.addItem(null); 
        for (Categoria categoria : Categoria.values()) {
            comboCategoria.addItem(categoria);
        }
        comboCategoria.addActionListener(this);
        panel.add(comboCategoria);

        // Tabla para mostrar los equipos
        String[] columnNames = {"Nom equip", "Tipus", "Categoria", "Nº Jugadors"};
        tableModel = new DefaultTableModel(new Object[0][columnNames.length], columnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 60, 600, 300);
        panel.add(scrollPane);

        // Seleccionar la temporada actual
        int temporadaActual = Calendar.getInstance().get(Calendar.YEAR);
        comboFiltre.setSelectedItem(String.valueOf(temporadaActual));

        return panel;
    }

    private JPanel crearEquipTemporada() throws ExceptionClubDB{
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Label y campo de texto para "Nova Temporada"
        JLabel lblNovaTemporada = new JLabel("Nova Temporada:");
        lblNovaTemporada.setBounds(50, 20, 120, 25);
        panel.add(lblNovaTemporada);

        txtNovaTemporada = new JTextField();
        txtNovaTemporada.setBounds(180, 20, 80, 25);
        panel.add(txtNovaTemporada);

        // Botón para crear temporada
        btnCrearTemporada = new JButton("Crear Temporada");
        btnCrearTemporada.setBounds(280, 20, 140, 25);
        btnCrearTemporada.addActionListener(this);
        panel.add(btnCrearTemporada);

        // error para crear temporada
        lblErrorTemporada = new JLabel("");
        lblErrorTemporada.setBounds(425, 20, 280, 25);
        lblErrorTemporada.setForeground(Color.RED);
        panel.add(lblErrorTemporada);

        // Label y ComboBox para "Temporada"
        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setBounds(50, 60, 100, 25);
        panel.add(lblTemporada);

        List<Integer> llistatTemporades = gDB.LlistatTemporades(); 
        String[] opcionsCombo = new String[llistatTemporades.size()];
        int index = 0;
        for (Integer temporada : llistatTemporades) {
            opcionsCombo[index++] = String.valueOf(temporada);
        }

        comboTemporada = new JComboBox<>(opcionsCombo);
        comboTemporada.setBounds(180, 60, 100, 25);
        panel.add(comboTemporada);
        int temporadaActual = Calendar.getInstance().get(Calendar.YEAR);
        comboTemporada.setSelectedItem(String.valueOf(temporadaActual));
        // Label y ComboBox para "Categoria"
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(350, 60, 100, 25);
        panel.add(lblCategoria);

        JComboBox<Categoria> comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(450, 60, 100, 25);
        comboCategoria.addItem(null); 
        for (Categoria categoria : Categoria.values()) {
            comboCategoria.addItem(categoria);
        }
        panel.add(comboCategoria);

        // Label y Radio Buttons para "Tipus"
        JLabel lblTipus = new JLabel("Tipus:");
        lblTipus.setBounds(50, 110, 100, 25);
        panel.add(lblTipus);

        rbFemeni = new JRadioButton("Femení");
        rbFemeni.setBounds(180, 110, 80, 25);
        panel.add(rbFemeni);

        rbMasculi = new JRadioButton("Masculí");
        rbMasculi.setBounds(260, 110, 80, 25);
        panel.add(rbMasculi);

        rbMixta = new JRadioButton("Mixta");
        rbMixta.setBounds(340, 110, 80, 25);
        panel.add(rbMixta);

        groupTipus = new ButtonGroup();
        groupTipus.add(rbFemeni);
        groupTipus.add(rbMasculi);
        groupTipus.add(rbMixta);

        
        JLabel lblNom = new JLabel("Nom de l'equip:");
        lblNom.setBounds(50, 140, 100, 25);
        panel.add(lblNom);

        txtNomEquipCreat = new JTextField();
        txtNomEquipCreat.setBounds(180, 140, 250, 25);
        panel.add(txtNomEquipCreat);
        
        JLabel lblResulta = new JLabel("Resultat de l'equip que es creara:");
        lblResulta.setFont(new Font("Arial", Font.BOLD, 20)); 
        lblResulta.setBounds(70, 180, 500, 40);
        panel.add(lblResulta);
        
        btnResulat = new JButton("Visualitzar");
        btnResulat.addActionListener(this);
        btnResulat.setBounds(400, 190, 100, 25);
        panel.add(btnResulat);

        // Botón para crear equipo
        btnCrearEquip = new JButton("Crear Equip");
        btnCrearEquip.addActionListener(this);
        btnCrearEquip.setBounds(180, 420, 100, 25);
        panel.add(btnCrearEquip);
        btnCrearEquip.setEnabled(false);
        // Botón de error para crear equipo
        lblErrorCrearEquip = new JLabel("");
        lblErrorCrearEquip.setBounds(270, 420, 120, 25);
        lblErrorCrearEquip.setForeground(Color.RED);
        panel.add(lblErrorCrearEquip);

        // Tabla para mostrar resultados
        String[] columnNames = { "Nom Equip", "Tipus", "Categoria", "Temporada"};
        DefaultTableModel tableModel = new DefaultTableModel(new Object[0][columnNames.length], columnNames);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 260, 600, 80);
        panel.add(scrollPane);

        return panel;
    } 
    
    private JPanel crearPanelBase(String titulo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel placeholderLabel = new JLabel(titulo, SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(placeholderLabel, BorderLayout.CENTER);
        return panel;
    }

    private void cargarDadesTaula() throws ExceptionClubDB {
        // Limpiar la tabla
        tableModel.setRowCount(0);

        // Obtener los filtros seleccionados
        String temporadaSeleccionada = (String) comboFiltre.getSelectedItem();
        Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

        // Obtener los datos filtrados
        List<Equip> equips = gDB.LlistatEquips();

        // Llenar la tabla con los datos obtenidos
        for (Equip equip : equips) {
            String tipus = switch (equip.getTipus()) {
                case 'H' -> "Masculí";
                case 'D' -> "Femení";
                case 'M' -> "Mixta";
                default -> "Desconegut";
            };

            Object[] fila = {
                equip.getNom(),       // Nombre del equipo
                tipus,                // Tipo (masculino, femenino, mixto)
                equip.getCategoria().name(), // Categoría
                gDB.qtJugadorsEquip(equip) // Número de jugadores
            };
            tableModel.addRow(fila);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboFiltre || e.getSource() == comboCategoria) {
            try {
                // Obtener filtros seleccionados
                String temporadaSeleccionada = (String) comboFiltre.getSelectedItem();
                Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

                if (temporadaSeleccionada != null && categoriaSeleccionada != null) {
                    int temporada = Integer.parseInt(temporadaSeleccionada);

                    // Obtener la lista de equipos filtrados
                    List<Equip> equips = gDB.llistatEquipsTemporadaCategoria(temporada, categoriaSeleccionada);

                    // Limpiar la tabla
                    tableModel.setRowCount(0);

                    if (equips.isEmpty()) {
                        // Mostrar alerta si la lista está vacía
                        JOptionPane.showMessageDialog(
                            this,
                            "No hi ha equips de la categoria " + categoriaSeleccionada.name() + 
                            " a la temporada " + temporada,
                            "Alerta",
                            JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        // Llenar la tabla con los datos obtenidos
                        for (Equip equip : equips) {
                            String tipus = switch (equip.getTipus()) {
                                case 'H' -> "Masculí";
                                case 'D' -> "Femení";
                                case 'M' -> "Mixta";
                                default -> "Desconegut";
                            };

                            Object[] fila = {
                                equip.getNom(),       // Nombre del equipo
                                tipus,                // Tipo (masculino, femenino, mixto)
                                equip.getCategoria(), // Categoría
                                gDB.qtJugadorsEquip(equip) // Número de jugadores
                            };
                            tableModel.addRow(fila);
                        }
                    }
                }
            } catch (ExceptionClubDB ex) {
                ex.printStackTrace();
            }
        }else if(e.getSource() == btnCrearTemporada){
            
            try{
                if(!txtNovaTemporada.getText().isEmpty()){
                    String temporada = txtNovaTemporada.getText().trim(); 
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR); 
                    int temporadaInt = Integer.parseInt(temporada);
                    int maxYear = currentYear + 20;
                    if (temporadaInt >= 1890 && temporadaInt <= maxYear) {
                        if(gDB.temporadaExistent(temporadaInt)){
                            lblErrorTemporada.setText("Aquesta temporada, ja existeix.");
                        }else{
                            lblErrorTemporada.setText("");
                            gDB.afegirTemporada(temporadaInt);
                            JOptionPane.showMessageDialog(crearPanel, 
                            "Temporada creada correctament.", 
                            "Èxit", 
                            JOptionPane.INFORMATION_MESSAGE);
                            txtNovaTemporada.setText("");
                            gDB.confirmarCanvis();
                            actualitzarPanel();
                        }           
                    } else {
                        lblErrorTemporada.setText("No es valida aquesta temporada.");
                    }
                }else{
                    lblErrorTemporada.setText("Has de posar una temporada.");
                }
            }catch(NumberFormatException ex){
                lblErrorTemporada.setText("Has de posar un any de temporada");
            } catch (ExceptionClubDB ex) {
                lblErrorTemporada.setText(ex.getMessage());
            }
        }else if(e.getSource() == btnCrearEquip){
            
        }
        
    
    }
    private void actualitzarPanel() {
        try {
            
            List<Integer> llistatTemporades = gDB.LlistatTemporades();
            comboTemporada.removeAllItems();
            for (Integer temporada : llistatTemporades) {
                comboTemporada.addItem(String.valueOf(temporada));
            }
            int temporadaActual = Calendar.getInstance().get(Calendar.YEAR);
            comboTemporada.setSelectedItem(String.valueOf(temporadaActual));
        } catch (ExceptionClubDB ex) {
            lblErrorTemporada.setText(ex.getMessage());
        }
    }
        
}
