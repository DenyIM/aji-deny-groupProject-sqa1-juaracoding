# 📌 Tasklist Pelaksanaan: Automation Testing magang.dikahadir.com (355 Test Cases: Deny Admin 190 TCs & Aji User 165 TCs)

Roadmap pengujian otomatisasi berbasis **Selenium WebDriver 4 (Java 21)**, **REST Assured 5**, dan **TestNG** dengan arsitektur enterprise sesuai acuan `reference.md`. Memuat **355 skenario SIT** (**240 Web UI** + **115 REST Assured API**) yang dibagi antara **Tester Deny (Admin HRD - 190 TCs)** dan **Tester Aji (User Magang - 165 TCs)** dengan tabel UI dan API yang dipisahkan dalam setiap sheet.

---

## 🧭 Ikhtisar Status 7 Fase & Alokasi Beban Kerja

| Fase | Deskripsi Fase | Output Deliverables | Alokasi Penanggung Jawab | Status |
| :---: | :--- | :--- | :---: | :---: |
| **Fase 1** | Discovery, Inspeksi Sub-Menu & Matriks SIT (Tabel UI & API Terpisah) | `docs/SIT_Scenario_Magang_Hadir.xlsx` (355 TCs) | Deny (190 TCs) & Aji (165 TCs) | ✅ Selesai (Completed) |
| **Fase 2** | Maven Setup & Struktur Multi-Tester Package | `pom.xml`, `pages/admin`, `pages/user` | Shared Foundation (Git) | ✅ Selesai (Completed) |
| **Fase 3** | Core Thread-Safe Engine & Keamanan Base64 | `SecurityUtil`, `DriverFactory`, `BaseApiTest` | Shared Foundation | ✅ Selesai (Completed) |
| **Fase 4** | Page Object Model (POM) Implementation | Admin POMs (7 Pages) & User POMs (10 Pages) | Deny (Admin) & Aji (User) | 🚀 Siap Dikerjakan (Ready) |
| **Fase 5** | Automated Tests Implementation (UI & REST Assured) | `tests/admin` (190 TCs) & `tests/user` (165 TCs) | Deny (190 TCs) & Aji (165 TCs) | ⏳ Pending |
| **Fase 6** | TestNG Suite Configuration (`testng-deny`, `testng-aji`) | `testng.xml`, `testng-deny.xml`, `testng-aji.xml` | Shared Execution / Multi-Device | ⏳ Pending |
| **Fase 7** | ExtentReports Dashboard Validation & Walkthrough | `ExtentReport.html`, `walkthrough.md` | Shared Deliverable | ⏳ Pending |

---

## 🌐 SOP Kolaborasi Multi-Device / Multi-Agent AI

Berikut tata cara pembagian dan sinkronisasi kerja jika pengujian dikerjakan pada 2 device berbeda menggunakan Agent AI:

| Aspek Kolaborasi | Device 1 (Tester Deny) | Device 2 (Tester Aji) |
| :--- | :--- | :--- |
| **Peran SDET** | **ADMIN (Web HRD - 190 TCs)** | **USER (Mobile Mirroring - 165 TCs)** |
| **Git Branch** | `feature/tester-deny-admin` | `feature/tester-aji-user` |
| **Target URL** | `https://magang.dikahadir.com/authentication/login` | `https://magang.dikahadir.com/absen/login` |
| **Kredensial** | `admin@hadir.com` (`Constants.ADMIN_PASSWORD_ENCODED`) | `hadirsqa1@gmail.com` (`Constants.USER_PASSWORD_ENCODED`) |
| **Scope Package POM** | `com.juaracoding.magang.pages.admin.*` | `com.juaracoding.magang.pages.user.*` |
| **Scope Package Test**| `com.juaracoding.magang.tests.admin.*` & `api.admin.*` | `com.juaracoding.magang.tests.user.*` & `api.user.*` |
| **Perintah Eksekusi** | `mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml` | `mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml` |
| **Proses Penggabungan**| Pull Request ke `main` setelah seluruh 190 TCs pass | Pull Request ke `main` setelah seluruh 165 TCs pass |
| **Master Verification**| Di branch `main`: `mvn clean test` (menjalankan 355 TCs sekaligus via `testng.xml`) |

---

## 📋 Rincian Tugas per Fase

