# 📌 Tasklist Pelaksanaan: Automation Testing magang.dikahadir.com (100 Test Cases: Deny Admin & Aji User)

Roadmap pengujian otomatisasi berbasis **Selenium WebDriver 4 (Java 21)**, **REST Assured 5**, dan **TestNG** dengan arsitektur enterprise sesuai acuan `reference.md`. Memuat 100 skenario SIT yang dibagi secara tegas antara **Tester Deny (Admin HRD - 50 TCs)** dan **Tester Aji (User Magang - 50 TCs)** dengan tabel UI dan API yang dipisahkan dalam setiap sheet.

---

## 🧭 Ikhtisar Status 7 Fase & Alokasi Beban Kerja

| Fase | Deskripsi Fase | Output Deliverables | Alokasi Penanggung Jawab | Status |
| :---: | :--- | :--- | :---: | :---: |
| **Fase 1** | Discovery, Inspeksi Sub-Menu & Matriks SIT (Tabel UI & API Terpisah) | `docs/SIT_Scenario_Magang_Hadir.xlsx` (100 TCs) | Deny (50 TCs) & Aji (50 TCs) | ✅ Selesai (Completed) |
| **Fase 2** | Maven Setup & Struktur Multi-Tester Package | `pom.xml`, `pages/admin`, `pages/user` | Shared Foundation (Git) | ✅ Selesai (Completed) |
| **Fase 3** | Core Thread-Safe Engine & Keamanan Base64 | `SecurityUtil`, `DriverFactory`, `BaseApiTest` | Shared Foundation | ✅ Selesai (Completed) |
| **Fase 4** | Page Object Model (POM) Implementation | Admin POMs (7 Pages) & User POMs (10 Pages) | Deny (Admin) & Aji (User) | 🚀 Siap Dikerjakan (Ready) |
| **Fase 5** | Automated Tests Implementation (UI & REST Assured) | `tests/admin` (50 TCs) & `tests/user` (50 TCs) | Deny (50 TCs) & Aji (50 TCs) | ⏳ Pending |
| **Fase 6** | TestNG Suite Configuration (`testng-deny`, `testng-aji`) | `testng.xml`, `testng-deny.xml`, `testng-aji.xml` | Shared Execution / Multi-Device | ⏳ Pending |
| **Fase 7** | ExtentReports Dashboard Validation & Walkthrough | `ExtentReport.html`, `walkthrough.md` | Shared Deliverable | ⏳ Pending |

---

## 🌐 SOP Kolaborasi Multi-Device / Multi-Agent AI

Berikut tata cara pembagian dan sinkronisasi kerja jika pengujian dikerjakan pada 2 device berbeda menggunakan Agent AI:

| Aspek Kolaborasi | Device 1 (Tester Deny) | Device 2 (Tester Aji) |
| :--- | :--- | :--- |
| **Peran SDET** | **ADMIN (Web HRD)** | **USER (Mobile Mirroring)** |
| **Git Branch** | `feature/tester-deny-admin` | `feature/tester-aji-user` |
| **Target URL** | `https://magang.dikahadir.com/authentication/login` | `https://magang.dikahadir.com/absen/login` |
| **Kredensial** | `admin@hadir.com` (`Constants.ADMIN_PASSWORD_ENCODED`) | `hadirsqa1@gmail.com` (`Constants.USER_PASSWORD_ENCODED`) |
| **Scope Package POM** | `com.juaracoding.magang.pages.admin.*` | `com.juaracoding.magang.pages.user.*` |
| **Scope Package Test**| `com.juaracoding.magang.tests.admin.*` & `api.admin.*` | `com.juaracoding.magang.tests.user.*` & `api.user.*` |
| **Perintah Eksekusi** | `mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml` | `mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml` |
| **Proses Penggabungan**| Pull Request ke `main` setelah seluruh 50 TCs pass | Pull Request ke `main` setelah seluruh 50 TCs pass |
| **Master Verification**| Di branch `main`: `mvn clean test` (menjalankan 100 TCs sekaligus via `testng.xml`) |

---

## 📋 Rincian Tugas per Fase

### 🔹 Fase 1: Discovery, Inspeksi Sub-Menu & SIT Multi-Sheet (.xlsx)
- [x] **1.1. Inspeksi Seluruh Sub-Menu & Endpoint API**
  - [x] Analisis 9 Sub-menu Laporan Admin, 4 Sub-menu Import, 13 Sub-menu Management.
  - [x] Analisis 9 Fitur Utama Mobile User (Presensi, Riwayat, Koreksi, Izin, Lembur, Cuti, Sakit, Profil).
