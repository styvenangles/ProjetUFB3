package project;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Planning extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private PreparedStatement statement = null;

    private JTextArea infos = new JTextArea("Pour ajouter, veuillez entrer une valeur dans les cinqs\nchamps ci-contre.\n\nPour modifier, veuillez entrer une valeur dans le\nchamps \"Id du Client\" ( ← Obligatoire), et dans les\nchamps \"Mail du Client\" ou \"Tel. du client\".\n\nPour supprimer, seulement le champ \"Id du client\"\nest nécessaire.");
    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    private JLabel labelId = new JLabel("Id du Client : ");
    private JLabel labelNom = new JLabel("Nom du Client : ");
    private JLabel labelPrenom = new JLabel("Prenom du Client : ");
    private JLabel labelMail = new JLabel("Mail du Client : ");
    private JLabel labelTel = new JLabel("Tel. du Client : ");

    private JTextField textFieldNewSupprId = new JTextField();
    private JTextField textFieldNewNom = new JTextField();
    private JTextField textFieldNewPrenom = new JTextField();
    private JTextField textFieldNewMail = new JTextField();
    private JTextField textFieldNewTel = new JTextField();

    private JButton buttonAjout = new JButton("Ajouter");
    private JButton buttonRemove = new JButton("Supprimer");
    private JButton btnModif = new JButton("Modifier");
    private JButton btnRetour = new JButton("Retour");

    private DefaultTableModel model = new DefaultTableModel();
    private JTable tb = new JTable();
    private JScrollPane pane = new JScrollPane(tb);

    public Planning(String prenom){
        super("Agence Manager");
        this.setSize(1200,800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Object[] entetes = {"", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        model.setColumnIdentifiers(entetes);
        tb.setModel(model);
        pane.setBounds(25,25,1135,525);
        this.add(pane);
        infos.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);
        buttonAjout.addActionListener(this);
        buttonRemove.addActionListener(this);
        btnModif.addActionListener(this);
        btnRetour.addActionListener(this);
        infos.setBounds(25, 561, 338, 190);
        labelId.setBounds(420,561,200,34);
        labelNom.setBounds(403,600,200,34);
        labelPrenom.setBounds(385,639,200,34);
        labelMail.setBounds(406,678,200,34);
        labelTel.setBounds(408,717,200,34);
        textFieldNewSupprId.setBounds(495,561,200,34);
        textFieldNewNom.setBounds(495,600,200,34);
        textFieldNewPrenom.setBounds(495,639,200,34);
        textFieldNewMail.setBounds(495,678,200,34);
        textFieldNewTel.setBounds(495,717,200,34);
        buttonAjout.setBounds(750,578,175,34);
        buttonRemove.setBounds(750,639,175,34);
        btnModif.setBounds(750, 700, 175, 34);
        btnRetour.setBounds(1050,700,75,34);
        /*this.add(infos);
        this.add(labelId);
        this.add(labelNom);
        this.add(labelPrenom);
        this.add(labelMail);
        this.add(labelTel);
        this.add(textFieldNewSupprId);
        this.add(textFieldNewNom);
        this.add(textFieldNewMail);
        this.add(textFieldNewPrenom);
        this.add(textFieldNewTel);
        this.add(buttonAjout);
        this.add(buttonRemove);
        this.add(btnModif);*/
        this.add(btnRetour);

        String request = "SELECT ID_Employe FROM `employes` WHERE Prenom = \"" + prenom + "\";";
        connectionBase(request);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == buttonAjout) {
            int newId = Integer.parseInt(textFieldNewSupprId.getText());
            String newNom = "\"" + textFieldNewNom.getText() + "\"";
            String newPrenom = "\"" + textFieldNewPrenom.getText() + "\"";
            String newMail = "\"" + textFieldNewMail.getText() + "\"";
            String newTel = "\"" + textFieldNewTel.getText() + "\"";
            String request = "SELECT id_client FROM client";
            String request2 = "INSERT INTO client(id_client, nom_client, prenom_client, mail_client, tel_client) VALUES (" + newId + "," + newNom + "," + newPrenom + "," + newMail + "," + newTel + ")";
            int index = request2.indexOf("@");
            if (index != -1){
                ajoutBase(request, request2, newId);
            }
            else {
                JOptionPane.showMessageDialog(this, "Adresse mail non-valide");
            }

        }

        if(e.getSource() == buttonRemove) {
            int removeNom = Integer.parseInt(textFieldNewSupprId.getText());
            String request = "DELETE FROM client WHERE id_client =" + removeNom;

            connectionBase(request);
        }

        if(e.getSource() == btnModif) {
            if (!textFieldNewSupprId.getText().equals("")) {
                int idCl = Integer.parseInt(textFieldNewSupprId.getText());
                if (textFieldNewMail.getText().equals("") & textFieldNewTel.getText().equals("")) {
                    JOptionPane.showMessageDialog(this, "Aucune valeur à modifier");
                }
                else if (!textFieldNewMail.getText().equals("")) {
                    if(!textFieldNewTel.getText().equals("")){
                        String mailCl = "\"" + textFieldNewMail.getText() + "\"";
                        String telCl = "\"" + textFieldNewTel.getText() + "\"";
                        String request = "UPDATE `client` SET `mail_client`= " + mailCl + ",`tel_client`= " + telCl + " WHERE " + idCl + " = id_client";
                        System.out.println(request);
                        connectionBase(request);
                    }
                    else {
                        String mailCl = "\"" + textFieldNewMail.getText() + "\"";
                        String request = "UPDATE `client` SET `mail_client`= " + mailCl + " WHERE " + idCl + " = id_client";
                        System.out.println(request);
                        connectionBase(request);
                    }
                }
                else {
                    String telCl = "\"" + textFieldNewTel.getText() + "\"";
                    String request = "UPDATE `client` SET `tel_client`= 33" + telCl + " WHERE " + idCl + " = id_client";
                    System.out.println(request);
                    connectionBase(request);
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "Le champ \"Id du Client\" doit obligatoirement avoir une valeur.");
            }
        }

        if(e.getSource() == btnRetour) {
            dispose();
        }
    }

    private ResultSet connectionBase(String request) {
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
            Integer IdUser = 0;
            if (index == 0){
                System.out.println(request);
                result = statement.executeQuery();
                while (result.next()) {
                    IdUser = result.getInt("ID_Employe");
                }
            }
            else {
                statement.executeUpdate();
            }
            String request2 = "SELECT * FROM `dispo` WHERE ID = " + IdUser;
            statement = (PreparedStatement) connexion.prepareStatement(request2);
            result = statement.executeQuery();
            Object[] row = new Object[8];
            Object[] dispoJours = new Object[7];
            model.setRowCount(0);
            String highRequest = "";
            while ( result.next() ) {
                dispoJours[0] = result.getInt( "Lundi" );
                dispoJours[1] = result.getInt( "Mardi" );
                dispoJours[2] = result.getInt( "Mercredi" );
                dispoJours[3] = result.getInt("Jeudi");
                dispoJours[4] = result.getInt( "Vendredi" );
                dispoJours[5] = result.getInt( "Samedi" );
                dispoJours[6] = result.getInt( "Dimanche" );
                for (int i = 0; i < dispoJours.length; i ++){
                    if (dispoJours[i].equals(1)){
                        switch (i){
                            case 0:

                                int k = 0;
                                int j = 1;
                                while (k < 13) {
                                    int altK = k +7;
                                    highRequest = "SELECT `"+ altK +"H` FROM horaires";
                                    statement = (PreparedStatement) connexion.prepareStatement(highRequest);
                                    result = statement.executeQuery();
                                    while (result.next()) {
                                        row[0] = k+7+"H";
                                        row[j] = result.getInt(k+7+"H");
                                        j++;
                                    }
                                    j = 1;
                                    k++;
                                    model.addRow(row);
                                }
                            case 1:
                                highRequest = "";
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                        }
                    }
                }

                //model.addRow(row);
            }
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

    private ResultSet ajoutBase(String request, String request2, int Id) {
        ArrayList<String> listId = new ArrayList<>();
        ResultSet result2 = null;
        Object [] retur = new Object[1];
        String returId = String.valueOf(Id);
        System.out.println(request);
        System.out.println(returId);

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/message", "root", "");
            System.out.println("Connection réussi");

            statement = (PreparedStatement) connexion.prepareStatement(request);
            result2 = statement.executeQuery();

            while (result2.next()) {
                retur[0] = result2.getInt(1);
                System.out.println(Arrays.toString(retur));
                String returStr = Arrays.toString(retur);
                listId.add(returStr);
                System.out.println(listId);
            }
            int index = listId.indexOf("[" + returId + "]");
            System.out.println(index);
            if (index == -1) {
                statement = (PreparedStatement) connexion.prepareStatement(request2);
                statement.executeUpdate();
            }
            else {
                JOptionPane.showMessageDialog(this, "Cet Id est déjà utilisé.");
            }

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM `client` ORDER BY id_client ASC");
            result = statement.executeQuery();
            Object[] row = new Object[5];
            model.setRowCount(0);
            while ( result.next() ) {
                row[0] = result.getInt( "id_client" );
                row[1] = result.getString( "nom_client" );
                row[2] = result.getString( "prenom_client" );
                row[3] = result.getString("mail_client");
                row[4] = result.getInt( "tel_client" );
                model.addRow(row);
            }
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