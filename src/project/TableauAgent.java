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

public class TableauAgent extends JFrame implements ActionListener {

    //Déclaration des variables globales
    private JLabel id = new JLabel("");

    private JButton infoBtn = new JButton("Informations");
    private JButton planningBtn = new JButton("Plannings");
    private JButton createUserBtn = new JButton("Nouvel employé");
    private JButton createPlanningBtn = new JButton("Nouveaux Plannings");

    private Font policeGrasItalique = new Font("Serif", Font.BOLD | Font.ITALIC, 14);
    private Border border = BorderFactory.createLineBorder(Color.BLACK);

    private DefaultTableModel model = new DefaultTableModel();
    private JTable tb = new JTable();

    public static void main(String[] args) {
        //Création de l'interface graphique
        login menu = new login();
        menu.setVisible(true);
    }

    //Interface graphique
    public TableauAgent(String prenom, String rankUser)  {
        //Paramètres généraux de l'interface
        super("Tableau de bord Employé");
        System.out.println(rankUser);
        this.setSize(800,285);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        JScrollPane pane = new JScrollPane(tb);
        JTextArea infos = new JTextArea("Voici les fonctionnalités qui vous sont disponibles ("+rankUser+").");
        infos.setBackground(pane.getBackground());
        infos.setFont(policeGrasItalique);
        infos.setEditable(false);

        //Création du texte du menu home
        JLabel textHome = new JLabel("Bienvenue");
        Font policeNormale = new Font("Serif", Font.PLAIN, 25);
        textHome.setFont(policeNormale);
        id.setText(prenom);
        // Positionnement des objets dans l'interface
        textHome.setBounds(345,25,400,34);
        infos.setBounds(235, 100, 475, 50);

        if (rankUser.equals("user")){
            infoBtn.setBounds(210,200,175,34);
            planningBtn.setBounds(415,200,175,34);
        } else if (rankUser.equals("admin")) {
            infoBtn.setBounds(40,200,150,34);
            planningBtn.setBounds(230,200,150,34);
            createUserBtn.setBounds(420,200,150,34);
            createPlanningBtn.setBounds(610,200,150,34);
            this.add(createUserBtn);
            this.add(createPlanningBtn);
        }

        //Ajout des objets dans l'interface
        this.add(textHome);
        this.add(infos);
        this.add(infoBtn);
        this.add(planningBtn);

        //Ajout d'event sur les boutons
        infoBtn.addActionListener(this);
        planningBtn.addActionListener(this);
        createUserBtn.addActionListener(this);
        createPlanningBtn.addActionListener(this);

        //Apparition de l'interface avec ses paramètres
        this.setVisible(true);
    }

    //Events liés aux boutons
    @Override
    public void actionPerformed(ActionEvent d) {

        if(d.getSource() == infoBtn) {
            System.out.println("Informations");
            String nomUser = id.getText();
            Employes menu4 = new Employes();
            menu4.setVisible(true);
        }

        else if(d.getSource() == planningBtn) {
            System.out.println("Plannings");
            String nomUser = id.getText();
            Planning menu2 = new Planning(nomUser);
            menu2.setVisible(true);
        }
        else if(d.getSource() == createUserBtn) {
            System.out.println("NewUser");
            Employes menu2 = new Employes();
            menu2.setVisible(true);
        }
    }


}
