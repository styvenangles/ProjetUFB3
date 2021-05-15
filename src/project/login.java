package project;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class login extends JFrame implements ActionListener {

    private java.sql.Connection connexion = null;
    private ResultSet result = null;
    private ResultSet result2 = null;
    private ResultSet result3 = null;
    private PreparedStatement statement = null;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private Date date = new Date();

    //Déclaration des variables globales
    private JButton connexionBtn = new JButton("Connexion");
    private JButton closeBtn = new JButton("Quitter");

    private JTextArea infos = new JTextArea("Pour aller plus loin, veuillez-vous connecter.");
    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);


    private JLabel labelPseudo = new JLabel("Login : ");
    private JLabel labelPsswd = new JLabel("Mdp : ");

    private JTextField textFieldPseudo = new JTextField();
    private JPasswordField passwordField = new JPasswordField();

    private JTable tb = new JTable();

    public static void main(String[] args) {
        //Création de l'interface graphique
        login menu = new login();
        menu.setVisible(true);
    }

    //Interface graphique
    public login()  {
        //Paramètres généraux de l'interface
        super("Accueil");
        this.setSize(800,300);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        System.out.println(date.getTime());
        System.out.println(dateFormat.format(date));

        JScrollPane pane = new JScrollPane(tb);
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);

        //Création du texte du menu home
        JLabel textHome = new JLabel("BioCoop Planning");
        Font policeNormale = new Font("Serif", Font.PLAIN, 25);
        textHome.setFont(policeNormale);

        // Positionnement des objets dans l'interface
            //Labels d'acceuil
        textHome.setBounds(307,15,186,30);
        infos.setBounds(270, 65, 260, 30);
            //Labels des champs
        labelPseudo.setBounds(150,100,200,30);
        labelPsswd.setBounds(450,100,200,30);
            //Champs
        textFieldPseudo.setBounds(150,140,200,30);
        passwordField.setBounds(450,140,200,30);
            //Btns
        connexionBtn.setBounds(225,200,150,30);
        closeBtn.setBounds(425,200,150,30);

        //Ajout des objets dans l'interface
        this.add(textHome);
        this.add(infos);
        this.add(connexionBtn);
        this.add(closeBtn);
        this.add(textFieldPseudo);
        this.add(passwordField);
        this.add(labelPseudo);
        this.add(labelPsswd);

        //Ajout d'event sur les boutons
        connexionBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        //Apparition de l'interface avec ses paramètres
        this.setVisible(true);
    }

    //Events liés aux boutons
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == connexionBtn){
            //System.out.println("Connexion");
            String stringPseudo = "\"" + textFieldPseudo.getText() + "\"";
            //System.out.println(stringPseudo);
            String stringPsswd = passwordField.getText();
            String request = "SELECT psswd FROM employes WHERE login = " + stringPseudo;
            connection(request, stringPsswd, stringPseudo);
        }

        else if(e.getSource() == closeBtn) {
            //System.out.println("Quitter");
            dispose();
        }
    }
    private ResultSet connection(String request, String stringPsswd, String stringPseudo) {
        //System.out.println(request);
        int index = request.indexOf("SELECT");
        //System.out.println(index);

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        }

        catch ( ClassNotFoundException e ) {
            //System.out.println(e);
        }

        try {
            connexion = DriverManager.getConnection( "jdbc:mysql://localhost:3306/planning", "root", "");
            //System.out.println("Connection réussi");
            statement = (PreparedStatement) connexion.prepareStatement(request);
            if (index == 0){
                result = statement.executeQuery();
                //System.out.println(result);
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
                                        //System.out.println(prenomShow);
                                        TableauAgent menu2 = new TableauAgent(prenomShow);
                                        menu2.setVisible(true);
                                    }
                                    else if (rankShow.equals("admin")) {
                                        System.out.println("Connecté en tant qu'administrateur");
                                        //System.out.println(prenomShow);
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
                        //System.out.println(psswdShow);
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
            //System.out.println(e);
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
