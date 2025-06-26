package project.database;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionTester {

    @Autowired
    private DataSource dataSource;

//    @PostConstruct
//    public void testConnection() {
//        try (Connection conn = dataSource.getConnection()) {
//            System.out.println("Conexiune reușită la: " + conn.getMetaData().getURL());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}

