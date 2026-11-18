# 🚀 HADIR Magang - Enterprise Web & API Test Automation Framework

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium%20WebDriver-4.29.0-green.svg)](https://www.selenium.dev/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-5.4.0-blue.svg)](https://rest-assured.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-red.svg)](https://testng.org/)
[![ExtentReports](https://img.shields.io/badge/ExtentReports-5.1.2-purple.svg)](https://www.extentreports.com/)
[![Apache Maven](https://img.shields.io/badge/Apache%20Maven-3.9+-C71A36.svg)](https://maven.apache.org/)

Repositori ini merupakan kerangka kerja otomatisasi pengujian (*enterprise test automation framework*) untuk platform presensi dan manajemen magang **[magang.dikahadir.com](https://magang.dikahadir.com)** (HADIR). 

Framework ini menggabungkan pengujian **Web UI (Selenium WebDriver 4)** dan **Backend API (REST Assured 5)** berbasis **Java 21**, dirancang khusus untuk kolaborasi multi-tester (*Tester Deny* & *Tester Aji*) dengan pembagian tugas yang terisolasi dan bebas konflik (*zero merge conflict*).

---

## 📑 Daftar Isi
- [1. Arsitektur & Teknologi](#1-arsitektur--teknologi)
- [2. Pembagian Peran & Alokasi Tugas Tim](#2-pembagian-peran--alokasi-tugas-tim)
- [3. Panduan Bergabung untuk Rekan Tester (Quickstart)](#3-panduan-bergabung-untuk-rekan-tester-quickstart)
- [4. Git Workflow & Multi-Device Branching SOP](#4-git-workflow--multi-device-branching-sop)
- [5. Panduan Menjalankan Pengujian (Execution Guide)](#5-panduan-menjalankan-pengujian-execution-guide)
- [6. Keamanan Kredensial (Base64 Security)](#6-keamanan-kredensial-base64-security)
- [7. Pelaporan & Bukti Cacat (ExtentReports 5)](#7-pelaporan--bukti-cacat-extentreports-5)
- [8. Struktur Direktori Proyek](#8-struktur-direktori-proyek)

---

## 1. Arsitektur & Teknologi

| Komponen | Spesifikasi & Versi | Keterangan |
| :--- | :--- | :--- |
| **Aplikasi Target (SUT)** | `https://magang.dikahadir.com` | Platform HADIR Magang |
| **Bahasa Pemrograman** | Java 21 LTS | Enforce via `maven-compiler-plugin` 3.13.0 |
| **Web UI Engine** | Selenium WebDriver `4.29.0` | ThreadLocal Driver, Chrome W3C options, default `headless = true` |
| **API Testing Engine** | REST Assured `5.4.0` | BDD Syntax (`given-when-then`), JSON Path, JWT Bearer Token |
| **Test Runner** | TestNG `7.10.2` | Multi-suite runner (`testng.xml`, `testng-deny.xml`, `testng-aji.xml`) |
| **Reporting Dashboard** | ExtentReports `5.1.2` | Dark Theme Spark Reporter, tag kategori per tester, Base64 screenshot |
| **Matriks Pengujian** | Apache POI `5.2.5` | Dokumen SIT 355 Test Cases (`docs/SIT_Scenario_Magang_Hadir.xlsx`) |

---

## 2. Pembagian Peran & Alokasi Tugas Tim

Beban kerja **355 Test Cases** (**240 Web UI** + **115 REST Assured API**) dibagi menjadi 2 porsi independen sesuai kepadatan modul:

```mermaid
flowchart TD
    subgraph HADIR Magang Platform
        ADM[Admin HRD Portal\n/authentication/login]
        USR[User Mobile Mirroring\n/absen/login]
    end

    subgraph Tester Deny - Admin Scope 190 TCs
        D[👤 Tester Deny] -->|130 Web UI Tests| ADM
        D -->|60 REST Assured Tests| ADM
        D -->|Suite Runner| RD[testng-deny.xml]
    end

    subgraph Tester Aji - User Scope 165 TCs
        A[👤 Tester Aji] -->|110 Web UI Tests| USR
        A -->|55 REST Assured Tests| USR
        A -->|Suite Runner| RA[testng-aji.xml]
    end
```

| Informasi | 👤 Tester Deny | 👤 Tester Aji |
| :--- | :--- | :--- |
| **Ruang Lingkup** | **ADMIN (Web HRD - 26 Sub-Menu)** | **USER (Mobile Mirroring - 10 Modul)** |
| **Akses URL** | `/authentication/login` | `/absen/login` |
| **Akun Login** | `admin@hadir.com` | `hadirsqa1@gmail.com` |
| **Package Page Objects** | `com.juaracoding.magang.pages.admin.*` | `com.juaracoding.magang.pages.user.*` |
| **Package Tests** | `tests.admin.*` & `api.admin.*` | `tests.user.*` & `api.user.*` |
| **Runner Suite XML** | `src/test/resources/testng-deny.xml` | `src/test/resources/testng-aji.xml` |
| **Git Working Branch** | `feature/tester-deny-admin` | `feature/tester-aji-user` |
| **Total Test Cases** | **190 TCs** (130 UI + 60 API) | **165 TCs** (110 UI + 55 API) |

---

## 3. Panduan Bergabung untuk Rekan Tester (Quickstart)

Bagi rekan tester yang baru bergabung, ikuti langkah berikut di komputer masing-masing:

### Langkah 1: Pastikan Prasyarat Terpasang
1. **Java 21 LTS**:
   ```bash
   java -version
   ```
2. **Apache Maven 3.9+**:
   ```bash
   mvn -version
   ```
3. **Google Chrome**: Versi terbaru.

### Langkah 2: Clone Repositori
```bash
git clone <URL_REPOSITORY_GITHUB_ANDA>
cd magang-automation-framework
```

### Langkah 3: Compile & Unduh Dependensi
```bash
mvn clean test-compile
```
*Jika muncul status `BUILD SUCCESS`, maka seluruh dependensi dan kompilator Java 21 siap digunakan!*

---

## 4. Git Workflow & Multi-Device Branching SOP

Untuk mencegah terjadinya bentrok kode (*merge conflict*) antar tester:

### 👤 Untuk Tester Deny:
```bash
# Buat branch kerja dari main
git checkout main
git pull origin main
git checkout -b feature/tester-deny-admin

# Kerjakan hanya di package:
# - src/main/java/com/juaracoding/magang/pages/admin/
# - src/test/java/com/juaracoding/magang/tests/admin/
# - src/test/java/com/juaracoding/magang/api/admin/

# Commit & Push ke branch sendiri
git add .
git commit -m "feat(admin): implement admin page objects and tests"
git push origin feature/tester-deny-admin
```

### 👤 Untuk Tester Aji:
```bash
# Buat branch kerja dari main
git checkout main
git pull origin main
git checkout -b feature/tester-aji-user

# Kerjakan hanya di package:
# - src/main/java/com/juaracoding/magang/pages/user/
# - src/test/java/com/juaracoding/magang/tests/user/
# - src/test/java/com/juaracoding/magang/api/user/

# Commit & Push ke branch sendiri
git add .
git commit -m "feat(user): implement user mobile page objects and tests"
git push origin feature/tester-aji-user
```

### 🔀 Penggabungan ke `main` (Merge via Pull Request):
Setelah seluruh skenario masing-masing selesai dan lulus pengujian, buat **Pull Request (PR)** ke branch `main`. Karena folder kerja terpisah 100%, Git akan menggabungkan kode secara otomatis tanpa konflik!

---

## 5. Panduan Menjalankan Pengujian (Execution Guide)

### 🔹 Menjalankan Test API Tertentu
```bash
# Jalankan Smoke API Admin (Tester Deny)
mvn test -Dtest=AdminSmokeApiTest

# Jalankan Smoke API User (Tester Aji)
mvn test -Dtest=UserSmokeApiTest
```

### 🔹 Menjalankan Berdasarkan Suite Tester
```bash
# Jalankan seluruh test milik Deny (Admin UI + API)
mvn test -DsuiteXmlFile=src/test/resources/testng-deny.xml

# Jalankan seluruh test milik Aji (User UI + API)
mvn test -DsuiteXmlFile=src/test/resources/testng-aji.xml
```

### 🔹 Menjalankan Test UI dengan Browser Terbuka (Headed / Visual Mode)
Secara default pengujian berjalan di latar belakang (`headless = true`). Untuk melihat browser bergerak secara visual di layar:
```bash
# Buka browser Chrome secara visual saat test berjalan
mvn test -Dheadless=false -DsuiteXmlFile=src/test/resources/testng-deny.xml
```

### 🔹 Menjalankan Seluruh Master Suite (355 Test Cases)
```bash
mvn clean test
```

---

## 6. Keamanan Kredensial (Base64 Security)

> [!IMPORTANT]
> **Tidak ada kata sandi yang disimpan dalam bentuk plaintext di repositori ini!**

Seluruh password sensitif telah di-encode menggunakan Base64 di kelas `Constants.java`:
- Admin Password: `TWFnYW5nU1FBX0pDQDEyMw==`
- User Password: `U1FBQEhhZGlyMTIzNDU=`

Saat pengujian dijalankan, `SecurityUtil.decode()` akan memulihkan password asli langsung di dalam memori JVM secara dinamis sehingga riwayat commit GitHub tetap bersih dan aman.

---

## 7. Pelaporan & Bukti Cacat (ExtentReports 5)

Setelah pengujian selesai dijalankan, buka file:
📂 **`reports/ExtentReport.html`**

Fitur laporan:
- **Tema Gelap (Dark Theme)** yang nyaman di mata.
- **Diagram Lingkaran (Pie Chart)** ringkasan metrik kelulusan test.
- **Filter Kategori**: Filter instan berdasarkan penguji (`Tester Deny` / `Tester Aji`) atau tipe (`REST Assured API` / `Selenium Web UI`).
- **Tangkapan Layar Otomatis (Base64 Evidence)**: Jika terjadi test gagal pada Web UI, screenshot otomatis tertanam langsung ke dalam file HTML.

Matriks lengkap 355 Skenario dapat dilihat di dokumen:
📂 **`docs/SIT_Scenario_Magang_Hadir.xlsx`**

---

## 8. Struktur Direktori Proyek

```
magang-automation-framework/
├── .gitignore                               # Aturan file yang diabaikan Git (target, ide, logs)
├── .gitattributes                           # Penanganan line endings & binary file (.xlsx)
├── .editorconfig                            # Standar indentasi dan encoding editor
├── pom.xml                                  # Konfigurasi dependensi Maven Java 21
├── README.md                                # Panduan onboarding & dokumentasi framework
├── plan.md                                  # Dokumen rencana implementasi arsitektur
├── tasklist.md                              # Roadmap dan status pelaksanaan 7 fase
│
├── docs/
│   └── SIT_Scenario_Magang_Hadir.xlsx       # Matriks 355 Test Cases (5 Sheet terpisah)
│
├── reports/
│   ├── ExtentReport.html                    # Dashboard interaktif laporan pengujian
│   └── screenshots/                         # Folder bukti screenshot
│
├── src/
│   ├── main/java/com/juaracoding/magang/
│   │   ├── drivers/
│   │   │   └── DriverFactory.java           # ThreadLocal WebDriver manager
│   │   ├── pages/
│   │   │   ├── BasePage.java                # Base POM (Explicit wait wrapper)
│   │   │   ├── admin/                       # [Tester Deny] Page Object Model Admin
│   │   │   │   └── AdminLoginPage.java
│   │   │   └── user/                        # [Tester Aji] Page Object Model User
│   │   │       └── UserLoginPage.java
│   │   ├── reports/
│   │   │   ├── ExtentManager.java           # Singleton ExtentReports 5
│   │   │   └── ExtentTestManager.java       # ThreadLocal test logger
│   │   └── utils/
│   │       ├── Constants.java               # URL, endpoints, Base64 passwords
│   │       ├── SecurityUtil.java            # Base64 decoder/encoder
│   │       └── ScreenshotUtil.java          # Screen capture util
│   │
│   └── test/
│       ├── java/com/juaracoding/magang/
│       │   ├── BaseTest.java                # Base UI Test setup & teardown
│       │   ├── listeners/
│       │   │   └── TestListener.java        # TestNG listener & failure screenshot
│       │   ├── api/
│       │   │   ├── BaseApiTest.java         # Base REST Assured engine & auth helper
│       │   │   ├── admin/                   # [Tester Deny] Skenario API Admin (60 TCs)
│       │   │   │   └── AdminSmokeApiTest.java
│       │   │   └── user/                    # [Tester Aji] Skenario API User (55 TCs)
│       │   │       └── UserSmokeApiTest.java
│       │   └── tests/
│       │       ├── admin/                   # [Tester Deny] Skenario UI Admin (130 TCs)
│       │       │   └── AdminSmokeUiTest.java
│       │       └── user/                    # [Tester Aji] Skenario UI User (110 TCs)
│       │           └── UserSmokeUiTest.java
│       │
│       └── resources/
│           ├── testng.xml                   # Master Suite (355 TCs)
│           ├── testng-deny.xml              # Suite khusus Tester Deny (Admin - 190 TCs)
│           └── testng-aji.xml               # Suite khusus Tester Aji (User - 165 TCs)
```

---
*Dibuat untuk JuaraCoding SQA Batch 31 - Kolaborasi Pengujian Otomasi Enterprise.*



---

## 9. SOP Git Opsi B: Feature Branching per Modul (Fase 4 & Fase 5)

Proyek ini menggunakan **Opsi B (Isolated Feature Branching tanpa merge langsung ke `main`)**:
1. Setiap kali akan membuat 1 fitur baru (pasangan **Page Object** di Fase 4 + **Test Class** di Fase 5), **wajib selalu berpindah ke `main` terlebih dahulu**:
   ```bash
   git checkout main
   git pull origin main
   git checkout -b <nama-branch-fitur>
   ```
2. Di dalam branch fitur tersebut, hanya buat/modifikasi file Page Object dan Test Class untuk fitur itu saja. **Jangan** mengubah file pondasi bersama (`BasePage`, `BaseTest`, `BaseApiTest`, `Constants`, `testng*.xml`) agar tidak terjadi *merge conflict* saat Pull Request di GitHub.
3. Jalankan pengujian class tersebut secara spesifik:
   ```bash
   mvn test -Dtest=<NamaClassTest>
   ```
4. Commit dan push branch fitur ke GitHub (tanpa merge ke `main`), lalu kembali ke `main` untuk fitur berikutnya:
   ```bash
   git add .
   git commit -m "feat(<modul>): implement <NamaPage> and <NamaTest>"
   git push -u origin <nama-branch-fitur>
   git checkout main
   ```

### Daftar 17 Branch Fitur Resmi (6 Branch Deny + 11 Branch Aji)

| Tester | Nama Branch Fitur | Page Object (Fase 4) | Test Class (Fase 5) | Cakupan SIT |
| :--- | :--- | :--- | :--- | :---: |
| **👤 Deny** | `feature/deny-admin-login` | `AdminLoginPage.java` | `AdminLoginTest.java` | `SIT_ADM_UI_001` - `012` (12 TCs) |
| **👤 Deny** | `feature/deny-admin-dashboard` | `AdminDashboardPage.java` | `AdminDashboardTest.java` | `SIT_ADM_UI_013` - `022` (10 TCs) |
| **👤 Deny** | `feature/deny-admin-reports` | `AdminReportsPage.java`, `AdminApprovalPage.java`, `AdminDownloadPage.java` | `AdminReportsTest.java` | `SIT_ADM_UI_023` - `067` (45 TCs) |
| **👤 Deny** | `feature/deny-admin-import` | `AdminImportPage.java` | `AdminImportTest.java` | `SIT_ADM_UI_068` - `079` (12 TCs) |
| **👤 Deny** | `feature/deny-admin-management` | `AdminManagementPage.java` | `AdminManagementTest.java` | `SIT_ADM_UI_080` - `130` (51 TCs) |
| **👤 Deny** | `feature/deny-admin-api` | *(Extends `BaseApiTest`)* | `AdminApiTest.java` | `SIT_ADM_API_001` - `060` (60 TCs) |
| **👤 Aji** | `feature/aji-user-login` | `UserLoginPage.java` | `UserLoginTest.java` | `SIT_USR_UI_001` - `012` (12 TCs) |
| **👤 Aji** | `feature/aji-user-register` | `RegisterPage.java` | `RegisterTest.java` | `SIT_USR_UI_013` - `024` (12 TCs) |
| **👤 Aji** | `feature/aji-user-reset-password` | `ResetPasswordPage.java` | `ResetPasswordTest.java` | `SIT_USR_UI_025` - `032` (8 TCs) |
| **👤 Aji** | `feature/aji-user-attendance` | `AttendancePage.java` | `AttendanceTest.java` | `SIT_USR_UI_033` - `050` (18 TCs) |
| **👤 Aji** | `feature/aji-user-activity` | `ActivityHistoryPage.java` | `ActivityHistoryTest.java` | `SIT_USR_UI_051` - `060` (10 TCs) |
| **👤 Aji** | `feature/aji-user-correction` | `CorrectionPage.java` | `CorrectionTest.java` | `SIT_USR_UI_061` - `070` (10 TCs) |
| **👤 Aji** | `feature/aji-user-permit` | `PermitPage.java` | `PermitTest.java` | `SIT_USR_UI_071` - `080` (10 TCs) |
| **👤 Aji** | `feature/aji-user-overtime` | `OvertimePage.java` | `OvertimeTest.java` | `SIT_USR_UI_081` - `090` (10 TCs) |
| **👤 Aji** | `feature/aji-user-leave-sick` | `LeavePage.java` | `LeaveAndSickTest.java` | `SIT_USR_UI_091` - `102` (12 TCs) |
| **👤 Aji** | `feature/aji-user-profile` | `UserProfilePage.java` | `UserProfileTest.java` | `SIT_USR_UI_103` - `110` (8 TCs) |
| **👤 Aji** | `feature/aji-user-api` | *(Extends `BaseApiTest`)* | `UserApiTest.java` | `SIT_USR_API_001` - `055` (55 TCs) |

---

## 10. 🤖 Template Prompt Standar untuk AI Agent (Copy-Paste Ready)

Agar AI Agent yang dijalankan di laptop **Tester Deny** maupun **Tester Aji** menghasilkan standar kode, penamaan class, serta alur branch Git yang **100% seragam**, gunakan prompt di bawah ini:

### 📋 A. Prompt AI Agent untuk Tester Deny (Admin HRD)
```text
Tolong baca file @plan.md, @tasklist.md, dan @README.md terlebih dahulu sebagai acuan utama.
Saya adalah Tester Deny yang bertanggung jawab pada modul ADMIN (Web HRD) dengan total 190 Test Cases (130 UI + 60 API) sesuai matriks @docs/SIT_Scenario_Magang_Hadir.xlsx.

Kerjakan Fase 4 dan Fase 5 khusus porsi Tester Deny menggunakan aturan ketat Git Opsi B (Isolated Feature Branching tanpa merge ke main):
1. Untuk SETIAP fitur Admin pada tabel "Daftar 6 Branch Fitur — Tester Deny" di @tasklist.md:
   a. Selalu pindah ke branch `main` terlebih dahulu (`git checkout main`), lalu buat branch baru sesuai nama resmi di tasklist (`git checkout -b feature/deny-admin-...`).
   b. Buat class Page Object di package `com.juaracoding.magang.pages.admin` yang mewarisi `BasePage`.
   c. Buat class Test di package `com.juaracoding.magang.tests.admin` (mewarisi `BaseTest`) atau `com.juaracoding.magang.api.admin` (mewarisi `BaseApiTest`).
   d. Gunakan `SecurityUtil.decode(Constants.ADMIN_PASSWORD_ENCODED)` untuk password, dan cantumkan ID Test Case (misal `SIT_ADM_UI_001`) pada `@Test(description = "...")` serta `ExtentTestManager.logInfo(...)`.
   e. JANGAN mengubah file core bersama (`BasePage`, `BaseTest`, `BaseApiTest`, `Constants`, `DriverFactory`, atau `testng*.xml`) agar tidak terjadi merge conflict saat Pull Request.
   f. Jalankan verifikasi `mvn test -Dtest=<NamaClassTest>` hingga kompilasi dan eksekusi berhasil.
   g. Lakukan `git add` dan `git commit` hanya untuk file fitur tersebut di branch fiturnya, JANGAN di-merge ke `main`, lalu kembali ke `git checkout main` sebelum lanjut ke branch fitur berikutnya.
```

### 📋 B. Prompt AI Agent untuk Tester Aji (User Magang)
```text
Tolong baca file @plan.md, @tasklist.md, dan @README.md terlebih dahulu sebagai acuan utama.
Saya adalah Tester Aji yang bertanggung jawab pada modul USER (Mobile Mirroring) dengan total 165 Test Cases (110 UI + 55 API) sesuai matriks @docs/SIT_Scenario_Magang_Hadir.xlsx.

Kerjakan Fase 4 dan Fase 5 khusus porsi Tester Aji menggunakan aturan ketat Git Opsi B (Isolated Feature Branching tanpa merge ke main):
1. Untuk SETIAP fitur User pada tabel "Daftar 11 Branch Fitur — Tester Aji" di @tasklist.md:
   a. Selalu pindah ke branch `main` terlebih dahulu (`git checkout main`), lalu buat branch baru sesuai nama resmi di tasklist (`git checkout -b feature/aji-user-...`).
   b. Buat class Page Object di package `com.juaracoding.magang.pages.user` yang mewarisi `BasePage`.
   c. Buat class Test di package `com.juaracoding.magang.tests.user` (mewarisi `BaseTest`) atau `com.juaracoding.magang.api.user` (mewarisi `BaseApiTest`).
   d. Gunakan `SecurityUtil.decode(Constants.USER_PASSWORD_ENCODED)` untuk password, dan cantumkan ID Test Case (misal `SIT_USR_UI_001`) pada `@Test(description = "...")` serta `ExtentTestManager.logInfo(...)`.
   e. JANGAN mengubah file core bersama (`BasePage`, `BaseTest`, `BaseApiTest`, `Constants`, `DriverFactory`, atau `testng*.xml`) agar tidak terjadi merge conflict saat Pull Request.
   f. Jalankan verifikasi `mvn test -Dtest=<NamaClassTest>` hingga kompilasi dan eksekusi berhasil.
   g. Lakukan `git add` dan `git commit` hanya untuk file fitur tersebut di branch fiturnya, JANGAN di-merge ke `main`, lalu kembali ke `git checkout main` sebelum lanjut ke branch fitur berikutnya.
```

### 📋 C. Prompt AI Agent untuk Mengerjakan 1 Fitur Tertentu Saja
```text
Mengacu pada @plan.md dan @tasklist.md (Aturan Git Opsi B), tolong kerjakan HANYA fitur [NAMA_BRANCH_FITUR, contoh: feature/deny-admin-login]:
1. Pastikan checkout dari `main` (`git checkout main && git checkout -b [NAMA_BRANCH_FITUR]`).
2. Implementasikan Page Object dan Test Class sesuai cakupan ID SIT untuk fitur ini tanpa mengubah file core bersama atau testng.xml.
3. Jalankan `mvn test -Dtest=[NAMA_CLASS_TEST]` untuk memvalidasi hasilnya.
4. Commit perubahan di branch `[NAMA_BRANCH_FITUR]` dan jangan di-merge ke `main`.
```