### 🔹 Fase 1: Discovery, Inspeksi Sub-Menu & SIT Multi-Sheet (.xlsx)
- [x] **1.1. Inspeksi Seluruh Sub-Menu & Endpoint API**
  - [x] Analisis 9 Sub-menu Laporan Admin, 4 Sub-menu Import, 13 Sub-menu Management.
  - [x] Analisis 10 Modul Fitur Mobile User (Auth, Registrasi NIK, Reset Sandi, Presensi, Riwayat, Koreksi, Izin, Lembur, Cuti/Sakit, Profil).
- [x] **1.2. Pembuatan Dokumen SIT Multi-Sheet Terpisah (Total 355 Test Cases)**
  - [x] **Sheet 1 (`Summary & Multi-Device Guide`)**: Panduan alur kerja multi-device Git, ringkasan beban kerja 355 TCs.
  - [x] **Sheet 2 (`Tester Deny - Admin HRD`)** (Total 190 TCs):
    - **Tabel A (Web UI Selenium)**: **130 Skenario Uji UI Admin** (`SIT_ADM_UI_001` s/d `130`).
    - **Tabel B (REST Assured API)**: **60 Skenario Uji API Admin** (`SIT_ADM_API_001` s/d `060`).
  - [x] **Sheet 3 (`Tester Aji - User Magang`)** (Total 165 TCs):
    - **Tabel A (Web UI Selenium)**: **110 Skenario Uji UI User** (`SIT_USR_UI_001` s/d `110`).
    - **Tabel B (REST Assured API)**: **55 Skenario Uji API User** (`SIT_USR_API_001` s/d `055`).
  - [x] **Sheet 4 (`Master Matrix - UI Scenarios`)**: **240 Skenario Web UI** terpadu.
  - [x] **Sheet 5 (`Master Matrix - API Scenarios`)**: **115 Skenario REST Assured** terpadu.

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

### 🔹 Fase 4: Page Object Model (POM) Implementation (Opsi B: Branch Terpisah per Fitur dari `main`)
#### 👤 Modul Halaman Tester Deny (Admin Web HRD):
- [ ] **4.1. `AdminLoginPage.java`** `[Branch: feature/deny-admin-login]`: Form login admin, alert, proteksi sesi.
- [ ] **4.2. `AdminDashboardPage.java`** `[Branch: feature/deny-admin-dashboard]`: Widget metrik approval, statistik absensi, menu sidebar.
- [ ] **4.3. `AdminReportsPage.java`** `[Branch: feature/deny-admin-reports]`: Laporan Semua, Kehadiran, Izin Terlambat, Pulang Cepat, Sakit.
- [ ] **4.4. `AdminApprovalPage.java`** `[Branch: feature/deny-admin-reports]`: Approval Koreksi Absen, Approval Cuti, Approval Lembur.
- [ ] **4.5. `AdminDownloadPage.java`** `[Branch: feature/deny-admin-reports]`: Download laporan kehadiran, cuti, lembur format Excel.
- [ ] **4.6. `AdminImportPage.java`** `[Branch: feature/deny-admin-import]`: Halaman template dan upload import status aktif & user.
- [ ] **4.7. `AdminManagementPage.java`** `[Branch: feature/deny-admin-management]`: Search user NIK, toggle status user, divisi, shifting.

#### 👤 Modul Halaman Tester Aji (User Mobile Mirroring):
- [ ] **4.8. `UserLoginPage.java`** `[Branch: feature/aji-user-login]`: Form login magang, alert, toggle show/hide password.
- [ ] **4.9. `RegisterPage.java`** `[Branch: feature/aji-user-register]`: Form pendaftaran NIK Excel, upload selfie dummy.
- [ ] **4.10. `ResetPasswordPage.java`** `[Branch: feature/aji-user-reset-password]`: Form permohonan reset password & link login.
- [ ] **4.11. `AttendancePage.java`** `[Branch: feature/aji-user-attendance]`: Presensi masuk (WFO/WFH), izin kamera, presensi pulang.
- [ ] **4.12. `ActivityHistoryPage.java`** `[Branch: feature/aji-user-activity]`: List kartu riwayat absensi & filter tanggal.
- [ ] **4.13. `CorrectionPage.java`** `[Branch: feature/aji-user-correction]`: Form pengajuan koreksi jam absensi.
- [ ] **4.14. `PermitPage.java`** `[Branch: feature/aji-user-permit]`: Form pengajuan izin terlambat & pulang cepat.
- [ ] **4.15. `LeavePage.java`** `[Branch: feature/aji-user-leave-sick]`: Form pengajuan cuti tahunan & klaim sakit dengan surat dokter.
- [ ] **4.16. `OvertimePage.java`** `[Branch: feature/aji-user-overtime]`: Form permohonan lembur kerja.
- [ ] **4.17. `UserProfilePage.java`** `[Branch: feature/aji-user-profile]`: Halaman profil, download rekap personal, ubah password.

