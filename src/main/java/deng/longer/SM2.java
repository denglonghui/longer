package deng.longer;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.ECPoint;
import java.util.Base64;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECCurve.Fp;
import org.bouncycastle.math.ec.ECFieldElement;

public class SM2 {

 

 

    //正式参数

    public static String[] ecc_param = {

            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF",

            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC",

            "28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93",

            "FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123",

            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7",

            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0"

    };

 

    private static SM2 instance;

 

 

    private BigInteger ecc_p;

    private BigInteger ecc_a;

    private BigInteger ecc_b;

    private BigInteger ecc_n;

    private BigInteger ecc_gx;

    private BigInteger ecc_gy;

    private ECCurve.Fp ecc_curve;

    private ECPoint.Fp ecc_point_g;

    private ECDomainParameters ecc_bc_spec;

    private ECKeyPairGenerator ecc_key_pair_generator;

    private ECFieldElement ecc_gx_fieldelement;

    private ECFieldElement ecc_gy_fieldelement;

 

 

    private BigInteger privateKey;

    private ECPoint publicKey;

 

    private SM2() {

        this.ecc_p = new BigInteger(ecc_param[0], 16);

        this.ecc_a = new BigInteger(ecc_param[1], 16);

        this.ecc_b = new BigInteger(ecc_param[2], 16);

        this.ecc_n = new BigInteger(ecc_param[3], 16);

        this.ecc_gx = new BigInteger(ecc_param[4], 16);

        this.ecc_gy = new BigInteger(ecc_param[5], 16);

 

        ecc_gx_fieldelement = new Fp(this.ecc_p, this.ecc_gx);

        ecc_gy_fieldelement = new Fp(this.ecc_p, this.ecc_gy);

 

        ecc_curve = new ECCurve.Fp(this.ecc_p, this.ecc_a, this.ecc_b);

        ecc_point_g = new ECPoint.Fp(this.ecc_curve, this.ecc_gx_fieldelement, this.ecc_gy_fieldelement);

 

        ecc_bc_spec = new ECDomainParameters(this.ecc_curve, this.ecc_point_g, this.ecc_n);

 

        ECKeyGenerationParameters ecc_ecgenparam;

        ecc_ecgenparam = new ECKeyGenerationParameters(ecc_bc_spec, new SecureRandom());

 

        ecc_key_pair_generator = new ECKeyPairGenerator();

        ecc_key_pair_generator.init(ecc_ecgenparam);

 

 

        AsymmetricCipherKeyPair key = ecc_key_pair_generator.generateKeyPair();

        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();

        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();

        privateKey = ecpriv.getD();

        publicKey = ecpub.getQ();

 

    }

 

 

    public static SM2 getInstance() {

        if (instance == null) {

            synchronized (SM2.class) {

                if (instance == null) {

                    instance = new SM2();

                }

            }

        }

        return instance;

    }

 

 

    //数据加密

    public String encrypt(byte[] data) throws IOException {

 

        byte[] source = new byte[data.length];

        System.arraycopy(data, 0, source, 0, data.length);

 

        CipherUtil cipher = new CipherUtil();

 

//        ECPoint userKey = ecc_curve.decodePoint(publicKey);

 

        ECPoint c1 = cipher.Init_enc(ecc_key_pair_generator, publicKey);   //userKey

        cipher.Encrypt(source);

        byte[] c3 = new byte[32];

        cipher.Dofinal(c3);

 

//      System.out.println("C1 " + StrUtil.byteToHex(c1.getEncoded()));

//      System.out.println("C2 " + StrUtil.byteToHex(source));

//      System.out.println("C3 " + StrUtil.byteToHex(c3));

        //C1 C2 C3拼装成加密字串

 

        byte[] result = new byte[c1.getEncoded().length + source.length + c3.length];

 

 

        return StrUtil.byteToHex(c1.getEncoded()) + StrUtil.byteToHex(source) + StrUtil.byteToHex(c3);

 

    }

 

    //数据加密

    public byte[] encryptByte(byte[] data) throws IOException {

 

        byte[] source = new byte[data.length];

        System.arraycopy(data, 0, source, 0, data.length);

 

        CipherUtil cipher = new CipherUtil();

 

//        ECPoint userKey = ecc_curve.decodePoint(publicKey);

 

        ECPoint c1 = cipher.Init_enc(ecc_key_pair_generator, publicKey);   //userKey

        cipher.Encrypt(source);

        byte[] c3 = new byte[32];

        cipher.Dofinal(c3);

 

//      System.out.println("C1 " + StrUtil.byteToHex(c1.getEncoded()));

//      System.out.println("C2 " + StrUtil.byteToHex(source));

//      System.out.println("C3 " + StrUtil.byteToHex(c3));

        //C1 C2 C3拼装成加密字串

 

        byte[] result = new byte[c1.getEncoded().length + source.length + c3.length];

        System.arraycopy(c1.getEncoded(), 0, result, 0, c1.getEncoded().length);

        System.arraycopy(source, 0, result, c1.getEncoded().length, source.length);

        System.arraycopy(c3, 0, result, c1.getEncoded().length + source.length, c3.length);

 

        return result;

 

    }

 

 

    //数据解密

