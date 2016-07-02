package peer2peer.gui;

import peer2peer.model.peers.Peer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.rmi.RemoteException;

/**
 * I am the main window of the Peer2Peer application.
 * I contains a list of connected peers and a list of files for the selected peer.
 * I contains also 3 buttons to update the peers, files or download a file.
 */
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

    /**
     * I init all the elements of the frame.
     */
    public void init() {
        JScrollPane peersPane = this.initPeersList();
        JScrollPane filesPane = this.initFilesList();
        this.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, peersPane, filesPane), BorderLayout.CENTER);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.initButtons(panel);
        this.getContentPane().add(panel, BorderLayout.SOUTH);

        this.setPeers();
        this.setFiles();
        this.selectFirstElementsOfLists();
    }

    /**
     * I init the peer list and return it.
     * @return a scroll pane with the list of peers.
     */
    public JScrollPane initPeersList() {
        this.peers = new JList<>();
        this.peers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane pane = new JScrollPane(peers);
        this.peers.addListSelectionListener(evt -> setFiles());

        this.peers.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
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

    /**
     * I init the files list and return it.
     * @return a scroll pane with the list of files.
     */
    public JScrollPane initFilesList() {
        this.files = new JList<>();
        JScrollPane pane = new JScrollPane(files);

        this.files.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                this.setText(((File) value).getName());
                return this;
            }
        });

        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }

    /**
     * I init all the buttons of the frame and add the in a panel.
     * @param panel the panel that will contains les buttons.
     */
    public void initButtons(JPanel panel) {
        this.refreshPeersButton = new JButton("Peers");
        this.refreshPeersButton.addActionListener(evt -> setPeers());
        panel.add(this.refreshPeersButton);

        this.refreshFilesButton = new JButton("Files");
        this.refreshFilesButton.addActionListener(evt -> setFiles());
        panel.add(this.refreshFilesButton);

        this.downloadButton = new JButton("Download");
        this.downloadButton.addActionListener(evt -> downloadFile());
        panel.add(this.downloadButton);
    }

    /**
     * I select the first elements of the different lists.
     */
    public void selectFirstElementsOfLists() {
        this.selectFirstElementOf(this.peers);
        this.selectFirstElementOf(this.files);
    }

    /**
     * I select the first element of a list.
     * @param list The list to select the first element.
     */
    public void selectFirstElementOf(JList list) {
        if (list.getModel().getSize() > 0) {
            list.setSelectedIndex(0);
        }
    }

    /**
     * I unregister a peer that was disconnected.
     * @param peer the peer to unregister.
     */
    public void unregister(Peer peer) {
        try {
            this.checkRoot();
            this.peer.unregister(peer);
            this.peers.setListData(this.peer.getPeers().toArray(new Peer[0]));
            this.files.setListData(new File[0]);
        } catch (RemoteException ignored) {

        }
        this.error("Sorry but the client was disconnected.");
        this.selectFirstElementsOfLists();
    }

    /**
     * I set the model of the peer list.
     */
    public void setPeers() {
        try {
            this.peers.setListData(this.peer.getPeers().toArray(new Peer[0]));
        } catch (RemoteException e) {
            this.error("Sorry but there was an error.");
        }
        this.selectFirstElementsOfLists();
    }

    /**
     * I set the model of the files list.
     */
    public void setFiles() {
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
        this.selectFirstElementsOfLists();
    }

    /**
     * I download the selected file of the selected peer.
     */
    public void downloadFile() {
        this.checkRoot();
        Peer selectedPeer = this.peers.getSelectedValue();
        File selectedFile = this.files.getSelectedValue();
        try {
            if (selectedPeer != null && selectedFile != null) {
                this.peer.downloadFrom(selectedPeer, selectedFile);
                JOptionPane.showMessageDialog(this, selectedFile.getName() + " was downloaded from " + selectedPeer.getPeerName() + ".");
            } else {
                this.error("You need to select a peer and a file.");
            }
        } catch (FileAlreadyExistsException e) {
            this.error("This file already exist in the destination folder.");
        } catch (IOException e) {
            this.unregister(selectedPeer);
        }
    }

    /**
     * I check that the root is still connected. Else I stop the application.
     */
    public void checkRoot() {
        try {
            this.peer.checkRoot();
        } catch (RemoteException e) {
            this.error("The root of the community was disconnect. The client will close.");
            System.exit(104);
        }
    }

    /**
     * I am a convenience method to display an error.
     * @param message the message of the error.
     */
    public void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);

    }

}