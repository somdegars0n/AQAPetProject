package org.example;

import org.example.interfaces.Sender;

public class EncryptedSender implements Sender {
  private final Sender wrappedSender;
  private final String encryptionKey;

  public EncryptedSender(Sender wrappedSender, String encryptionKey) {
    this.wrappedSender = wrappedSender;
    this.encryptionKey = encryptionKey;
  }

  @Override
  public String send(String url, String path, String body) {
    String encryptedBody = encrypt(body);
    return wrappedSender.send(url, path, encryptedBody);
  }

  @Override
  public String send(String url, String path) {
    return wrappedSender.send(url, path);
  }

  @Override
  public String send(String url) {
    return wrappedSender.send(url);
  }

  @Override
  public String send() {
    return wrappedSender.send();
  }

  private String encrypt(String text) {
    // Простая имитация шифрования для примера
    if (text == null) return null;
    return "ENCRYPTED[" + text + "]";
  }
}