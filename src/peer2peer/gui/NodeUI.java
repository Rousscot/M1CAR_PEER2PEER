/**
 * Created by sylvie on 02/06/16.
 */
package peer2peer.gui;

import peer2peer.model.Peer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NodeUI extends JFrame {

    protected JList lnode, lfile ;
    protected JButton brefresh, bdownload ;
    protected JLabel message;
    protected Peer node;

    public NodeUI (Peer node) throws RemoteException {
        super (node.getPeerName()) ;
        this.node = node;
        this.init () ;
        this.pack () ;
        this.setVisible (true) ;
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE) ;
    }

    public void init () {
        this.message = new JLabel();
        this.getContentPane().add(message, BorderLayout.NORTH);
		/* list of peers */
        this.lnode = new JList () ;
        javax.swing.JScrollPane scrollPaneln = new javax.swing.JScrollPane(lnode);
        scrollPaneln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.lnode.addListSelectionListener(new FilesListener());

		/* list of file for a selected node */
        this.lfile = new JList () ;
        javax.swing.JScrollPane scrollPanelf = new javax.swing.JScrollPane(lfile);
        scrollPanelf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT,scrollPaneln,scrollPanelf);
        this.getContentPane () .add (splitPane, BorderLayout.CENTER) ;

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));

		/* button for refreshing node list */
        this.brefresh = new JButton ("Nodes") ;
        this.brefresh.addActionListener (new RefreshListener ()) ;
        p.add (this.brefresh) ;

		/* button for download file */
        this.bdownload = new JButton ("Download") ;
        this.bdownload.addActionListener (new DownloadListener ()) ;
        p.add (this.bdownload) ;
        this.getContentPane () .add (p, BorderLayout.SOUTH) ;
    }

    //Traitement des clients déconnectés
    public void traitement(String nname){
        try {
            //Suppression du client de la liste des connectés
            node.unregister(nname);
            //Rafraichissement de la liste noeuds
            lnode.setListData(node.getPeers().keySet().toArray(new String[0]));
            //vide la liste des fichiers
            lfile.setListData(new String[0]);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } finally {
            JOptionPane.showMessageDialog(null,
                    "Pb lors du listing de fichiers, le client est déconnecté",
                    "Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    //Sécurité - Gestion de l'inexistance du noeud root
    public void verificationExistanceRoot(){
        try {
            node.rootIsConnected();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "La communauté n'a plus de noeud Root, le programme va s'arrêter",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    class RefreshListener implements ActionListener {
        public void actionPerformed (ActionEvent evt) {
            try {
                verificationExistanceRoot();
                lnode.setListData(node.getPeers().keySet().toArray(new String[0]));
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null,
                        "Pb lors du rafraichissement des noeuds",
                        "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    class FilesListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent evt) {
            verificationExistanceRoot();
            getFilesByNode();
        }

        public void getFilesByNode(){
            lfile.setListData(new String[0]);
            String nname = (String)lnode.getSelectedValue() ;
            String[] localFiles = new String[0];
            if (nname != null) {
                try {
                    localFiles = node.getPeers().get(nname).getLocalFiles();
                } catch (Exception e) {
                    traitement(nname);
                }
                lfile.setListData(localFiles);
            }
        }
    }

    class DownloadListener implements ActionListener {
        public void actionPerformed (ActionEvent evt) {
            verificationExistanceRoot();
            String nname = (String) lnode.getSelectedValue () ;
            String fname = (String) lfile.getSelectedValue () ;
            try {
                if (nname != null && fname != null) {
                    node.download(fname, nname);
                    JOptionPane.showMessageDialog(null,"Le fichier a été téléchargé");
                }
                else {
                    JOptionPane.showMessageDialog(null,"Veuillez choisir un noeud et un fichier");
                }
            } catch (Exception e) {
                e.printStackTrace();
                traitement(nname);
            }
        }
    }

}