package org.example;
//konmtol
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AppTest {

    static boolean isAdmin = false;

    static DefaultTableModel katalogModel;
    static DefaultTableModel keranjangModel;
    static DefaultTableModel historyModel;

    static CardLayout cardLayout;
    static JPanel contentPanel;

    static JButton btnKelola;
    static JPanel homePanel;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Amba Cell");
        frame.setSize(400, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        /* ================= TOP BAR ================= */
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 45));

        JButton btnLogin = new JButton("Login");
        btnKelola = new JButton("Kelola");
        btnKelola.setVisible(false);

        topBar.add(btnLogin, BorderLayout.WEST);
        topBar.add(btnKelola, BorderLayout.EAST);

        /* ================= BOTTOM NAV ================= */
        JPanel bottomNav = new JPanel(new GridLayout(1, 4));
        bottomNav.setPreferredSize(new Dimension(0, 60));

        JButton btnHome = new JButton("Home");
        JButton btnKatalog = new JButton("Katalog");
        JButton btnKeranjang = new JButton("Keranjang");
        JButton btnHistory = new JButton("History");

        bottomNav.add(btnHome);
        bottomNav.add(btnKatalog);
        bottomNav.add(btnKeranjang);
        bottomNav.add(btnHistory);

        /* ================= CONTENT ================= */
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePage(), "HOME");
        contentPanel.add(createKatalogPage(), "KATALOG");
        contentPanel.add(createKeranjangPage(), "KERANJANG");
        contentPanel.add(createHistoryPage(), "HISTORY");
        contentPanel.add(createKelolaPage(), "KELOLA");

        /* ================= ACTION ================= */
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
        btnKatalog.addActionListener(e -> cardLayout.show(contentPanel, "KATALOG"));
        btnKeranjang.addActionListener(e -> cardLayout.show(contentPanel, "KERANJANG"));
        btnHistory.addActionListener(e -> cardLayout.show(contentPanel, "HISTORY"));
        btnKelola.addActionListener(e -> cardLayout.show(contentPanel, "KELOLA"));

        btnLogin.addActionListener(e -> {
            if (btnLogin.getText().equals("Logout")) {
                // LOGOUT
                isAdmin = false;
                btnKelola.setVisible(false);
                btnLogin.setText("Login");
                cardLayout.show(contentPanel, "HOME");
                JOptionPane.showMessageDialog(frame, "Anda telah logout");
            } else {
                showLogin(frame, btnLogin);
            }
        });

        frame.add(topBar, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(bottomNav, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /* ================= LOGIN ================= */
    private static void showLogin(JFrame frame, JButton btnLogin) {
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        Object[] msg = {"Username:", user, "Password:", pass};

        int opt = JOptionPane.showConfirmDialog(frame, msg, "Login",
                JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            String u = user.getText();
            String p = new String(pass.getPassword());

            if (u.equals("admin") && p.equals("admin")) {
                isAdmin = true;
                btnKelola.setVisible(true);
                btnLogin.setText("Logout");
                JOptionPane.showMessageDialog(frame, "Login sebagai Admin");
            } else if (u.equals("user") && p.equals("user")) {
                isAdmin = false;
                btnKelola.setVisible(false);
                btnLogin.setText("Logout");
                JOptionPane.showMessageDialog(frame, "Login sebagai User");
            } else {
                JOptionPane.showMessageDialog(frame, "Login gagal");
            }
        }
    }

    /* ================= HOME ================= */
    private static JScrollPane createHomePage() {
        homePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        refreshHome();
        return new JScrollPane(homePanel);
    }

    private static void refreshHome() {
        if (homePanel == null || katalogModel == null) return;

        homePanel.removeAll();

        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            ImageIcon icon = (ImageIcon) katalogModel.getValueAt(i, 3);
            String nama = katalogModel.getValueAt(i, 1).toString();
            String harga = katalogModel.getValueAt(i, 2).toString();

            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(150, 200));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel img = new JLabel(icon);
            img.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel text = new JLabel(
                    "<html><center>" + nama + "<br>Rp " + harga + "</center></html>",
                    SwingConstants.CENTER
            );

            JButton btnBuy = new JButton("+ Keranjang");
            int finalI = i;
            btnBuy.addActionListener(e -> {
                keranjangModel.addRow(new Object[]{
                        katalogModel.getValueAt(finalI, 0),
                        katalogModel.getValueAt(finalI, 1),
                        katalogModel.getValueAt(finalI, 2)
                });
                JOptionPane.showMessageDialog(homePanel, "Barang masuk ke keranjang");
            });

            card.add(img, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new GridLayout(2,1));
            bottom.add(text);
            bottom.add(btnBuy);

            card.add(bottom, BorderLayout.SOUTH);
            homePanel.add(card);
        }

        homePanel.revalidate();
        homePanel.repaint();
    }

    /* ================= KATALOG ================= */
    private static JPanel createKatalogPage() {
        JPanel panel = new JPanel(new BorderLayout());

        katalogModel = new DefaultTableModel(
                new Object[]{"Kode", "Nama", "Harga", "Gambar"}, 0
        );

        katalogModel.addRow(new Object[]{"BRG01", "Pulsa 10K", "12000", null});

        JTable table = new JTable(katalogModel);
        table.removeColumn(table.getColumnModel().getColumn(3));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAddCart = new JButton("+ Keranjang");

        btnAddCart.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                keranjangModel.addRow(new Object[]{
                        katalogModel.getValueAt(row, 0),
                        katalogModel.getValueAt(row, 1),
                        katalogModel.getValueAt(row, 2)
                });
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(btnAddCart);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    /* ================= KERANJANG ================= */
    private static JPanel createKeranjangPage() {
        JPanel panel = new JPanel(new BorderLayout());

        keranjangModel = new DefaultTableModel(
                new Object[]{"Kode", "Nama", "Harga"}, 0
        );

        JTable table = new JTable(keranjangModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnBayar = new JButton("Bayar");
        JButton btnHapus = new JButton("Hapus");

        btnBayar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                historyModel.addRow(new Object[]{
                        keranjangModel.getValueAt(row, 1), "Selesai"
                });
                keranjangModel.removeRow(row);
                JOptionPane.showMessageDialog(panel, "Pembayaran berhasil");
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih barang yang ingin dibayar");
            }
        });

        btnHapus.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                keranjangModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih barang yang ingin dihapus");
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(btnBayar);
        bottom.add(btnHapus);

        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    /* ================= HISTORY ================= */
    private static JPanel createHistoryPage() {
        JPanel panel = new JPanel(new BorderLayout());

        historyModel = new DefaultTableModel(
                new Object[]{"Nama Barang", "Status"}, 0
        );

        JTable table = new JTable(historyModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    /* ================= KELOLA (ADMIN) ================= */
    private static JPanel createKelolaPage() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable table = new JTable(katalogModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JTextField kode = new JTextField();
        JTextField nama = new JTextField();
        JTextField harga = new JTextField();

        final ImageIcon[] selectedImage = {null};

        JButton upload = new JButton("Upload Gambar");
        upload.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                ImageIcon icon = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                selectedImage[0] = new ImageIcon(img);
            }
        });

        JPanel form = new JPanel(new GridLayout(4, 2));
        form.add(new JLabel("Kode"));
        form.add(kode);
        form.add(new JLabel("Nama"));
        form.add(nama);
        form.add(new JLabel("Harga"));
        form.add(harga);
        form.add(new JLabel("Gambar"));
        form.add(upload);

        JButton add = new JButton("Input");
        add.addActionListener(e -> {
            katalogModel.addRow(new Object[]{
                    kode.getText(), nama.getText(), harga.getText(), selectedImage[0]
            });
            refreshHome();
        });

        JPanel action = new JPanel();
        action.add(add);

        panel.add(form, BorderLayout.NORTH);
        panel.add(action, BorderLayout.SOUTH);
        return panel;
    }
}
