package com.wisdom.webservice.token;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by Jingjing.Ma on 2018/5/3 9:40
 */
public class MyHeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private String userName;

    private String passWord;

    public MyHeaderHandler(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext ctx) {

        try {
            Boolean out = (Boolean) ctx.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (out) {
                SOAPMessage msg = ctx.getMessage();
                SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
                SOAPHeader hdr = env.getHeader();
                if (hdr == null) {
                    hdr = env.addHeader();
                }

                QName name2 = new QName("http://esb.szmtr.com/wsdl/soapheader/", "ESBSecurityToken", "aaa");
                SOAPHeaderElement header2 = hdr.addHeaderElement(name2);

                SOAPElement username = header2.addChildElement("username", "aaa");
                username.addTextNode(userName);

                SOAPElement timestamp = header2.addChildElement("timestamp", "aaa");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(new Date());

                SOAPElement password = header2.addChildElement("password", "aaa");
                String passStr = Md5Util.StringInMd5(passWord);
                String passMD5 = Md5Util.StringInMd5(passStr + time);
                password.addTextNode(passMD5);

                timestamp.addTextNode(time);
                msg.saveChanges();


                return true;
            }
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void close(MessageContext context) {
        // TODO Auto-generated method stub
    }

    @Override
    public Set<QName> getHeaders() {
        // TODO Auto-generated method stub
        return null;
    }
}
