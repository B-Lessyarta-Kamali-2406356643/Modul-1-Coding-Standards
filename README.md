# Module 1 Reflection 1

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

# Module 1 Reflection 2

1. Setelah menulis unit test, saya merasa lebih yakin terhadap perilaku kode karena setiap fungsi penting diuji dengan skenario yang jelas. 
Jumlah unit test dalam satu class saya tidak ditentukan oleh angka tertentu, tetapi saya membuat satu untuk kondisi normal, satu untuk edge case, dan satu untuk kondisi gagal. 
Untuk memastikan unit test sudah “cukup”, saya biasanya memetakan requirement/fitur ke test case (misalnya create, find, edit, delete), lalu menambahkan test untuk kondisi batas (input kosong, ID tidak ditemukan, quantity 0, dan sebagainya). 
Code coverage membantu saya melihat bagian mana yang belum tersentuh oleh test. 
Memiliki 100% coverage tidak otomatis berarti kode bebas bug, karena masih ada kemungkinan kesalahan logika, skenario yang belum terpikirkan, masalah integrasi, concurrency, konfigurasi environment, atau bug yang muncul dari interaksi antar komponen yang tidak diuji.

2. Jika setelah membuat CreateProductFunctionalTest.java saya diminta membuat functional test suite baru untuk memverifikasi jumlah item pada product list, 
dan saya menyalin setup prosedur serta instance variables yang sama (serverPort, baseUrl, anotasi SpringBootTest dan SeleniumJupiter), 
maka dari sisi clean code hal ini berpotensi menurunkan kualitas karena terjadi duplikasi kode. 
Duplikasi membuat maintenance lebih sulit: saat ada perubahan pada setup (misalnya cara membentuk baseUrl, selector elemen, atau endpoint), saya harus mengubah banyak file test dan berisiko ada yang tertinggal. 
Selain itu, penamaan variabel dan hard-coded selector juga bisa membuat test rapuh ketika HTML berubah sedikit.
Perbaikan yang bisa dilakukan agar lebih bersih adalah melakukan refactor dengan membuat kelas dasar/utility untuk shared setup, 
misalnya BaseFunctionalTest yang menyimpan serverPort, testBaseUrl, baseUrl, serta method helper seperti openCreatePage(), openListPage(), dan helper untuk mengisi form. 
Dengan begitu test suite baru tinggal fokus pada skenario yang diuji (misalnya menghitung jumlah item) tanpa mengulang boilerplate yang sama.

# Module 2 Reflection
1. Code quality issues yang diperbaiki + strategi
Selama exercise, saya menambahkan PMD sebagai tool code analysis dan menemukan beberapa isu sederhana tapi penting terkait kebersihan kode. 
Isu yang saya perbaiki adalah penggunaan modifier public yang redundant pada method di ProductService (karena method dalam interface sudah otomatis public) dan adanya wildcard import yang tidak dipakai di ProductController (unused import). 
Strategi saya adalah memprioritaskan perbaikan yang tidak mengubah behavior aplikasi, hanya memperbaiki readability dan maintainability. 
Saya juga memastikan setiap perbaikan dilakukan dalam commit terpisah agar perubahan mudah ditinjau, lalu memverifikasi hasilnya dengan menjalankan ulang workflow PMD untuk memastikan isu yang sama tidak muncul lagi. 

2. Apakah sudah memenuhi definisi CI dan CD (min. 3 kalimat)
Menurut saya implementasi saat ini sudah memenuhi definisi Continuous Integration karena setiap push dan pull request otomatis memicu workflow GitHub Actions untuk menjalankan unit tests dan memeriksa kualitas kode (CI), sehingga integrasi perubahan bisa cepat terdeteksi jika ada kegagalan. 
Selain itu, pipeline juga memenuhi aspek Continuous Deployment/Delivery karena codebase saya di-deploy ke Koyeb sehingga perubahan pada branch yang dideploy dapat menghasilkan versi aplikasi yang bisa diakses publik melalui URL. 
Walaupun deployment bisa dianggap “continuous” ketika dilakukan otomatis pada perubahan branch yang terhubung, tetap penting untuk memastikan konfigurasi health check dan port sudah benar agar rilis stabil dan tidak hanya sekadar berhasil build. 
Dengan adanya workflow test dan code analysis sebelum deploy, risiko bug yang masuk ke environment produksi menjadi lebih kecil.

URL Deployment: https://rival-lucina-b-lessyartakamali-2406356643-fc9d62f5.koyeb.app