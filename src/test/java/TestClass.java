import org.junit.jupiter.api.Test;

public class TestClass {

    @Test
    void testThis(){
//        int i = 22;
//        System.out.println(i/7);

        String result = "+CMGL: 0,\"REC READ\",\"AD-PHONPE\",,\"21/03/12,20:30:02+32\"\n" +
                "38468 is your one time password to proceed on PhonePe. It is valid for 10 minutes. Do not share your OTP with anyone.\n" +
                "+CMGL: 1,\"REC READ\",\"AA-AIRGSF\",,\"21/03/12,20:02:33+22\"\n" +
                "Not able to call? Outgoing services are inactive for 8686345820. To restart, recharge NOW with an Airtel Unlimited Pack u.airtel.in/FDP1 Ignore if recharged\n";

        String[] strs = result.split("\"(?:,)\"|(?:\n)"); //replace("\"", "")

        int i=1;
        for(String s: strs){
            System.out.println(i+"::: " +s );
            i++;
        }

    }

}
