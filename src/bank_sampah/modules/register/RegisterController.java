package bank_sampah.modules.register;

import bank_sampah.MainApp;
import bank_sampah.util.AlertUtil;

public class RegisterController {
    private final RegisterDAO dao = new RegisterDAO();

    public void register(
            String nama,
            String username,
            String email,
            String password,
            String konfirmasi,
            String role
    ) {

        if (nama == null || nama.isEmpty()
                || username == null || username.isEmpty()
                || email == null || email.isEmpty()
                || password == null || password.isEmpty()
                || konfirmasi == null || konfirmasi.isEmpty()
                || role == null || role.isEmpty()) {

            AlertUtil.warning(
                    "Registrasi Gagal",
                    "Semua field harus diisi."
            );
            return;
        }

        if (!password.equals(konfirmasi)) {
            AlertUtil.error(
                    "Registrasi Gagal",
                    "Password dan konfirmasi password tidak cocok."
            );
            return;
        }

        if (password.length() < 6) {
            AlertUtil.warning(
                    "Registrasi Gagal",
                    "Password minimal 6 karakter."
            );
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            AlertUtil.warning(
                    "Registrasi Gagal",
                    "Format email tidak valid."
            );
            return;
        }

        try {
            dao.register(
                    nama,
                    username,
                    email,
                    password,
                    role
            );

            AlertUtil.info(
                    "Registrasi Berhasil",
                    "Akun berhasil dibuat. Silakan login menggunakan akun baru Anda."
            );

            MainApp.showLogin();

        } catch (Exception e) {
            AlertUtil.error(
                    "Registrasi Gagal",
                    "Terjadi kesalahan: " + e.getMessage()
            );
        }
    }

    public void goToLogin() {
        MainApp.showLogin();
    }
}