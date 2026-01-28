package com.peatroxd.congratsinator.testdata;

import java.time.LocalDate;

public final class JsonBodies {

    public static String createPersonRequest(String name, LocalDate birthday) {
        return """
                {"name":"%s","birthday":"%s"}
                """.formatted(escape(name), birthday);
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private JsonBodies() {}
}
