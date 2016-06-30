package peer2peer.gui;

import peer2peer.model.Peer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
        this.initButtons(panel);
        this.getContentPane().add(panel, BorderLayout.SOUTH);
    }

    public JScrollPane initPeersList() {
        this.peers = new JList<>();
        this.peers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane pane = new JScrollPane(peers);
        this.peers.addListSelectionListener(evt -> setFiles());

        this.peers.setCellRenderer(new DefaultListCellRenderer(){
            public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                try {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    this.setText(((Peer) value).getPeerName());
                } catch (RemoteException e) {
                    unregister((Peer) value);
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

    public void initButtons(JPanel panel) {
        this.refreshPeersButton = new JButton("Peers");
        this.refreshPeersButton.addActionListener(evt -> setPeers());
        panel.add(this.refreshPeersButton);

        this.refreshFilesButton = new JButton("Files");
        this.refreshFilesButton.addActionListener(evt -> setFiles());
        panel.add(this.refreshFilesButton);

        this.downloadButton = new JButton("Download");
        this.downloadButton.addActionListener(evt -> download());
        panel.add(this.downloadButton);
    }

    public void unregister(Peer peer) {
        try {
            this.checkRoot();
            this.peer.unregister(peer);
            this.peers.setListData(this.peer.getPeers().toArray(new Peer[0]));
            this.files.setListData(new File[0]);
        } catch (RemoteException ignored) {

        }

        this.error("Sorry but the client was disconnected.");
    }

    public void setPeers(){
        try {
            this.peers.setListData(this.peer.getPeers().toArray(new Peer[0]));
        } catch (RemoteException e) {
            this.error("Sorry but there was an error.");
        }
    }

    public void setFiles(){
        this.checkRoot();
        Peer selectedPeer = this.peers.getSelectedValue();
        File[] localFiles = new File[0];
        if (selectedPeer != null) {
            try {
                localFiles = selectedPeer.getLocalFiles();
            } catch (RemoteException e) {
                this.unregister(selectedPeer);
            }
        }
        this.files.setListData(localFiles);
    }

    public void download(){
        this.checkRoot();
        Peer selectedPeer = this.peers.getSelectedValue();
        File selectedFile = this.files.getSelectedValue();
        try {
            if (selectedPeer != null && selectedFile != null) {
                this.peer.download(selectedFile, selectedPeer);
                JOptionPane.showMessageDialog(this, "The file was downloaded.");
            } else {
                this.error("You need to select a node and a file.");
            }
        } catch (IOException e) {
            this.unregister(selectedPeer);
        }
    }

    public void checkRoot() {
        try {
            this.peer.hasRoot();
        } catch (RemoteException e) {
            this.error("The root of the community was disconnect. The client will close.");
            System.exit(104);
        }
    }

    public void error(String message){
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);

    }

}