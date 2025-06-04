package com.poly.beestaycyberknightbackend.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class Connect {
    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void testConnection() {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ Kết nối SQL Server thành công: " + conn.getMetaData().getURL());
        } catch (SQLException e) {
            System.out.println("❌ Kết nối thất bại: " + e.getMessage());
        }
    }
}
