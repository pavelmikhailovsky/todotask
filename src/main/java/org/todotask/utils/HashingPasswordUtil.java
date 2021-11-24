package org.todotask.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class HashingPasswordUtil {

    public String getHashPassword(String stringToEncrypt) {
        return BCrypt.hashpw(stringToEncrypt, BCrypt.gensalt(10));
    }

    public boolean isHashedPassword(String hashedString, String string) {
        return BCrypt.checkpw(hashedString, string);
    }

}
