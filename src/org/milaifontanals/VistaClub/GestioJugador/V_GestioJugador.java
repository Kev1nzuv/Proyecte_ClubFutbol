/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.VistaClub.GestioJugador;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.milaifontanals.CapaModel_P1.*;
import org.milaifontanals.InterficiePersistencia_P1.*;
import org.milaifontanals.VistaClub.V_MenuPrincipal;

/**
 *
 * @author kevin
 */
public class V_GestioJugador extends JFrame implements ActionListener {
    private JButton btnListarCategoria, btnCerca, btnListarFecha,btnEditar, btnFotoUpload, btnAlta, btnModificar,btnCancelar,btnEliminarjugador,btnBaixaEquipsJugador;
    private JPanel consultaPanel, altaModPanel, baixaPanel;
    private JTabbedPane tabbedPane;
    private JTextField txtNom, txtCognom, txtNIF, txtFecha,txtNomModi,txtCognomModi, txtIDLegal, txtIBAN, txtAdrecaModi,txtLocalitatModi, txtCodiPostalModi,txtNIFBaixa;
    private JComboBox<Categoria> comboCategoria;
    private IGestorDB gDB;
    private JTable tableJugadors;
    private JLabel lblError,lblErrorGlobalAlta,lblErrorNom,lblErrorCognom,lblErrorNaix,lblErrorNif,lblErrorIban,lblErrorAdreca,lblNomCognom;
    private DefaultTableModel tableModel;
    private JDateChooser dateChooser, dateChooserNaixement;
    private JRadioButton rbH, rbD,rbSi,rbNo; 
    private ButtonGroup sexeGroup, revisioGroup;
    private String imgPath = null;
    
            
    public V_GestioJugador(IGestorDB gDB) throws ExceptionClubDB {
        this.gDB=gDB;
        
        // Configuración de la ventana
        setTitle("Gestió Jugadors");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // Crear un TabbedPane para las tres pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50, 50, 700, 500);
        
        // Crear el panel de "Consulta" (pestaña visible ahora)
        consultaPanel = crearConsultaPanel();
        tabbedPane.addTab("Consulta", consultaPanel);
        
        // Placeholder para las otras pestañas (por ahora vacías)
        altaModPanel = crearAltaModificacioPanel();
        tabbedPane.addTab("Alta-Modificació", altaModPanel);
        
        
        baixaPanel = crearBaixaPanel();
        tabbedPane.addTab("Baixa", baixaPanel);
        
        lblError = new JLabel("");
        lblError.setBounds(75, 230, 300, 25);
        lblError.setForeground(Color.RED);
        consultaPanel.add(lblError);

        consultaPanel.revalidate();
        consultaPanel.repaint();
        
        // Añadir el TabbedPane a la ventana
        add(tabbedPane);
        
        cargarJugadors(gDB.llistatJugadors(), (Categoria) comboCategoria.getSelectedItem(),dateChooser.getDate(),txtNom.getText().trim(),txtCognom.getText().trim());
        
        /*
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
        
        });
        */
        
    }
    private JPanel crearBaixaPanel() {
        JPanel baixaPanel = new JPanel();
        baixaPanel.setLayout(null); // Layout absoluto

        // Etiqueta y campo de texto para NIF
        JLabel lblNIF = new JLabel("ID Legal");
        lblNIF.setBounds(50, 30, 100, 25);
        baixaPanel.add(lblNIF);

        txtNIFBaixa = new JTextField();
        txtNIFBaixa.setBounds(150, 30, 150, 25);
        baixaPanel.add(txtNIFBaixa);

        // Etiqueta para mostrar el nombre completo del jugador
        lblNomCognom = new JLabel("");
        lblNomCognom.setBounds(320, 30, 300, 100);
        lblNomCognom.setFont(new Font("Arial", Font.BOLD, 18)); 
        baixaPanel.add(lblNomCognom);

        // Tabla para mostrar los equipos
        String[] columnNames = {"Temporada", "Equip", "Categoria","Tipus"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable tablaEquips = new JTable(tableModel);
        tablaEquips.setEnabled(false); // La tabla no será editable

        JScrollPane scrollPane = new JScrollPane(tablaEquips);
        scrollPane.setBounds(100, 140, 400, 150);
        baixaPanel.add(scrollPane);

        btnBaixaEquipsJugador = new JButton("Baixa dels equips");
        btnBaixaEquipsJugador.setBounds(100, 400, 150, 30);
        btnBaixaEquipsJugador.setEnabled(false); // Por defecto está deshabilitado
        baixaPanel.add(btnBaixaEquipsJugador);

        // Botón "Eliminar"
        btnEliminarjugador = new JButton("Eliminar Jugador");
        btnEliminarjugador.setBounds(400, 400, 150, 30);
        btnEliminarjugador.setEnabled(false); // Por defecto está deshabilitado
        baixaPanel.add(btnEliminarjugador);

        // Acción al introducir el NIF
        txtNIFBaixa.addActionListener(e -> {

            String nif = txtNIFBaixa.getText().trim();
            try {
                if(gDB.existenciaJugador(new Jugador(nif))>0){
                    Jugador jugador = gDB.cercaJugadorIDLegal(nif);
                    lblNomCognom.setText("<html>"+jugador.getNom() + " " + jugador.getCognom()+"<br>"+jugador.getDataNaix()+"</html>");

                    List<Equip> equips = gDB.llistatEquipsDelJugador(jugador); // Método de base de datos
                    tableModel.setRowCount(0); 

                    for (Equip equip : equips) {
                        tableModel.addRow(new Object[]{equip.getTemporada(), equip.getNom(),equip.getCategoria().name(),equip.getTipus()});
                    }
                    btnBaixaEquipsJugador.setEnabled(!equips.isEmpty());
                    btnEliminarjugador.setEnabled(equips.isEmpty());
                }else{
                    lblNomCognom.setText("<html> No hi ha cap jugador <br>amb aquestes credencials. </html>");
                }
            } catch (ExceptionClubDB ex) {
                lblNomCognom.setText("Jugador no trobat");
                btnEliminarjugador.setEnabled(false);
                tableModel.setRowCount(0);
            } catch (ExceptionClub ex) {
                lblNomCognom.setText("NIF incorrecte");
            }
        });
        btnBaixaEquipsJugador.addActionListener(e ->{
            String nif = txtNIFBaixa.getText().trim();
            try{
                if(gDB.existenciaJugador(new Jugador(nif))>0){        
                    Jugador j=gDB.cercaJugadorIDLegal(nif);
                    gDB.elimarMembresiaTotsEquips(j);
                    int confirm = JOptionPane.showConfirmDialog(
                        baixaPanel, 
                        "Estàs segur que vols eliminar les associacions del jugador amb els equips?", 
                        "Confirmació d'eliminació", 
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        gDB.confirmarCanvis();
                                          
                        lblNomCognom.setText("<html>" + j.getNom() + " " + j.getCognom() + "<br>" + j.getDataNaix() + "</html>");
                        List<Equip> equips = gDB.llistatEquipsDelJugador(j); // Lista actualizada de equipos
                        tableModel.setRowCount(0);
                        for (Equip equip : equips) {
                            tableModel.addRow(new Object[]{equip.getTemporada(), equip.getNom(), equip.getCategoria().name(), equip.getTipus()});
                        }
                        btnBaixaEquipsJugador.setEnabled(!equips.isEmpty());
                        btnEliminarjugador.setEnabled(equips.isEmpty());  
                    }else{
                         gDB.desferCanvis();
                    }
                }else{
                    lblNomCognom.setText("Aquest jugador no existeix.");
                }
            } catch (ExceptionClubDB ex) {
                lblNomCognom.setText(ex.getMessage());
            } catch (ExceptionClub ex) {
                lblNomCognom.setText(ex.getMessage());
            }
        });
        // Acción del botón "Eliminar"
        btnEliminarjugador.addActionListener(e -> {
            String nif = txtNIFBaixa.getText().trim();
            try {
                if(gDB.existenciaJugador(new Jugador(nif))>0){ 
                    gDB.deleteJugador(gDB.cercaJugadorIDLegal(nif));
                    int confirm = JOptionPane.showConfirmDialog(
                        baixaPanel, 
                        "Estàs segur que vols eliminar el jugador?", 
                        "Confirmació d'eliminació", 
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        gDB.confirmarCanvis();
                        txtNIFBaixa.setText("");
                        lblNomCognom.setText("");
                        tableModel.setRowCount(0); 
                        btnEliminarjugador.setEnabled(false);
                    }else{
                        gDB.desferCanvis();
                    }
                }else{
                    lblNomCognom.setText("Aquest jugador no existeix.");
                }
            } catch (ExceptionClubDB ex) {
                lblNomCognom.setText(ex.getMessage());
            } catch (ExceptionClub ex) {
                lblNomCognom.setText(ex.getMessage());
            }
        });

        return baixaPanel;
    }
    // Método para crear el panel de "Consulta"
    private JPanel crearConsultaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE); 
        