---

### 🔹 Fase 5: Implementasi Automated Test Suites (Opsi B: Pasangan Test Class di Branch Fitur Masing-Masing)
#### 👤 Skenario Pengujian Tester Deny (Total: 190 Test Cases — 130 UI + 60 API):
- [ ] **5.1. `AdminLoginTest.java`** `[Branch: feature/deny-admin-login]` (12 Test Cases: `SIT_ADM_UI_001` s/d `012`)
- [ ] **5.2. `AdminDashboardTest.java`** `[Branch: feature/deny-admin-dashboard]` (10 Test Cases: `SIT_ADM_UI_013` s/d `022`)
- [ ] **5.3. `AdminReportsTest.java`** `[Branch: feature/deny-admin-reports]` (45 Test Cases: `SIT_ADM_UI_023` s/d `067` — 9 Sub-menu Laporan)
- [ ] **5.4. `AdminImportTest.java`** `[Branch: feature/deny-admin-import]` (12 Test Cases: `SIT_ADM_UI_068` s/d `079` — 4 Sub-menu Import)
- [ ] **5.5. `AdminManagementTest.java`** `[Branch: feature/deny-admin-management]` (51 Test Cases: `SIT_ADM_UI_080` s/d `130` — 13 Sub-menu Management)
- [ ] **5.6. `AdminApiTest.java`** `[Branch: feature/deny-admin-api]` (60 Test Cases API: `SIT_ADM_API_001` s/d `060`)

#### 👤 Skenario Pengujian Tester Aji (Total: 165 Test Cases — 110 UI + 55 API):
- [ ] **5.7. `UserLoginTest.java`** `[Branch: feature/aji-user-login]` (12 Test Cases: `SIT_USR_UI_001` s/d `012`)
- [ ] **5.8. `RegisterTest.java`** `[Branch: feature/aji-user-register]` (12 Test Cases: `SIT_USR_UI_013` s/d `024`)
- [ ] **5.9. `ResetPasswordTest.java`** `[Branch: feature/aji-user-reset-password]` (8 Test Cases: `SIT_USR_UI_025` s/d `032`)
- [ ] **5.10. `AttendanceTest.java`** `[Branch: feature/aji-user-attendance]` (18 Test Cases: `SIT_USR_UI_033` s/d `050`)
- [ ] **5.11. `ActivityHistoryTest.java`** `[Branch: feature/aji-user-activity]` (10 Test Cases: `SIT_USR_UI_051` s/d `060`)
- [ ] **5.12. `CorrectionTest.java`** `[Branch: feature/aji-user-correction]` (10 Test Cases: `SIT_USR_UI_061` s/d `070`)
- [ ] **5.13. `PermitTest.java`** `[Branch: feature/aji-user-permit]` (10 Test Cases: `SIT_USR_UI_071` s/d `080`)
- [ ] **5.14. `OvertimeTest.java`** `[Branch: feature/aji-user-overtime]` (10 Test Cases: `SIT_USR_UI_081` s/d `090`)
- [ ] **5.15. `LeaveAndSickTest.java`** `[Branch: feature/aji-user-leave-sick]` (12 Test Cases: `SIT_USR_UI_091` s/d `102`)
- [ ] **5.16. `UserProfileTest.java`** `[Branch: feature/aji-user-profile]` (8 Test Cases: `SIT_USR_UI_103` s/d `110`)
- [ ] **5.17. `UserApiTest.java`** `[Branch: feature/aji-user-api]` (55 Test Cases API: `SIT_USR_API_001` s/d `055`)

---

