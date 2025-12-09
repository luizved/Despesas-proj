package org.despesas.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ATENÇÃO: Substitua estas credenciais pelas suas
    private static final String URL ="jdbc:postgresql://ep-autumn-resonance-ae9uec7y-pooler.c-2.us-east-2.aws.neon.tech/neondb?user=neondb_owner&password=npg_ERbxUzFr8Sn7&sslmode=require&channelBinding=require";

    public static Connection getConnection() throws SQLException {
     //   try {
            // Tenta estabelecer a conexão
            return DriverManager.getConnection(URL);
        //} catch (SQLException e) {
          //  System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            //throw e;
        //}
    }
}