        // Títulos de los subpaneles
        JLabel lblCercaJugadors = new JLabel("Cercà Jugadors");
        lblCercaJugadors.setFont(new Font("Arial", Font.BOLD, 16));
        lblCercaJugadors.setBounds(50, 20, 150, 20);
        panel.add(lblCercaJugadors);
        
        JLabel lblLlistatJugadors = new JLabel("Llistat de Jugadors");
        lblLlistatJugadors.setFont(new Font("Arial", Font.BOLD, 16));
        lblLlistatJugadors.setBounds(400, 20, 200, 20);
        panel.add(lblLlistatJugadors);
        
        // Campos del formulario "Cercà Jugadors"
        JLabel lblNom = new JLabel("Nom:");
        lblNom.setBounds(20, 60, 100, 25);
        panel.add(lblNom);
        
        txtNom = new JTextField();
        txtNom.setBounds(80, 60, 150, 25);
        panel.add(txtNom);
        
        JLabel lblCognom = new JLabel("Cognom:");
        lblCognom.setBounds(20, 100, 100, 25);
        panel.add(lblCognom);
        
        txtCognom = new JTextField();
        txtCognom.setBounds(80, 100, 150, 25);
        panel.add(txtCognom);
        
        JLabel lblNIF = new JLabel("NIF:");
        lblNIF.setBounds(20, 140, 100, 25);
        panel.add(lblNIF);
        
