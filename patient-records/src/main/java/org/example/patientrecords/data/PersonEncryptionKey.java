package org.example.patientrecords.data;


import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.persistence.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static java.util.Objects.requireNonNull;

@Entity
public class PersonEncryptionKey extends AbstractPersistable<Long> {

    private static final int IV_LENGTH = 16;
    private static final String KEY_GENERATION_ALGORITHM = "AES";
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final SecureRandom RND = new SecureRandom();

    private static IvParameterSpec generateIv() {
        var iv = new byte[IV_LENGTH];
        RND.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @OneToOne(optional = false)
    @JoinColumn(nullable = false, unique = true)
    private Person person;

    private String secretKeyBase64;

    @Transient
    private SecretKey secretKey;

    protected PersonEncryptionKey() {
    }

    @PostLoad
    void onLoad() {
        deserializeSecretKey();
    }

    private void serializeSecretKey() {
        if (secretKey == null) {
            secretKeyBase64 = null;
        } else {
            var keySpec = new X509EncodedKeySpec(secretKey.getEncoded());
            secretKeyBase64 = Base64.getEncoder().encodeToString(keySpec.getEncoded());
        }
    }

    private void deserializeSecretKey() {
        if (secretKeyBase64 == null) {
            secretKey = null;
        } else {
            var secretKeyBytes = Base64.getDecoder().decode(secretKeyBase64);
            var keySpec = new X509EncodedKeySpec(secretKeyBytes);
            try {
                var keyFactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALGORITHM);
                secretKey = keyFactory.generateSecret(keySpec);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                throw new IllegalStateException("Could not deserialize key", ex);
            }
        }
    }

    public PersonEncryptionKey(Person person) {
        this.person = person;
        try {
            var keyGenerator = KeyGenerator.getInstance(KEY_GENERATION_ALGORITHM);
            keyGenerator.init(KEY_SIZE);
            secretKey = keyGenerator.generateKey();
            serializeSecretKey();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not generate key", ex);
        }
    }

    public byte[] encrypt(byte[] input) {
        if (secretKey == null) {
            throw new IllegalStateException("No secret key");
        }
        requireNonNull(input, "input must not be null");
        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            var iv = generateIv();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            var cipherBytes = cipher.doFinal(input);
            var byteBuf = ByteBuffer.allocate(cipherBytes.length + iv.getIV().length);
            byteBuf.put(iv.getIV());
            byteBuf.put(cipherBytes);
            return byteBuf.array();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new IllegalArgumentException("Could not encrypt input", ex);
        }
    }

    public String encrypt(String input) {
        requireNonNull(input, "input must not be null");
        return Base64.getEncoder().encodeToString(encrypt(input.getBytes(StandardCharsets.UTF_8)));
    }

    public byte[] decrypt(byte[] input) {
        if (secretKey == null) {
            throw new IllegalStateException("No secret key");
        }
        requireNonNull(input, "input must not be null");
        try {
            var cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            var iv = new IvParameterSpec(input, 0, IV_LENGTH);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return cipher.doFinal(input, IV_LENGTH, input.length - IV_LENGTH);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new IllegalArgumentException("Could not decrypt input", ex);
        }
    }

    public String decrypt(String input) {
        requireNonNull(input, "input must not be null");
        return new String(decrypt(Base64.getDecoder().decode(input)), StandardCharsets.UTF_8);
    }

    public Person getPerson() {
        return person;
    }

    public void deleteKey() {
        secretKey = null;
        secretKeyBase64 = null;
    }
}
