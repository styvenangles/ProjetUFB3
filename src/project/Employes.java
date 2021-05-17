package project;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Employes extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private PreparedStatement statement = null;

    private String caracters = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN0123456789!?";
    private Random r = new Random();

    private JLabel labelId = new JLabel("Adresse : ");
    private JLabel labelNom = new JLabel("Nom : ");
    private JLabel labelPrenom = new JLabel("Prenom : ");
    private JLabel labelMail = new JLabel("Mail : ");
    private JLabel labelTel = new JLabel("Tel. : ");

    private JTextField textFieldNewSupprId = new JTextField();
    private JTextField textFieldNewNom = new JTextField();
    private JTextField textFieldNewPrenom = new JTextField();
    private JTextField textFieldNewMail = new JTextField();
    private JTextField textFieldNewTel = new JTextField();

    private JTextArea infos = new JTextArea("Lors de l'ajout d'un employé, le login et le mot de \npasse sont automatiquement générés et devront être \ndonnés par un administrateur à l'employé concerné.");
    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    private JButton btnAdd = new JButton("Ajouter");
    private JButton buttonRemove = new JButton("Supprimer");
    private JButton btnModif = new JButton("Modifier");
    private JButton btnRetour = new JButton("Retour");

    private DefaultTableModel model = new DefaultTableModel();
    private JTable tb = new JTable();

    public Employes(){
        super("Gestion des employés");
        this.setSize(1200,800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Object[] entetes = {"ID", "Nom", "Prénom", "Mail", "Tel", "Adresse", "Login", "Mot de passe", "Rang"};
        model.setColumnIdentifiers(entetes);
        //tb.setDefaultEditor(Object.class, null);
        tb.setModel(model);
        JScrollPane pane = new JScrollPane(tb);
        infos.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);
        pane.setBounds(25,25,1150,350);
        infos.setBounds(25, 600, 338, 120);
        btnAdd.setBounds(520, 700, 150, 34);
        buttonRemove.setBounds(1025,380,150,34);
        btnModif.setBounds(25,380,150,34);
        labelNom.setBounds(420,501,200,34);
        labelPrenom.setBounds(420,540,200,34);
        labelMail.setBounds(420,579,200,34);
        labelTel.setBounds(420,618,200,34);
        labelId.setBounds(420,657,200,34);
        textFieldNewNom.setBounds(495,501,200,34);
        textFieldNewPrenom.setBounds(495,540,200,34);
        textFieldNewMail.setBounds(495,579,200,34);
        textFieldNewTel.setBounds(495,618,200,34);
        textFieldNewSupprId.setBounds(495,657,200,34);
        btnRetour.setBounds(1050,700,75,34);
        buttonRemove.setBackground(Color.RED);
        this.add(infos);
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
        this.add(buttonRemove);
        this.add(btnModif);
        this.add(pane);
        this.add(infos);
        this.add(btnRetour);
        this.add(btnAdd);
        btnRetour.addActionListener(this);
        btnAdd.addActionListener(this);
        buttonRemove.addActionListener(this);
        btnModif.addActionListener(this);
        String request = "SELECT * FROM employes";
        connectionBase(request);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btnRetour) {
            this.dispose();
        }

        if(e.getSource() == btnAdd) {
            String nomUser = "\"" + textFieldNewNom.getText() + "\"";
            String prenomUser = "\"" + textFieldNewPrenom.getText() + "\"";
            String mailUser = "\"" + textFieldNewMail.getText() + "\"";
            String telUser = "\"" + textFieldNewTel.getText() + "\"";
            String addressUser = "\"" + textFieldNewSupprId.getText() + "\"";
            if(nomUser.isEmpty() || prenomUser.isEmpty() || mailUser.isEmpty() || telUser.isEmpty() || addressUser.isEmpty()){
                JOptionPane.showMessageDialog (null, "Veillez à remplir tous les champs", "Champ vide", JOptionPane.ERROR_MESSAGE);
            } else  {
                ajoutBase(nomUser, prenomUser, mailUser, telUser, addressUser);
                connectionBase("Nul");
                textFieldNewSupprId.setText("");
                textFieldNewTel.setText("");
                textFieldNewMail.setText("");
                textFieldNewPrenom.setText("");
                textFieldNewNom.setText("");
            }
        }

        if(e.getSource() == buttonRemove) {
            try {
                int clmn = tb.getSelectedRow();
                Object idClmn = tb.getValueAt(clmn, 0);
                    String request = "DELETE FROM `employes` WHERE ID_Employe = " + idClmn + ";";
                    supprBase(request);
                    connectionBase("Nul");
            }
            catch (Exception h) {
                JOptionPane.showMessageDialog (null, "Pour supprimer un employé, veuillez le selectionné dans le tableau.", "Aucun employé selectionné", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(e.getSource() == btnModif) {
            int lastClmn = tb.getRowCount();
            Object[][] tableauContent =new Object[lastClmn][8];
            //System.out.println(lastClmn);
            int kRow = 0;
            while(kRow < lastClmn) {
                for(int iClmn = 0; iClmn < 8; iClmn++){
                    tableauContent[kRow][iClmn] = tb.getValueAt(kRow,iClmn);
                }
                kRow++;
            }
            modifBase(tableauContent, lastClmn);
            connectionBase("Nul");
        }
    }

    private ResultSet connectionBase(String request) {

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM employes ORDER BY 'rank'");
            result = statement.executeQuery();
            Object[] row = new Object[9];
            model.setRowCount(0);
            while ( result.next() ) {
                row[0] = result.getInt( "ID_Employe" );
                row[1] = result.getString( "Nom" );
                row[2] = result.getString( "Prenom" );
                row[3] = result.getString("Mail");
                row[4] = result.getString( "Tel" );
                row[5] = result.getString( "Adresse" );
                row[6] = result.getString( "Login" );
                row[7] = result.getString( "Psswd" );
                row[8] = result.getString( "Rank" );
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

    private ResultSet ajoutBase(String nomUser, String prenomUser, String mailUser, String telUser, String addressUser) {
        String loginUser = prenomUser.toLowerCase();
        String psswdUser = "\"";


        for(int i = 0; i < 7; i++) {
            int rand = r.nextInt((64));
            psswdUser = psswdUser + caracters.substring(rand,rand+1);
        }

        psswdUser = psswdUser + "\"";
        System.out.println(psswdUser);
        String request = "INSERT INTO `employes`(`Nom`, `Prenom`, `Mail`, `Tel`, `Adresse`, `Login`, `Psswd`) VALUES ("+ nomUser + ","+ prenomUser +","+ mailUser + "," + telUser +","+ addressUser +","+ loginUser +","+ psswdUser +");";

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");
            statement = (PreparedStatement) connexion.prepareStatement(request);
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

    private ResultSet supprBase(String requestp) {

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");
            statement = (PreparedStatement) connexion.prepareStatement(requestp);
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

    private ResultSet modifBase(Object[][] values, int firstDimension) {

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            System.out.println(e);
        }

        try {
            int kRow = 0;
            String request = "";
            while(kRow < firstDimension){
                request = "UPDATE `employes` SET `Nom`= \"";
                for(int iIndex = 1; iIndex < 8; iIndex++){
                    switch(iIndex) {
                        case 1: request = request +  (String) values[kRow][iIndex] + "\","; break;
                        case 2: request = request + " `Prenom` = \"" + (String) values[kRow][iIndex] + "\","; break;
                        case 3: request = request +  " `Mail` = \"" + (String) values[kRow][iIndex] + "\","; break;
                        case 4: request = request +  " `Tel` = \"" + (String) values[kRow][iIndex] + "\","; break;
                        case 5: request = request +  " `Adresse` = \"" + (String) values[kRow][iIndex] + "\","; break;
                        case 6: request = request +  " `Login` = \"" + (String) values[kRow][iIndex] + "\","; break;
                        case 7: request = request +  " `Psswd` = \"" + (String) values[kRow][iIndex] + "\" WHERE `ID_Employe` = " + values[kRow][0] + ";"; break;
                    }
                }
                connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");
                System.out.println(request);
                statement = (PreparedStatement) connexion.prepareStatement(request);
                statement.executeUpdate();
                kRow++;
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