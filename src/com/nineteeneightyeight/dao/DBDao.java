package com.nineteeneightyeight.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nineteeneightyeight.util.DaoUtil;

/**
 * 数据库操作父类,其子类必须实现composeEntity方法
 * 
 * @author flytreeleft
 * 
 */
public abstract class DBDao {
	private Connection conn = null;
	private ResultSet resultSet = null;
	private PreparedStatement statement = null;

	public DBDao() {

	}

	/**
	 * 组装数据库实体对象
	 * 
	 * @param resultSet
	 *            从数据库中获取的记录集
	 * @return
	 * @throws SQLException
	 */
	protected abstract Object composeEntity(ResultSet resultSet) throws SQLException;

	/**
	 * 若数据库连接对象为null或已被关闭,则打开数据库连接,否则使用原连接对象
	 * */
	public void open() {
		try {
			if (conn == null || conn.isClosed()) {
				conn = DaoUtil.getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库连接对象
	 * */
	public void close() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeStatement() {
		try {
			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void closeResultSetAndStatement() {
		try {
			// 关闭数据库连接
			if (resultSet != null)
				resultSet.close();
			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据库连接对象是否已被关闭或为null,是则返回true,否则返回false
	 * */
	private boolean isClosed() {
		try {
			if (conn == null || conn.isClosed()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取连接数据库对象
	 * */
	private Connection getConnection() {
		return conn;
	}

	/**
	 * 搜索数据库,返回搜索结果,结果中所存对象为子类所对应的对象
	 * 
	 * @param query
	 *            完整的select语句
	 * @param index
	 *            起始号 ,表示从编号(从0开始)为index的记录开始取数据
	 * @param count
	 *            所要取的记录总数 ,若count=0则返回所有记录
	 * @return
	 * @throws SQLException
	 */
	protected List<Object> executeQuery(String query, int index, int count) throws SQLException {
		List<Object> list = new ArrayList<Object>();

		if (query != null && !query.isEmpty() && index >= 0 && count >= 0) {
			// 数据库连接未打开,则抛出异常
			if (isClosed()) {
				throw new SQLException("the connection is closed or null");
			}

			try {
				if (count == 0) { // 搜索所有的记录
					statement = getConnection().prepareStatement(query);
				} else { // 限制范围搜索
					statement = getConnection().prepareStatement(query + " limit ?,?");
					statement.setInt(1, index);
					statement.setInt(2, count);
				}
				// 执行查询,并返回结果
				resultSet = statement.executeQuery();
				// 组装实体对象,并添加到List集合中
				while (resultSet.next()) {
					list.add(composeEntity(resultSet));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSetAndStatement();
			}
		}

		return list;
	}
	
	/**
	 * 更新数据库信息
	 * 
	 * @param update
	 *            更新表达式 ,可以执行完整的update/delete/insert语句
	 * @return
	 * @throws SQLException
	 */
	protected boolean executeUpdate(String update) throws SQLException {
		boolean succ = false;

		if (update != null && !update.isEmpty()) {
			// 数据库连接未打开,则抛出异常
			if (isClosed()) {
				throw new SQLException("the connection is closed or null");
			}

			try {
				statement = getConnection().prepareStatement(update);
				// update返回影响的行数,
				succ = statement.executeUpdate() > 0 ? true : false;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatement();
			}
		}

		return succ;
	}

	/**
	 * 获取某字段的值,仅返回第一条记录的值
	 * 
	 * @param field
	 *            字段名称
	 * @param query
	 *            检索式
	 * @return 第一条记录的field字段值,若不存在则返回null
	 * @throws SQLException
	 */
	protected Object getValueOf(String field, String query) throws SQLException {
		Object value = null;

		if (field != null && !field.isEmpty() && query != null && !query.isEmpty()) {
			// 数据库连接未打开,则抛出异常
			if (isClosed()) {
				throw new SQLException("the connection is closed or null");
			}

			try {
				statement = getConnection().prepareStatement(query);
				// 执行查询,并返回结果
				resultSet = statement.executeQuery();
				if (resultSet.next()) {
					value = resultSet.getObject(field); // 获取field字段的值
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResultSetAndStatement();
			}
		}

		return value;
	}
}