        txtNIF = new JTextField();
        txtNIF.setBounds(80, 140, 150, 25);
        panel.add(txtNIF);
        
        btnCerca = new JButton("Cerca");
        btnCerca.setBounds(100, 180, 100, 30);
        btnCerca.addActionListener(this);
        panel.add(btnCerca);
        
        
        // Panel derecho "Llistat de Jugadors"
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(250, 60, 100, 25);
        panel.add(lblCategoria);
        
        comboCategoria = new JComboBox<>();
        comboCategoria.setBounds(320, 60, 100, 25);
        comboCategoria.addItem(null); 
        for (Categoria categoria : Categoria.values()) {
            comboCategoria.addItem(categoria);
        }
        comboCategoria.addActionListener(this);
        panel.add(comboCategoria);
        
        dateChooser = new JDateChooser();
        dateChooser.setBounds(465, 60, 150, 25);
        dateChooser.setDateFormatString("dd-MM-yyyy");
        panel.add(dateChooser);
        
        dateChooser.addPropertyChangeListener("date", evt -> {
            try {
                cargarJugadors(
                        gDB.llistatJugadors(),
                        (Categoria) comboCategoria.getSelectedItem(),
                        dateChooser.getDate(),txtNom.getText().trim()
                        ,txtCognom.getText().trim()
                );      } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
  
        // Parte del método crearConsultaPanel
        tableModel = new DefaultTableModel(new String[]{"Nom", "Cognom", "NIF", "Data Naixement", "Sexe", "Categoria"}, 0);
        tableJugadors = new JTable(tableModel);

        // Configurar el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableJugadors.setRowSorter(sorter);

        // Comparador personalizado para ordenar fechas en la columna 3 (Data Naixement)
        sorter.setComparator(3, Comparator.comparing((String date) -> {
            try {
                return new SimpleDateFormat("dd-MM-yyyy").parse(date);
            } catch (ParseException e) {
                return null;
            }
        }));

        JScrollPane scrollPane = new JScrollPane(tableJugadors);
        scrollPane.setBounds(260, 120, 400, 250);
        panel.add(scrollPane);

        return panel;
        
        
    }
    
    public void cargarJugadors(List<Jugador> jugadores, Categoria categoriaSeleccionada, Date fechaSeleccionada, String nom, String cognom) {
        tableModel.setRowCount(0); // Limpiar tabla

        LocalDate fechaFiltro = fechaSeleccionada != null
            ? fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            : null;

        for (Jugador jugador : jugadores) {
            boolean categoriaCoincide = (categoriaSeleccionada == null || jugador.getCategoriaJugador().equals(categoriaSeleccionada));
            boolean fechaCoincide = (fechaFiltro == null || jugador.getDataNaix().isAfter(fechaFiltro));
            boolean nomCoincide = (nom == null || nom.isEmpty() || jugador.getNom().equalsIgnoreCase(nom));
            boolean cognomCoincide = (cognom == null || cognom.isEmpty() || jugador.getCognom().equalsIgnoreCase(cognom));

            if (categoriaCoincide && fechaCoincide && nomCoincide && cognomCoincide) {
                String sexe = jugador.getSexe() == 'H' ? "Masculí" : "Femení";
                tableModel.addRow(new Object[]{
                    jugador.getNom(), 
                    jugador.getCognom(), 
                    jugador.getNif(), 
                    jugador.getDataNaix().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
                    sexe,
                    jugador.getCategoriaJugador()
                });
            }
        }
    }

    public JPanel crearAltaModificacioPanel() {
        JPanel altaModPanel = new JPanel();
        altaModPanel.setLayout(null);

        // Campo "Nom"
        JLabel lblNom = new JLabel("Nom");
        lblNom.setBounds(20, 20, 80, 25);
        altaModPanel.add(lblNom);

        txtNomModi = new JTextField();
        txtNomModi.setBounds(100, 20, 150, 25);
        altaModPanel.add(txtNomModi);
        
        lblErrorNom = new JLabel("");
        lblErrorNom.setBounds(20, 40, 400, 25);
        lblErrorNom.setForeground(Color.RED);
        altaModPanel.add(lblErrorNom);

        // Campo "Cognom"
        JLabel lblCognom = new JLabel("Cognom");
        lblCognom.setBounds(20, 60, 80, 25);
        altaModPanel.add(lblCognom);

        txtCognomModi = new JTextField();
        txtCognomModi.setBounds(100, 60, 150, 25);
        altaModPanel.add(txtCognomModi);

        lblErrorCognom = new JLabel("");
        lblErrorCognom.setBounds(20, 80, 400, 25);
        lblErrorCognom.setForeground(Color.RED);
        altaModPanel.add(lblErrorCognom);
        
        // Campo "Data Naix"
        JLabel lblDataNaix = new JLabel("Data Naix");
        lblDataNaix.setBounds(20, 100, 80, 25);
        altaModPanel.add(lblDataNaix);

        dateChooserNaixement = new JDateChooser();
        dateChooserNaixement.setBounds(100, 100, 150, 25);
        altaModPanel.add(dateChooserNaixement);
        
        lblErrorNaix = new JLabel("");
        lblErrorNaix.setBounds(20, 120, 400, 25);
        lblErrorNaix.setForeground(Color.RED);
        altaModPanel.add(lblErrorNaix);

        // Campo "Sexe"
        JLabel lblSexe = new JLabel("Sexe");
        lblSexe.setBounds(20, 140, 80, 25);
        altaModPanel.add(lblSexe);

        rbH = new JRadioButton("Home");
        rbH.setBounds(100, 140, 80, 25);
        altaModPanel.add(rbH);

        rbD = new JRadioButton("Dona");
        rbD.setBounds(180, 140, 80, 25);
        altaModPanel.add(rbD);

        sexeGroup = new ButtonGroup();
        sexeGroup.add(rbH);
        sexeGroup.add(rbD);

        // Campo "ID Legal"
        JLabel lblIDLegal = new JLabel("ID Legal");
        lblIDLegal.setBounds(300, 20, 80, 25);
        altaModPanel.add(lblIDLegal);

        txtIDLegal = new JTextField();
        txtIDLegal.setBounds(380, 20, 150, 25);
        altaModPanel.add(txtIDLegal);
        
        lblErrorNif = new JLabel("");
        lblErrorNif.setBounds(300, 40, 400, 25);
        lblErrorNif.setForeground(Color.RED);
        altaModPanel.add(lblErrorNif);
        
        btnEditar = new JButton("Editar");
        btnEditar.setBounds(540, 20, 100, 25);
        btnEditar.addActionListener(this);
        altaModPanel.add(btnEditar);
        // Campo "IBAN"
        JLabel lblIBAN = new JLabel("IBAN");
        lblIBAN.setBounds(300, 60, 80, 25);
        altaModPanel.add(lblIBAN);

        txtIBAN = new JTextField();
        txtIBAN.setBounds(380, 60, 150, 25);
        altaModPanel.add(txtIBAN);
        
        lblErrorIban = new JLabel("");
        lblErrorIban.setBounds(300, 80, 400, 25);
        lblErrorIban.setForeground(Color.RED);
        altaModPanel.add(lblErrorIban);

        // Campo "Adreça"
        JLabel lblAdreca = new JLabel("Adreça");
        lblAdreca.setBounds(300, 100, 80, 25);
        altaModPanel.add(lblAdreca);

        txtAdrecaModi = new JTextField();
        txtAdrecaModi.setBounds(380, 100, 150, 25);
        altaModPanel.add(txtAdrecaModi);

        // Campo "Localitat"
        JLabel lblLocalitat = new JLabel("Localitat");
        lblLocalitat.setBounds(300, 140, 80, 25);
        altaModPanel.add(lblLocalitat);

        txtLocalitatModi = new JTextField();
        txtLocalitatModi.setBounds(380, 140, 150, 25);
        altaModPanel.add(txtLocalitatModi);
        
        

        // Campo "Codi Postal"
        JLabel lblCodiPostal = new JLabel("Codi Postal");
        lblCodiPostal.setBounds(300, 180, 80, 25);
        altaModPanel.add(lblCodiPostal);

        txtCodiPostalModi = new JTextField();
        txtCodiPostalModi.setBounds(380, 180, 150, 25);
        altaModPanel.add(txtCodiPostalModi);

        lblErrorAdreca = new JLabel("");
        lblErrorAdreca.setBounds(300, 200, 400, 25);
        lblErrorAdreca.setForeground(Color.RED);
        altaModPanel.add(lblErrorAdreca);
        
        // Revisió Mèdica
        JLabel lblRevisio = new JLabel("Revisió Mèdica");
        lblRevisio.setBounds(20, 180, 100, 25);
        altaModPanel.add(lblRevisio);

        rbSi = new JRadioButton("Sí");
        rbSi.setBounds(120, 180, 50, 25);
        altaModPanel.add(rbSi);

        rbNo = new JRadioButton("No");
        rbNo.setBounds(180, 180, 50, 25);
        altaModPanel.add(rbNo);

        revisioGroup = new ButtonGroup();
        revisioGroup.add(rbSi);
        revisioGroup.add(rbNo);

        // Botón para subir foto
        JLabel lblFoto = new JLabel("Foto");
        lblFoto.setBounds(20, 220, 80, 25);
        altaModPanel.add(lblFoto);

        btnFotoUpload = new JButton("Afegir Foto");
        btnFotoUpload.setBounds(100, 220, 150, 25);
        btnFotoUpload.addActionListener(this);
        altaModPanel.add(btnFotoUpload);

        // Mensaje de error global
        lblErrorGlobalAlta = new JLabel();
        lblErrorGlobalAlta.setBounds(300, 290, 400, 25);
        lblErrorGlobalAlta.setForeground(Color.RED);
        altaModPanel.add(lblErrorGlobalAlta);

        // Botones
        btnAlta = new JButton("Alta");
        btnAlta.setBounds(300, 320, 100, 30);
        btnAlta.addActionListener(this);
        altaModPanel.add(btnAlta);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(420, 320, 100, 30);
        btnModificar.addActionListener(this);
        altaModPanel.add(btnModificar);
        btnModificar.setEnabled(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(540, 320, 100, 30);
        btnCancelar.addActionListener(this);
        altaModPanel.add(btnCancelar);
        
        
        return altaModPanel;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCerca) {
            lblError.setText("");
            String nom = txtNom.getText().trim();
            String cognom = txtCognom.getText().trim();
            String nif = txtNIF.getText().trim();
            if(!nif.isEmpty()){
                try {
                    if(gDB.existenciaJugador(new Jugador(nif))>0){
                        Jugador j=this.gDB.cercaJugadorIDLegal(nif);
                        this.dispose();
                        V_JugadorCercat jugador = new V_JugadorCercat(gDB,j);
                        jugador.setVisible(true);
                    }else{
                        lblError.setText("Aquest jugador no existeix.");
                        consultaPanel.revalidate();
                        consultaPanel.repaint();
                    }    
                } catch (ExceptionClub ex) {
                    lblError.setText(ex.getMessage());
                    consultaPanel.revalidate();
                    consultaPanel.repaint();
                } catch (ExceptionClubDB ex) {
                    lblError.setText(ex.getMessage());
                    consultaPanel.revalidate();
                    consultaPanel.repaint();
                }  
            }else if(!nom.isEmpty() && !cognom.isEmpty()){
                try {
                    int num = this.gDB.JugadorsCoinciNC(nom,cognom);
                    if(num>1){
                        try {
                            cargarJugadors(
                                    gDB.llistatJugadors(),
                                    (Categoria) comboCategoria.getSelectedItem(),
                                    dateChooser.getDate(),txtNom.getText().trim()
                                    ,txtCognom.getText().trim()
                            );      
                        } catch (ExceptionClubDB ex) {
                                        Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(num==1){
                        Jugador j=this.gDB.cercaJugadorNomCognom(nom, cognom);
                        this.dispose();
                        V_JugadorCercat jugador = new V_JugadorCercat(gDB,j);
                        jugador.setVisible(true);
                    }else{
                        lblError.setText("Aquest jugador no existeix.");
                        consultaPanel.revalidate();
                        consultaPanel.repaint();
                    }
                } catch (ExceptionClubDB ex) {
                    Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(!nom.isEmpty() && cognom.isEmpty()){
                try {
                    int num = this.gDB.JugadorsCoinciNom(nom);
                    if(num>1){
                    try {
                        cargarJugadors(
                                gDB.llistatJugadors(),
                                (Categoria) comboCategoria.getSelectedItem(),
                                dateChooser.getDate(),txtNom.getText().trim()
                                ,txtCognom.getText().trim()
                        );      
                    } catch (ExceptionClubDB ex) {
                                    Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }else if(num==1){
                        Jugador j=this.gDB.cercaJugadorNom(nom);
                        this.dispose();
                        V_JugadorCercat jugador = new V_JugadorCercat(gDB,j);
                        jugador.setVisible(true);
                    }else{
                        lblError.setText("Aquest jugador no existeix.");
                        consultaPanel.revalidate();
                        consultaPanel.repaint();
                    }
                } catch (ExceptionClubDB ex) {
                    Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(nom.isEmpty() && !cognom.isEmpty()){
                try {
                    int num = this.gDB.JugadorsCoinciCognom(cognom);
                    if(num>1){
                        try {
                            cargarJugadors(
                                    gDB.llistatJugadors(),
                                    (Categoria) comboCategoria.getSelectedItem(),
                                    dateChooser.getDate(),txtNom.getText().trim()
                                    ,txtCognom.getText().trim()
                            );      
                        } catch (ExceptionClubDB ex) {
                                        Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }else if(num==1){
                        Jugador j=this.gDB.cercaJugadorCognom(cognom);
                        this.dispose();
                        V_JugadorCercat jugador = new V_JugadorCercat(gDB,j);
                        jugador.setVisible(true);
                    }else{
                        lblError.setText("Aquest jugador no existeix.");
                        consultaPanel.revalidate();
                        consultaPanel.repaint();
                    }
                } catch (ExceptionClubDB ex) {
                    Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                lblError.setText("Posa algun camp per cercar.");
                consultaPanel.revalidate();
                consultaPanel.repaint();
            }
            
        }else if (e.getSource() == comboCategoria) {
            try {
                // Actualizar la tabla cuando se cambia la categoría en el JComboBox
                cargarJugadors(
                        gDB.llistatJugadors(),
                        (Categoria) comboCategoria.getSelectedItem(),
                        dateChooser.getDate(),txtNom.getText().trim(),
                        txtCognom.getText().trim()
                );  } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(e.getSource() == btnAlta){
            if (txtNomModi.getText().trim().isEmpty() || 
                txtCognomModi.getText().trim().isEmpty() || 
                txtIDLegal.getText().trim().isEmpty() || 
                txtIBAN.getText().trim().isEmpty() || 
                dateChooserNaixement.getDate() == null ||
                imgPath == null || txtAdrecaModi.getText().trim().isEmpty() ||
                txtCodiPostalModi.getText().trim().isEmpty()||
                txtLocalitatModi.getText().trim().isEmpty()||
                sexeGroup.getSelection() == null || 
                revisioGroup.getSelection() == null) {
            
                lblErrorGlobalAlta.setText("Tots els camps són obligatoris ❗❗❗");
            }else{
                lblErrorGlobalAlta.setText("");
                
                    char sexe;
                    if(rbH.isSelected()){
                        sexe='H';
                    }else{
                        sexe='D';
                    }
                    Date naix=dateChooserNaixement.getDate();
                    LocalDate localDatenaix = naix.toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                    LocalDate rev;
                    if(rbSi.isSelected()){
                        rev=LocalDate.now().plusYears(1);
                    }else{
                        rev=LocalDate.now().plusYears(-1);
                    }
                    String adre=txtAdrecaModi.getText()+"$";
                    adre+=txtLocalitatModi.getText()+"$";
                    adre+=txtCodiPostalModi.getText();
     
                
                        /*Jugador j=new Jugador(txtNomModi.getText().trim(),txtCognomModi.getText().trim(), 
                        txtIBAN.getText().trim(), txtIDLegal.getText().trim(), 
                        sexe, localDatenaix, 
                        rev,adre,destinationDir);*/
                lblErrorNif.setText("");
                lblErrorNom.setText("");
                lblErrorCognom.setText("");
                lblErrorNaix.setText("");
                lblErrorIban.setText("");
                lblErrorAdreca.setText("");
                lblErrorGlobalAlta.setText("");
                    Jugador j = null;
                    boolean correcte = true;

                    // Crear el objeto Jugador antes de realizar cualquier validación
                    try {
                        j = new Jugador(txtIDLegal.getText().trim());
                    } catch (Exception ex) {
                        lblErrorNif.setText(ex.getMessage());
                        correcte = false;
                    }

                    // Validar si el jugador ya existe
                    if (correcte) {
                        try {
                            if (gDB.existenciaJugador(j) != 0) {
                                lblErrorNif.setText("Aquest jugador ja està donat d'alta.");
                                correcte = false;
                            }
                        } catch (Exception ex) {
                            lblErrorNif.setText(ex.getMessage());
                            correcte = false;
                        }
                    }

                    // Configurar los datos del jugador solo si el objeto se creó correctamente
                    if (correcte) {
                        try {
                            j.setNom(txtNomModi.getText().trim());
                        } catch (Exception ex) {
                            lblErrorNom.setText(ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setCognom(txtCognomModi.getText().trim());
                        } catch (Exception ex) {
                            lblErrorCognom.setText(ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setDataNaix(localDatenaix);
                        } catch (Exception ex) {
                            lblErrorNaix.setText(ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setIban(txtIBAN.getText().trim());
                        } catch (Exception ex) {
                            lblErrorIban.setText(ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setAdreca(adre);
                        } catch (Exception ex) {
                            lblErrorAdreca.setText(ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setSexe(sexe);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                            correcte = false;
                        }
                        try {
                            j.setRivisioMedica(rev);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                            correcte = false;
                        }
                    }

        // Guardar la imagen solo si no hay errores
            if (imgPath != null && correcte) {
                try {
                    String destinationDir = "img/";
                    File destinationFolder = new File(destinationDir);
                    if (!destinationFolder.exists()) {
                        destinationFolder.mkdir();
                    }
                    String destinationPath = destinationDir + txtIDLegal.getText().trim() + ".jpg";
                    File destinationFile = new File(destinationPath);
                    File sourceFile = new File(imgPath);
                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    j.setFoto(destinationDir);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar la imatge: " + ex.getMessage());
                    correcte = false;
                }
            } else if (imgPath == null) {
                JOptionPane.showMessageDialog(null, "Eps, no hi cap imatge seleccionada.");
            }


        if (correcte) {
            try {
                gDB.insertarJugador(j);
                gDB.confirmarCanvis();
                JOptionPane.showMessageDialog(null, "Jugador inserit correctament!");
            } catch (ExceptionClubDB ex) {
                JOptionPane.showMessageDialog(null, "Error al inserir: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No s'ha pogut inserir el jugador. Revisa els errors.");
        }
    }
            
            
        }else if(e.getSource()==btnEditar){
            lblErrorGlobalAlta.setText(""); 
            lblErrorNif.setText("");
            lblErrorNom.setText("");
            lblErrorCognom.setText("");
            lblErrorNaix.setText("");
            lblErrorIban.setText("");
            lblErrorAdreca.setText("");
            lblErrorGlobalAlta.setText("");
            String nif=txtIDLegal.getText().trim();
            if(nif.isEmpty()){
                lblErrorGlobalAlta.setText("El camp ID Legal s'ha d'omplir.");
            }else{
                try {//editar jugador
                    if(gDB.existenciaJugador(new Jugador(nif))>0){
                        lblErrorGlobalAlta.setText("");
                        Jugador j=gDB.cercaJugadorIDLegal(nif);
                        btnModificar.setEnabled(true);
                        btnAlta.setEnabled(false);
                        btnEditar.setEnabled(false);
                        txtIDLegal.setEnabled(false);
                        txtNomModi.setText(j.getNom());
                        txtCognomModi.setText(j.getCognom());
                        txtIBAN.setText(j.getIban());
                        
                        if (j.getSexe() == 'H') {
                            rbH.setSelected(true);
                        } else {
                            rbD.setSelected(true);
                        }
                        rbD.setEnabled(false);
                        rbH.setEnabled(false);
                        if(j.getRivisioMedica().isAfter(LocalDate.now())){
                            rbSi.setSelected(true); 
                        }else{
                            rbNo.setSelected(true);
                        }
                        Date date = Date.from(j.getDataNaix().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        dateChooserNaixement.setDate(date);
                        dateChooserNaixement.setEnabled(false);
                        String [] adre=j.getAdreca().split("\\$");
                        txtAdrecaModi.setText(adre[0]);
                        txtLocalitatModi.setText(adre[1]);
                        txtCodiPostalModi.setText(adre[2]);
                         
                    }else{
                        lblErrorGlobalAlta.setText("");
                        lblErrorGlobalAlta.setText("Aquest jugador no existeix.");
                    }                    
                } catch (ExceptionClubDB ex) {
                    lblErrorGlobalAlta.setText(ex.getMessage());
                } catch (ExceptionClub ex) {
                    lblErrorGlobalAlta.setText(ex.getMessage());
                }
            }
        }else if(e.getSource()==btnCancelar){
            Cancelar();
            
        }else if(e.getSource()==btnModificar){
            lblErrorNif.setText("");
            lblErrorNom.setText("");
            lblErrorCognom.setText("");
            lblErrorNaix.setText("");
            lblErrorIban.setText("");
            lblErrorAdreca.setText("");
            if (txtNomModi.getText().trim().isEmpty() || 
                txtCognomModi.getText().trim().isEmpty() || 
                txtIDLegal.getText().trim().isEmpty() || 
                txtIBAN.getText().trim().isEmpty() || 
                dateChooserNaixement.getDate() == null ||
                 txtAdrecaModi.getText().trim().isEmpty() ||
                txtCodiPostalModi.getText().trim().isEmpty()||
                txtLocalitatModi.getText().trim().isEmpty()||
                sexeGroup.getSelection() == null || 
                revisioGroup.getSelection() == null) {
                
                lblErrorGlobalAlta.setText("Tots els camps són obligatoris ❗❗❗");
            }else{
                boolean correcte=true;
                lblErrorGlobalAlta.setText("");
                String iban=txtIBAN.getText().trim();
                String nom=txtNomModi.getText().trim();
                String Cognom=txtCognomModi.getText().trim();
                String local=txtLocalitatModi.getText().trim();
                String codi=txtCodiPostalModi.getText().trim();
                String are=txtAdrecaModi.getText().trim();
                String adreca=are+"$"+local+"$"+codi;
                try {
                    Jugador j=gDB.cercaJugadorIDLegal(txtIDLegal.getText());
                    LocalDate revMedi;
                    if (j.getRivisioMedica().isBefore(LocalDate.now()) && rbSi.isSelected()) {
                        revMedi = LocalDate.now().plusYears(1);
                    } else {
                        revMedi = j.getRivisioMedica();
                    }
                    
                    Jugador modificat=new Jugador(j.getNif());
                    try{
                        modificat.setNom(nom);
                    }catch(ExceptionClub ex){
                        lblErrorNom.setText(ex.getMessage());
                        correcte=false;
                    }
                    try{
                        modificat.setCognom(Cognom);
                    }catch(ExceptionClub ex){
                        lblErrorCognom.setText(ex.getMessage());
                        correcte=false;
                    }
                    try{
                        modificat.setIban(iban);
                    }catch(ExceptionClub ex){
                        lblErrorIban.setText(ex.getMessage());
                        correcte=false;
                    }
                    try{
                        modificat.setAdreca(adreca);
                    }catch(ExceptionClub ex){
                        lblErrorAdreca.setText(ex.getMessage());
                        correcte=false;
                    }

                    modificat.setDataNaix(j.getDataNaix());
                    modificat.setRivisioMedica(revMedi);
                    modificat.setSexe(j.getSexe());
                    modificat.setId(j.getId());


                    String novaRuta = j.getFoto(); // Ruta actual de la imagen
                    if (imgPath != null && correcte) { // El usuario subió una nueva imagen
                        // Eliminar la imagen anterior
                        File imagenAnterior = new File(j.getFoto());
                        if (imagenAnterior.exists()) {
                            imagenAnterior.delete();
                        }

                        // Guardar la nueva imagen con el nombre IDLegal.jpg
                        String destinationDir = "img/";
                        File destinationFolder = new File(destinationDir);
                        if (!destinationFolder.exists()) {
                            destinationFolder.mkdir();
                        }

                        novaRuta = destinationDir + txtIDLegal.getText().trim() + ".jpg";
                        File sourceFile = new File(imgPath);
                        File destinationFile = new File(novaRuta);
                        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                        modificat.setFoto(novaRuta);
                        gDB.updateJugador(modificat);
                        gDB.confirmarCanvis();
                        Cancelar();
                } catch (ExceptionClubDB ex) {
                    lblErrorGlobalAlta.setText(ex.getMessage());  
                } catch (ExceptionClub ex) {
                    lblErrorGlobalAlta.setText(ex.getMessage());
                } catch (IOException ex) {
                    Logger.getLogger(V_GestioJugador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }else if(e.getSource()==btnFotoUpload){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Selecciona una foto");
            // Filtro para asegurarnos de que solo se seleccionen imágenes
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos de Imagen", "jpg", "png", "jpeg"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imgPath = selectedFile.getAbsolutePath();
                JOptionPane.showMessageDialog(null, "Imagen seleccionada: " + imgPath);
            }
        }
        
        
        
    }
    public void Cancelar(){
        lblErrorGlobalAlta.setText(""); 
            btnAlta.setEnabled(true);
            btnEditar.setEnabled(true);
            btnModificar.setEnabled(false);
            btnEditar.setEnabled(true);
            dateChooserNaixement.setEnabled(true);
            txtIDLegal.setEnabled(true);
            rbH.setEnabled(true);
            rbD.setEnabled(true);
            txtNomModi.setText("");
            txtCognomModi.setText("");
            txtIBAN.setText("");
            txtIDLegal.setText("");
            sexeGroup.clearSelection();
            revisioGroup.clearSelection();
            txtAdrecaModi.setText("");
            txtLocalitatModi.setText("");
            txtCodiPostalModi.setText("");
            dateChooserNaixement.setDate(null);
            lblErrorNif.setText("");
            lblErrorNom.setText("");
            lblErrorCognom.setText("");
            lblErrorNaix.setText("");
            lblErrorIban.setText("");
            lblErrorAdreca.setText("");
    }
    
}
