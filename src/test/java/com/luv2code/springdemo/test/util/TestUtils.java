package com.luv2code.springdemo.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

public class TestUtils {

	public static void executeQueryAndVerifySingleRow(Connection con,
			String sql, Map<String, Object> columnValueMapToVerify)
			throws SQLException {
		executeQueryAndVerifySingleRow(con, sql, -1, columnValueMapToVerify);
	}

	public static void executeQueryAndVerifySingleRow(Connection con,
			String sql, int resultSizeToAssert,
			Map<String, Object> columnValueMapToVerify) throws SQLException {
		MapListHandler beanListHandler = new MapListHandler();

		QueryRunner runner = new QueryRunner();
		List<Map<String, Object>> list = runner.query(con, sql,
				beanListHandler);

		if (resultSizeToAssert != -1) {
			assertTrue(list.size() == resultSizeToAssert);
		}

		if (resultSizeToAssert > 0) {
			Map<String, Object> firstRow = list.get(0);
			for (Map.Entry<String, Object> entry : columnValueMapToVerify
					.entrySet()) {
				assertEquals(firstRow.get(entry.getKey()), entry.getValue());
			}
		}
	}

	public static void insertSingleRow(Connection con, String insertSQL,
			Object... values) throws SQLException {
		QueryRunner runner = new QueryRunner();

		int numRowsInserted = runner.update(con, insertSQL, values);

		assertEquals(numRowsInserted, 1);
	}

}
