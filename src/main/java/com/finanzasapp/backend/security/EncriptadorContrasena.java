package com.finanzasapp.security;

import org.mindrot.jbcrypt.BCrypt;

public class EncriptadorContrasena {

    private EncriptadorContrasena() {
    }

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificar(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
