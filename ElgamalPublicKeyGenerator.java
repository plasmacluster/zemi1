import java.math.BigInteger;
import java.security.SecureRandom;

public class ElgamalPublicKeyGenerator {
    public static void main(String[] args) {
        int nb_bits = 512; // 鍵のビット長。適宜変更可能
        SecureRandom random = new SecureRandom();

        // 素数 p の生成
        BigInteger p = getPrimeCert(nb_bits, random, 100);
        BigInteger q = (p.subtract(BigInteger.ONE)).divide(new BigInteger("2"));

        // 原始元 g の検索
        BigInteger g;
        boolean found = false;
        do {
            g = new BigInteger(p.bitCount() - 1, random);
            if (p.compareTo(g) == 1 &&
                    g.modPow(q, p).compareTo(BigInteger.ONE) != 0 &&
                    !g.modPow(BigInteger.TWO, p).equals(BigInteger.ONE)) {
                found = true;
            }
        } while (!found);

        // 秘密鍵 x の生成
        BigInteger x;
        do {
            x = new BigInteger(q.bitCount() - 1, random);
        } while (q.compareTo(x) == -1);

        // 公開鍵 y の計算
        BigInteger y = g.modPow(x, p);

        // 公開鍵の表示
        System.out.println("Public Key:");
        System.out.println("p = " + p);
        System.out.println("g = " + g);
        System.out.println("y = " + y);
    }

    private static BigInteger getPrimeCert(int nb_bits, SecureRandom rng, int cert) {
        BigInteger two = new BigInteger("2");
        BigInteger q, p;
        do {
            q = BigInteger.probablePrime(nb_bits - 1, rng);
            p = q.multiply(two).add(BigInteger.ONE);
        } while (!p.isProbablePrime(cert));

        return p;
    }
}
