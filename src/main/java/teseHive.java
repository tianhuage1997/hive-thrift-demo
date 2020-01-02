import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.*;

public class teseHive {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
       //设置jvm启动时krb5的读取路径参数
        System.setProperty("java.security.krb5.conf", "D:\\idea_workspace\\krb5.conf");
//配置kerberos认证
        Configuration conf = new Configuration();
        conf.setBoolean("hadoop.security.authorization", true);
        conf.set("hadoop.security.authentication", "kerberos");
//        System.out.println(System.getProperty("java.security.krb5.conf"));
        UserGroupInformation.setConfiguration(conf);
        try {
            UserGroupInformation.loginUserFromKeytab("user1", "D:\\idea_workspace\\user1.keytab");
        } catch (IOException e) {
            e.printStackTrace();
        }
       System.out.println("Succeeded in authenticating through Kerberos!");
        String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
        Class.forName(JDBC_DRIVER);
        //DriverManager.getConnection("jdbc:hive2://hadoop102:10000/;principal=mammut/qa@MAMMUT.QA.HZ.NETEASE.COM;);
        Connection con =DriverManager.getConnection("jdbc:hive2://hadoop102:10000/default;principal=hive/hadoop102@HADOOP.COM");
        Statement stmt= con.createStatement();

        String tableName = "table3";// hive表名
        String sql = "select * from " + tableName;
        System.out.println("Running:" + sql);

        ResultSet res = stmt.executeQuery(sql);
        System.out.println("执行 select * query 运行结果:");
        while (res.next()) {
            System.out.println(res.getString(1) + "\t" + res.getString(2));
        }

    }
}