- [x] **1.2. Pembuatan Dokumen SIT Multi-Sheet Terpisah (Total 100 Test Cases)**
  - [x] **Sheet 1 (`Summary & Multi-Device Guide`)**: Panduan alur kerja multi-device Git, ringkasan beban kerja.
  - [x] **Sheet 2 (`Tester Deny - Admin HRD`)**:
    - **Tabel A (Web UI Selenium)**: 35 Skenario Uji UI Admin (SIT_ADM_UI_001 s/d 035).
    - **Tabel B (REST Assured API)**: 15 Skenario Uji API Admin (SIT_ADM_API_001 s/d 015).
  - [x] **Sheet 3 (`Tester Aji - User Magang`)**:
    - **Tabel A (Web UI Selenium)**: 35 Skenario Uji UI User (SIT_USR_UI_001 s/d 035).
    - **Tabel B (REST Assured API)**: 15 Skenario Uji API User (SIT_USR_API_001 s/d 015).
  - [x] **Sheet 4 (`Master Matrix - UI Scenarios`)**: 70 Skenario Web UI terpadu.
  - [x] **Sheet 5 (`Master Matrix - API Scenarios`)**: 30 Skenario REST Assured terpadu.

---

### 🔹 Fase 2: Inisialisasi Proyek & Konfigurasi Maven (Java 21)
- [x] **2.1. Konfigurasi `pom.xml` Terpadu**
  - [x] Java 21 compiler configuration (`source` & `target` 21).
  - [x] Selenium WebDriver terbaru (`4.29.0`).
  - [x] REST Assured (`5.4.0`), `json-path`, `json-schema-validator`.
  - [x] TestNG (`7.10.2`) & ExtentReports 5 (`5.1.2`).
  - [x] Apache POI (`5.2.5`) & `poi-ooxml` untuk file Excel SIT.
  - [x] Maven Surefire Plugin (`3.5.0`) pendukung parallel runner & multi-suite XML.
- [x] **2.2. Pembuatan Struktur Folder Multi-Tester**
  - [x] `src/main/java/com/juaracoding/magang/pages/admin/` (Modul Tester Deny)
  - [x] `src/main/java/com/juaracoding/magang/pages/user/` (Modul Tester Aji)
  - [x] `src/test/java/com/juaracoding/magang/tests/admin/`
  - [x] `src/test/java/com/juaracoding/magang/tests/user/`
  - [x] `src/test/java/com/juaracoding/magang/api/{admin,user}/`

---

### 🔹 Fase 3: Core Framework Engine & Keamanan Kredensial (Base64)
- [x] **3.1. Implementasi Keamanan Kredensial (`SecurityUtil.java`)**
  - [x] Method `decode(String base64)` dan `encode(String plain)` berbasis `java.util.Base64`.
  - [x] Encoding password Admin: `MagangSQA_JC@123` -> `TWFnYW5nU1FBX0pDQDEyMw==`
  - [x] Encoding password User: `SQA@Hadir12345` -> `U1FBQEhhZGlyMTIzNDU=`
- [x] **3.2. Implementasi `Constants.java`**
  - [x] URL Admin, URL User, API base URI, kredensial ter-encode, path folder laporan.
- [x] **3.3. Implementasi `DriverFactory.java`**
  - [x] ThreadLocal isolation untuk `WebDriver`.
  - [x] Mode `headless = true` secara default dengan argument W3C terkini.
- [x] **3.4. Implementasi `BaseApiTest.java` (REST Assured Engine)**
  - [x] Inisialisasi `baseURI`, RequestSpecification, logging filter.
- [x] **3.5. Implementasi `ExtentManager.java`, `ExtentTestManager.java`, `TestListener.java`**
  - [x] ExtentSparkReporter bertema Dark Theme dengan tagging kategori tester (`Tester Deny` / `Tester Aji`).
  - [x] Penangkapan otomatis screenshot Base64 saat test gagal.

---

