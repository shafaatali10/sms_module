package com.shafaat.apps.sms;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestClass {

    @Test
    void testSplit(){
        String msg = "+CMGL: 0,\"REC UNREAD\",\"AD-PHONPE\",,\"21/03/11,21:57:24+32\"\n" +
            "19514 is your one time password to proceed on PhonePe. It is valid for 10 minutes. Do not share your OTP with anyone.\n" +
            "+CMGL: 0,\"REC UNREAD\",\"AD-PHONPE\",,\"21/03/11,21:57:24+32\"\n" +
            "19514 is your one time password to proceed on PhonePe. It is valid for 10 minutes. Do not share your OTP with anyone.\n" +
            "\n" +
            "OK";

        //String[] strs = msg.replace("\"", "").split("(?:,)|(?:\r\n)");
        msg = msg.replace("\"", ""); // Remove all the double quotes
//        String[] strs= msg.split(",");
//        String temp = strs[strs.length-1];

        //String[] strs = msg.replace("\"", "").split("(?:,)|(?:\r\n)");
        String[] strs = msg.replace("\"", "").split("(?:,)|(?:\n)|(?:\r)");
        int i=1;
        for(String s: strs){
            System.out.println(i+":: " + s);
            i++;
        }


        assertThat("s").isEqualTo("s");


    }
}
