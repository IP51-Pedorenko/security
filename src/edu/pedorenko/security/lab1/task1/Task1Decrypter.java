package edu.pedorenko.security.lab1.task1;

public class Task1Decrypter {

    private static final String encodedString = "]|d3gaj3r3avcvrgz}t>xvj3K\\A3pzc{va=3V=t=3zg3`{|f\177w3grxv3r3`gaz}t31{v\177\177|3d|a\177w13r}w?3tzev}3g{v3xvj3z`31xvj1?3k|a3g{v3uza`g3\177vggva31{13dzg{31x1?3g{v}3k|a31v13dzg{31v1?3g{v}31\17713dzg{31j1?3r}w3g{v}3k|a3}vkg3p{ra31\17713dzg{31x13rtrz}?3g{v}31|13dzg{31v13r}w3`|3|}=3J|f3~rj3f`v3z}wvk3|u3p|z}pzwv}pv?3[r~~z}t3wz`gr}pv?3Xr`z`xz3vkr~z}rgz|}?3`grgz`gzpr\1773gv`g`3|a3d{rgveva3~vg{|w3j|f3uvv\1773d|f\177w3`{|d3g{v3qv`g3av`f\177g=";

    public static void main(String[] args) {

        for (int i = 0; i < 256; ++i) {
            String decodedString = decodeWithKey(encodedString, i);//19

            if (isOkayString(decodedString)) {
                System.out.println("Key is " + i);
                System.out.println(decodedString);
            }
        }
    }

    private static boolean isOkayString(String decodedString) {

        int spaces = 0;
        for (char c : decodedString.toCharArray()) {
            if (!(c >= 32 && c <= 127)) {
                return false;
            }
            if (c == ' ') {
                spaces++;
            }
        }

        return spaces >= decodedString.length() / 7; //average length of English words are 4.5 letters
    }

    private static String decodeWithKey(String encodedString, int key) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < encodedString.length(); ++i) {
            char encodedChar = encodedString.charAt(i);
            char decodedChar = (char) (encodedChar ^ key);
            sb.append(decodedChar);
        }

        return sb.toString();
    }
}
