package org.milaifontanals.VistaClub;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.milaifontanals.CapaModel_P1.*;
import org.milaifontanals.InterficiePersistencia_P1.*;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kevin
 */
public class V_login extends JFrame implements ActionListener{
    private IGestorDB gDB=null;
    private JLabel lblUser, lblPassword, lblTitle,lblError;
    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    static private String nomClassePersistencia = null;
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Cal passar el nom de la classe que dona la persistència com a primer argument");
            System.exit(0);
        }
        nomClassePersistencia = args[0];
        V_login l=new V_login();
        l.setVisible(true);
    }

    // Constructor
    public V_login() {
         try {
            this.gDB = (IGestorDB) Class.forName(nomClassePersistencia).newInstance();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        // Configuración básica de la ventana
        setSize(600, 400); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Acción al cerrar
        setLayout(null); // Usaremos posicionamiento absoluto
        setTitle("Club Kevin - Login");
        getContentPane().setBackground(Color.WHITE);
        // Título
        lblTitle = new JLabel("⚽ Club Kevin ⚽");
        lblTitle.setBounds(180, 30, 300, 50); // Posición y tamaño
        lblTitle.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24));

        add(lblTitle);

        // Etiqueta "User"
        lblUser = new JLabel("User:");
        lblUser.setBounds(100, 120, 100, 30);
        add(lblUser);

        // Campo de texto para el usuario
        txtUser = new JTextField();
        txtUser.setBounds(200, 120, 200, 30);
        add(txtUser);

        // Etiqueta "Password"
        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 180, 100, 30);
        add(lblPassword);

        // Campo de texto para la contraseña
        txtPassword = new JPasswordField();
        txtPassword.setBounds(200, 180, 200, 30);
        add(txtPassword);

        // Botón de login
        btnLogin = new JButton("Login");
        btnLogin.setBounds(250, 250, 100, 30);
        btnLogin.addActionListener(this); // Agregar acción al botón
        add(btnLogin);
        
      
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String user = txtUser.getText().trim();
            String password = new String(txtPassword.getPassword());

            try {
                Usuari u = new Usuari(user, password.trim());
                if(gDB.login(u)){
                    this.dispose();
                    V_MenuPrincipal menu = new V_MenuPrincipal(gDB);
                    menu.setVisible(true);
                }else{
                    if (lblError == null) {
                    lblError = new JLabel("Usuari o contrasenya incorrecte.");
                    lblError.setBounds(210, 210, 250, 30);
                    lblError.setForeground(Color.RED);
                    add(lblError);
                    } else {
                        lblError.setVisible(true);
                    }
                    revalidate();
                    repaint();
                }
            } catch (ExceptionClubDB ex) {
                if (lblError == null) {
                    lblError = new JLabel("Usuari o contrasenya incorrecte.");
                    lblError.setBounds(210, 210, 250, 30);
                    lblError.setForeground(Color.RED);
                    add(lblError);
                } else {
                    lblError.setVisible(true);
                }
                revalidate();
                repaint();
            }
        }
    }

}
