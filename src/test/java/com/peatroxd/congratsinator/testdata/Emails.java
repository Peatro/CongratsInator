package com.peatroxd.congratsinator.testdata;

import java.util.List;

public final class Emails {
    public static List<String> recipients() {
        return List.of("a@ex.com", "b@ex.com");
    }

    private Emails() {
    }
}
