package com.alura.Literalura.model;

public enum Idioma {
    EN ("en"),
    ES ("es"),
    FR("fr"),
    PT("pt");

    private final String idioma;

    Idioma(String idioma){
        this.idioma = idioma;

    }

    public String getIdioma() {
        return idioma;
    }

    public static Idioma toString(String text) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.idioma.equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningun idioma encontrado" + text);
    }
}
