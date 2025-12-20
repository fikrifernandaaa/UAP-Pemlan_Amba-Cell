package org.example;

class Produk {
    public String kode;
    public String nama;
    public String harga;
    public int stok;
    public String gambarPath; // simpan PATH gambar

    public Produk(String kode, String nama, String harga, int stok, String gambarPath) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.gambarPath = gambarPath;
    }
}