package com.itclj.demo2;

import org.apache.hadoop.hive.thrift.client.TUGIAssumingTransport;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hive.service.cli.thrift.TCLIService;
import org.apache.hive.service.cli.thrift.TOpenSessionReq;
import org.apache.hive.service.cli.thrift.TOpenSessionResp;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TSaslClientTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QueryTool {
    private static Logger logger= LoggerFactory.getLogger(QueryTool.class);

    public static TTransport getSocketInstance(String host, int port) throws IOException
    {
        TTransport transport = new TSocket(host, port);
        Map<String, String> saslProperties = new HashMap<String, String>();
        saslProperties.put("javax.security.sasl.qop", "auth");// kerberos认证关键参数
        saslProperties.put("javax.security.sasl.server.authentication", "true");//Kerberos 认证关键参数

        logger.info("Security is enabled: " + UserGroupInformation.isSecurityEnabled());


        UserGroupInformation currentUser = UserGroupInformation.getCurrentUser();
        logger.info("Current user: {}" + currentUser);

        TSaslClientTransport saslTransport = new TSaslClientTransport("GSSAPI", // 设置SASL使用GSSAPI
                null, // authorizationid - null
                "hive", // kerberos primary for server - "hive" in hive/myservername@MY.REALM
                "myservername",// kerberos instance for server - "myservername" in hive/myservername@MY.REALM
                saslProperties, // Properties set, above
                null, // callback handler - null
                transport); // underlying transport

        TUGIAssumingTransport ugiTransport = new TUGIAssumingTransport(saslTransport, currentUser);

        return ugiTransport;
    }

    /**
     * 如果使用此方法中设置的user进行访问，则需要 HiveServer2 启用模拟 hive.server2.enable.impersonation, hive.server2.enable.doAs =
     * true即HiveServer2 Default Group打钩 获取TOpenSessionResp
     *
     * @return
     * @throws TException
     */

    public static TOpenSessionResp openSession(TCLIService.Client client, String user, String pwd) throws TException
    {
        TOpenSessionReq openSessionReq = new TOpenSessionReq();
        openSessionReq.setUsername(user);
        openSessionReq.setPassword(pwd);
        openSessionReq.setUsernameIsSet(true);

        return client.OpenSession(openSessionReq);
    }

    public static TOpenSessionResp openSession(TCLIService.Client client) throws TException
    {
        TOpenSessionReq openSessionReq = new TOpenSessionReq();
        return client.OpenSession(openSessionReq);
    }
}
