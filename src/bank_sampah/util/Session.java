package bank_sampah.util;

public class Session {
    private static int idUser;
    private static String nama;
    private static String username;
    private static String role;

    public static void setUser(int idUser, String nama, String username, String role) {
        Session.idUser = idUser;
        Session.nama = nama;
        Session.username = username;
        Session.role = role;
    }

    public static boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }

    public static void clear() {
        idUser = 0;
        nama = null;
        username = null;
        role = null;
    }

    public static int getIdUser() { return idUser; }
    public static String getNama() { return nama == null ? "Admin Utama" : nama; }
    public static String getUsername() { return username; }
    public static String getRole() { return role == null ? "admin" : role; }
}
