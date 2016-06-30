package peer2peer.gui;

import peer2peer.model.Peer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.rmi.RemoteException;

public class NodeUI extends JFrame {

    protected JList<Peer> peers;
    protected JList<File> files;
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
        this.peers = new JList<>();
        JScrollPane pane = new JScrollPane(peers);
        this.peers.addListSelectionListener(evt -> {
            this.checkRoot();
            this.files.setListData(new File[0]);
            Peer selectedPeer = peers.getSelectedValue();
            File[] localFiles = new File[0];
            if (selectedPeer != null) {
                try {
                    localFiles = selectedPeer.getLocalFiles();
                } catch (Exception e) {
                    traitement(selectedPeer);
                }
                files.setListData(localFiles);
            }
        });

        this.peers.setCellRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                try {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    this.setText(((Peer) value).getPeerName());
                } catch (RemoteException e) {
                    e.printStackTrace(); //TODO
                }
                return this;
            }
        });

        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }

    public JScrollPane initFilesList() {
        this.files = new JList<>();
        JScrollPane pane = new JScrollPane(files);

        this.files.setCellRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setText(((File) value).getName());
                return this;
            }
        });

        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }

    public void initRefreshPeersButton() {
        this.refreshPeersButton = new JButton("Peers");
        this.refreshPeersButton.addActionListener(evt -> {
            try {
                checkRoot();
                peers.setListData(peer.getPeers().values().toArray(new Peer[0]));
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
                files.setListData(new File[0]);
                Peer selectedPeer = peers.getSelectedValue();
                if (selectedPeer == null) return;
                files.setListData(selectedPeer.getLocalFiles());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void initDownloadButton() {
        this.downloadButton = new JButton("Download");
        this.downloadButton.addActionListener(evt -> {
            checkRoot();
            Peer selectedPeer = peers.getSelectedValue();
            File selectedFile = files.getSelectedValue();
            try {
                if (selectedPeer != null && selectedFile != null) {
                    peer.download(selectedFile, selectedPeer);
                    JOptionPane.showMessageDialog(null, "Le fichier a été téléchargé");
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez choisir un noeud et un fichier");
                }
            } catch (Exception e) {
                e.printStackTrace();
                traitement(selectedPeer);
            }
        });
    }

    public void traitement(Peer peer) {
        try {
            peer.unregister(peer);
            peers.setListData(peer.getPeers().keySet().toArray(new Peer[0]));
            files.setListData(new File[0]);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } finally {
            JOptionPane.showMessageDialog(this,
                    "Pb lors du listing de fichiers, le client est déconnecté",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void checkRoot() {
        try {
            this.peer.rootIsConnected();
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "The root of the community was disconnect. The client will close.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(104);
        }
    }

}