### 🔹 Fase 6: Konfigurasi Suite Runner & Verifikasi Headless
- [ ] **6.1. Pembuatan Multi-Suite XML Runner**
  - [ ] `src/test/resources/testng.xml`: Master Runner (355 test cases).
  - [ ] `src/test/resources/testng-deny.xml`: Runner khusus Tester Deny (Admin HRD - 190 test cases).
  - [ ] `src/test/resources/testng-aji.xml`: Runner khusus Tester Aji (User Magang - 165 test cases).
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


---

## 🌿 6. SOP Alur Kerja Git Opsi B (Branch Terpisah per Fitur Tanpa Merge ke `main` Dulu)

Agar setiap fitur di **Fase 4 (Page Object)** dan **Fase 5 (Test Class)** memiliki branch mandiri yang bersih dan siap di-*Pull Request* (PR) satu per satu di GitHub tanpa saling menimpa:

### 📌 Aturan Emas Opsi B (Wajib Dipatuhi Seluruh Tester & AI Agent)
1. **Selalu Mulai dari Branch `main`**:
   Setiap kali ingin mengerjakan fitur baru, **wajib** pindah ke `main` terlebih dahulu baru membuat branch fitur:
   ```powershell
   git checkout main
   git pull origin main
   git checkout -b <nama-branch-fitur>
   ```
   *(Jangan membuat branch fitur baru dari dalam branch fitur sebelumnya agar riwayat commit antar fitur tidak tercampur!)*
2. **Satu Branch = Satu Pasang Fitur (Vertical Slice)**:
   Di dalam satu branch fitur, kerjakan **Page Object (Fase 4)** sekaligus **Test Class (Fase 5)** untuk fitur tersebut saja.
3. **Jangan Mengubah File Bersama (`Shared Core`) & `testng*.xml` di Branch Fitur**:
   - Jangan memodifikasi `BasePage.java`, `BaseTest.java`, `BaseApiTest.java`, `Constants.java`, atau `testng*.xml` di dalam branch fitur agar tidak terjadi *merge conflict* saat Pull Request.
   - Untuk mengetes fitur di branch tersebut, jalankan class test-nya secara langsung:
     ```powershell
     mvn test -Dtest=<NamaClassTest>
     ```
4. **Commit & Push ke Branch Fitur Masing-Masing (Tanpa Merge ke `main`)**:
   ```powershell
   git add .
   git commit -m "feat(<modul>): implement <NamaPage> and <NamaTest>"
   git push -u origin <nama-branch-fitur>
   ```
   Setelah di-*push*, kembali lagi ke `main` (`git checkout main`) untuk mulai mengerjakan branch fitur berikutnya.

---

### 📋 Daftar Resmi 17 Branch Fitur (Fase 4 & Fase 5)

#### 👤 A. Daftar 6 Branch Fitur — Tester Deny (Admin HRD — 190 TCs)
| No | Nama Branch Git (Opsi B) | File Fase 4 (POM) yang Dibuat | File Fase 5 (Test) yang Dibuat | Cakupan ID SIT |
| :-: | :--- | :--- | :--- | :---: |
| 1 | `feature/deny-admin-login` | `pages/admin/AdminLoginPage.java` | `tests/admin/AdminLoginTest.java` | `SIT_ADM_UI_001` - `012` (12 TCs) |
| 2 | `feature/deny-admin-dashboard` | `pages/admin/AdminDashboardPage.java` | `tests/admin/AdminDashboardTest.java` | `SIT_ADM_UI_013` - `022` (10 TCs) |
| 3 | `feature/deny-admin-reports` | `pages/admin/AdminReportsPage.java`<br>`pages/admin/AdminApprovalPage.java`<br>`pages/admin/AdminDownloadPage.java` | `tests/admin/AdminReportsTest.java` | `SIT_ADM_UI_023` - `067` (45 TCs) |
| 4 | `feature/deny-admin-import` | `pages/admin/AdminImportPage.java` | `tests/admin/AdminImportTest.java` | `SIT_ADM_UI_068` - `079` (12 TCs) |
| 5 | `feature/deny-admin-management` | `pages/admin/AdminManagementPage.java` | `tests/admin/AdminManagementTest.java` | `SIT_ADM_UI_080` - `130` (51 TCs) |
| 6 | `feature/deny-admin-api` | *(Menggunakan `BaseApiTest`)* | `api/admin/AdminApiTest.java` | `SIT_ADM_API_001` - `060` (60 TCs) |

