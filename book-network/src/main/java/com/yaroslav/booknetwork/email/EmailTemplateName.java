package com.yaroslav.booknetwork.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    // Перечисление названий шаблонов электронной почты
    ACTIVATE_ACCOUNT("activate_account"); // Шаблон для активации аккаунта

    private final String name; // Поле для хранения названия шаблона

    // Конструктор для инициализации названия шаблона
    EmailTemplateName(String name) {
        this.name = name;
    }
}
