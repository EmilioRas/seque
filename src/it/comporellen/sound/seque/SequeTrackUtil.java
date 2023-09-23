package seque;

public class SequeTrackUtil {

        private static final String digits = "0123456789ABCDEF";
        public String integerToHex(int input) {
            if (input <= 0)
                return "0";
            StringBuilder hex = new StringBuilder();
            while (input > 0) {
                int digit = input % 16;

                hex.insert(0, digits.charAt(digit));

                input = input / 16;
            }
            return hex.toString();
        }

        public String[] hexStringArray(byte[] btr){
            String[] buff = new String[btr.length];
            for (int i = 0; i < btr.length; i++){
                int real16 = (btr[i] & 0xFF);
                buff[i] =  this.integerToHex(real16);
            }
            return buff;
        }
}
