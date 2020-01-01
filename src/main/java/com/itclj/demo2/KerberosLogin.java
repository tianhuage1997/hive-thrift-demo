package com.itclj.demo2;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KerberosLogin {
    private Logger logger= LoggerFactory.getLogger(KerberosLogin.class);


    public void login() {
        String hiveUserName = "xxxxx@BCHKDC";//kerberos认证的用户principal名称
        String hiveKeytab = "F:/NL_MYECLIPSE2014_WORK/usrkrb5/conf/xxxxx.keytab";//用户的keytab认证文件
        String krbconf = "F:/NL_MYECLIPSE2014_WORK/usrkrb5/conf/krb5.conf";//kerberos5的配置文件

        System.setProperty("java.security.krb5.conf", krbconf);
        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab(hiveUserName, hiveKeytab);
        } catch (IOException e) {
            logger.error("Kerberos login fail.", e);
        }
    }

}
