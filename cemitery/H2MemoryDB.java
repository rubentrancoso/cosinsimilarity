package de.home24.test.db;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

@Component
public class H2MemoryDB {

	private static final String DATABASE_URL = "jdbc:h2:mem:home24";
	// jdbc:h2:~/home24;LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0
	private static Connection conn;

	@BeforeClass
	public static void initDatabase() throws SQLException {
		conn = DriverManager.getConnection(DATABASE_URL); // creating SQL
															// connection will
															// trigger the DB
															// creation
	}

	@AfterClass
	public static void closeDatabase() throws SQLException {
		conn.close();
	}

	@Test
	public void testMethodA() throws SQLException {
		Server server = Server.createTcpServer("-tcpPort", "9999");
		server.start();
		server.stop();
	}

}
