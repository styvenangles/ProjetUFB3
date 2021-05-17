package project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Connexion extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private ResultSet result2 = null;
    private PreparedStatement statement = null;

    private JLabel labelPseudo = new JLabel("Email : ");
    private JLabel labelPsswd = new JLabel("Mot de passe : ");

    private JTextField textFieldPseudo = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    private JButton buttonConnect = new JButton("Connexion");
    private JButton buttonRetour = new JButton("Retour");

    public Connexion(){
        super("Connexion");
        this.setSize(500,225);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buttonConnect.addActionListener(this);
        buttonRetour.addActionListener(this);
        textFieldPseudo.setBounds(150,25,200,34);
        passwordField.setBounds(150,75,200,34);
        labelPseudo.setBounds(85,25,200,34);
        labelPsswd.setBounds(40,75,200,34);
        buttonConnect.setBounds(120,135,125,34);
        buttonRetour.setBounds(250,135,125,34);
        this.add(textFieldPseudo);
        this.add(passwordField);
        this.add(labelPseudo);
        this.add(labelPsswd);
        this.add(buttonConnect);
        this.add(buttonRetour);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == buttonConnect) {
            String stringEmail = "\"" + textFieldPseudo.getText() + "\"";
            String stringPsswd = passwordField.getText();
            String request = "SELECT password FROM users WHERE email = " + stringEmail ;
            connectionBase2(request, stringPsswd, stringEmail);
        }

        if(e.getSource() == buttonRetour) {
            dispose();
        }
    }

    private ResultSet connectionBase2(String request, String stringPsswd, String stringEmail) {
        System.out.println(request);
        int index = request.indexOf("SELECT");
        System.out.println(index);

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");
            System.out.println("Connection réussi");
            statement = (PreparedStatement) connexion.prepareStatement(request);
            if (index == 0){
                result = statement.executeQuery();
                System.out.println(result);
                if (result != null) {
                    while (result.next()) {
                        String psswdShow = result.getString(1);
                        if (psswdShow.equals(stringPsswd)) {
                            String request2 = "SELECT rank FROM users WHERE email = " + stringEmail;
                            statement = (PreparedStatement) connexion.prepareStatement(request2);
                            result2 = statement.executeQuery();
                            while (result2.next()){
                                String rankShow = result2.getString(1);
                                if (rankShow.equals("user")){
                                    dispose();
                                    System.out.println("Connecté en tant qu'agent immobilier");
                                    TableauAgent menu2 = new TableauAgent("Moi", "Nul");
                                    menu2.setVisible(true);
                                }
                                else if (rankShow.equals("admin")) {
                                    dispose();
                                    System.out.println("Connecté en tant qu'agent immobilier");
                                    TableauAdmin menu2 = new TableauAdmin("");
                                    menu2.setVisible(true);
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Mot de passe non valide", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                        System.out.println(psswdShow);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Email non répértorié", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            }
            else {
                statement.executeUpdate();
            }

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM `users`");
            result = statement.executeQuery();
        }

        catch ( SQLException e ) {
            System.out.println(e);
        }

        finally {
            if ( result != null ) {
                try {
                    result.close();
                } catch ( SQLException ignore ) {
                }
            }
            if ( statement != null ) {
                try {
                    statement.close();
                } catch ( SQLException ignore ) {
                }
            }
            if ( connexion != null ) {
                try {
                    connexion.close();
                } catch ( SQLException ignore ) {
                }
            }
        }
        return result;
    }
}