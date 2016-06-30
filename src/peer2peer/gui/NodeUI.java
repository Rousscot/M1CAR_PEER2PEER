package peer2peer.gui;

import peer2peer.model.Peer;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class NodeUI extends JFrame {

    protected JList peers;
    protected JList files;
    protected JButton refreshPeersButton;
    protected JButton refreshFilesButton;
    protected JButton downloadButton;
    protected Peer peer;

    public NodeUI(Peer peer) throws RemoteException {
        super(peer.getPeerName());
        this.peer = peer;
        this.init();
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void init() {
        JScrollPane peersPane = this.initPeersList();
        JScrollPane filesPane = this.initFilesList();
        this.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, peersPane, filesPane), BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.initRefreshPeersButton();
        panel.add(this.refreshPeersButton);

        this.initRefreshFilesButton();
        panel.add(this.refreshFilesButton);

        this.initDownloadButton();
        panel.add(this.downloadButton);

        this.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    public JScrollPane initPeersList() {
        this.peers = new JList();
        JScrollPane pane = new JScrollPane(peers);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.peers.addListSelectionListener(evt -> {
            verificationExistanceRoot();
            files.setListData(new String[0]);
            String nname = (String) peers.getSelectedValue();
            String[] localFiles = new String[0];
            if (nname != null) {
                try {
                    localFiles = peer.getPeers().get(nname).getLocalFiles();
                } catch (Exception e) {
                    traitement(nname);
                }
                files.setListData(localFiles);
            }
        });
        return pane;
    }

    public JScrollPane initFilesList() {
        this.files = new JList();
        JScrollPane pane = new JScrollPane(files);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }

    public void initRefreshPeersButton() {
        this.refreshPeersButton = new JButton("Peers");
        this.refreshPeersButton.addActionListener(evt -> {
            try {
                verificationExistanceRoot();
                peers.setListData(peer.getPeers().keySet().toArray(new String[0]));
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null,
                        "Pb lors du rafraichissement des noeuds", /* TODO */
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void initRefreshFilesButton() {
        this.refreshFilesButton = new JButton("Files");
        this.refreshFilesButton.addActionListener(evt -> {
            try {
                files.setListData(new String[0]);
                String nname = (String) peers.getSelectedValue();
                if (nname == null) return;
                files.setListData(peer.getPeers().get(nname).getLocalFiles());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initDownloadButton() {
        this.downloadButton = new JButton("Download");
        this.downloadButton.addActionListener(evt -> {
            verificationExistanceRoot();
            String nname = (String) peers.getSelectedValue();
            String fname = (String) files.getSelectedValue();
            try {
                if (nname != null && fname != null) {
                    peer.download(fname, nname);
                    JOptionPane.showMessageDialog(null, "Le fichier a été téléchargé");
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez choisir un noeud et un fichier");
                }
            } catch (Exception e) {
                e.printStackTrace();
                traitement(nname);
            }
        });
    }

    public void traitement(String nname) {
        try {
            peer.unregister(nname);
            peers.setListData(peer.getPeers().keySet().toArray(new String[0]));
            files.setListData(new String[0]);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } finally {
            JOptionPane.showMessageDialog(null,
                    "Pb lors du listing de fichiers, le client est déconnecté",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void verificationExistanceRoot() {
        try {
            peer.rootIsConnected();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "La communauté n'a plus de noeud Root, le programme va s'arrêter",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

}