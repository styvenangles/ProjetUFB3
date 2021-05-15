package project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private PreparedStatement statement = null;

    Pattern pattern = Pattern.compile("^[\\$#\\+{}:\\?\\.,~@\"a-zA-Z0-9 ]+$");

    private JLabel labelPseudo = new JLabel("Pseudo : ");
    private JLabel labelPsswd = new JLabel("Mot de passe : ");
    private JLabel labelNom = new JLabel("Nom : ");
    private JLabel labelPrenom = new JLabel("Prénom : ");
    private JLabel labelBio = new JLabel("Biographie : ");
    private JLabel labelEmail = new JLabel("Email : ");
    private JLabel labelTel = new JLabel("Téléphone : ");
    private JLabel labelNaissance = new JLabel("Date de naissance : ");

    private JTextField textFieldPseudo = new JTextField();
    private JPasswordField textFieldPsswd = new JPasswordField();
    private JTextField textFieldNom = new JTextField();
    private JTextField textFieldPrenom = new JTextField();
    private JTextField textFieldBio = new JTextField();
    private JTextField textFieldEmail = new JTextField();
    private JTextField textFieldTel = new JTextField();

    private JButton btnAdd = new JButton("S'inscrire");
    private JButton btnRetour = new JButton("Retour");

    private String dates[]
            = { "01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25",
            "26", "27", "28", "29", "30",
            "31" };
    private String months[]
            = { "01", "02", "03", "04",
            "05", "06", "07", "08",
            "09", "10", "11", "12" };
    private String years[]
            = { "1975", "1976", "1977", "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1989","1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998",
            "1999", "2000", "2001", "2002"};

    private JComboBox comboJours = new JComboBox(dates);
    private JComboBox comboMois = new JComboBox(months);
    private JComboBox comboAnnees = new JComboBox(years);


    private DefaultTableModel model = new DefaultTableModel();
    private JTable tb = new JTable();

    public Register(){
        super("Inscription");
        this.setSize(575,500);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        labelPseudo.setBounds(25, 25, 150, 34);
        labelPsswd.setBounds(25, 75, 115, 34);
        labelNom.setBounds(25, 125, 115, 34);
        labelPrenom.setBounds(25, 175, 150, 34);
        labelBio.setBounds(25, 225, 115, 34);
        labelEmail.setBounds(25, 275, 150, 34);
        labelTel.setBounds(25, 325, 115, 34);
        labelNaissance.setBounds(25, 375, 150, 34);
        textFieldPseudo.setBounds(150, 25, 175, 34);
        textFieldPsswd.setBounds(150, 75, 175, 34);
        textFieldNom.setBounds(150, 125, 175, 34);
        textFieldPrenom.setBounds(150, 175, 175, 34);
        textFieldBio.setBounds(150, 225, 175, 34);
        textFieldEmail.setBounds(150, 275, 175, 34);
        textFieldTel.setBounds(150, 325, 175, 34);
        comboJours.setBounds(150, 375, 40, 34);
        comboMois.setBounds(190, 375, 60, 34);
        comboAnnees.setBounds(250, 375, 80, 34);
        btnAdd.setBounds(350, 175, 150, 34);
        btnRetour.setBounds(350, 225, 150, 34);
        this.add(labelPsswd);
        this.add(labelPseudo);
        this.add(labelNom);
        this.add(labelPrenom);
        this.add(labelBio);
        this.add(labelEmail);
        this.add(labelTel);
        this.add(labelNaissance);
        this.add(textFieldPsswd);
        this.add(textFieldPseudo);
        this.add(textFieldNom);
        this.add(textFieldPrenom);
        this.add(textFieldBio);
        this.add(textFieldEmail);
        this.add(textFieldTel);
        this.add(comboJours);
        this.add(comboMois);
        this.add(comboAnnees);
        this.add(btnRetour);
        this.add(btnAdd);
        btnRetour.addActionListener(this);
        btnAdd.addActionListener(this);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnRetour) {
            dispose();
        }

        if(e.getSource() == btnAdd) {
            String Pseudo = "\'" + textFieldPseudo.getText() + "\'";
            String Psswd = "\'" + textFieldPsswd.getText() + "\'";
            String Nom = "\'" + textFieldNom.getText() + "\'";
            String Prenom = "\'" + textFieldPrenom.getText() + "\'";
            String Bio = "\'" + textFieldBio.getText() + "\'";
            String Email = "\'" + textFieldEmail.getText() + "\'";
            String Tel = "\'" + textFieldTel.getText() + "\'";
            String Jour = (String) comboJours.getSelectedItem();
            String Mois = (String) comboMois.getSelectedItem();
            String Années = (String) comboAnnees.getSelectedItem();

            Matcher emailMatch = pattern.matcher(Email);

            String dateFinale = "\'" + Années + "-" + Mois + "-" + Jour + "\'";

                System.out.println(dateFinale);
                System.out.println(emailMatch);
                modifieBase(Pseudo, Psswd, Nom, Prenom, Bio, Email, Tel, dateFinale);
                dispose();
        }
    }

    private ResultSet modifieBase(String Pseudo, String Psswd, String Nom, String Prenom, String Bio, String Email, String Tel, String dateFinale) {

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/projetufb2", "root", "");
            statement = (PreparedStatement) connexion.prepareStatement("INSERT INTO `users`(`name`, `firstname`, `lastname`, `bio`, `email`, `tel`, `date_naissance`, `password`) VALUES (" + Pseudo + "," + Prenom + "," + Nom + "," + Bio + "," + Email + "," + Tel + "," + dateFinale + "," + Psswd + ");");
            statement.executeUpdate();
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