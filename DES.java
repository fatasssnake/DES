public class DES {
    //置换选择1矩阵
    static final int[] PC_1C = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36
    };
    static final int[] PC_1D = {
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    //循环左移位数表
    static final int[] LeftMove = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    //置换选择2矩阵
    static final int[] PC_2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    /**
     * 子密钥的产生
     *
     * @param sKey 64位密钥
     * @return 16个48位子密钥
     */
    static byte[][] generateKeys(byte[] sKey) {
        byte[] C = new byte[28];
        byte[] D = new byte[28];
        byte[][] keys = new byte[16][48];
        //置换选择1
        for (int i = 0; i < 28; i++) {
            C[i] = sKey[PC_1C[i] - 1];
            D[i] = sKey[PC_1D[i] - 1];
        }
        for (int i = 0; i < 16; i++) {
            //循环左移
            C = RSHR(C, LeftMove[i]);
            D = RSHR(D, LeftMove[i]);
            //置换选择2
            for (int j = 0; j < 48; j++) {
                if (PC_2[j] <= 28) {
                    keys[i][j] = C[PC_2[j] - 1];
                } else {
                    keys[i][j] = D[PC_2[j] - 29];
                }
            }
        }
        return keys;
    }

    /**
     * 循环左移
     *
     * @param b 数组
     * @param n 位数
     * @return
     */
    static byte[] RSHR(byte[] b, int n) {
        String s = new String(b);
        s = (s + s.substring(0, n)).substring(n);
        return s.getBytes();
    }

    //初始置换矩阵
    static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    /**
     * 初始置换IP
     *
     * @param text 64位数据
     * @return
     */
    static byte[] IP(byte[] text) {
        byte[] newtext = new byte[64];
        for (int i = 0; i < 64; i++) {
            newtext[i] = text[IP[i] - 1];
        }
        return newtext;
    }

    //选择运算矩阵
    static int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    //代替函数组
    static final int[][][] S_Box = {
            //S1
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            //S2
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            //S3
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            //S4
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            //S5
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            //S6
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            //S7
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            //S8
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    //置换运算矩阵
    static int[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    /**
     * 轮函数
     *
     * @param A 32位输入
     * @param K 48位子密钥
     * @return 32位输出
     */
    static byte[] f(byte[] A, byte[] K) {
        byte[] t = new byte[48];
        byte[] r = new byte[32];
        byte[] result = new byte[32];
        //选择运算E
        for (int i = 0; i < 48; i++) {
            t[i] = A[E[i] - 1];
        }
        //模2相加
        for (int i = 0; i < 48; i++) {
            t[i] = (byte) (t[i] ^ K[i]);
        }
        //代替函数组S
        for (int i = 0, a = 0; i < 48; i += 6, a += 4) {
            int j = t[i] * 2 + t[i + 5];   //b1b6
            int k = t[i + 1] * 8 + t[i + 2] * 4 + t[i + 3] * 2 + t[i + 4];   //b2b3b4b5
            byte[] b = Integer.toBinaryString(S_Box[i / 6][j][k] + 16).substring(1).getBytes();
            for (int n = 0; n < 4; n++) {
                r[a + n] = (byte) (b[n] - '0');
            }
        }
        //置换运算P
        for (int i = 0; i < 32; i++) {
            result[i] = r[P[i] - 1];
        }
        return result;
    }
    //逆初始置换矩阵
    static final int[] IP_1 = {
            40,  8, 48, 16, 56, 24, 64, 32,
            39,  7, 47, 15, 55, 23, 63, 31,
            38,  6, 46, 14, 54, 22, 62, 30,
            37,  5, 45, 13, 53, 21, 61, 29,
            36,  4, 44, 12, 52, 20, 60, 28,
            35,  3, 43, 11, 51, 19, 59, 27,
            34,  2, 42, 10, 50, 18, 58, 26,
            33,  1, 41,  9, 49, 17, 57, 25
    };
    /**
     * 逆初始置换IP^-1
     * @param text  64位数据
     * @return
     */
    static byte[] rIP(byte[] text) {
        byte[] newtext = new byte[64];
        for (int i = 0; i < 64; i++) {
            newtext[i] = text[IP_1[i] - 1];
        }
        return newtext;
    }
    /**
     * 加密
     * @param plaintext  64位明文
     * @param sKey       64位密钥
     * @return           64位密文
     */
    public byte[] encrypt(byte[] plaintext, byte[] sKey) {
        byte[][] L = new byte[17][32];
        byte[][] R = new byte[17][32];
        byte[] ciphertext = new byte[64];
        //子密钥的产生
        byte[][] K = DES.generateKeys(sKey);
        //初始置换IP
        plaintext = DES.IP(plaintext);
        //将明文分成左半部分L0和右半部分R0
        for (int i = 0; i < 32; i++) {
            L[0][i] = plaintext[i];
            R[0][i] = plaintext[i + 32];
        }
        //加密迭代
        for (int i = 1; i <= 16; i++) {
            L[i] = R[i - 1];
            R[i] = xor(L[i - 1], DES.f(R[i - 1], K[i - 1]));
        }
        //以R16为左半部分，L16为右半部分合并
        for (int i = 0; i < 32; i++) {
            ciphertext[i] = R[16][i];
            ciphertext[i + 32] = L[16][i];
        }
        //逆初始置换IP^-1
        ciphertext = DES.rIP(ciphertext);
        return ciphertext;
    }
    /**
     * 解密
     * @param ciphertext  64位密文
     * @param sKey        64位密钥
     * @return            64位明文
     */
    public byte[] decrypt(byte[] ciphertext, byte[] sKey) {
        byte[][] L = new byte[17][32];
        byte[][] R = new byte[17][32];
        byte[] plaintext = new byte[64];
        //子密钥的产生
        byte[][] K = DES.generateKeys(sKey);
        //初始置换IP
        ciphertext = DES.IP(ciphertext);
        //将密文分成左半部分R16和右半部分L16
        for (int i = 0; i < 32; i++) {
            R[16][i] = ciphertext[i];
            L[16][i] = ciphertext[i + 32];
        }
        //解密迭代
        for (int i = 16; i >= 1; i--) {
            L[i - 1] = xor(R[i], DES.f(L[i], K[i - 1]));
            R[i - 1] = L[i];
            R[i] = xor(L[i - 1], DES.f(R[i - 1], K[i - 1]));
        }
        //以L0为左半部分，R0为右半部分合并
        for (int i = 0; i < 32; i++) {
            plaintext[i] = L[0][i];
            plaintext[i + 32] = R[0][i];
        }
        //逆初始置换IP^-1
        plaintext = DES.rIP(plaintext);
        return plaintext;
    }
    /**
     * 两数组异或
     * @param a
     * @param b
     * @return
     */
    static byte[] xor(byte[] a, byte[] b) {
        byte[] c = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            c[i] = (byte) (a[i] ^ b[i]);
        }
        return c;
    }


}
