**Aplikasi Belanja Gadget: Amba Cell**

Java Desktop Application (Swing + JSON)

**Deskripsi Singkat**

Aplikasi ini adalah aplikasi desktop berbasis Java Swing yang mensimulasikan sistem toko sederhana.
Pengguna dapat melihat katalog produk, menambahkan produk ke keranjang belanja, serta menyimpan data secara lokal menggunakan file JSON.

Aplikasi ini tidak menggunakan database, sehingga seluruh data tetap tersimpan walaupun aplikasi ditutup.

**Tools yang Digunakan**

Java JDK 8 atau lebih baru

Java Swing (GUI Desktop)

Gson (JSON Parser)

IDE: IntelliJ IDEA 

**Cara Menjalankan Program**

Buka project di IntelliJ IDEA

Pastikan file class utama (yang memiliki main()) sudah benar

Klik tombol Run ▶

Aplikasi akan tampil dalam bentuk GUI Desktop dan bisa dijalankan.

**Struktur File JSON**

katalog.json >	Menyimpan data produk

keranjang_user.json>	Menyimpan isi keranjang

history.json	> Menyimpan riwayat transaksi

**Penjelasan Halaman Aplikasi & Fitur**

**1. Halaman Home**

Halaman awal saat aplikasi dijalankan.

Isi halaman:

Banner yang berisi nama aplikasi dan slogan

Navigasi ke halaman lain

Login / Logout user

**2. Halaman Katalog Produk**

Menampilkan seluruh produk yang tersedia.

Isi halaman:

Menampilkan nama, harga, stok, dan gambar produk

Sorting harga (termurah / termahal)

Pencarian produk berdasarkan nama

Tombol tambah ke keranjang (jika stok tersedia)

**3. Halaman Keranjang Belanja**

Menampilkan produk yang sudah dipilih pengguna.

Isi halaman:

Menampilkan produk dalam bentuk tabel

Menampilkan gambar produk di tabel

Menyimpan isi keranjang secara otomatis ke JSON

Keranjang akan dimuat kembali saat aplikasi dibuka

Keranjang akan dikosongkan saat logout

**4. Halaman Riwayat Transaksi**

Menyimpan dan menampilkan transaksi yang telah dilakukan.

Isi halaman:

Menampilkan daftar transaksi

Data disimpan di history.json

Riwayat tetap tersedia meskipun aplikasi ditutup

**5. Login & Logout**

Mengatur sesi pengguna.

Isi halaman:

Login user

Logout menghapus sesi

Logout otomatis mengosongkan keranjang
