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

	public static void executeQueryAndVerifySingleRow(Connection con, String sql,
			Map<String, Object> columnValueMapToVerify) throws SQLException {
		MapListHandler beanListHandler = new MapListHandler();

		QueryRunner runner = new QueryRunner();
		List<Map<String, Object>> list = runner.query(con, sql,
				beanListHandler);
		assertTrue(list.size()>0);
		Map<String, Object> firstRow = list.get(0);
		for(Map.Entry<String, Object> entry: columnValueMapToVerify.entrySet()) {
			assertEquals(firstRow.get(entry.getKey()), entry.getValue());
		}
	}

}