#### 👤 B. Daftar 11 Branch Fitur — Tester Aji (User Magang — 165 TCs)
| No | Nama Branch Git (Opsi B) | File Fase 4 (POM) yang Dibuat | File Fase 5 (Test) yang Dibuat | Cakupan ID SIT |
| :-: | :--- | :--- | :--- | :---: |
| 1 | `feature/aji-user-login` | `pages/user/UserLoginPage.java` | `tests/user/UserLoginTest.java` | `SIT_USR_UI_001` - `012` (12 TCs) |
| 2 | `feature/aji-user-register` | `pages/user/RegisterPage.java` | `tests/user/RegisterTest.java` | `SIT_USR_UI_013` - `024` (12 TCs) |
| 3 | `feature/aji-user-reset-password` | `pages/user/ResetPasswordPage.java` | `tests/user/ResetPasswordTest.java` | `SIT_USR_UI_025` - `032` (8 TCs) |
| 4 | `feature/aji-user-attendance` | `pages/user/AttendancePage.java` | `tests/user/AttendanceTest.java` | `SIT_USR_UI_033` - `050` (18 TCs) |
| 5 | `feature/aji-user-activity` | `pages/user/ActivityHistoryPage.java` | `tests/user/ActivityHistoryTest.java` | `SIT_USR_UI_051` - `060` (10 TCs) |
| 6 | `feature/aji-user-correction` | `pages/user/CorrectionPage.java` | `tests/user/CorrectionTest.java` | `SIT_USR_UI_061` - `070` (10 TCs) |
| 7 | `feature/aji-user-permit` | `pages/user/PermitPage.java` | `tests/user/PermitTest.java` | `SIT_USR_UI_071` - `080` (10 TCs) |
| 8 | `feature/aji-user-overtime` | `pages/user/OvertimePage.java` | `tests/user/OvertimeTest.java` | `SIT_USR_UI_081` - `090` (10 TCs) |
| 9 | `feature/aji-user-leave-sick` | `pages/user/LeavePage.java` | `tests/user/LeaveAndSickTest.java` | `SIT_USR_UI_091` - `102` (12 TCs) |
| 10 | `feature/aji-user-profile` | `pages/user/UserProfilePage.java` | `tests/user/UserProfileTest.java` | `SIT_USR_UI_103` - `110` (8 TCs) |
| 11 | `feature/aji-user-api` | *(Menggunakan `BaseApiTest`)* | `api/user/UserApiTest.java` | `SIT_USR_API_001` - `055` (55 TCs) |

---

## 🤖 7. Template Prompt Standar untuk AI Agent (Copy-Paste Ready)

Agar **Tester Deny** dan **Tester Aji** (di laptop/device berbeda) mendapatkan hasil kode, struktur class, penamaan method, dan alur Git yang **100% seragam**, cukup *copy-paste* prompt di bawah ini ke AI Agent masing-masing:

### 📋 A. Prompt AI Agent untuk Tester Deny (Admin HRD — Opsi B)
> **Copy-paste prompt berikut ke AI Agent di Device Tester Deny:**
>
> ```text
> Tolong baca file @plan.md, @tasklist.md, dan @README.md terlebih dahulu sebagai acuan utama.
> Saya adalah Tester Deny yang bertanggung jawab pada modul ADMIN (Web HRD) dengan total 190 Test Cases (130 UI + 60 API) sesuai matriks @docs/SIT_Scenario_Magang_Hadir.xlsx.
>
> Kerjakan Fase 4 dan Fase 5 khusus porsi Tester Deny menggunakan aturan ketat Git Opsi B (Isolated Feature Branching tanpa merge ke main):
> 1. Untuk SETIAP fitur Admin pada tabel "Daftar 6 Branch Fitur — Tester Deny" di @tasklist.md:
>    a. Selalu pindah ke branch `main` terlebih dahulu (`git checkout main`), lalu buat branch baru sesuai nama resmi di tasklist (`git checkout -b feature/deny-admin-...`).
>    b. Buat class Page Object di package `com.juaracoding.magang.pages.admin` yang mewarisi `BasePage`.
>    c. Buat class Test di package `com.juaracoding.magang.tests.admin` (mewarisi `BaseTest`) atau `com.juaracoding.magang.api.admin` (mewarisi `BaseApiTest`).
>    d. Gunakan `SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED)` untuk password, dan cantumkan ID Test Case (misal `SIT_ADM_UI_001`) pada `@Test(description = "...")` serta `ExtentTestManager.logInfo(...)`.
>    e. JANGAN mengubah file core bersama (`BasePage`, `BaseTest`, `BaseApiTest`, `Constants`, `DriverFactory`, atau `testng*.xml`) agar tidak terjadi merge conflict saat Pull Request.
>    f. Jalankan verifikasi `mvn test -Dtest=<NamaClassTest>` hingga kompilasi dan eksekusi berhasil.
>    g. Lakukan `git add` dan `git commit` hanya untuk file fitur tersebut di branch fiturnya, JANGAN di-merge ke `main`, lalu kembali ke `git checkout main` sebelum lanjut ke branch fitur berikutnya.
> ```

