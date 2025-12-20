package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class KeranjangStorage {

    private static final String FILE = "keranjang_user.json";

    public static void save(DefaultTableModel model) {
        try (Writer writer = new FileWriter(FILE)) {

            List<KeranjangItem> list = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                list.add(new KeranjangItem(
                        model.getValueAt(i, 0).toString(),
                        model.getValueAt(i, 1).toString(),
                        model.getValueAt(i, 2).toString(),
                        (int) model.getValueAt(i, 3),
                        ((javax.swing.ImageIcon) model.getValueAt(i, 4)).getDescription()
                ));
            }

            new Gson().toJson(list, writer);

        } catch (IOException e) {
            System.out.println("Gagal menyimpan keranjang");
        }
    }

    public static void load(DefaultTableModel model) {
        File file = new File(FILE);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {

            Type type = new TypeToken<List<KeranjangItem>>(){}.getType();
            List<KeranjangItem> list = new Gson().fromJson(reader, type);

            model.setRowCount(0);

            for (KeranjangItem item : list) {
                javax.swing.ImageIcon icon =
                        new javax.swing.ImageIcon(item.gambarPath);
                icon.setDescription(item.gambarPath);

                model.addRow(new Object[]{
                        item.kode, item.nama, item.harga, item.jumlah, icon
                });
            }

        } catch (Exception e) {
            System.out.println("Gagal load keranjang");
        }
    }
}