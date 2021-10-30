public class Test {
    public static void main(String[] args) {
        long a= System.currentTimeMillis();//获取当前系统时间(毫秒)
        DES des=new DES();
        byte[] sKey={0,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1,0,0,1,1,1,0,0,0};
        byte[] plaintext={0,0,1,1,0,0,0,0,0,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,0,0,1,1,0,0,1,1,0,0,1,1,0,1,0,0,0,0,1,1,0,1,0,1,0,0,1,1,0,1,1,0,0,0,1,1,0,1,1,1};
        byte[] ciphertext=des.encrypt(plaintext,sKey);
        byte[] dPlaintext=des.decrypt(ciphertext,sKey);
        for (int i = 0; i < ciphertext.length; i++) {
            if((i)%8==0){
                System.out.print(" ");
            }
            System.out.print(ciphertext[i]);
        }
        System.out.println();
        for (int i = 0; i < dPlaintext.length; i++) {
            if((i)%8==0){
                System.out.print(" ");
            }
            System.out.print(dPlaintext[i]);
        }
        /**
         * 密钥：00110001 00110010 00110011 00110100 00110101 00110110 00110111 00111000
         * 明文：00110000 00110001 00110010 00110011 00110100 00110101 00110110 00110111
         */
        System.out.println();
        System.out.println(System.currentTimeMillis()-a+"毫秒");
    }
}
