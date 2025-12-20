package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppTest {

    static boolean isAdmin = false;

    static DefaultTableModel katalogModel;
    static DefaultTableModel keranjangModel;
    static DefaultTableModel historyModel;

    static CardLayout cardLayout;
    static JPanel contentPanel;
    static JPanel homePanel;
    static JPanel katalogPanel;

    static JButton btnKelola;

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

        /* ================= MODEL ================= */
        katalogModel = new DefaultTableModel(
                new Object[]{"Kode", "Nama", "Harga", "Stok", "Gambar"}, 0
        );

        keranjangModel = new DefaultTableModel(
                new Object[]{"Kode", "Nama", "Harga", "Jumlah", "Gambar"}, 0
        );

        historyModel = new DefaultTableModel(
                new Object[]{"Nama Barang", "Status"}, 0
        );

        List<Produk> list = JsonStorage.loadKatalog();
        for (Produk p : list) {
            katalogModel.addRow(new Object[]{
                    p.kode, p.nama, p.harga, p.stok,
                    new ImageIcon(p.gambarPath)
            });
        }

        /* ================= CONTENT ================= */
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePage(), "HOME");
        contentPanel.add(createKatalogPage(), "KATALOG");
        contentPanel.add(createKeranjangPage(), "KERANJANG");
        contentPanel.add(createHistoryPage(), "HISTORY");
        contentPanel.add(createKelolaPage(), "KELOLA");

        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
        btnKatalog.addActionListener(e -> cardLayout.show(contentPanel, "KATALOG"));
        btnKeranjang.addActionListener(e -> cardLayout.show(contentPanel, "KERANJANG"));
        btnHistory.addActionListener(e -> cardLayout.show(contentPanel, "HISTORY"));
        btnKelola.addActionListener(e -> cardLayout.show(contentPanel, "KELOLA"));

        btnLogin.addActionListener(e -> {
            if (btnLogin.getText().equals("Logout")) {
                isAdmin = false;
                btnKelola.setVisible(false);
                btnLogin.setText("Login");
                cardLayout.show(contentPanel, "HOME");
            } else {
                showLogin(frame, btnLogin);
            }
        });

        frame.add(topBar, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.add(bottomNav, BorderLayout.SOUTH);
        frame.setVisible(true);

        refreshHome();
        refreshKatalog();
    }



    /* ================= LOGIN ================= */
    private static void showLogin(JFrame frame, JButton btnLogin) {
        JTextField u = new JTextField();
        JPasswordField p = new JPasswordField();

        Object[] msg = {"Username:", u, "Password:", p};

        if (JOptionPane.showConfirmDialog(frame, msg, "Login",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            if (u.getText().equals("admin") && new String(p.getPassword()).equals("admin")) {
                isAdmin = true;
                btnKelola.setVisible(true);
                btnLogin.setText("Logout");
            } else if (u.getText().equals("user") && new String(p.getPassword()).equals("user")) {
                btnLogin.setText("Logout");
            } else {
                JOptionPane.showMessageDialog(frame, "Login gagal");
            }
        }
    }

    /* ================= HOME (GRID CARD) ================= */
    private static JScrollPane createHomePage() {
        homePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        refreshHome();
        return new JScrollPane(homePanel);
    }

    private static void refreshHome() {
        if (homePanel == null) return;

        homePanel.removeAll();

        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            ImageIcon icon = (ImageIcon) katalogModel.getValueAt(i, 4);
            int stok = (int) katalogModel.getValueAt(i, 3);

            JPanel card = new JPanel(new BorderLayout());
            card.setPreferredSize(new Dimension(150, 200));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel img = new JLabel(icon, SwingConstants.CENTER);

            JLabel text = new JLabel("<html><center>" +
                    katalogModel.getValueAt(i, 1) +
                    "<br>Rp " + katalogModel.getValueAt(i, 2) +
                    "<br>Stok: " + stok +
                    "</center></html>", SwingConstants.CENTER);

            card.add(img, BorderLayout.CENTER);
            card.add(text, BorderLayout.SOUTH);

            homePanel.add(card);
        }

        homePanel.revalidate();
        homePanel.repaint();
    }

    /* ================= KATALOG (GRID CARD + JUMLAH) ================= */
    private static JPanel createKatalogPage() {
        katalogPanel = new JPanel(new BorderLayout());
        refreshKatalog();
        return katalogPanel;
    }

    private static void refreshKatalog() {
        katalogPanel.removeAll();

        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 10));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < katalogModel.getRowCount(); i++) {

            int row = i;
            String kode = katalogModel.getValueAt(i, 0).toString();
            String nama = katalogModel.getValueAt(i, 1).toString();
            String harga = katalogModel.getValueAt(i, 2).toString();
            int stok = (int) katalogModel.getValueAt(i, 3);
            ImageIcon img = (ImageIcon) katalogModel.getValueAt(i, 4);

            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel lblImg = new JLabel(img, SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(150, 120));

            JLabel lblInfo = new JLabel("<html><b>" + nama + "</b><br>Rp " +
                    harga + "<br>Stok: " + stok + "</html>");

            JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, stok), 1));
            qty.setEnabled(stok > 0);

            JButton btnCart = new JButton("+ Keranjang");
            btnCart.setEnabled(stok > 0);

            btnCart.addActionListener(e -> {
                int jumlah = (int) qty.getValue();

                keranjangModel.addRow(new Object[]{
                        kode, nama, harga, jumlah, img
                });

                katalogModel.setValueAt(stok - jumlah, row, 3);
                refreshHome();
                refreshKatalog();
            });

            JPanel bottom = new JPanel(new GridLayout(2, 1));
            bottom.add(qty);
            bottom.add(btnCart);

            card.add(lblImg, BorderLayout.NORTH);
            card.add(lblInfo, BorderLayout.CENTER);
            card.add(bottom, BorderLayout.SOUTH);

            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        katalogPanel.add(scroll, BorderLayout.CENTER);

        katalogPanel.revalidate();
        katalogPanel.repaint();
    }

    /* ================= KERANJANG ================= */
    private static JPanel createKeranjangPage() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable table = new JTable(keranjangModel);
        table.removeColumn(table.getColumnModel().getColumn(4));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton bayar = new JButton("Bayar");
        bayar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                historyModel.addRow(new Object[]{
                        keranjangModel.getValueAt(r, 1), "Selesai"
                });
                keranjangModel.removeRow(r);
            }
        });

        JButton hapus = new JButton("Hapus");
        hapus.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                String kode = keranjangModel.getValueAt(r, 0).toString();
                int jumlah = (int) keranjangModel.getValueAt(r, 3);

                // Cari baris di katalogModel dengan kode yang sama dan kembalikan stok
                for (int i = 0; i < katalogModel.getRowCount(); i++) {
                    if (katalogModel.getValueAt(i, 0).equals(kode)) {
                        int currentStok = (int) katalogModel.getValueAt(i, 3);
                        katalogModel.setValueAt(currentStok + jumlah, i, 3);
                        break;
                    }
                }

                keranjangModel.removeRow(r);
                refreshHome();
                refreshKatalog();
            }
        });

        buttonPanel.add(bayar);
        buttonPanel.add(hapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    /* ================= HISTORY ================= */
    private static JScrollPane createHistoryPage() {
        return new JScrollPane(new JTable(historyModel));
    }

    /* ================= KELOLA ADMIN (CRUD) ================= */
    private static JPanel createKelolaPage() {
        JPanel panel = new JPanel(new BorderLayout());

        JTable table = new JTable(katalogModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JTextField kode = new JTextField();
        JTextField nama = new JTextField();
        JTextField harga = new JTextField();
        JTextField stok = new JTextField();
        final ImageIcon[] img = {null};

        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                kode.setText(katalogModel.getValueAt(r, 0).toString());
                nama.setText(katalogModel.getValueAt(r, 1).toString());
                harga.setText(katalogModel.getValueAt(r, 2).toString());
                stok.setText(katalogModel.getValueAt(r, 3).toString());
                img[0] = (ImageIcon) katalogModel.getValueAt(r, 4);
            }
        });

        JButton upload = new JButton("Upload");
        upload.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();

                ImageIcon icon = new ImageIcon(
                        new ImageIcon(path)
                                .getImage()
                                .getScaledInstance(150, 120, Image.SCALE_SMOOTH)
                );
                icon.setDescription(path);

                img[0] = resizeImage(path, 150, 120);

            }
        });

        JPanel form = new JPanel(new GridLayout(5, 2));
        form.add(new JLabel("Kode")); form.add(kode);
        form.add(new JLabel("Nama")); form.add(nama);
        form.add(new JLabel("Harga")); form.add(harga);
        form.add(new JLabel("Stok")); form.add(stok);
        form.add(new JLabel("Gambar")); form.add(upload);

        JButton add = new JButton("Input");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        add.addActionListener(e -> {
            katalogModel.addRow(new Object[]{
                    kode.getText(), nama.getText(), harga.getText(),
                    Integer.parseInt(stok.getText()), img[0]
            });
            refreshHome();
            saveKatalogToJson();
            refreshKatalog();
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                katalogModel.setValueAt(kode.getText(), r, 0);
                katalogModel.setValueAt(nama.getText(), r, 1);
                katalogModel.setValueAt(harga.getText(), r, 2);
                katalogModel.setValueAt(Integer.parseInt(stok.getText()), r, 3);
                katalogModel.setValueAt(img[0], r, 4);
                refreshHome();
                saveKatalogToJson();
                refreshKatalog();
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                katalogModel.removeRow(r);
                refreshHome();
                saveKatalogToJson();
                refreshKatalog();
            }
        });

        JPanel btn = new JPanel();
        btn.add(add);
        btn.add(update);
        btn.add(delete);

        panel.add(form, BorderLayout.NORTH);
        panel.add(btn, BorderLayout.SOUTH);

        return panel;
    }
    //===json===
    private static void saveKatalogToJson() {
        java.util.List<Produk> list = new java.util.ArrayList<>();

        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            ImageIcon icon = (ImageIcon) katalogModel.getValueAt(i, 4);

            list.add(new Produk(
                    katalogModel.getValueAt(i, 0).toString(),
                    katalogModel.getValueAt(i, 1).toString(),
                    katalogModel.getValueAt(i, 2).toString(),
                    (int) katalogModel.getValueAt(i, 3),
                    icon.getDescription() // path gambar
            ));
        }

        JsonStorage.saveKatalog(list);
    }

    private static ImageIcon resizeImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(
                width, height, Image.SCALE_SMOOTH
        );
        ImageIcon resized = new ImageIcon(img);
        resized.setDescription(path); // penting buat JSON
        return resized;
    }




}
