package org.example;

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

        contentPanel.add(createPage("HALAMAN HOME"), "HOME");
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

        btnLogin.addActionListener(e -> showLogin(frame, btnLogin));

        /* ================= ADD ================= */
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
                btnLogin.setText("Admin");
                JOptionPane.showMessageDialog(frame, "Login sebagai Admin");
            } else if (u.equals("user") && p.equals("user")) {
                isAdmin = false;
                btnKelola.setVisible(false);
                btnLogin.setText("User");
                JOptionPane.showMessageDialog(frame, "Login sebagai User");
            } else {
                JOptionPane.showMessageDialog(frame, "Login gagal");
            }
        }
    }

    /* ================= KATALOG ================= */
    private static JPanel createKatalogPage() {
        JPanel panel = new JPanel(new BorderLayout());

        katalogModel = new DefaultTableModel(
                new Object[]{"Kode", "Nama", "Harga"}, 0
        );
        katalogModel.addRow(new Object[]{"BRG01", "Pulsa 10K", "12000"});
        katalogModel.addRow(new Object[]{"BRG02", "Pulsa 20K", "22000"});

        JTable table = new JTable(katalogModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnAddCart = new JButton("+ Keranjang");
        btnAddCart.setPreferredSize(new Dimension(160, 40));

        btnAddCart.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                keranjangModel.addRow(new Object[]{
                        katalogModel.getValueAt(row, 0),
                        katalogModel.getValueAt(row, 1),
                        katalogModel.getValueAt(row, 2)
                });
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih barang terlebih dahulu");
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
        btnBayar.setPreferredSize(new Dimension(120, 35));

        btnBayar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                historyModel.addRow(new Object[]{
                        keranjangModel.getValueAt(row, 1),
                        "Selesai"
                });
                keranjangModel.removeRow(row);
                JOptionPane.showMessageDialog(panel, "Pembayaran berhasil");
            } else {
                JOptionPane.showMessageDialog(panel, "Pilih barang yang ingin dibayar");
            }
        });

        JPanel bottom = new JPanel();
        bottom.add(btnBayar);

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

        JPanel form = new JPanel(new GridLayout(3, 2));
        form.add(new JLabel("Kode"));
        form.add(kode);
        form.add(new JLabel("Nama"));
        form.add(nama);
        form.add(new JLabel("Harga"));
        form.add(harga);

        JButton add = new JButton("Input");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        add.addActionListener(e ->
                katalogModel.addRow(new Object[]{
                        kode.getText(), nama.getText(), harga.getText()
                })
        );

        update.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                katalogModel.setValueAt(kode.getText(), row, 0);
                katalogModel.setValueAt(nama.getText(), row, 1);
                katalogModel.setValueAt(harga.getText(), row, 2);
            }
        });

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) katalogModel.removeRow(row);
        });

        JPanel action = new JPanel();
        action.add(add);
        action.add(update);
        action.add(delete);

        panel.add(form, BorderLayout.NORTH);
        panel.add(action, BorderLayout.SOUTH);

        return panel;
    }

    /* ================= SIMPLE PAGE ================= */
    private static JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}