### 🔹 Fase 4: Page Object Model (POM) Implementation
#### 👤 Modul Halaman Tester Deny (Admin Web HRD):
- [ ] **4.1. `AdminLoginPage.java`**: Form login admin, alert, proteksi sesi.
- [ ] **4.2. `AdminDashboardPage.java`**: Widget metrik approval, statistik absensi, menu sidebar.
- [ ] **4.3. `AdminReportsPage.java`**: Laporan Semua, Kehadiran, Izin Terlambat, Pulang Cepat, Sakit.
- [ ] **4.4. `AdminApprovalPage.java`**: Approval Koreksi Absen, Approval Cuti, Approval Lembur.
- [ ] **4.5. `AdminDownloadPage.java`**: Download laporan kehadiran, cuti, lembur format Excel.
- [ ] **4.6. `AdminImportPage.java`**: Halaman template dan upload import status aktif & user.
- [ ] **4.7. `AdminManagementPage.java`**: Search user NIK, toggle status user, divisi, shifting.

#### 👤 Modul Halaman Tester Aji (User Mobile Mirroring):
- [ ] **4.8. `UserLoginPage.java`**: Form login magang, alert, toggle show/hide password.
- [ ] **4.9. `RegisterPage.java`**: Form pendaftaran NIK Excel, upload selfie dummy.
- [ ] **4.10. `ResetPasswordPage.java`**: Form permohonan reset password & link login.
- [ ] **4.11. `AttendancePage.java`**: Presensi masuk (WFO/WFH), izin kamera, presensi pulang.
- [ ] **4.12. `ActivityHistoryPage.java`**: List kartu riwayat absensi & filter tanggal.
- [ ] **4.13. `CorrectionPage.java`**: Form pengajuan koreksi jam absensi.
- [ ] **4.14. `PermitPage.java`**: Form pengajuan izin terlambat & izin sakit dengan surat dokter.
- [ ] **4.15. `LeavePage.java`**: Form pengajuan cuti tahunan & validasi saldo cuti.
- [ ] **4.16. `OvertimePage.java`**: Form permohonan lembur kerja.
- [ ] **4.17. `UserProfilePage.java`**: Halaman profil, download rekap personal, ubah password.

---

### 🔹 Fase 5: Implementasi Automated Test Suites (Selenium UI & REST Assured)
#### 👤 Skenario Pengujian Tester Deny (Total: 50 Test Cases):
- [ ] **5.1. `AdminLoginTest.java`** (5 Test Cases: SIT_ADM_UI_001 s/d 005)
- [ ] **5.2. `AdminDashboardTest.java`** (3 Test Cases: SIT_ADM_UI_006 s/d 008)
- [ ] **5.3. `AdminReportsTest.java`** (14 Test Cases: SIT_ADM_UI_009 s/d 022)
- [ ] **5.4. `AdminDownloadTest.java`** (3 Test Cases: SIT_ADM_UI_023 s/d 025)
- [ ] **5.5. `AdminImportTest.java`** (2 Test Cases: SIT_ADM_UI_026 s/d 027)
- [ ] **5.6. `AdminManagementTest.java`** (8 Test Cases: SIT_ADM_UI_028 s/d 035)
- [ ] **5.7. `AdminApiTest.java`** (15 Test Cases API: SIT_ADM_API_001 s/d 015)

#### 👤 Skenario Pengujian Tester Aji (Total: 50 Test Cases):
- [ ] **5.8. `UserLoginTest.java`** (6 Test Cases: SIT_USR_UI_001 s/d 006)
- [ ] **5.9. `RegisterTest.java`** (4 Test Cases: SIT_USR_UI_007 s/d 010)
- [ ] **5.10. `ResetPasswordTest.java`** (3 Test Cases: SIT_USR_UI_011 s/d 013)
- [ ] **5.11. `AttendanceTest.java`** (5 Test Cases: SIT_USR_UI_014 s/d 018)
- [ ] **5.12. `ActivityHistoryTest.java`** (3 Test Cases: SIT_USR_UI_019 s/d 021)
- [ ] **5.13. `CorrectionTest.java`** (2 Test Cases: SIT_USR_UI_022 s/d 023)
- [ ] **5.14. `PermitTest.java`** (5 Test Cases: SIT_USR_UI_024 s/d 028)
- [ ] **5.15. `LeaveAndOvertimeTest.java`** (4 Test Cases: SIT_USR_UI_029 s/d 032)
- [ ] **5.16. `UserProfileTest.java`** (3 Test Cases: SIT_USR_UI_033 s/d 035)
- [ ] **5.17. `UserApiTest.java`** (15 Test Cases API: SIT_USR_API_001 s/d 015)

---

