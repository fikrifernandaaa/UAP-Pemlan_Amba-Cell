package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppTest {

    static boolean isLogin = false;
    static boolean isUser = false;
    static boolean isAdmin = false;

    static DefaultTableModel katalogModel;
    static DefaultTableModel keranjangModel;
    static DefaultTableModel historyModel;

    static CardLayout cardLayout;
    static JPanel contentPanel;
    static JPanel homePanel;
    static JPanel katalogPanel;

    static JButton btnKelola;

    // 🎨 MODERN COLOR PALETTE
    static final Color PRIMARY_COLOR = new Color(25, 118, 210);      // Blue
    static final Color SECONDARY_COLOR = new Color(66, 165, 245);    // Light Blue
    static final Color ACCENT_COLOR = new Color(255, 152, 0);        // Orange
    static final Color SUCCESS_COLOR = new Color(76, 175, 80);       // Green
    static final Color DANGER_COLOR = new Color(244, 67, 54);        // Red
    static final Color BG_COLOR = new Color(250, 250, 250);          // Light Gray
    static final Color CARD_BG = Color.WHITE;
    static final Color TEXT_COLOR = new Color(33, 33, 33);

    public static void main(String[] args) {

        // Set Look and Feel Modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Amba Cell - Premium Phone Store");
        frame.setSize(420, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        /* ================= TOP BAR (MODERN) ================= */
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 55));
        topBar.setBackground(PRIMARY_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Logo & Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftPanel.setOpaque(false);
        JLabel logo = new JLabel("📱");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        JLabel title = new JLabel("Amba Cell");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        leftPanel.add(logo);
        leftPanel.add(title);

        // Buttons
        JButton btnLogin = createModernButton("🔐 Login", SECONDARY_COLOR);
        btnKelola = createModernButton("⚙️ Kelola", ACCENT_COLOR);
        btnKelola.setVisible(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightPanel.setOpaque(false);
        rightPanel.add(btnLogin);
        rightPanel.add(btnKelola);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        /* ================= BOTTOM NAV (MODERN) ================= */
        JPanel bottomNav = new JPanel(new GridLayout(1, 4, 1, 0));
        bottomNav.setPreferredSize(new Dimension(0, 65));
        bottomNav.setBackground(Color.BLACK);
        bottomNav.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(224, 224, 224)));

        JButton btnHome = createNavButton("🏠", "Home");
        JButton btnKatalog = createNavButton("📦", "Katalog");
        JButton btnKeranjang = createNavButton("🛒", "Keranjang");
        JButton btnHistory = createNavButton("📋", "History");

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
        contentPanel.setBackground(BG_COLOR);

        contentPanel.add(createHomePage(), "HOME");
        contentPanel.add(createKatalogPage(), "KATALOG");
        contentPanel.add(createKeranjangPage(), "KERANJANG");
        contentPanel.add(createHistoryPage(), "HISTORY");
        contentPanel.add(createKelolaPage(), "KELOLA");

        btnHome.addActionListener(e -> {
            cardLayout.show(contentPanel, "HOME");
            highlightNavButton(btnHome, btnKatalog, btnKeranjang, btnHistory);
        });
        btnKatalog.addActionListener(e -> {
            cardLayout.show(contentPanel, "KATALOG");
            highlightNavButton(btnKatalog, btnHome, btnKeranjang, btnHistory);
        });
        btnKeranjang.addActionListener(e -> {
            cardLayout.show(contentPanel, "KERANJANG");
            highlightNavButton(btnKeranjang, btnHome, btnKatalog, btnHistory);
        });
        btnHistory.addActionListener(e -> {
            cardLayout.show(contentPanel, "HISTORY");
            highlightNavButton(btnHistory, btnHome, btnKatalog, btnKeranjang);
        });
        btnKelola.addActionListener(e -> cardLayout.show(contentPanel, "KELOLA"));

        btnLogin.addActionListener(e -> {
            if (btnLogin.getText().contains("Logout")) {
                isLogin = false;
                isAdmin = false;
                isUser = false;

                btnKelola.setVisible(false);
                btnLogin.setText("🔐 Login");
                btnLogin.setBackground(SECONDARY_COLOR);

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
        highlightNavButton(btnHome, btnKatalog, btnKeranjang, btnHistory);
    }

    /* ================= MODERN BUTTON CREATOR ================= */
    private static JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(75, 35));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    /* ================= NAV BUTTON CREATOR ================= */
    private static JButton createNavButton(String iconText, String text) {

        // Panel isi tombol
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        // ICON (emoji / logo)
        JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // TEXT
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(4));
        panel.add(icon);
        panel.add(Box.createVerticalStrut(2));
        panel.add(label);

        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.add(panel, BorderLayout.CENTER);

        btn.setBackground(new Color(200, 200, 200));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 65));

        return btn;
    }


    /* ================= HIGHLIGHT NAV ================= */
    private static void highlightNavButton(JButton active, JButton... others) {
        active.setForeground(PRIMARY_COLOR);
        for (JButton btn : others) {
            btn.setForeground(new Color(120, 120, 120));
        }
    }

    /* ================= LOGIN (MODERN) ================= */
    private static void showLogin(JFrame frame, JButton btnLogin) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField u = new JTextField();
        JPasswordField p = new JPasswordField();

        u.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        p.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(new JLabel("Username:"));
        panel.add(u);
        panel.add(new JLabel("Password:"));
        panel.add(p);

        int result = JOptionPane.showConfirmDialog(frame, panel, "🔐 Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String user = u.getText();
            String pass = new String(p.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                isLogin = true;
                isAdmin = true;
                isUser = false;

                btnKelola.setVisible(true);
                btnLogin.setText("🚪 Logout");
                btnLogin.setBackground(DANGER_COLOR);

                showModernMessage(frame, "✅ Login Berhasil",
                        "Selamat datang Admin!\nAnda dapat mengelola barang.", SUCCESS_COLOR);

            } else if (user.equals("user") && pass.equals("user")) {
                isLogin = true;
                isAdmin = false;
                isUser = true;

                btnKelola.setVisible(false);
                btnLogin.setText("🚪 Logout");
                btnLogin.setBackground(DANGER_COLOR);

                KeranjangStorage.load(keranjangModel);

                showModernMessage(frame, "✅ Login Berhasil",
                        "Selamat datang User!\nSilakan berbelanja 🛒", SUCCESS_COLOR);

            } else {
                showModernMessage(frame, "❌ Login Gagal",
                        "Username atau password salah!", DANGER_COLOR);
            }
        }
    }

    /* ================= MODERN MESSAGE DIALOG ================= */
    private static void showModernMessage(JFrame frame, String title, String message, Color color) {
        JOptionPane pane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                new Object[]{},
                null
        );

        JDialog dialog = pane.createDialog(frame, title);
        dialog.getContentPane().setBackground(color);

        Timer timer = new Timer(2000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    /* ================= HOME (MODERN BANNER) ================= */
    static JPanel homePanelTop;
    static JPanel homePanelBottom;

    private static JPanel createHomePage() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        /* ================= MODERN BANNER ================= */
        JPanel banner = new JPanel();
        banner.setBackground(new Color(66, 165, 245));
        banner.setPreferredSize(new Dimension(0, 140));
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Amba Cell");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Futura Md BT", Font.BOLD, 36));

        JLabel slogan = new JLabel("''Solusi Genggaman Anda''");
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);
        slogan.setForeground(Color.WHITE);
        slogan.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        JLabel emoji = new JLabel("📱 💎 🔥");
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);
        emoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));

        banner.add(Box.createVerticalGlue());
        banner.add(title);
        banner.add(Box.createRigidArea(new Dimension(0, 5)));
        banner.add(slogan);
        banner.add(Box.createRigidArea(new Dimension(0, 5)));
        banner.add(emoji);
        banner.add(Box.createVerticalGlue());

        /* ================= CONTAINER PRODUK ================= */
        JPanel productContainer = new JPanel();
        productContainer.setLayout(new BoxLayout(productContainer, BoxLayout.Y_AXIS));
        productContainer.setBackground(BG_COLOR);

        homePanelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        homePanelTop.setBackground(BG_COLOR);
        homePanelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        homePanelBottom.setBackground(BG_COLOR);

        JScrollPane scrollTop = new JScrollPane(
                homePanelTop,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollTop.setBorder(null);
        scrollTop.getViewport().setBackground(BG_COLOR);

        JScrollPane scrollBottom = new JScrollPane(
                homePanelBottom,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        scrollBottom.setBorder(null);
        scrollBottom.getViewport().setBackground(BG_COLOR);

        productContainer.add(scrollTop);
        productContainer.add(scrollBottom);

        mainPanel.add(banner, BorderLayout.NORTH);
        mainPanel.add(productContainer, BorderLayout.CENTER);

        return mainPanel;
    }

    private static void refreshHome() {
        if (homePanelTop == null || homePanelBottom == null) return;

        homePanelTop.removeAll();
        homePanelBottom.removeAll();

        int topCount = 0;
        int bottomCount = 0;

        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            ImageIcon icon = (ImageIcon) katalogModel.getValueAt(i, 4);
            int stok = (int) katalogModel.getValueAt(i, 3);
            String nama = katalogModel.getValueAt(i, 1).toString();
            String harga = katalogModel.getValueAt(i, 2).toString();

            // 🎨 MODERN PRODUCT CARD
            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setPreferredSize(new Dimension(165, 220));
            card.setBackground(CARD_BG);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel img = new JLabel(icon, SwingConstants.CENTER);

            JLabel lblNama = new JLabel("<html><b>" + nama + "</b></html>", SwingConstants.CENTER);
            lblNama.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblNama.setForeground(TEXT_COLOR);

            JLabel lblHarga = new JLabel("Rp " + harga, SwingConstants.CENTER);
            lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblHarga.setForeground(ACCENT_COLOR);

            JLabel lblStok = new JLabel("Stok: " + stok, SwingConstants.CENTER);
            lblStok.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblStok.setForeground(stok > 0 ? SUCCESS_COLOR : DANGER_COLOR);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(CARD_BG);

            lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblStok.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(lblNama);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(lblHarga);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(lblStok);

            card.add(img, BorderLayout.CENTER);
            card.add(infoPanel, BorderLayout.SOUTH);

            if (i % 2 == 0) {
                homePanelTop.add(card);
                topCount++;
            } else {
                homePanelBottom.add(card);
                bottomCount++;
            }
        }

        homePanelTop.setPreferredSize(new Dimension(topCount * 180, 240));
        homePanelBottom.setPreferredSize(new Dimension(bottomCount * 180, 240));

        homePanelTop.revalidate();
        homePanelBottom.revalidate();
        homePanelTop.repaint();
        homePanelBottom.repaint();
    }

    /* ================= KATALOG (MODERN) ================= */
    private static JPanel createKatalogPage() {
        katalogPanel = new JPanel(new BorderLayout());
        katalogPanel.setBackground(BG_COLOR);
        refreshKatalog();
        return katalogPanel;
    }

    static JTextField txtSearch;
    static String keywordSearch = "";
    static int selectedSort = 0; // 0=default, 1=termurah, 2=termahal

    private static void refreshKatalog() {
        katalogPanel.removeAll();

        /* ================= MODERN SEARCH + SORT BAR ================= */
        JPanel topFilter = new JPanel(new BorderLayout());
        topFilter.setBackground(Color.WHITE);
        topFilter.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(224, 224, 224)));

        // LEFT (SEARCH)
        JPanel leftFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftFilter.setOpaque(false);

        txtSearch = new JTextField(10);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        JButton btnCari = createModernButton("🔍 Cari", PRIMARY_COLOR);
        JButton btnReset = createModernButton("🔄 Reset", new Color(120, 120, 120));

        leftFilter.add(new JLabel("🔎"));
        leftFilter.add(txtSearch);
        leftFilter.add(btnCari);
        leftFilter.add(btnReset);

        // RIGHT (SORTING)
        JPanel rightFilter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightFilter.setOpaque(false);

        JComboBox<String> cbSort = new JComboBox<>(new String[]{
                "↕️ Urutkan Harga",
                "⬆️ Termurah",
                "⬇️ Termahal"
        });
        cbSort.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbSort.setPreferredSize(new Dimension(90, 30));
        cbSort.setSelectedIndex(selectedSort); // 🔥 penting

        cbSort.addActionListener(e -> {
            selectedSort = cbSort.getSelectedIndex();
            refreshKatalog();
        });

        rightFilter.add(cbSort);

        topFilter.add(leftFilter, BorderLayout.WEST);
        topFilter.add(rightFilter, BorderLayout.EAST);

        katalogPanel.add(topFilter, BorderLayout.NORTH);

        /* ================= GRID ================= */
        JPanel grid = new JPanel(new GridLayout(0, 2, 15, 15));
        grid.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        grid.setBackground(BG_COLOR);

        boolean ditemukan = false;

        // ====== BUAT LIST INDEX UNTUK SORTING ======
        java.util.List<Integer> indexList = new java.util.ArrayList<>();
        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            indexList.add(i);
        }

        if (selectedSort == 1) { // ⬆️ Termurah
            indexList.sort((a, b) -> {
                double ha = Double.parseDouble(
                        katalogModel.getValueAt(a, 2).toString().replace(".", "")
                );
                double hb = Double.parseDouble(
                        katalogModel.getValueAt(b, 2).toString().replace(".", "")
                );
                return Double.compare(ha, hb);
            });
        }
        else if (selectedSort == 2) { // ⬇️ Termahal
            indexList.sort((a, b) -> {
                double ha = Double.parseDouble(
                        katalogModel.getValueAt(a, 2).toString().replace(".", "")
                );
                double hb = Double.parseDouble(
                        katalogModel.getValueAt(b, 2).toString().replace(".", "")
                );
                return Double.compare(hb, ha);
            });
        }

        // ====== TAMPILKAN DATA ======
        for (int idx : indexList) {
            String nama = katalogModel.getValueAt(idx, 1).toString().toLowerCase();

            if (!keywordSearch.isEmpty() && !nama.contains(keywordSearch)) {
                continue;
            }

            ditemukan = true;

            int row = idx;
            String kode = katalogModel.getValueAt(idx, 0).toString();
            String harga = katalogModel.getValueAt(idx, 2).toString();
            int stok = (int) katalogModel.getValueAt(idx, 3);
            ImageIcon img = (ImageIcon) katalogModel.getValueAt(idx, 4);

            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBackground(CARD_BG);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel lblImg = new JLabel(img, SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(150, 120));

            JLabel lblInfo = new JLabel("<html><b style='font-size:11px;'>" + nama +
                    "</b><br><span style='color:#FF9800; font-size:12px;'>Rp " + harga +
                    "</span><br><span style='font-size:10px; color:" +
                    (stok > 0 ? "#4CAF50" : "#F44336") + ";'>Stok: " + stok +
                    "</span></html>");

            JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, Math.max(1, stok), 1));
            qty.setEnabled(stok > 0);

            JButton btnCart = createModernButton("🛒 Tambah", SUCCESS_COLOR);
            btnCart.setEnabled(stok > 0);

            btnCart.addActionListener(e -> {
                if (!isLogin) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Silakan login sebagai USER terlebih dahulu",
                            "Belum Login", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (isAdmin) {
                    JOptionPane.showMessageDialog(null,
                            "❌ Admin tidak diperbolehkan berbelanja",
                            "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int jumlah = (int) qty.getValue();

                keranjangModel.addRow(new Object[]{
                        kode, nama, harga, jumlah, img
                });

                KeranjangStorage.save(keranjangModel);
                katalogModel.setValueAt(stok - jumlah, row, 3);
                refreshHome();
                refreshKatalog();
                saveKatalogToJson();
            });

            JPanel bottom = new JPanel(new GridLayout(2, 1, 5, 5));
            bottom.setOpaque(false);
            bottom.add(qty);
            bottom.add(btnCart);

            card.add(lblImg, BorderLayout.NORTH);
            card.add(lblInfo, BorderLayout.CENTER);
            card.add(bottom, BorderLayout.SOUTH);

            grid.add(card);
        }

        if (!keywordSearch.isEmpty() && !ditemukan) {
            JOptionPane.showMessageDialog(null,
                    "😔 Barang tidak ditemukan", "Pencarian",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_COLOR);
        katalogPanel.add(scroll, BorderLayout.CENTER);


        // ====== ACTION ======
        btnCari.addActionListener(e -> {
            keywordSearch = txtSearch.getText().trim().toLowerCase();
            refreshKatalog();
        });

        btnReset.addActionListener(e -> {
            keywordSearch = "";
            txtSearch.setText("");
            refreshKatalog();
        });

        cbSort.addActionListener(e -> refreshKatalog());

        katalogPanel.revalidate();
        katalogPanel.repaint();
    }

    /* ================= KERANJANG (MODERN) ================= */
    private static JPanel createKeranjangPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JTable table = new JTable(keranjangModel);
        table.setRowHeight(90);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.BLACK);
        table.setSelectionBackground(SECONDARY_COLOR);

        table.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));

        JButton bayar = createModernButton("💳 Bayar", SUCCESS_COLOR);
        bayar.setPreferredSize(new Dimension(130, 40));

        JButton hapus = createModernButton("🗑️ Hapus", DANGER_COLOR);
        hapus.setPreferredSize(new Dimension(130, 40));

        bayar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                historyModel.addRow(new Object[]{
                        keranjangModel.getValueAt(r, 1), "✅ Selesai"
                });
                keranjangModel.removeRow(r);
                KeranjangStorage.save(keranjangModel);
                JOptionPane.showMessageDialog(panel, "✅ Pembayaran berhasil!");
            } else {
                JOptionPane.showMessageDialog(panel, "⚠️ Pilih produk terlebih dahulu!");
            }
        });

        hapus.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                String kode = keranjangModel.getValueAt(r, 0).toString();
                int jumlah = (int) keranjangModel.getValueAt(r, 3);

                for (int i = 0; i < katalogModel.getRowCount(); i++) {
                    if (katalogModel.getValueAt(i, 0).equals(kode)) {
                        int currentStok = (int) katalogModel.getValueAt(i, 3);
                        katalogModel.setValueAt(currentStok + jumlah, i, 3);
                        break;
                    }
                }

                keranjangModel.removeRow(r);
                KeranjangStorage.save(keranjangModel);
                refreshHome();
                refreshKatalog();
                saveKatalogToJson();
            } else {
                JOptionPane.showMessageDialog(panel, "⚠️ Pilih produk terlebih dahulu!");
            }
        });

        buttonPanel.add(bayar);
        buttonPanel.add(hapus);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    static class ImageRenderer extends JLabel implements javax.swing.table.TableCellRenderer {
        public ImageRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
                setText("");
            } else {
                setIcon(null);
                setText("No Image");
            }
            return this;
        }
    }

    /* ================= HISTORY (MODERN) ================= */
    private static JScrollPane createHistoryPage() {
        JTable table = new JTable(historyModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(SECONDARY_COLOR);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        return scroll;
    }

    /* ================= KELOLA (MODERN) ================= */
    private static JPanel createKelolaPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        JTable table = new JTable(katalogModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(null);
        panel.add(tableScroll, BorderLayout.CENTER);

        JTextField kode = new JTextField();
        JTextField nama = new JTextField();
        JTextField harga = new JTextField();
        JTextField stok = new JTextField();
        final ImageIcon[] img = {null};

        // Modern text field styling
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        kode.setFont(fieldFont);
        nama.setFont(fieldFont);
        harga.setFont(fieldFont);
        stok.setFont(fieldFont);

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

        JButton upload = createModernButton("📁 Upload Gambar", SECONDARY_COLOR);
        upload.setPreferredSize(new Dimension(150, 30));

        upload.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                img[0] = resizeImage(path, 150, 120);
            }
        });

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Kode:")); form.add(kode);
        form.add(new JLabel("Nama:")); form.add(nama);
        form.add(new JLabel("Harga:")); form.add(harga);
        form.add(new JLabel("Stok:")); form.add(stok);
        form.add(new JLabel("Gambar:")); form.add(upload);

        JButton add = createModernButton("➕ Input", SUCCESS_COLOR);
        JButton update = createModernButton("✏️ Update", ACCENT_COLOR);
        JButton delete = createModernButton("🗑️ Delete", DANGER_COLOR);

        add.setPreferredSize(new Dimension(110, 35));
        update.setPreferredSize(new Dimension(110, 35));
        delete.setPreferredSize(new Dimension(110, 35));

        add.addActionListener(e -> {
            try {
                int stokValue = Integer.parseInt(stok.getText());
                String hargaText = harga.getText();

                if (!hargaText.matches("[0-9.]+")){
                    throw new NumberFormatException("Harga Tidak Valid");
                }

                katalogModel.addRow(new Object[]{
                        kode.getText(), nama.getText(), hargaText, stokValue, img[0]
                });

                refreshHome();
                refreshKatalog();
                saveKatalogToJson();
                JOptionPane.showMessageDialog(panel, "✅ Data berhasil ditambahkan");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                        "❌ Harga dan stok HARUS angka!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                try {
                    int stokValue = Integer.parseInt(stok.getText());
                    String hargaText = harga.getText();

                    if (!hargaText.matches("^[0-9.]+$")) {
                        throw new NumberFormatException("Harga tidak valid");
                    }

                    katalogModel.setValueAt(kode.getText(), r, 0);
                    katalogModel.setValueAt(nama.getText(), r, 1);
                    katalogModel.setValueAt(hargaText, r, 2);
                    katalogModel.setValueAt(stokValue, r, 3);
                    katalogModel.setValueAt(img[0], r, 4);

                    refreshHome();
                    refreshKatalog();
                    saveKatalogToJson();
                    JOptionPane.showMessageDialog(panel, "✅ Data berhasil diupdate");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel,
                            "❌ Harga dan stok HARUS berupa ANGKA!", "Update Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "⚠️ Pilih data terlebih dahulu");
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                int confirm = JOptionPane.showConfirmDialog(panel,
                        "Yakin ingin menghapus data ini?", "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    katalogModel.removeRow(r);
                    refreshHome();
                    refreshKatalog();
                    saveKatalogToJson();
                    JOptionPane.showMessageDialog(panel, "✅ Data berhasil dihapus");
                }
            } else {
                JOptionPane.showMessageDialog(panel, "⚠️ Pilih data terlebih dahulu");
            }
        });

        JPanel btn = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));
        btn.add(add);
        btn.add(update);
        btn.add(delete);

        panel.add(form, BorderLayout.NORTH);
        panel.add(btn, BorderLayout.SOUTH);

        return panel;
    }

    /* ================= HELPER METHODS ================= */
    private static void saveKatalogToJson() {
        java.util.List<Produk> list = new java.util.ArrayList<>();

        for (int i = 0; i < katalogModel.getRowCount(); i++) {
            ImageIcon icon = (ImageIcon) katalogModel.getValueAt(i, 4);

            list.add(new Produk(
                    katalogModel.getValueAt(i, 0).toString(),
                    katalogModel.getValueAt(i, 1).toString(),
                    katalogModel.getValueAt(i, 2).toString(),
                    (int) katalogModel.getValueAt(i, 3),
                    icon.getDescription()
            ));
        }

        JsonStorage.saveKatalog(list);
    }

    private static ImageIcon resizeImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resized = new ImageIcon(img);
        resized.setDescription(path);
        return resized;
    }
}