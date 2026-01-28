package com.peatroxd.congratsinator.testdata;

import java.util.UUID;

public final class Paths {
    public static String photoFor(UUID id) {
        return "photos/" + id + ".jpg";
    }

    private Paths() {
    }
}
