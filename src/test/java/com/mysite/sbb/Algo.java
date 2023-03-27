package com.mysite.sbb;

import org.junit.jupiter.api.Test;

public class Algo {

    @Test
    void name() {
        int n = 2;
        long[] answer = new long[n + 1];
        //-----------------------------//

        answer[1] = 1;
        answer[2] = 2;
        for (int i = 3; i < answer.length; i++)
            answer[i] = (answer[i - 2] + answer[i - 1]) % 1234567;

        System.out.println(answer[n]);
    }
}
