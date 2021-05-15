package project;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Clients extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private PreparedStatement statement = null;

    private JLabel labelCl = new JLabel("ID du client : ");

    private JTextArea infos = new JTextArea("Pour modifier ou supprimer un client, veuillez entrer \nl'id du client dans le champ \"Id du client\".");
    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    private JTextField textFieldId = new JTextField();

    private JButton btnAdd = new JButton("Modifier");
    private JButton buttonRemove = new JButton("Supprimer");
    private JButton btnRetour = new JButton("Retour");

    private DefaultTableModel model = new DefaultTableModel();
    private JTable tb = new JTable();

    public Clients(String prenom){
        super("Client Manager");
        this.setSize(1200,800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Object[] entetes = {"ID Client", "Pseudo", "Prénom", "Nom", "Bio", "Rang", "Email", "Tel", "Date de Naissance"};
        model.setColumnIdentifiers(entetes);
        //tb.setDefaultEditor(Object.class, null);
        tb.setModel(model);
        JScrollPane pane = new JScrollPane(tb);
        infos.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);
        pane.setBounds(25,25,1135,550);
        infos.setBounds(25, 600, 338, 120);
        labelCl.setBounds(440, 600, 115, 34);
        textFieldId.setBounds(525, 600, 175, 34);
        btnAdd.setBounds(425, 700, 175, 34);
        buttonRemove.setBounds(600,700,175,34);
        btnRetour.setBounds(1050,700,75,34);
        this.add(pane);
        this.add(infos);
        this.add(labelCl);
        this.add(textFieldId);
        this.add(btnRetour);
        this.add(btnAdd);
        this.add(buttonRemove);
        btnRetour.addActionListener(this);
        btnAdd.addActionListener(this);
        buttonRemove.addActionListener(this);
        String request = "SELECT * FROM users WHERE rank = 'acheteur' OR rank = 'vendeur'";
        connectionBase(request);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnRetour) {
            dispose();
        }

        if(e.getSource() == btnAdd) {
            int idCl = Integer.parseInt(textFieldId.getText());

            String request = "SELECT nom_client FROM client WHERE " + idCl + " = id_client;";
            //modifieBase(request);
        }
        if(e.getSource() == buttonRemove) {
            String idClient = textFieldId.getText();

            String request = "DELETE * FROM users WHERE id = " + "\'" + idClient + "\'";
            supprBase(request);
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
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/projetufb2", "root", "");

            if (index == 0) {
                statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM users WHERE rank = 'acheteur' OR rank = 'vendeur' ORDER BY 'rank'");
                result = statement.executeQuery();
                Object[] row = new Object[9];
                model.setRowCount(0);
                while (result.next()) {
                    row[0] = result.getInt("id");
                    row[1] = result.getString("name");
                    row[2] = result.getString("firstname");
                    row[3] = result.getString("lastname");
                    row[4] = result.getString("bio");
                    row[5] = result.getString("rank");
                    row[6] = result.getString("email");
                    row[7] = result.getString("tel");
                    row[8] = result.getDate("date_naissance");
                    model.addRow(row);
                }
            }
            else {
                statement.executeUpdate();
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

    private ResultSet modifieBase(String request, String requestp) {
        ResultSet result2 = null;
        ResultSet result3 = null;
        ResultSet result4 = null;
        String nomClient;
        Boolean retur;
        int numChbr;

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/message", "root", "");
            statement = (PreparedStatement) connexion.prepareStatement(request);
            result3 = statement.executeQuery();
            result3.next();
            nomClient = "\"" + result3.getString(1) + "\"";
            System.out.println(nomClient);

            statement = (PreparedStatement) connexion.prepareStatement(requestp);
            result2 = statement.executeQuery();
            result2.next();
            numChbr = result2.getInt(1);
            System.out.println(numChbr);

            statement = (PreparedStatement) connexion.prepareStatement("UPDATE `chambres` SET `nom_r_client`=" + nomClient + " WHERE " + numChbr + " = num_chbr");
            statement.executeUpdate();

            statement = (PreparedStatement) connexion.prepareStatement("SELECT IF(nom_r_client IS NULL, 0, 1) FROM chambres" + " WHERE " + numChbr + " = num_chbr");
            result4 = statement.executeQuery();
            result4.next();
            retur = result4.getBoolean(1);
            System.out.println(retur);

            statement = (PreparedStatement) connexion.prepareStatement("UPDATE chambres SET reserv = " + retur + " WHERE " + numChbr + " = num_chbr");
            statement.executeUpdate();

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM `chambres` ORDER BY num_chbr ASC");
            result = statement.executeQuery();

            Object[] row = new Object[6];
            model.setRowCount(0);
            while ( result.next() ) {
                row[0] = result.getInt( "num_chbr" );
                row[1] = result.getString( "type_chbr" );
                row[2] = result.getString( "nom_chbr" );
                row[3] = result.getInt("nb_pieces");
                row[4] = result.getBoolean( "reserv" );
                row[5] = result.getString( "nom_r_client" );
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

    private ResultSet supprBase(String requestp) {
        ResultSet result2 = null;
        ResultSet result4 = null;
        Boolean retur;
        int numChbr;

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/message", "root", "");

            statement = (PreparedStatement) connexion.prepareStatement(requestp);
            result2 = statement.executeQuery();
            result2.next();
            numChbr = result2.getInt(1);
            System.out.println(numChbr);

            statement = (PreparedStatement) connexion.prepareStatement("UPDATE `chambres` SET `nom_r_client`= null WHERE " + numChbr + " = num_chbr");
            statement.executeUpdate();

            statement = (PreparedStatement) connexion.prepareStatement("SELECT IF(nom_r_client IS NULL, 0, 1) FROM chambres" + " WHERE " + numChbr + " = num_chbr");
            result4 = statement.executeQuery();
            result4.next();
            retur = result4.getBoolean(1);
            System.out.println(retur);

            statement = (PreparedStatement) connexion.prepareStatement("UPDATE chambres SET reserv = " + retur + " WHERE " + numChbr + " = num_chbr");
            statement.executeUpdate();

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM `chambres` ORDER BY num_chbr ASC");
            result = statement.executeQuery();

            Object[] row = new Object[6];
            model.setRowCount(0);
            while ( result.next() ) {
                row[0] = result.getInt( "num_chbr" );
                row[1] = result.getString( "type_chbr" );
                row[2] = result.getString( "nom_chbr" );
                row[3] = result.getInt("nb_pieces");
                row[4] = result.getBoolean( "reserv" );
                row[5] = result.getString( "nom_r_client" );
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