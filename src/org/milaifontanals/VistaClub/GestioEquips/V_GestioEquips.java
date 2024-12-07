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
import org.milaifontanals.CapaModel_P1.ExceptionClub;
import org.milaifontanals.CapaModel_P1.Jugador;
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
    private JComboBox comboFiltre, comboTemporada,comboTemporadaEditar,comboTemporadaEliminar;
    private JComboBox <Categoria> comboCategoria,comboCategoriaCrear;
    private DefaultTableModel tableModel,tableModelResul,Modelposibles,ModelJugadorsEquip,ModelJugadorsEquipEliminar ;
    private JTable table,tableResul,tableJugadorsposibles,tableJugadorsEquip,tableJugadorsEquipEliminar;
    private JTextField txtNovaTemporada,txtNomEquipCreat,txtNomEquipCerca,txtNomEquipEliminar;
    private JLabel lblErrorTemporada,lblErrorCrearEquip,lblErrorValidar;
    private JButton btnCrearTemporada,btnCrearEquip,btnResulat,btnEditarEquip,btnInsertJugador,btnBaixaJugador,btnGuardar,btnDesfer,btnEliminarEquip;
    private ButtonGroup groupTipus;
    private JRadioButton rbMixta,rbMasculi,rbFemeni;
    private String temporadaResu,nomEquipResul,tipusResu;
    private Categoria categoriaResu;
    private boolean isProgrammaticChange = false;
            
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

        editarPanel = crearEditarEquip();
        tabbedPane.addTab("Fitxa Equip", editarPanel);

        eliminarPanel = crearEliminarEquip();
        tabbedPane.addTab("Eliminar", eliminarPanel);

        /*informePanel = crearPanelBase("Informe");
        tabbedPane.addTab("Informe", informePanel);*/

        // Añadir el TabbedPane a la ventana
        add(tabbedPane);
    }
    private JPanel crearEliminarEquip() throws ExceptionClubDB {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setBounds(50, 20, 100, 25);
        panel.add(lblTemporada);

        List<Integer> llistatTemporades = gDB.LlistatTemporades();
        String[] opcionsCombo = llistatTemporades.stream().map(String::valueOf).toArray(String[]::new);

        comboTemporadaEliminar = new JComboBox<>(opcionsCombo);
        comboTemporadaEliminar.setBounds(120, 20, 80, 25);
        comboTemporadaEliminar.addActionListener(this);
        panel.add(comboTemporadaEliminar);

        int temporadaActual = Calendar.getInstance().get(Calendar.YEAR);
        comboTemporadaEliminar.setSelectedItem(String.valueOf(temporadaActual));

        JLabel lblNomEquip = new JLabel("Nom:");
        lblNomEquip.setBounds(300, 20, 100, 25);
        panel.add(lblNomEquip);

        txtNomEquipEliminar = new JTextField();
        txtNomEquipEliminar.setBounds(350, 20, 150, 25);
        panel.add(txtNomEquipEliminar);

        btnEliminarEquip = new JButton("Eliminar Equip");
        btnEliminarEquip.setBounds(150, 420, 220, 25);
        btnEliminarEquip.addActionListener(this);
        panel.add(btnEliminarEquip);

        JLabel lblNomEquipTit = new JLabel("");
        lblNomEquipTit.setBounds(40, 110, 200, 35);
        lblNomEquipTit.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(lblNomEquipTit);
        
        JLabel sexeEquip = new JLabel("");
        sexeEquip.setBounds(40, 150, 200, 35);
        sexeEquip.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(sexeEquip);
        
        JLabel lbltempEquip = new JLabel("");
        lbltempEquip.setBounds(40, 190, 200, 35);
        lbltempEquip.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(lbltempEquip);
        
        JLabel lblEquipCat = new JLabel("");
        lblEquipCat.setBounds(40, 230, 200, 35);
        lblEquipCat.setFont(new Font("Arial", Font.BOLD, 18)); 
        panel.add(lblEquipCat);
        
        JLabel jequiptit = new JLabel("");
        jequiptit.setFont(new Font("Arial", Font.BOLD, 18)); 
        jequiptit.setBounds(400, 80, 300, 35);
        panel.add(jequiptit);
        
        txtNomEquipEliminar.addActionListener(e -> {
            String equipNom = txtNomEquipEliminar.getText().trim();
            try {
                Equip eq = gDB.cercaEquipNom(new Equip(Integer.parseInt(comboTemporadaEliminar.getSelectedItem().toString()), equipNom));
                if (eq != null) {
                    if (ModelJugadorsEquipEliminar == null) {
                        ModelJugadorsEquipEliminar = new DefaultTableModel(new String[]{"NIF", "Cognom", "Titular"}, 0){
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                return false; 
                            }

                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnIndex == 2 ? Boolean.class : String.class;
                            }
                        };
                    }
                    ModelJugadorsEquipEliminar.setRowCount(0); 
                    lblNomEquipTit.setText(equipNom.toUpperCase());
                    String tipus = eq.getTipus() == 'H' ? "MASCULÍ" : (eq.getTipus() == 'D' ? "FEMENÍ" : "MIXTA");
                    sexeEquip.setText(tipus);
                    lbltempEquip.setText(comboTemporadaEliminar.getSelectedItem().toString());
                    lblEquipCat.setText(eq.getCategoria().name());
                    List<Jugador> jugadorsEquip = gDB.LlistatJugadorsUnEquip(eq);
                    if(jugadorsEquip.isEmpty()){
                        jequiptit.setText("Aquest equip no té membres.");
                    }else{
                        jequiptit.setText("Jugadors de l'equip.");
                    }
                    for (Jugador jugador : jugadorsEquip) {
                        Object[] rowData = {
                            jugador.getNif(),
                            jugador.getCognom(),
                        gDB.asossiacioEquiJugadorTitular(jugador.getId(), eq.getId())};
                        ModelJugadorsEquipEliminar.addRow(rowData);

                    tableJugadorsEquipEliminar = new JTable(ModelJugadorsEquipEliminar);
                    JScrollPane selectedScrollPane = new JScrollPane(tableJugadorsEquipEliminar);
                    selectedScrollPane.setBounds(380, 110, 300, 250);
                    panel.add(selectedScrollPane);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                    null,
                    "Advertencia: " + "No tenim cap equip aquesta temporada amb aquest nom",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
);
                }
            } catch (ExceptionClubDB ex) {
                JOptionPane.showMessageDialog(
                                null,
                                "Error:"+ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
            } catch (ExceptionClub ex) {
                JOptionPane.showMessageDialog(
                                null,
                                "Error:"+ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
            }
        });
        btnEliminarEquip.addActionListener(e -> {
            String equipNom = txtNomEquipEliminar.getText().trim();
            try {
                Equip eq = gDB.cercaEquipNom(new Equip(Integer.parseInt(comboTemporadaEliminar.getSelectedItem().toString()), equipNom));
                List<Jugador> jugadorsEquip = gDB.LlistatJugadorsUnEquip(eq);
                if(jugadorsEquip.isEmpty()){
                    gDB.deleteEquip(eq);
                    gDB.confirmarCanvis();
                }else{
                    int confirm = JOptionPane.showConfirmDialog(
                        panel, 
                        "Aquest equip té membres, estas segur que vols eliminarlo?", 
                        "Confirmació d'eliminació", 
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        for(Jugador j : jugadorsEquip){
                            gDB.deleteJugadorEquip(j.getId(), eq.getId());
                        }
                        gDB.deleteEquip(eq);
                        gDB.confirmarCanvis();
                    }
                }
                txtNomEquipEliminar.setText("");
                lblNomEquipTit.setText(""); 
                sexeEquip.setText(""); 
                lbltempEquip.setText(""); 
                lblEquipCat.setText(""); 
                jequiptit.setText(""); 
                panel.revalidate(); 
                panel.repaint();
                if (ModelJugadorsEquipEliminar != null) {
                    ModelJugadorsEquipEliminar.setRowCount(0); // Vaciar la tabla
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(
                                null,
                                "Error:"+ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
            }
        });
        

        return panel;
    }


    
    private JPanel crearEditarEquip() throws ExceptionClubDB {
        
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setBounds(50, 20, 100, 25);
        panel.add(lblTemporada);

        List<Integer> llistatTemporades = gDB.LlistatTemporades();
        String[] opcionsCombo = new String[llistatTemporades.size()];
        int index = 0; 
        for (Integer temporada : llistatTemporades) {
            opcionsCombo[index] = String.valueOf(temporada); 
            index++;
        }

        comboTemporadaEditar = new JComboBox<>(opcionsCombo);
        comboTemporadaEditar.setBounds(120, 20, 80, 25);
        comboTemporadaEditar.addActionListener(this);      
        panel.add(comboTemporadaEditar);

        int temporadaActual = Calendar.getInstance().get(Calendar.YEAR);
        comboTemporadaEditar.setSelectedItem(String.valueOf(temporadaActual));

        JLabel lblNomEquip = new JLabel("Nom:");
        lblNomEquip.setBounds(300, 20, 100, 25);
        panel.add(lblNomEquip);

        txtNomEquipCerca = new JTextField();
        txtNomEquipCerca.setBounds(350, 20, 150, 25);
        panel.add(txtNomEquipCerca);

        btnEditarEquip = new JButton("Editar Equip");
        btnEditarEquip.setBounds(510, 20, 120, 25);
        btnEditarEquip.addActionListener(this);
        panel.add(btnEditarEquip);

        JLabel lblJugadorsposibles = new JLabel("Jugadors possibles");
        lblJugadorsposibles.setFont(new Font("Arial", Font.BOLD, 18)); 
        lblJugadorsposibles.setBounds(50, 80, 250, 25);
        panel.add(lblJugadorsposibles);

        // Modelo para tabla de jugadores posibles (no editable)
        String[] columnesPosibles = {"NIF", "Cognom", "Pot ser Titular"};
        Modelposibles = new DefaultTableModel(columnesPosibles, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };
        tableJugadorsposibles = new JTable(Modelposibles);
        JScrollPane posiblesScroll = new JScrollPane(tableJugadorsposibles);
        posiblesScroll.setBounds(30, 110, 300, 250);
        panel.add(posiblesScroll);

        JLabel lblJugadorsSeleccionats = new JLabel("Jugadors de l'equip");
        lblJugadorsSeleccionats.setFont(new Font("Arial", Font.BOLD, 18)); 
        lblJugadorsSeleccionats.setBounds(400, 80, 250, 25);
        panel.add(lblJugadorsSeleccionats);
        
        // Modelo para tabla de jugadores del equipo (editable en la tercera columna)
        String[] columnesEquip = {"NIF", "Cognom", "Titular"};
        ModelJugadorsEquip = new DefaultTableModel(columnesEquip, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la tercera columna es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class; // Checkbox en la tercera columna
            }
        };
        ModelJugadorsEquip.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            
            if(isProgrammaticChange){
                return;
            }
       
            if (column == 2) {
                Boolean isChecked = (Boolean) ModelJugadorsEquip.getValueAt(row, column);
                String nif = (String) ModelJugadorsEquip.getValueAt(row, 0); 

                try {
  
                    Equip eqCercat = new Equip(
                        Integer.parseInt(comboTemporadaEditar.getSelectedItem().toString()), 
                        txtNomEquipCerca.getText()
                    );
                    Equip eqExisteix = gDB.cercaEquipNom(eqCercat);
                    Jugador jugador = gDB.cercaJugadorIDLegal(nif); 

                    
                    if (!isChecked) {
                        gDB.TreureTitularitat(jugador.getId(), eqExisteix.getId());
                    }else{
                        // Validar que la categoría del jugador coincide con la categoría del equipo
                        if (!jugador.getCategoriaJugador().equals(eqExisteix.getCategoria())) {
                            JOptionPane.showMessageDialog(
                                null,
                                "Error: El jugador no pot ser titular d'un equip de diferent categoria.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            isProgrammaticChange = true;
                            ModelJugadorsEquip.setValueAt(false, row, column);
                            isProgrammaticChange = false;
                            return; 
                        }
                        Equip titularActual = gDB.esTitular(jugador, eqExisteix.getTemporada());
                        if(titularActual!=null){
                            gDB.TreureTitularitat(jugador.getId(), titularActual.getId());
                            gDB.insertarTitularitat(jugador.getId(), eqExisteix.getId());
                        }else{
                            gDB.insertarTitularitat(jugador.getId(), eqExisteix.getId());
                        }
                    }
 
                   
                } catch (ExceptionClub ex) {
                    // Manejo de errores de lógica del negocio
                    System.out.println(ex.getMessage());
                    ModelJugadorsEquip.setValueAt(false, row, column); // Desmarcar el checkbox si ocurre un error
                    JOptionPane.showMessageDialog(
                        null,
                        "Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (ExceptionClubDB ex) {
                    // Manejo de errores de base de datos
                    ModelJugadorsEquip.setValueAt(false, row, column); // Desmarcar el checkbox si ocurre un error
                    JOptionPane.showMessageDialog(
                        null,
                        "Error en la base de datos: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        tableJugadorsEquip = new JTable(ModelJugadorsEquip);
        JScrollPane selectedScrollPane = new JScrollPane(tableJugadorsEquip);
        selectedScrollPane.setBounds(380, 110, 300, 250);
        panel.add(selectedScrollPane);

        btnInsertJugador = new JButton(">");
        btnInsertJugador.setBounds(330, 150, 50, 30);
        btnInsertJugador.addActionListener(this);
        panel.add(btnInsertJugador);

        btnBaixaJugador = new JButton("<");
        btnBaixaJugador.setBounds(330, 200, 50, 30);
        btnBaixaJugador.addActionListener(this);
        panel.add(btnBaixaJugador);

        btnGuardar = new JButton("Guardar Canvis");
        btnGuardar.addActionListener(this);
        btnGuardar.setBounds(150, 400, 150, 25);
        panel.add(btnGuardar);

        btnDesfer = new JButton("Desfer Canvis");
        btnDesfer.addActionListener(this);
        btnDesfer.setBounds(350, 400, 150, 25);
        panel.add(btnDesfer);


        tableJugadorsposibles.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableJugadorsposibles.getSelectedRow() != -1) {
                btnInsertJugador.setEnabled(true);
                btnBaixaJugador.setEnabled(false);
            }
        });
        tableJugadorsEquip.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableJugadorsEquip.getSelectedRow() != -1) {
                btnInsertJugador.setEnabled(false);
                btnBaixaJugador.setEnabled(true);
            }
        });
        btnInsertJugador.setEnabled(false);
        btnBaixaJugador.setEnabled(false);
        
        return panel;
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

        comboCategoriaCrear = new JComboBox<>();
        comboCategoriaCrear.setBounds(450, 60, 100, 25);
        comboCategoriaCrear.addItem(null); 
        for (Categoria categoria : Categoria.values()) {
            comboCategoriaCrear.addItem(categoria);
        }
        panel.add(comboCategoriaCrear);

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
        
        lblErrorValidar = new JLabel("");
        lblErrorValidar.setBounds(100, 200, 600, 105);
        lblErrorValidar.setForeground(Color.RED);
        panel.add(lblErrorValidar);

        // Botón para crear equipo
        btnCrearEquip = new JButton("Crear Equip");
        btnCrearEquip.addActionListener(this);
        btnCrearEquip.setBounds(300, 440, 100, 25);
        panel.add(btnCrearEquip);
        btnCrearEquip.setEnabled(false);
        // Botón de error para crear equipo
        lblErrorCrearEquip = new JLabel("");
        lblErrorCrearEquip.setBounds(100, 410,520, 25);
        lblErrorCrearEquip.setForeground(Color.RED);
        panel.add(lblErrorCrearEquip);

        // Tabla para mostrar resultados
        String[] columnNames = { "Nom Equip", "Tipus", "Categoria", "Temporada"};
        tableModelResul = new DefaultTableModel(new Object[0][columnNames.length], columnNames);
        tableResul = new JTable(tableModelResul);
        tableResul.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(tableResul);
        scrollPane.setBounds(50, 300, 600, 60);
        panel.add(scrollPane);

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
        }else if(e.getSource() == btnResulat){
            lblErrorCrearEquip.setText("");
            if(validarCamps()){
                nomEquipResul = txtNomEquipCreat.getText().trim();
                tipusResu = rbFemeni.isSelected() ? "Femení" :
                               rbMasculi.isSelected() ? "Masculí" : "Mixta";
                categoriaResu = (Categoria) comboCategoriaCrear.getSelectedItem();
                temporadaResu = comboTemporada.getSelectedItem().toString();
                tableModelResul.setRowCount(0);
                tableModelResul.addRow(new Object[]{nomEquipResul, tipusResu, categoriaResu, temporadaResu});
                btnCrearEquip.setEnabled(true);
            }
        }else if(e.getSource() == btnCrearEquip){
            char tipus = tipusResu.equals("Femení") ? 'D' : tipusResu.equals("Masculí") ? 'H' : 'M';
            lblErrorCrearEquip.setText("");
            try {
                Equip eq_result=new Equip(Integer.parseInt(temporadaResu),nomEquipResul, categoriaResu, tipus);
                Equip eq=gDB.cercaEquipNom(eq_result);
                if(eq==null){
                    gDB.crearEquip(eq_result);
                    JOptionPane.showMessageDialog(crearPanel, 
                            "Equip cret correctament.", 
                            "Èxit", 
                            JOptionPane.INFORMATION_MESSAGE);
                            gDB.confirmarCanvis();
                            actualitzarPanel();
                }else{
                    lblErrorCrearEquip.setText("Ja hi ha un equip amb aquest nom, aquesta temporada.");
                    btnCrearEquip.setEnabled(false);    
                } 
            } catch (ExceptionClub ex) {
                lblErrorCrearEquip.setText(ex.getMessage());
                btnCrearEquip.setEnabled(false);   
            } catch (ExceptionClubDB ex) {
                lblErrorCrearEquip.setText(ex.getMessage());
                btnCrearEquip.setEnabled(false);    
            }
            
        }else if(e.getSource() == btnEditarEquip){
            try {
                Equip eq_cercat=new Equip(Integer.parseInt(comboTemporadaEditar.getSelectedItem().toString()),txtNomEquipCerca.getText());
                Equip eq_existeix=gDB.cercaEquipNom(eq_cercat);
                if(eq_existeix==null){
                    JOptionPane.showMessageDialog(crearPanel, 
                            "No s'ha trobat cap equip a la tempara "+comboTemporadaEditar.getSelectedItem().toString()+ 
                                    " amb aquest nom "+txtNomEquipCerca.getText(), 
                            "Èxit", 
                            JOptionPane.INFORMATION_MESSAGE);
                }else{
                    omplirJugadorsPosibles(eq_existeix);
                    omplirJugadorsEquip(eq_existeix);
                }
            } catch (ExceptionClub ex) {
                JOptionPane.showMessageDialog(crearPanel, 
                            ex.getMessage(), 
                            "Èxit", 
                            JOptionPane.INFORMATION_MESSAGE);
            } catch (ExceptionClubDB ex) {
                JOptionPane.showMessageDialog(crearPanel, 
                            ex.getMessage(), 
                            "Èxit", 
                            JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getSource() == btnGuardar){
            try {
                gDB.confirmarCanvis();
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_GestioEquips.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(e.getSource() == btnDesfer){
            try {
                gDB.desferCanvis();
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_GestioEquips.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(e.getSource() == btnInsertJugador){
            int selectedRow = tableJugadorsposibles.getSelectedRow();
            if (selectedRow != -1) {
                String nif = (String) Modelposibles.getValueAt(selectedRow, 0);
                try {
                    Equip eq = gDB.cercaEquipNom(new Equip(
                            Integer.parseInt(comboTemporadaEditar.getSelectedItem().toString()),
                            txtNomEquipCerca.getText()
                    ));
                    Jugador jugador = gDB.cercaJugadorIDLegal(nif);
                    gDB.insertarJugadorEquip(jugador.getId(), eq.getId()); 
                    omplirJugadorsPosibles(eq);
                    omplirJugadorsEquip(eq);
                } catch (ExceptionClub | ExceptionClubDB ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }else if(e.getSource() == btnBaixaJugador){
            int selectedRow = tableJugadorsEquip.getSelectedRow();
            if (selectedRow != -1) {
                String nif = (String) ModelJugadorsEquip.getValueAt(selectedRow, 0); 
                try {
                    Equip eq = gDB.cercaEquipNom(new Equip(
                            Integer.parseInt(comboTemporadaEditar.getSelectedItem().toString()),
                            txtNomEquipCerca.getText()
                    ));
                    Jugador jugador = gDB.cercaJugadorIDLegal(nif);
                    gDB.deleteJugadorEquip(jugador.getId(), eq.getId());

                    omplirJugadorsPosibles(eq);
                    omplirJugadorsEquip(eq);
                } catch (ExceptionClub | ExceptionClubDB ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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
            txtNovaTemporada.setText("");
        } catch (ExceptionClubDB ex) {
            lblErrorTemporada.setText(ex.getMessage());
        }
    }
    private boolean validarCamps() {
        txtNomEquipCreat.setBackground(Color.WHITE);
        comboCategoriaCrear.setBackground(Color.WHITE);
        comboTemporada.setBackground(Color.WHITE);
        rbFemeni.setForeground(Color.BLACK);
        rbMasculi.setForeground(Color.BLACK);
        rbMixta.setForeground(Color.BLACK);
        lblErrorValidar.setText("");
        String error="Tots els camps son obligatoris. ";
        boolean valid=true;
        if (comboTemporada.getSelectedItem() == null) {
            comboTemporada.setBackground(Color.PINK);
            valid=false;
        }

        // Verificar que la categoría esté seleccionada
        if (comboCategoriaCrear.getSelectedItem() == null) {
            comboCategoriaCrear.setBackground(Color.PINK);
            error+=" Selecciona una categoria.";
            valid=false;
        }

        // Verificar que se haya seleccionado un tipo
        if (!rbFemeni.isSelected() && !rbMasculi.isSelected() && !rbMixta.isSelected()) {
            rbFemeni.setForeground(Color.RED);
            rbMasculi.setForeground(Color.RED);
            rbMixta.setForeground(Color.RED);
             valid=false;
        }

        // Verificar que el nombre del equipo no esté vacío
        if (txtNomEquipCreat.getText().trim().isEmpty()) {
            txtNomEquipCreat.setBackground(Color.PINK);
            valid=false;
        }
    
        if(valid){
            lblErrorValidar.setText("");
        }else{
            lblErrorValidar.setText(error);
            return false;
        }
        return true;
    }
    public void omplirJugadorsPosibles(Equip eq) throws ExceptionClubDB {
    // Limpiamos la tabla antes de rellenar
        Modelposibles.setRowCount(0);
        List <Jugador> jugadorsPosibles=gDB.llistatEquipsPosiblesJugador(eq);
        // Agregamos los jugadores a la tabla
        for (Jugador jugador : jugadorsPosibles) {
            Object[] rowData = {
                jugador.getNif(),
                jugador.getCognom(),
                jugador.getCategoriaJugador().equals(eq.getCategoria())
            };
            Modelposibles.addRow(rowData);
        }
    }
    public void omplirJugadorsEquip(Equip eq) throws ExceptionClubDB {
        // Limpiamos la tabla antes de rellenar
        ModelJugadorsEquip.setRowCount(0);
        List <Jugador> jugadorsEquip=gDB.LlistatJugadorsUnEquip(eq);

        // Agregamos los jugadores a la tabla
        for (Jugador jugador : jugadorsEquip) {
            Object[] rowData = {
                jugador.getNif(),           
                jugador.getCognom(),     
                gDB.asossiacioEquiJugadorTitular(jugador.getId(),eq.getId())    
            };
            ModelJugadorsEquip.addRow(rowData);
        }
    }
        
}
