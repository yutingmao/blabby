<%@ page contentType="text/html; charset=gb2312" %>
<%@ page language="java" %>
<%@ page import="com.mysql.jdbc.Driver" %>
<%@ page import="java.sql.*" %>
<%
String driverName="com.mysql.jdbc.Driver";
String userName="root";
String userPasswd="123456";
String dbName="microblogdb";
String tableName="usertable";
Class.forName("com.mysql.jdbc.Driver");  
Connection con = DriverManager.getConnection("jdbc:mysql://localhost/microblogdb","root","123456");  


Statement statement = con.createStatement();
String sql="SELECT * FROM "+tableName;
ResultSet rs = statement.executeQuery(sql);
//ݽ
ResultSetMetaData rmeta = rs.getMetaData();
//ȷݼֶ
int numColumns=rmeta.getColumnCount();
// ÿһֵ
out.print("id");
out.print("|");
out.print("num");
out.print("<br>");
while(rs.next()) {
out.print(rs.getString(2)+" ");
out.print("|");
out.print(rs.getString(3));
out.print("<br>");
}
out.print("<br>");
out.print("ok");
rs.close();
statement.close();
con.close();
%>