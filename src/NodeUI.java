import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NodeUI extends JFrame {

    protected JList lnode;
    protected JList lfile;
    protected JButton brefresh;
    protected JButton bfiles;
    protected JButton bdownload;
    protected PeerImpl _peer;
    protected SharedDirectory _fs;

    public NodeUI(String id, PeerImpl peer, SharedDirectory fs) {
        super(id);
        this._peer = peer;
        this._fs = fs;
        this.init();
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {

        if (args.length < 4) {
            System.err.println("usage : java EBidet id path isroot rootname");
            System.exit(0);
        }

        //NodeUI UI = new NodeUI("essai");
        NodeUI ui = new NodeUI(id, peer, fs);

    }

    public void init() {

        /* list of peers */
        this.lnode = new JList();
        JScrollPane scrollPaneln = new JScrollPane(lnode);
        scrollPaneln.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		/* list of file for a selected node */
        this.lfile = new JList();
        JScrollPane scrollPanelf = new JScrollPane(lfile);
        scrollPanelf.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneln, scrollPanelf);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));

		/* button for refreshing node list */
        this.brefresh = new JButton("Nodes");
        this.brefresh.addActionListener(new RefreshListener());
        p.add(this.brefresh);

		/* button for refreshing file list */
        this.bfiles = new JButton("Files");
        this.bfiles.addActionListener(new FilesListener());
        p.add(this.bfiles);

		/* button for download file */
        this.bdownload = new JButton("Download");
        this.bdownload.addActionListener(new DownloadListener());
        p.add(this.bdownload);
        this.getContentPane().add(p, BorderLayout.SOUTH);
    }

    class RefreshListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            try {
                lnode.setListData(new String[0]);
                _peer.directory().keySet().toArray();
                lfile.setListData(new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class FilesListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            try {
                lfile.setListData(new String[0]);
                String nname = (String) lnode.getSelectedValue();
                if (nname == null) return;
                lfile.setListData(_peer.directory().get(nname).files());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DownloadListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            try {
                String nname = (String) lnode.getSelectedValue();
                String fname = (String) lfile.getSelectedValue();
                if (nname == null || fname == null) return;
                /* get the peer reference, then ask it the file */
                Peer p = _peer.directory().get(nname);
                byte[] fvalue = p.getFile(fname);
                _fs.put(fname, fvalue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
