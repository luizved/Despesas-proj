package org.despesas.dao;

import org.despesas.db.DatabaseConnection;
import org.despesas.model.Despesa;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {

    private static final String INSERT_SQL = "INSERT INTO despesas (descricao, valor, data_despesa, categoria) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM despesas ORDER BY data DESC";
    private static final String UPDATE_SQL = "UPDATE despesas SET descricao = ?, valor = ?, data_despesa = ?, categoria = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM despesas WHERE id = ?";
    private static final String SELECT_TOTAL_SQL = "SELECT SUM(valor) FROM despesas";

    /**
     * Salva uma nova despesa no banco de dados.
     * @param despesa A despesa a ser salva.
     * @return O ID gerado para a despesa.
     */
    public int salvarDespesa(Despesa despesa) {
        int idGerado = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, despesa.getDescricao());
            stmt.setDouble(2, despesa.getValor());
            stmt.setDate(3, Date.valueOf(despesa.getData()));
            stmt.setString(4, despesa.getCategoria());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Dentro do if (affectedRows > 0)
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        // No MySQL, o ID gerado é geralmente retornado na primeira coluna
                        idGerado = rs.getInt(1);
                        // Se houver problemas, tente: idGerado = rs.getInt("GENERATED_KEY");
                        despesa.setId(idGerado);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idGerado;
    }

    /**
     * Lista todas as despesas do banco de dados.
     * @return Uma lista de objetos Despesa.
     */
    public List<Despesa> listarDespesas() {
        List<Despesa> despesas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String descricao = rs.getString("descricao");
                double valor = rs.getDouble("valor");
                LocalDate data = rs.getDate("data_despesa").toLocalDate();
                String categoria = rs.getString("categoria");

                despesas.add(new Despesa(id, descricao, valor, data, categoria));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return despesas;
    }

    /**
     * Atualiza uma despesa existente no banco de dados.
     * @param despesa A despesa com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    public boolean atualizarDespesa(Despesa despesa) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, despesa.getDescricao());
            stmt.setDouble(2, despesa.getValor());
            stmt.setDate(3, Date.valueOf(despesa.getData()));
            stmt.setString(4, despesa.getCategoria());
            stmt.setInt(5, despesa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Remove uma despesa do banco de dados.
     * @param id O ID da despesa a ser removida.
     * @return true se a remoção foi bem-sucedida, false caso contrário.
     */
    public boolean excluirDespesa(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calcula o total de todas as despesas registradas.
     * @return O valor total das despesas.
     */
    public double calcularTotalDespesas() {
        double total = 0.0;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_TOTAL_SQL)) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}

