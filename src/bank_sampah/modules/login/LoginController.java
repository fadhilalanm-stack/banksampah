package bank_sampah.modules.login;

import bank_sampah.MainApp;
import bank_sampah.util.AlertUtil;
import bank_sampah.util.Session;

public class LoginController {
    private final LoginDAO dao = new LoginDAO();

    public void login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            AlertUtil.warning("Login Gagal", "Username dan password harus diisi.");
            return;
        }

        LoginDAO.UserLogin user = dao.login(username, password);
        if (user != null) {
            Session.setUser(user.idUser, user.nama, user.username, user.role);
            AlertUtil.info("Login Berhasil", "Selamat datang di Sistem Informasi Bank Sampah. APAPUN YANG MANAUDHRHRBR, TEMPEKKKKKK");
            MainApp.showAdminPage("dashboard");
        } else {
            AlertUtil.error("Login Gagal", "Username atau password salah.");
        }
    }
}