### 🔹 Fase 6: Konfigurasi Suite Runner & Verifikasi Headless
- [ ] **6.1. Pembuatan Multi-Suite XML Runner**
  - [ ] `src/test/resources/testng.xml`: Master Runner (100 test cases).
  - [ ] `src/test/resources/testng-deny.xml`: Runner khusus Tester Deny (Admin HRD - 50 test cases).
  - [ ] `src/test/resources/testng-aji.xml`: Runner khusus Tester Aji (User Magang - 50 test cases).
  - [ ] Seluruh suite dikonfigurasi dengan parameter `<parameter name="headless" value="true" />`.
- [ ] **6.2. Verifikasi Eksekusi Terpisah dan Terpadu**
  - [ ] Device 1: `mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml`
  - [ ] Device 2: `mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml`
  - [ ] Master Integration: `mvn clean test`

---

### 🔹 Fase 7: Validasi ExtentReports, Sinkronisasi SIT & Finalisasi
- [ ] **7.1. Validasi Laporan Interaktif `reports/ExtentReport.html`**
  - [ ] Dashboard memuat kategori `Tester Deny (Admin)` dan `Tester Aji (User)`.
  - [ ] Evidence Base64 screenshot otomatis tertanam pada failure steps.
- [ ] **7.2. Sinkronisasi Dokumen SIT Multi-Sheet Excel**
  - [ ] Pastikan file `docs/SIT_Scenario_Magang_Hadir.xlsx` memuat status hasil eksekusi aktual.
- [ ] **7.3. Sinkronisasi Tasklist & Dokumentasi Walkthrough**
  - [ ] Sinkronisasi `tasklist.md` di kedua lokasi file.
  - [ ] Penyusunan dokumen `walkthrough.md` untuk panduan tim penguji.


---

## 📖 Panduan Praktis Langkah demi Langkah (Step-by-Step Guide) Menjalankan Test UI dan API untuk Tester

Panduan ini ditujukan bagi **Tester Deny (Admin HRD)** dan **Tester Aji (User Magang)** maupun tim penguji yang menjalankan otomasi baik di terminal lokal, IDE (VS Code / IntelliJ), maupun multi-device environment.

### ⚙️ 1. Persiapan Awal (Prerequisites Check)
Pastikan lingkungan lokal telah terpasang:
1. **Java Development Kit (JDK 21 LTS)**:
   ```powershell
   java -version
   # Output harus menunjukkan: java version "21.x.x"
   ```
2. **Apache Maven (3.9+)**:
   ```powershell
   mvn -version
   # Output harus menunjukkan: Apache Maven 3.9.x
   ```
3. **Google Chrome Browser**: Versi terbaru (Driver otomatis dikelola oleh Selenium Manager).

---

### 🌐 2. Langkah demi Langkah Pengujian API (REST Assured)

Pengujian API memvalidasi logika bisnis backend dan status respons data secara langsung tanpa antarmuka grafis.

#### Langkah 1: Memahami Alur Kredensial & Autentikasi
- Password tersimpan dalam bentuk aman Base64 di kelas `Constants.java`.
- Di runtime, `SecurityUtil.decode()` akan membuka password secara aman langsung di memori untuk dikirim ke endpoint `/api/login`.
- REST Assured akan menangkap `accessToken` (JWT) dan menyematkannya pada header request berikutnya:
  `Authorization: Bearer <token>`

#### Langkah 2: Menjalankan Test API Tertentu (Single Class Execution)
Gunakan perintah Maven berikut di terminal:
- **Untuk Tester Deny (Admin API Test)**:
  ```powershell
  # Menjalankan smoke test API Admin
  mvn test -Dtest=AdminSmokeApiTest
  ```
- **Untuk Tester Aji (User API Test)**:
  ```powershell
  # Menjalankan smoke test API User Magang
  mvn test -Dtest=UserSmokeApiTest
  ```

#### Langkah 3: Menjalankan Test API via Runner Suite Khusus
- **Tester Deny (Seluruh Scope Admin)**:
  ```powershell
  mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml
  ```
- **Tester Aji (Seluruh Scope User)**:
  ```powershell
  mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml
  ```

#### Langkah 4: Membaca & Memvalidasi Hasil di Terminal
- Terminal akan menampilkan status eksekusi:
  - `Status Code Response: 200` atau `201` untuk skenario positif.
  - `Status Code Response: 400`, `401`, atau `422` untuk skenario negatif (misal: password salah, endpoint terproteksi tanpa token).
  - Indikator `PASSED` jika semua asersi (`Assert.assertEquals`, `Assert.assertTrue`) terpenuhi.

