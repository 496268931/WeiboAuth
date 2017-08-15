package com.wiseweb.tools;

import java.io.ByteArrayOutputStream;

/**
 * Base64编码要求把3个8位字节（3*8=24）转化为4个6位的字节（4*6=24），之后在6位的前面补两个0，形成8位一个字节的形式。 
 * 如果剩下的字符不足3个字节，则用0填充，
 *
 * 标准的base64如下默认实现；输出字符使用'='，因此编码后输出的文本末尾可能会出现1或2个'='。
 * D是素数，N是两个素数(P,Q)之积，
 * (D * E) mod ((P-1) * (Q-1))=1
 * C=(A EXP D)mod N 
 *
 * @author anxlng@sina.com
 *
 */
public class Base64 {
    //标准加密密钥，这个是标准的base64字符排序；
    private static final char[] DEFAULT_ENCODER_TABLE =
            new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private static final byte DEFAULT_PAD =  0x3d; // 字符 ‘=’, 标准base64加密末位补字符

    private static final String URL_SAFE_ENCODE =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private final char[] encodeTable;
    private final byte[] decodeTable;
    private final byte padding;
    private final int seed;

    public Base64() {
        this.encodeTable = DEFAULT_ENCODER_TABLE;
        this.decodeTable = calculateDecodeTable(this.encodeTable);
        this.padding = DEFAULT_PAD;
        this.seed = -1;
    }

    public Base64(boolean urlSafe) {
        if (urlSafe) {
            this.encodeTable = URL_SAFE_ENCODE.toCharArray();
            this.padding = -1;
        } else {
            this.encodeTable = DEFAULT_ENCODER_TABLE;
            this.padding = DEFAULT_PAD;
        }
        this.decodeTable = calculateDecodeTable(this.encodeTable);
        this.seed = -1;
    }

    public Base64(int seed) {
        this.seed = seed;
        char[] table = createEncodeTable(seed);
        char[] _encodeTable = new char[64];
        System.arraycopy(table, 0, _encodeTable, 0, _encodeTable.length);
        this.encodeTable = _encodeTable;
        this.decodeTable = calculateDecodeTable(this.encodeTable);
        padding = (byte)(table[table.length-1] & 0xff);
    }

    public int getSeed() {
        return seed;
    }

    public String encode(byte[] data) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < data.length; ) {
            // 三个字节 8 * 3 转成 6 * 4的四个字节
            int b1 = data[i++] & 0xff;
            if (i == data.length) { //说明多了一个字节，后边补两个 ‘=’ 字符
                buff.append(encodeTable[b1 >>> 2]);
                buff.append(encodeTable[(b1 & 0x03) << 4]);
                if (encodeTable == DEFAULT_ENCODER_TABLE) {
                    buff.append((char)padding);
                    buff.append((char)padding);
                }
                break;
            }

            int b2 = data[i++] & 0xff;
            if (i == data.length) { //说明多了两个个字节，后边补一个 ‘=’ 字符
                buff.append(encodeTable[b1 >>> 2]);
                buff.append(encodeTable[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                buff.append(encodeTable[(b2 & 0x0f) << 2]);
                if (encodeTable == DEFAULT_ENCODER_TABLE) {
                    buff.append((char)padding);
                }
                break;
            }

            int b3 = data[i++] & 0xff;
            buff.append(encodeTable[b1 >>> 2]);
            buff.append(encodeTable[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            buff.append(encodeTable[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            buff.append(encodeTable[b3 & 0x3f]);
        }
        return buff.toString();
    }

    public byte[] decode(String data) {
        char[] chs = data.toCharArray();
        int len = chs.length;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(len);
        byte b1, b2, b3, b4;
        for (int i = 0; i < len; ) {
            do {
                b1 = decodeTable[chs[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) { //说明已完成字符解码；
                break;
            }

            do {
                b2 = decodeTable[chs[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) { //说明已完成字符解码；
                break;
            }
            buffer.write((b1 << 2) | ((b2 >>> 4) & 0x03));

            if (i == len) {
                return buffer.toByteArray();
            }

            do {
                int p = chs[i++];
                if (p == padding) { //如果找到结尾，则
                    return buffer.toByteArray();
                }
                b3 = decodeTable[p];
            } while (i < len && b3 == -1);
            if (b3 == -1) { //说明已完成字符解码；
                break;
            }
            buffer.write(((b2 << 4) & 0xf0) | ((b3 >>> 2) & 0x0f));

            if (i == len) {
                return buffer.toByteArray();
            }

            do {
                int p = chs[i++];
                if (p == padding) { //如果找到结尾，则
                    return buffer.toByteArray();
                }
                b4 = decodeTable[p];
            } while (i < len && b4 == -1);
            if (b4 == -1) { //说明已完成字符解码；
                break;
            }
            buffer.write(((b3 << 6) & 0xc0) | (b4 & 0x3f));
        }
        return buffer.toByteArray();
    }

    /** 这个主要是在定义了编码规则后，生成一个解码表*/
    private byte[] calculateDecodeTable(char[] encodeTable) {
        byte[] decodeTable = new byte[128];
        for (int i = 0; i < 128; i++) {
            decodeTable[i] = -1;
        }
        for (int i = 0; i < 64; i++) {
            int code = encodeTable[i];
            decodeTable[code] = (byte)(i & 0xff);
        }
        return decodeTable;
    }

    /**
     * 根据种子参数，生成一个加密表；共65位 ；加密表为64位,最后一位为末尾标记符
     * @param seed
     * @return
     */
    private char[] createEncodeTable(int seed) {
        long multiplier = getPrime(seed);
        char [] tab = new char[127];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = (char)i;
        }

        for (int i = 0x21; i < tab.length; i++) { //减去前边33位不可见字符
            int n = getNumber(i, multiplier);
            if (n > 0x21) {
                char temp = tab[i];
                tab[i] = tab[n];
                tab[n] = temp;
            }
        }

        char[] result = new char[65];
        System.arraycopy(tab, 0x21, result, 0, result.length);
        return result;
    }

    private int getNumber(long index, long multiplier) {
        // multiplier 和 0xbL为连个素数；
        return (int)((index * multiplier) + 0xbL) % 127;
    }

    /**
     * 获取第 index 位的素数；从2开始；
     * @param index
     * @return
     */
    private long getPrime(int index) {
        final int _index = index % 4096; //不能太大；
        int count = 0;

        for (int n = 2; n < Integer.MAX_VALUE; n++) {
            if (isPrime(n)) {
                count++;
            }
            if (count >= _index) {
                return n;
            }
        }
        return 0x5DEECE66DL; //若没找到则使用这个素数
    }

    private boolean isPrime(int num) {
        if (num == 2) {
            return true;
        }

        if (num < 2) {
            return false;
        }
        long max = Math.round(Math.sqrt(num));
        for (int i = 2; i < max; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        byte[] a = "1111wo是中国人1".getBytes();
        String b = new Base64().encode(a);
        System.out.println(b);
        byte[] c = new Base64().decode(b);
        System.out.println(new String(c));
    }
}
