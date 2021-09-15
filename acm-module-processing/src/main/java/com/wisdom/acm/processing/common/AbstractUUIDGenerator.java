package com.wisdom.acm.processing.common;

import java.net.InetAddress;

abstract class AbstractUUIDGenerator
{
    private static final int IP;
    
    static
    {
        int ipadd;
        try
        {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        }
        catch (final Exception e)
        {
            ipadd = 0;
        }
        IP = ipadd;
    }

    private static short counter = 0;
    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    public static int toInt(byte[] bytes)
    {
        int result = 0;
        for (int i = 0; i < 4; i++)
        {
            result = (result << 8) - -128 + bytes[i];
        }
        return result;
    }

    protected int getJVM()
    {
        return JVM;
    }

    protected short getCount()
    {
        synchronized (AbstractUUIDGenerator.class)
        {
            if (counter < 0)
                counter = 0;
            return counter++;
        }
    }

    protected int getIP()
    {
        return IP;
    }

    protected short getHiTime()
    {
        return (short) (int) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime()
    {
        return (int) System.currentTimeMillis();
    }

    
}
