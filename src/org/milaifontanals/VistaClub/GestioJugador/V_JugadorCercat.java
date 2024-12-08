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
    private boolean isProgrammaticChange = false;
    
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
        lblFoto.setBounds(45, 20, 160, 150);
        ImageIcon icono = new ImageIcon(jugador.getFoto());
        Image imgOriginal = icono.getImage(); 
        Image imgEscalada = imgOriginal.getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
        lblFoto.setIcon(new ImageIcon(imgEscalada));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setVerticalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblFoto);
        

        // Nombre y Apellidos
        lblNom = new JLabel(jugador.getNom() + " " + jugador.getCognom());
        lblNom.setBounds(20, 180, 200, 30);
        lblNom.setHorizontalAlignment(SwingConstants.CENTER);
        lblNom.setFont(new Font("Arial", Font.BOLD, 18));
        panelIzquierdo.add(lblNom);

        // NIF
        lblNif = new JLabel("NIF: " + jugador.getNif());
        lblNif.setBounds(20, 200, 200, 30);
        lblNif.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblNif);
        
        
        //Sexe
        String sexe="";
        switch(jugador.getSexe()){
                case 'H':sexe="Home";break;
                case 'D':sexe="Dona";break;               
        }    
        lblSexe = new JLabel("Sexe: "+ sexe);
        lblSexe.setBounds(10, 240, 200, 30);
        lblSexe.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblSexe);

        // Fecha de nacimiento
        lblDataNaix = new JLabel("<html>Data naixement:<br>" + jugador.getDataNaix() + "</html>");
        lblDataNaix.setBounds(20, 250, 200, 100);
        lblDataNaix.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblDataNaix);
        
        lblrevisio = new JLabel("<html>Revisió medica:<br> " + jugador.getRivisioMedica()+ "</html>");
        lblrevisio.setBounds(20, 280, 200, 160);
        lblrevisio.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(lblrevisio);

        
        JLabel cate=new JLabel("<html>Categoria:<br> "+ jugador.getCategoriaJugador().name()+ "</html>");
        cate.setBounds(60, 350, 100, 100);
        cate.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(cate);
        String []adreca=jugador.getAdreca().split("\\$");
        
        JLabel adre=new JLabel("<html>Adreça: "+adreca[0]+"<br>"+"Localitat: "+adreca[1]+"<br>"+"Codi Postal: "+adreca[2]+"</html>");
        adre.setBounds(20, 400, 200, 100);
        adre.setHorizontalAlignment(SwingConstants.CENTER);
        panelIzquierdo.add(adre);
        

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
        btnVolver.setBounds(10, 500, 100, 30); 
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
        };
        table.getColumnModel().getColumn(3).setCellRenderer(new CustomCheckboxRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new CustomCheckboxEditor());
       // Añadir el TableModelListener
        tableModel.addTableModelListener(e -> {
            if (isProgrammaticChange) {
                return; // Ignorar cambios programáticos
            }
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 3) { // Columna "Titular"
                Boolean isChecked = (Boolean) tableModel.getValueAt(row, column);

                try {
                    // Obtener datos del equipo seleccionado
                    Equip eq = new Equip(
                        Integer.parseInt((String) comboFiltre.getSelectedItem()),
                        (String) tableModel.getValueAt(row, 0));
                    

                    Equip equip = gDB.cercaEquipNom(eq);

                    
                    if (!this.jugador.getCategoriaJugador().equals(equip.getCategoria())) {
                        JOptionPane.showMessageDialog(this,
                            "Error: El jugador no pot ser titular d'un equip de diferent categoria.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                        isProgrammaticChange = true; 
                        tableModel.setValueAt(false, row, column);
                        isProgrammaticChange = false; 
                        return;
                    }

                    // Verificar si ya es titular en otro equipo
                    Equip titularActual = gDB.esTitular(jugador, equip.getTemporada());
                    if(titularActual==null){
                        gDB.insertarTitularitat(jugador.getId(), equip.getId());
                    }else{
                        gDB.TreureTitularitat(jugador.getId(), titularActual.getId());
                        gDB.insertarTitularitat(jugador.getId(), equip.getId());
                    }
                    
                    gDB.confirmarCanvis();
                    cargarDadesTaula((String) comboFiltre.getSelectedItem());
                    
                    
                } catch (ExceptionClub | ExceptionClubDB ex) {
                    tableModel.setValueAt(false, row, column); 
                    JOptionPane.showMessageDialog(this,
                        "Error: " + ex.getMessage(),
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
            boolean esTitular = equip.equals(gDB.esTitular(jugador, temporadaSeleccionada)); 


            String equipName = equip.getNom();
            String equipSexe ="";
            String categoria = equip.getCategoria().name();
            switch(equip.getTipus()){
                case 'H':equipSexe="Masculi";break;
                case 'D':equipSexe="Femeni";break;
                case 'M':equipSexe="Mixta";break;
            }
   
            Object[] fila = new Object[4];
            fila[0] = equipName;   
            fila[1] = equipSexe;        
            fila[2] = categoria;    
            fila[3] = esTitular;  

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
            checkbox.setEnabled(false); 
            return checkbox;
        }
    }

    class CustomCheckboxEditor extends AbstractCellEditor implements TableCellEditor {
        private final JCheckBox checkbox;

        public CustomCheckboxEditor() {
            checkbox = new JCheckBox();
            checkbox.setHorizontalAlignment(SwingConstants.CENTER);


            checkbox.addActionListener(e -> {
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            checkbox.setSelected((Boolean) value);

            boolean isTitular = (Boolean) value;
            checkbox.setEnabled(!isTitular); 
            return checkbox;
        }

        @Override
        public Object getCellEditorValue() {
            return checkbox.isSelected();
        }
    }


    
}
