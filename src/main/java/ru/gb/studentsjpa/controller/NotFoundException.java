package ru.gb.studentsjpa.controller;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String exception) {
    super(exception);
  }
}
