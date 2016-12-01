package com.wang.math.encrypt.rsa;

import com.wang.java_util.MathUtil;

import java.math.BigInteger;

/**
 * by wangrongjun on 2016/12/1.
 */
public class RSA {

    private BigInteger p1;
    private BigInteger p2;
    private BigInteger e;
    private BigInteger n;
    private BigInteger d;

    public RSA(int bit) {
//        1.生成两个大素数
        p1 = RSAUtil.createBigPrime(bit);
        p2 = RSAUtil.createBigPrime(bit);
//        p1 = new BigInteger("101");
//        p2 = new BigInteger("113");

//        2.求n
        n = p1.multiply(p2);

//        3.求d
        BigInteger fn = p1.subtract(BigInteger.ONE).//fn=(p1-1)*(p2-1)
                multiply(p2.subtract(BigInteger.ONE));
        e = new BigInteger("3");
        while (fn.mod(e).compareTo(BigInteger.ONE) != 0) {//while(e % fn != 1) 保证e与fn互质
            e = e.add(BigInteger.ONE);
        }

        System.out.println("p1=" + p1);
        System.out.println("p2=" + p2);
        System.out.println("n=" + n);
        System.out.println("e=" + e);
        System.out.println("fn=" + fn);

        d = RSAUtil.modReverse(e, fn);
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    /**
     * RSA加密
     *
     * @param data 其中的clear的位数要小于n位
     * @return 返回密文。若clear的位数超出，返回null
     */
    public static BigInteger encode(RSAData data) {
        BigInteger clear = data.getClear();
        BigInteger e = data.getE();
        BigInteger n = data.getN();
        if (clear.toString().length() >= n.toString().length()) {
            return null;
        }
        return MathUtil.indexModule(clear, e, n);
    }

    public BigInteger decode(BigInteger cipher) {
        return MathUtil.indexModule(cipher, d, n);
    }

}
