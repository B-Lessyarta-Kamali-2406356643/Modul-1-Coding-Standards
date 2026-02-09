# Module 1 Reflection

Pada modul ini saya mengimplementasikan dua fitur baru pada aplikasi Spring Boot, yaitu:
1. Edit Product: menampilkan halaman edit berdasarkan productId dan menyimpan perubahan data produk.
2. Delete Product: menghapus produk berdasarkan productId melalui endpoint POST.

## Penerapan Clean Code Principles:
- Struktur kode yang rapih:

a) Controller hanya mengelola request/response dan pemilihan view

b) Service menjadi penghubung logika aplikasi

c) Repository fokus pada operasi data

Pemisahan ini membuat kode lebih mudah dibaca dan dipelihara.

- Meaningful names

Saya menambahkan method seperti findById, editProduct, deleteProduct, generateId agar nama method langsung menggambarkan tujuannya.


- Setiap fungsi mempunyai satu tujuan

Setiap method yang saya buat mempunyai satu fokus sendiri, sehingga ada satu method untuk pencarian (findById), satu method untuk membuat ID (generateId), satu method untuk edit product (editProduct), dsb.

## Secure Coding Practices

- Input data validation

Saya memastikan productQuantity tidak negatif.

- HTTP method yang lebih aman (untuk deleteProduct)

Delete dijalankan dengan POST (bukan GET).

## Kesalahan yang bisa diperbaiki

- Validasi quantity

Saat ini quantity negatif selalu diubah menjadi 0 di backend. Untuk UX yang lebih jelas, sebetulnya bisa dibuat mekanisme “ditolak dan kembali ke form dengan pesan error”.
Saya juga mengganti input quantity pada form menjadi type="number` dengan min="0" agar dari sisi user juga lebih sulit memasukkan nilai negatif. Ini meningkatkan pengalaman user sekaligus mengurangi input invalid, walaupun validasi server tetap wajib.
