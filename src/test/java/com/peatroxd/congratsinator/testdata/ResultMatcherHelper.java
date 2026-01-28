package com.peatroxd.congratsinator.testdata;

import org.springframework.test.web.servlet.ResultMatcher;

public final class ResultMatcherHelper {

    public static ResultMatcher all(ResultMatcher... matchers) {
        return result -> {
            for (ResultMatcher matcher : matchers) {
                matcher.match(result);
            }
        };
    }

    private ResultMatcherHelper() {}
}
