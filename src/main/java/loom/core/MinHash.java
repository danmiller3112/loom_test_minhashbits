package loom.core;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class MinHash {

    private int size_sign;
    final HashFunction[] hashFunctions;

    public MinHash(int size) {
        size_sign = size;
        hashFunctions = new HashFunction[size_sign];
        for (int i = 0; i < size_sign; i++) {
            hashFunctions[i] = Hashing.murmur3_128(i);
        }
    }

    public byte[] getSignature(String[] set) {
        long[] minHashValues = getMinHash(set);
        return convertMinHashToBits(minHashValues, 1);
    }

    private long[] getMinHash(String[] set) {
        long[] minHashValues = new long[size_sign];
        for (int i = 0; i < set.length; i++) {
            for (int j = 0; j < size_sign; j++) {
                final HashCode hashCode = hashFunctions[j].hashUnencodedChars(set[i]);
                final long value = hashCode.asLong();
                if (value < minHashValues[j]) {
                    minHashValues[j] = value;
                }
            }
        }
        return minHashValues;
    }

    private static byte[] convertMinHashToBits(final long[] minHashValues, final int hashBit) {
        final int shift = 1;
        final int radix = 1 << shift;
        final long mask = radix - 1;
        int pos = 0;
        final int nbits = minHashValues.length * hashBit;
        final FastBitSet bitSet = new FastBitSet(nbits);
        for (long i : minHashValues) {
            for (int j = 0; j < hashBit; j++) {
                bitSet.set(pos, (int) (i & mask) == 1);
                pos++;
                i >>>= shift;
            }
        }
        return bitSet.toByteArray();
    }

//    public float getSimilarity(byte[] sig1, byte[] sig2) {
//        int equal_count = 0;
//        for (int i = 0; i < size_sign; i++) {
//            if (sig1[i] == sig2[i])
//                equal_count++;
//        }
//        return ((float) equal_count / (float) size_sign);
//    }

    /**
     * Compare bytes for MinHash.
     *
     * @param data1 MinHash bytes
     * @param data2 MinHash bytes
     * @return similarity (0 to 1.0f)
     */
    public float getSimilarity(final byte[] data1, final byte[] data2) {
        final int count = countSameBits(data1, data2);
        return (float) count / (float) (data1.length * 8);
    }


    protected static int countSameBits(final byte[] data1, final byte[] data2) {
        int count = 0;
        for (int i = 0; i < data1.length; i++) {
            byte b1 = data1[i];
            byte b2 = data2[i];
            for (int j = 0; j < 8; j++) {
                if ((b1 & 1) == (b2 & 1)) {
                    count++;
                }
                b1 >>= 1;
                b2 >>= 1;
            }
        }
        return count;
    }
}