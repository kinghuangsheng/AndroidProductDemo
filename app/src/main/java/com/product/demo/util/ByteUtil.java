package com.product.demo.util;

import java.io.UnsupportedEncodingException;


public final class ByteUtil
{
    
    private static final int THREE = 3;
    
    private static final int TWO = 2;
    
    private static final int FOUR = 4;
    
    private static final int TEN = 10;
    
    /**空格*/
    private static final int SPACE = 32;
    
    private static final int DATA_00001111 = 0x0f;
    
    private static final int DATA_11111111 = 0xff;
    
    private static byte[] hex;

    private static final String UTF8 = "UTF-8";
    
    private ByteUtil()
    {
        
    }
    
    static
    {
        try
        {
            hex = "0123456789ABCDEF".getBytes(UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
    }
    
    // 从字节数组到十六进制字符串转换
    public static String bytes2HexString(byte[] b)
    {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++)
        {
            buff[TWO * i] = hex[(b[i] >> FOUR) & DATA_00001111];
            buff[TWO * i + 1] = hex[b[i] & DATA_00001111];
        }
        try
        {
            return new String(buff, UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    // 从字节数组到十六进制字符串转换 , 中间增加空隔
    public static String bytes2HexStringAddSpace(byte[] b)
    {
        byte[] buff = new byte[THREE * b.length];
        for (int i = 0; i < b.length; i++)
        {
            buff[THREE * i] = hex[(b[i] >> FOUR) & DATA_00001111];
            buff[THREE * i + 1] = hex[b[i] & DATA_00001111];
            buff[THREE * i + 2] = SPACE;
        }
        try
        {
            return new String(buff, UTF8);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    // 从十六进制字符串到字节数组转换
    public static byte[] hexString2Bytes(String hexstr)
    {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++)
        {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << FOUR) | parse(c1));
        }
        return b;
    }
    
    private static int parse(char c)
    {
        if (c >= 'a')
        {
            return (c - 'a' + TEN) & DATA_00001111;
        }
        if (c >= 'A')
        {
            return (c - 'A' + TEN) & DATA_00001111;
        }
        return (c - '0') & DATA_00001111;
    }
    
    public static int byte2Int(byte source)
    {
        int target = source;
        target = target & DATA_11111111;
        return target;
    }
    
    /**
     * 两个字节数组合并
     */
    public static byte[] byteMerger(byte[] head, byte[] tail)
    {
        byte[] total = new byte[head.length + tail.length];
        System.arraycopy(head, 0, total, 0, head.length);
        System.arraycopy(tail, 0, total, head.length, tail.length);
        return total;
    }

    /**
     * 十六进制字节数组转十进制数， index从0到length依次为从高到低
     */
    public static long bytes2Int(byte... bytes)
    {
        //先进行高低字节转换（也就是高低字节进行换序）
        int[] tmpbyte = new int[bytes.length];
        int maxIndex = bytes.length - 1;
        for (int i = 0; i < bytes.length; i++)
        {
            tmpbyte[i] = bytes[maxIndex - i] & DATA_11111111;
        }
        //对转换后的数组进行计算
        long value = 0;
        for (int i = 0; i < tmpbyte.length; i++)
        {
            value += Math.pow(2, i * 8) * tmpbyte[i];
        }
        return value;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。
     *
     * @param value
     *            要转换的int值
     * @return byte数组
     */
    public static byte[] intTo4BytesLowBitInFront(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序
     */
    public static byte[] intTo4BytesLowInBehind(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 将int数值转换为占2个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。
     */
    public static byte[] intTo2BytesLowInBehind(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }
}