---

### 🖥️ 3. Langkah demi Langkah Pengujian Web UI (Selenium WebDriver)

Pengujian Web UI mengotomasi interaksi browser Chrome secara end-to-end.

#### Langkah 1: Memilih Mode Eksekusi (Headless vs Visual/Headed)
- **Mode Background (Headless - Default)**:
  Browser tidak akan muncul di layar (hemat memori dan cepat, cocok untuk background testing):
  ```powershell
  mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml
  ```
- **Mode Visual / Peragaan Langsung (Headed - Browser Terbuka)**:
  Browser Chrome akan terbuka secara visual sehingga Anda dapat melihat form terisi otomatis dan tombol diklik:
  ```powershell
  mvn test -Dheadless=false -DsuiteXmlFile=src/test/resources/testng-deny.xml
  ```

#### Langkah 2: Menjalankan Test UI Tertentu
- **Tester Deny (Admin UI Login & Dashboard)**:
  ```powershell
  mvn test -Dtest=AdminSmokeUiTest -Dheadless=false
  ```
- **Tester Aji (User UI Mobile Mirroring)**:
  ```powershell
  mvn test -Dtest=UserSmokeUiTest -Dheadless=false
  ```

#### Langkah 3: Memantau Alur Uji di Layar
1. Chrome akan terbuka dan memaksimalkan jendela (*maximize window*).
2. URL tujuan akan dimuat secara otomatis (`/authentication/login` untuk Admin, `/absen/login` untuk User).
3. Field input email dan password akan diisi secara otomatis.
4. Tombol *Login* akan ditekan.
5. Verifikasi URL dan alert akan dicek secara instan oleh sistem.
6. Browser akan tertutup secara otomatis setelah pengujian selesai.

---

### 📊 4. Langkah demi Langkah Membuka & Memeriksa Laporan Pengujian

Setiap kali pengujian selesai dijalankan, framework secara otomatis merangkum seluruh hasil ke dalam laporan interaktif dan matriks SIT.

#### Langkah 1: Membuka Dashboard Laporan HTML (ExtentReports 5)
1. Buka file explorer dan masuk ke direktori:
   `c:\Users\Asus\Documents\JuaraCoding\Latihan\proyek\day31\reports\`
2. Klik ganda file `ExtentReport.html` (atau buka menggunakan Chrome / Edge).
3. Anda akan melihat:
   - **Pie Chart**: Persentase kelulusan test (*Passed, Failed, Skipped*).
   - **Filter Kategori**: Klik tag `Tester Deny (Admin HRD)` atau `Tester Aji (User Magang)` untuk memfilter pengujian masing-masing.
   - **Filter Tipe Test**: Klik tag `REST Assured API` atau `Selenium Web UI`.
   - **Bukti Cacat (Failure Evidence)**: Jika ada test yang gagal, tangkapan layar (screenshot) Base64 akan otomatis tertanam pada detail langkah uji yang bersangkutan.

#### Langkah 2: Memeriksa dan Memperbarui Dokumen SIT Spreadsheet
1. Buka dokumen matriks pengujian di:
   `c:\Users\Asus\Documents\JuaraCoding\Latihan\proyek\day31\docs\SIT_Scenario_Magang_Hadir.xlsx`
2. Buka sheet masing-masing:
   - Tester Deny membuka sheet `Tester Deny (Admin HRD)` (Tabel A untuk UI, Tabel B untuk API).
   - Tester Aji membuka sheet `Tester Aji (User Magang)` (Tabel A untuk UI, Tabel B untuk API).
3. Bandingkan Test Case ID yang lulus dengan log ExtentReports, lalu tandai kolom status (Pass/Fail) sesuai hasil eksekusi aktual.

---

### 🚀 5. Ringkasan Cheat Sheet Perintah Eksekusi Cepat

| Tujuan Eksekusi | Perintah Maven Terminal |
| :--- | :--- |
| **Compile & Validasi Dependensi** | `mvn clean test-compile` |
| **Jalankan Seluruh Test Deny (Admin UI + API)** | `mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml` |
| **Jalankan Seluruh Test Aji (User UI + API)** | `mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml` |
| **Jalankan Test UI Secara Visual (Browser Muncul)** | `mvn test -Dheadless=false -Dtest=AdminSmokeUiTest` |
| **Jalankan Seluruh Master Suite (100 Test Cases)** | `mvn clean test` |
