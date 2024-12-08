/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.milaifontanals.VistaClub;

import org.milaifontanals.VistaClub.GestioJugador.V_GestioJugador;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.milaifontanals.InterficiePersistencia_P1.*;
import org.milaifontanals.VistaClub.ExportacionsReport.V_ExportacionsReport;
import org.milaifontanals.VistaClub.GestioEquips.V_GestioEquips;


/**
 *
 * @author kevin
 */
public class V_MenuPrincipal extends JFrame implements ActionListener {
    private JButton btnGestioJugadors, btnGestioEquips, btnExportarDades, btnLogout;
    private JLabel lblTitle, lblCalendar,lblCalendartxt;
    private JPanel pnlMain;
    private IGestorDB gDB=null;
     // Constructor
    public V_MenuPrincipal(IGestorDB gDB) {
     
        this.gDB=gDB;
        setSize(900, 600);
        int x = (int) ((this.getToolkit().getScreenSize().getWidth() - this.getWidth()) / 2);
        int y = (int) ((this.getToolkit().getScreenSize().getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(null);
        setTitle("Club Kevin - Menú Principal");
        getContentPane().setBackground(Color.WHITE);
       
        lblTitle = new JLabel("⚽ Club Kevin ⚽");
        lblTitle.setBounds(300, 30, 300, 50);
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));
        add(lblTitle);

        // Botón Gestión de Jugadores
        btnGestioJugadors = new JButton("Gestió de Jugadors");
        btnGestioJugadors.setBounds(50, 100, 200, 50);
        btnGestioJugadors.addActionListener(this);
        add(btnGestioJugadors);

        // Botón Gestión de Equipos
        btnGestioEquips = new JButton("Gestió d' Equips");
        btnGestioEquips.setBounds(300, 100, 200, 50);
        btnGestioEquips.addActionListener(this);
        add(btnGestioEquips);

        // Botón Exportar Datos
        btnExportarDades = new JButton("Exportar dades i Reports");
        btnExportarDades.setBounds(550, 100, 200, 50);
        btnExportarDades.addActionListener(this);
        add(btnExportarDades);

        // Botón Logout
        btnLogout = new JButton("Log-out");
        btnLogout.setBounds(650, 520, 100, 30);
        btnLogout.addActionListener(this);
        add(btnLogout);

        lblCalendartxt = new JLabel("Calendari d'events");
        lblCalendartxt.setBounds(590, 230, 200, 200);
        lblCalendartxt.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        add(lblCalendartxt);
        
        ImageIcon calendarIcon = new ImageIcon("calendari.png"); // Cambia la ruta
        JLabel calendarLabel = new JLabel(calendarIcon);
        calendarLabel.setBounds(550, 300, 200, 200); // Ajusta el tamaño y posición de la imagen
        add(calendarLabel);
        
        ImageIcon adllarga = new ImageIcon("largo.png"); // Cambia la ruta
        JLabel lbladllarga = new JLabel(adllarga);
        lbladllarga.setBounds(0, 400, 600, 200); // Ajusta el tamaño y posición de la imagen
        add(lbladllarga);

       // Placeholder de imagen o contenido en el centro
        JPanel redPanel = new JPanel();
        redPanel.setBounds(300, 200, 200, 200);
        redPanel.setBackground(Color.WHITE);
        redPanel.setLayout(null);
        ImageIcon shieldIcon = new ImageIcon("escudo.png"); // Asegúrate de colocar la ruta correcta
        JLabel shieldLabel = new JLabel(shieldIcon);
        shieldLabel.setBounds(25, 25, 150, 150); 
        redPanel.add(shieldLabel);

        add(redPanel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGestioJugadors) {
            this.dispose();
            try {
                new V_GestioJugador(this.gDB).setVisible(true);
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == btnGestioEquips) {
            this.dispose();
            try {
                new V_GestioEquips(this.gDB).setVisible(true);
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == btnExportarDades) {
            this.dispose();
            try {
                new V_ExportacionsReport(this.gDB).setVisible(true);
            } catch (ExceptionClubDB ex) {
                Logger.getLogger(V_MenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }  
        } else if (e.getSource() == btnLogout) {

            dispose();
            new V_login().setVisible(true);
        }
    }
    
}
