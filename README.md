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

# Module 3 Reflection

Makna setiap prinsip SOLID:

1. S (Single Responsibility Principle / SRP): Satu kelas sebaiknya punya satu tanggung jawab utama. Kalau sebuah kelas mengurus banyak hal, kelas itu jadi sulit dirawat dan mudah rusak saat ada perubahan.
2. O (Open/Closed Principle / OCP): Jika mau tambah fitur, idealnya programmer menambah kelas/komponen baru tanpa merusak kode yang sudah stabil.
3. L (Liskov Substitution Principle / LSP): Jika B adalah turunan dari A, maka objek B harus bisa menggantikan A tanpa merusak perilaku program. Subclass tidak boleh mengubah perilaku yang diharapkan dari superclass.
4. I (Interface Segregation Principle / ISP): Lebih baik banyak interface kecil dan spesifik daripada satu interface besar yang memaksa kelas mengimplementasikan method yang tidak dibutuhkan.
5. D (Dependency Inversion Principle / DIP): Modul level tinggi (misalnya controller) tidak sebaiknya bergantung langsung pada detail implementasi level rendah (misalnya repository). Keduanya sebaiknya bergantung pada abstraksi (interface), sehingga mudah diganti dan mudah di-test.


1) Prinsip apa saja yang diterapkan pada project ini

a) SRP (Single Responsibility Principle)

Saya memisahkan tanggung jawab menjadi beberapa layer:
- Controller: mengurus request/response web dan view (ProductController, CarController).Service: mengurus logika bisnis (CarService, CarServiceImpl).
- Repository: mengurus penyimpanan data dan operasi CRUD (CarRepository).
- Model: representasi data (Product, Car).

Ini membuat tiap komponen fokus pada satu hal.

b) DIP (Dependency Inversion Principle)

Saya membuat abstraksi CarService (interface).

CarServiceImpl mengimplementasikan CarService.
Secara konsep, ini mengarahkan agar komponen pemakai (misalnya controller) bergantung pada interface, bukan implementasi detail.

c) OCP (Open/Closed Principle)

Dengan adanya layer service dan repository, ketika ingin menambah fitur (misalnya validasi harga, filter list, atau integrasi database sungguhan), kode bisa diperluas dengan menambah implementasi/logic di service atau mengganti repository, tanpa mengubah controller terlalu banyak.

d) ISP (Interface Segregation Principle)

Interface CarService yang isinya hanya method yang relevan untuk fitur mobil.
Jadi class yang butuh car-related behavior tidak dipaksa pakai method yang tidak relevan.

e) LSP (Liskov Substitution Principle)

Di awal sempat ada masalah karena CarController extends ProductController dan mapping bentrok.
Solusi yang dipakai adalah memisahkan CarController agar tidak mewarisi ProductController.
Ini menghindari masalah substitusi dan perilaku tak terduga, karena controller produk dan controller mobil punya kontrak route berbeda.

2) Keuntungan menerapkan SOLID pada project ini

a) Kode lebih mudah dipahami dan dirawat (SRP)

Contoh: Sebelum refactor, logic car bisa campur dengan logic product atau bahkan mapping conflict.
Sesudah refactor, kalau error terjadi saat delete car, hanya fokus cek di:
- route @PostMapping("/delete/{id}") di CarController
- method deleteCarById di service
- delete di repository

Jadi debugging lebih cepat karena alurnya jelas.

b) Mudah menambah fitur tanpa merusak fitur lain (OCP)

Contoh: Kalau nanti mau menambahkan field baru seperti carBrand atau aturan validasi carPrice tidak boleh negatif:
cukup menambah logic di layer service (misalnya validasi sebelum create/update).

Controller dan repository tidak harus diubah banyak.

c) Lebih mudah diuji (DIP + SRP)

Contoh: Dengan CarService sebagai interface, seharusnya saya bisa membuat mock service untuk test controller tanpa benar-benar menyentuh repository list.
Ini juga membantu menghindari error test yang sebelumnya terjadi karena flow view atau template.

d) Mengurangi bug route tabrakan dan design yang rawan (LSP, desain yang lebih aman)

Contoh:
Saat CarController mewarisi ProductController, muncul error: Ambiguous mapping untuk /car/edit/{id}. Setelah dipisahkan, mapping jadi jelas dan app bisa bootRun normal.

e) Struktur project lebih scalable

Contoh: Sekarang, saya punya pola yang bisa di-copy untuk entitas lain (misalnya Book, Motor, dll):
- buat model
- buat repository
- buat interface service + implementation
- buat controller + template

Ini bikin development lebih cepat dan konsisten.

3) Kerugian jika tidak menerapkan SOLID

a) Sulit maintenance karena satu file mengurus semuanya (melanggar SRP)

Contoh: Kalau controller juga mengurus penyimpanan list, generate id, update data, dan render view, perubahan kecil bisa memicu banyak bug.
Misalnya menambah field carPrice bisa bikin create, edit, list, dan test semuanya error sekaligus.

b) Mudah terjadi konflik dan perilaku tidak konsisten (melanggar LSP)

Contoh: CarController extends ProductController membuat mapping edit bentrok dan aplikasi gagal start.
Ini bikin user tidak bisa akses /car/listCar sama sekali karena server tidak jalan.

c) Testing jadi susah dan fragile (melanggar DIP)

Contoh: Jika controller selalu bergantung pada implementasi konkrit dan detail penyimpanan, test jadi sulit karena harus benar-benar menjalankan semua komponen.
Akibatnya, perubahan kecil di view atau flow bisa bikin banyak test gagal.

d) Sulit menambah fitur tanpa mengubah banyak bagian (melanggar OCP)

Contoh: Kalau semua logic ditaruh di satu tempat, menambah fitur seperti “filter car by color” bisa memaksa kamu mengubah banyak method lama. Ini meningkatkan risiko regression bug.

e) Kode jadi “besar dan gemuk” (melanggar ISP)

Contoh: Kalau ada satu interface/service besar untuk semua hal (product + car + lainnya), nanti class yang hanya butuh sebagian fitur tetap dipaksa implement banyak method yang tidak dipakai.