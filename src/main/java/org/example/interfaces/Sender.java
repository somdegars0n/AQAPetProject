package org.example.interfaces;

public interface Sender {
  String send(String url, String path, String body);
  String send(String url, String path);
  String send(String url);
  String send();
}