---

### 📋 B. Prompt AI Agent untuk Tester Aji (User Magang — Opsi B)
> **Copy-paste prompt berikut ke AI Agent di Device Tester Aji:**
>
> ```text
> Tolong baca file @plan.md, @tasklist.md, dan @README.md terlebih dahulu sebagai acuan utama.
> Saya adalah Tester Aji yang bertanggung jawab pada modul USER (Mobile Mirroring) dengan total 165 Test Cases (110 UI + 55 API) sesuai matriks @docs/SIT_Scenario_Magang_Hadir.xlsx.
>
> Kerjakan Fase 4 dan Fase 5 khusus porsi Tester Aji menggunakan aturan ketat Git Opsi B (Isolated Feature Branching tanpa merge ke main):
> 1. Untuk SETIAP fitur User pada tabel "Daftar 11 Branch Fitur — Tester Aji" di @tasklist.md:
>    a. Selalu pindah ke branch `main` terlebih dahulu (`git checkout main`), lalu buat branch baru sesuai nama resmi di tasklist (`git checkout -b feature/aji-user-...`).
>    b. Buat class Page Object di package `com.juaracoding.magang.pages.user` yang mewarisi `BasePage`.
>    c. Buat class Test di package `com.juaracoding.magang.tests.user` (mewarisi `BaseTest`) atau `com.juaracoding.magang.api.user` (mewarisi `BaseApiTest`).
>    d. Gunakan `SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED)` untuk password, dan cantumkan ID Test Case (misal `SIT_USR_UI_001`) pada `@Test(description = "...")` serta `ExtentTestManager.logInfo(...)`.
>    e. JANGAN mengubah file core bersama (`BasePage`, `BaseTest`, `BaseApiTest`, `Constants`, `DriverFactory`, atau `testng*.xml`) agar tidak terjadi merge conflict saat Pull Request.
>    f. Jalankan verifikasi `mvn test -Dtest=<NamaClassTest>` hingga kompilasi dan eksekusi berhasil.
>    g. Lakukan `git add` dan `git commit` hanya untuk file fitur tersebut di branch fiturnya, JANGAN di-merge ke `main`, lalu kembali ke `git checkout main` sebelum lanjut ke branch fitur berikutnya.
> ```

---

### 📋 C. Prompt AI Agent Jika Ingin Mengerjakan Satu Fitur Saja (Cicil per Fitur)
> **Jika Anda ingin menyuruh AI Agent mengerjakan 1 fitur spesifik saja pada satu waktu:**
>
> ```text
> Mengacu pada @plan.md dan @tasklist.md (Aturan Git Opsi B), tolong kerjakan HANYA fitur [NAMA_BRANCH_FITUR, contoh: feature/deny-admin-login]:
> 1. Pastikan checkout dari `main` (`git checkout main && git checkout -b [NAMA_BRANCH_FITUR]`).
> 2. Implementasikan Page Object dan Test Class sesuai cakupan ID SIT untuk fitur ini tanpa mengubah file core bersama atau testng.xml.
> 3. Jalankan `mvn test -Dtest=[NAMA_CLASS_TEST]` untuk memvalidasi hasilnya.
> 4. Commit perubahan di branch `[NAMA_BRANCH_FITUR]` dan jangan di-merge ke `main`.
> ```
