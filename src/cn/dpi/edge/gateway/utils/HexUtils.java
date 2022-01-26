package cn.dpi.edge.gateway.utils;
/**
 * 
 * @author jiangtao
 *
 */
public class HexUtils {
	private static final char[] HEXES = {
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f'
    };

    /**
     * byte数组 转换成 16进制大写字符串
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        String hex = new String();

        for ( int i=0;i<bytes.length;i++) {
        	byte b=bytes[i];
            hex+=(HEXES[(b >> 4) & 0x0F]);
            hex+=(HEXES[b & 0x0F]);
        }

        return hex.toUpperCase();
    }

    /**
     * 16进制字符串 转换为对应的 byte数组
     */
    public static byte[] hex2Bytes(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[hexChars.length / 2];   // 如果 hex 中的字符不是偶数个, 则忽略最后一个

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt("" + hexChars[i * 2] + hexChars[i * 2 + 1], 16);
        }

        return bytes;
    }



}
