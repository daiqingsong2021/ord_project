package com.wisdom.acm.dc5.common;

import java.io.Serializable;
import java.util.Properties;

public class UUIDHexGenerator extends AbstractUUIDGenerator
{
    public static final UUIDHexGenerator DEFAULT = new UUIDHexGenerator();
    private String sep = "";

    protected String format(int intval)
    {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval)
    {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    public static String format(String strval)
    {
        String hexString = "";
        for (int i = 0; i < strval.length(); i++)
        {
            int ch = strval.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString = hexString + strHex;
        }
        return hexString;
    }

    public Serializable generate(Object obj)
    {
        return 36 + format(getIP()) + this.sep + obj + this.sep + format(getJVM()) + this.sep + format(getHiTime())
                + this.sep + format(getLoTime()) + this.sep + format(getCount());
    }

    public Serializable generateTen()
    {
        return (36 + format(getCount()) + format(getHiTime()) + format(getLoTime())).substring(0, 10);
    }


    public void configure(Properties params)
    {
        this.sep = params.getProperty("separator", "");
    }

    public static final String generator()
    {
        return String.valueOf(DEFAULT.generate(null));
    }

    public static final String generator(Object obj)
    {
        return String.valueOf(DEFAULT.generate(obj));
    }

    public static final String generatorTen()
    {
        return String.valueOf(DEFAULT.generateTen());
    }

    public static void main(String []args)
    {

    }
}
