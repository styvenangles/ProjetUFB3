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

    private JButton clientBtn = new JButton("Informations  ");
    private JButton bienBtn = new JButton("Plannings");

    private JTextArea infos = new JTextArea("Voici les fonctionnalités qui vous sont disponibles.");
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
    public TableauAgent(String prenom)  {
        //Paramètres généraux de l'interface
        super("Tableau de bord Employé");
        this.setSize(800,285);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);

        JScrollPane pane = new JScrollPane(tb);
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
        infos.setBounds(250, 100, 475, 50);
        clientBtn.setBounds(30,200,175,34);
        clientBtn.setBounds(210,200,175,34);
        bienBtn.setBounds(415,200,175,34);

        //Ajout des objets dans l'interface
        this.add(textHome);
        this.add(infos);
        this.add(clientBtn);
        this.add(bienBtn);

        //Ajout d'event sur les boutons
        clientBtn.addActionListener(this);
        bienBtn.addActionListener(this);

        //Apparition de l'interface avec ses paramètres
        this.setVisible(true);
    }

    //Events liés aux boutons
    @Override
    public void actionPerformed(ActionEvent d) {

        if(d.getSource() == clientBtn){
            System.out.println("Informations");
            String nomUser = id.getText();
            Clients menu4 = new Clients(nomUser);
            menu4.setVisible(true);
        }

        else if(d.getSource() == bienBtn) {
            System.out.println("Plannings");
            String nomUser = id.getText();
            Planning menu2 = new Planning(nomUser);
            menu2.setVisible(true);
        }
    }


}