    public byte[] decrypt(byte[] encryptedData) throws IOException {

 

        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2

        String data = StrUtil.byteToHex(encryptedData);

        /***分解加密字串

         * （C1 = C1标志位2位 + C1实体部分128位 = 130）

         * （C3 = C3实体部分64位  = 64）

         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）

         */

        byte[] c1Bytes = StrUtil.hexToByte(data.substring(0, 130));

        int c2Len = encryptedData.length - 97;

        byte[] c2 = StrUtil.hexToByte(data.substring(130, 130 + 2 * c2Len));

        byte[] c3 = StrUtil.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

 

 

        // BigInteger userD = new BigInteger(1, privateKey);

 

        //通过C1实体字节来生成ECPoint

        ECPoint c1 = ecc_curve.decodePoint(c1Bytes);

        CipherUtil cipher = new CipherUtil();

        cipher.Init_dec(privateKey, c1);   //userKey

        cipher.Decrypt(c2);

        cipher.Dofinal(c3);

 

        //返回解密结果

        return c2;

    }

 

 

    public String Sm2Sign(byte[] md) {

 

        byte[] pk = publicKey.getEncoded();

        byte[] prk = privateKey.toByteArray();

        SM3Digest sm3 = new SM3Digest();

 

        byte[] pkX = SubByte(pk, 0, 32);

        byte[] pkY = SubByte(pk, 32, 32);

 

        byte[] z = sm3.getSM2Za(pkX, pkY, "1234567812345678".getBytes());

 

        sm3.update(z, 0, z.length);

 

        byte[] p = md;

        sm3.update(p, 0, p.length);

 

        byte[] hashData = new byte[32];

        sm3.doFinal(hashData, 0);

 

        // e

        BigInteger e = new BigInteger(1, hashData);

        // k

        BigInteger k = null;

        BigInteger r = null;

        BigInteger s = null;

        BigInteger userD = null;

        BigInteger x = new BigInteger(1, pkX);

        BigInteger pr = new BigInteger(1, prk);

        do {

            do {

 

                // ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters)

                // keypair

                // .getPrivate();

                k = pr;

                // ecpriv.getD().toString(16);//私钥

                // kp = ecpub.getQ();//pk

 

                userD = pr;

 

                // r

                r = e.add(x);

                r = r.mod(ecc_n);

            } while (r.equals(BigInteger.ZERO) || r.add(k).equals(ecc_n));

 

            // (1 + dA)~-1

            BigInteger da_1 = userD.add(BigInteger.ONE);

            da_1 = da_1.modInverse(ecc_n);

            // s

            s = r.multiply(userD);

            s = k.subtract(s).mod(ecc_n);

            s = da_1.multiply(s).mod(ecc_n);

        } while (s.equals(BigInteger.ZERO));

 

        byte[] btRS = new byte[64];

        byte[] btR = r.toByteArray();

        byte[] btS = s.toByteArray();

        System.arraycopy(btR, btR.length - 32, btRS, 0, 32);

        System.arraycopy(btS, btS.length - 32, btRS, 32, 32);

        r.toByteArray();

        s.toByteArray();

        byte[] encode = Base64.encode(btRS);

        System.out.println("sssssss-------r" + r.toString(16));

        System.out.println("sssssss-------s" + s.toString(16));

        return new String(encode);

    }

 

    public boolean Verify(byte[] msg, byte[] signData) {

        byte[] certPK = publicKey.getEncoded();

 

        byte[] pkX = SubByte(certPK, 0, 32);

 

        byte[] pkY = SubByte(certPK, 32, 32);

//        System.out.println("\n");

//        BigInteger biX = new BigInteger(1, pkX);

//        BigInteger biY = new BigInteger(1, pkY);

//        ECFieldElement x = new Fp(ecc_p, biX);

//        ECFieldElement y = new Fp(ecc_p, biY);

        ECPoint userKey = publicKey;

        //

        SM3Digest sm3 = new SM3Digest();

        byte[] sm2Za = sm3.getSM2Za(pkX, pkY, "1234567812345678".getBytes());

        System.out.println("\n");

        // printHexString(sm2Za);

        sm3.update(sm2Za, 0, sm2Za.length);

        System.out.println("\n");

        // printHexString(sm2Za);

        System.out.println("\n");

        byte[] p = msg;

        sm3.update(p, 0, p.length);

        System.out.println("\n");

        byte[] md = new byte[32];

        sm3.doFinal(md, 0);

        byte[] btRS = signData;

        byte[] btR = SubByte(btRS, 0, btRS.length / 2);

        byte[] btS = SubByte(btRS, btR.length, btRS.length - btR.length);

 

        BigInteger r = new BigInteger(1, btR);

        BigInteger s = new BigInteger(1, btS);

 

        // e_

        BigInteger e = new BigInteger(1, md);

 

        // t

        BigInteger t = r.add(s).mod(ecc_n);

 

        if (t.equals(BigInteger.ZERO))

            return false;

 

        // x1y1

        ECPoint x1y1 = ecc_point_g.multiply(s);

        x1y1 = x1y1.add(userKey.multiply(t));

 

        // R

        BigInteger R = e.add(x1y1.getX().toBigInteger()).mod(ecc_n);

 

        return r.equals(R);

 

    }

 

 

}

