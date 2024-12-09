/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.VistaClub.ExportacionsReport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.milaifontanals.CapaModel_P1.Categoria;
import org.milaifontanals.CapaModel_P1.*;
import org.milaifontanals.InterficiePersistencia_P1.ExceptionClubDB;
import org.milaifontanals.InterficiePersistencia_P1.IGestorDB;

/**
 *
 * @author kevin
 */
public class V_ExportacionsReport extends JFrame implements ActionListener{
    private IGestorDB gDB=null;
    private JComboBox comboTemporada;
    private JComboBox<Categoria> comboCategoria;
    private JComboBox<String> comboEquips;
    
    public V_ExportacionsReport(IGestorDB gDB) throws ExceptionClubDB{
        
        this.gDB=gDB;
        setTitle("Exportacios/Reports");
        setSize(900, 600);
        int x = (int) ((this.getToolkit().getScreenSize().getWidth() - this.getWidth()) / 2);
        int y = (int) ((this.getToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        JLabel lblTemporada = new JLabel("Temporada:");
        lblTemporada.setBounds(50, 20, 100, 25);
        this.add(lblTemporada);
        List<Integer> llistatTemporades = gDB.LlistatTemporades();
        comboTemporada = new JComboBox<>();
        comboTemporada.addItem(null); // Agrega una opción vacía
        for (Integer temporada : llistatTemporades) {
            comboTemporada.addItem(String.valueOf(temporada));
        }
        comboTemporada.setBounds(30, 50, 100, 25);
        comboTemporada.addActionListener(this);
        this.add(comboTemporada);

        // Combo Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setBounds(50, 90, 100, 25);
        this.add(lblCategoria);

        comboCategoria = new JComboBox<>();
        comboCategoria.addItem(null);
        for (Categoria categoria : Categoria.values()) {
            comboCategoria.addItem(categoria);
        }
        comboCategoria.setBounds(30, 120, 100, 25);
        comboCategoria.addActionListener(this);
        this.add(comboCategoria);

     
        JLabel lblEquips = new JLabel("Equip:");
        lblEquips.setBounds(50, 160, 100, 25);
        this.add(lblEquips);

        comboEquips = new JComboBox<>();
        comboEquips.addItem(null); 
        List<Equip> equips = gDB.LlistatEquips();
        for (Equip equip : equips) {
            comboEquips.addItem(equip.getNom());
        }
        comboEquips.setBounds(30, 190, 100, 25);
        comboEquips.addActionListener(this);
        this.add(comboEquips);
   
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == comboTemporada || e.getSource() == comboCategoria) {
            try {
                actualitzarComboEquips();
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_ExportacionsReport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void actualitzarComboEquips() throws ExceptionClubDB {
        comboEquips.removeAllItems();
        comboEquips.addItem(null); // Opción vacía por defecto

        // Obtener la selección de temporada
        String temporadaSeleccionadaStr = (String) comboTemporada.getSelectedItem();
        final Integer temporadaSeleccionada;

        if (temporadaSeleccionadaStr != null && !temporadaSeleccionadaStr.isEmpty()) {
            try {
                temporadaSeleccionada = Integer.parseInt(temporadaSeleccionadaStr);
            } catch (NumberFormatException e) {
                System.err.println("Error: Temporada seleccionada no es un número válido: " + temporadaSeleccionadaStr);
                return; // Salimos para evitar errores
            }
        } else {
            temporadaSeleccionada = null; // Si no hay temporada seleccionada
        }

        // Obtener la selección de categoría
        Categoria categoriaSeleccionada = (Categoria) comboCategoria.getSelectedItem();

        // Filtrar equipos
        List<Equip> equiposFiltrados = gDB.LlistatEquips().stream()
            .filter(equip -> 
                (temporadaSeleccionada == null || equip.getTemporada() == temporadaSeleccionada) &&
                (categoriaSeleccionada == null || equip.getCategoria().equals(categoriaSeleccionada))
            )
            .toList();

        for (Equip equip : equiposFiltrados) {
            comboEquips.addItem(equip.getNom());
        }
    }

}
