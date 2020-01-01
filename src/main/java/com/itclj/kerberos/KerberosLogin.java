package com.itclj.kerberos;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by lujun.chen on 2018/11/16
 */
public class KerberosLogin {

  private Logger logger= LoggerFactory.getLogger(KerberosLogin.class);

  public void login() {
    String hiveUserName = "user1";
    String hiveKeytab = "D:\\idea_workspace\\user1.keytab";
    String krbconf = "D:\\idea_workspace\\krb5.conf";

    System.setProperty("java.security.krb5.conf", krbconf);
    Configuration conf = new Configuration();
    conf.set("hadoop.security.authentication", "Kerberos");
    UserGroupInformation.setConfiguration(conf);
    try {
      UserGroupInformation.loginUserFromKeytab(hiveUserName, hiveKeytab);
      System.out.println("认证成功");
    } catch (IOException e) {
      logger.error("Kerberos login fail.", e);
    }
  }

}
