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

public class TableauAdmin extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private ResultSet result2 = null;
    private ResultSet result3 = null;
    private PreparedStatement statement = null;

    //Déclaration des variables globales
    private JButton agentBtn = new JButton("Employés");
    private JButton agenceBtn = new JButton("Plannings");

    private JTextArea infos = new JTextArea("Connecté en tant qu'administrateur");
    private JTextArea perso = new JTextArea("Nom:\n\nPrenom:\n\nMail:\n\nTel:\n\nAdresse:\n\nRank:");
    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    private JTable tb = new JTable();

    public static void main(String[] args) {
        //Création de l'interface graphique
        login menu = new login();
        menu.setVisible(true);
    }

    //Interface graphique
    public TableauAdmin(String prenom)  {
        //Paramètres généraux de l'interface
        super("Tableau de bord Admin");
        this.setSize(900,600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        JScrollPane pane = new JScrollPane(tb);
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);

        //Création du texte du menu home
        JTextArea textHome = new JTextArea("Bienvenue " + prenom);
        Font policeNormale = new Font("Serif", Font.PLAIN, 20);
        textHome.setFont(policeNormale);
        textHome.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        textHome.setEditable(false);
        perso.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        perso.setEditable(false);

        // Positionnement des objets dans l'interface
        textHome.setBounds(-1,-1,900,34);
        perso.setBounds(15, 45, 435, 500);
        infos.setBounds(670, 8, 475, 34);
        agentBtn.setBounds(30,200,175,34);
        agentBtn.setBounds(210,200,175,34);
        agenceBtn.setBounds(415,200,175,34);

        //Ajout des objets dans l'interface
        this.add(textHome);
        this.add(perso);
        this.add(infos);
        this.add(agentBtn);
        this.add(agenceBtn);

        //Ajout d'event sur les boutons
        agentBtn.addActionListener(this);
        agenceBtn.addActionListener(this);

        //Apparition de l'interface avec ses paramètres
        this.setVisible(true);
    }

    //Events liés aux boutons
    @Override
    public void actionPerformed(ActionEvent d) {

        if(d.getSource() == agentBtn){
            System.out.println("Employés");
            Employes menu4 = new Employes();
            menu4.setVisible(true);
            this.dispose();
        }

        else if(d.getSource() == agenceBtn) {
            System.out.println("Plannings");
            Planning menu2 = new Planning("Nul");
            menu2.setVisible(true);
            this.dispose();
        }
    }

    private ResultSet connection(String request, String stringPsswd, String stringPseudo) {
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
                            String request2 = "SELECT rank FROM employes WHERE login = " + stringPseudo;
                            statement = (PreparedStatement) connexion.prepareStatement(request2);
                            result2 = statement.executeQuery();
                            while (result2.next()){
                                String rankShow = result2.getString(1);
                                String request3 = "SELECT Prenom FROM employes WHERE login =" + stringPseudo;
                                statement = (PreparedStatement) connexion.prepareStatement(request3);
                                result3 = statement.executeQuery();
                                while (result3.next()){
                                    String prenomShow = result3.getString(1);
                                    if (rankShow.equals("user")){
                                        dispose();
                                        System.out.println("Connecté en tant qu'employé");
                                        System.out.println(prenomShow);
                                        TableauAgent menu2 = new TableauAgent("Nul", "Nul");
                                        menu2.setVisible(true);
                                    }
                                    else if (rankShow.equals("admin")) {
                                        System.out.println("Connecté en tant qu'administrateur");
                                        System.out.println(prenomShow);
                                        TableauAdmin menu2 = new TableauAdmin(prenomShow);
                                        menu2.setVisible(true);
                                        dispose();
                                    }
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
                    JOptionPane.showMessageDialog(null, "Login non répértorié", "Erreur", JOptionPane.ERROR_MESSAGE);
                }

            }
            else {
                statement.executeUpdate();
            }

            statement = (PreparedStatement) connexion.prepareStatement("SELECT * FROM `employes`");
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
