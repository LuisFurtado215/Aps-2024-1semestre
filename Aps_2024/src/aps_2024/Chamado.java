/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps_2024;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class Chamado {
    private int id;
    private int idPessoa;
    private String descricao;
    private Timestamp dataAbertura;
    private Timestamp dataFechamento;
    private String prioridade;
    private String status;

    public Chamado(int idPessoa, String descricao, String prioridade) {
        this.idPessoa = idPessoa;
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Timestamp getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Timestamp dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Timestamp getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Timestamp dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void inserir_chamado(Chamado chamado) throws SQLException {
        String string_de_insercao = "INSERT INTO tb_chamado (id_pessoa, descricao, prioridade) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_insercao, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, chamado.getIdPessoa());
            preparedStatement.setString(2, chamado.getDescricao());
            preparedStatement.setString(3, chamado.getPrioridade());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        chamado.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao inserir o chamado: " + e.getMessage());
        }
    }

    public static Chamado buscar_chamado_por_id(int id) throws SQLException {
        String string_de_selecao = "SELECT * FROM tb_chamado WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_selecao);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Chamado chamado = new Chamado(resultSet.getInt("id_pessoa"), resultSet.getString("descricao"), resultSet.getString("prioridade"));
                chamado.setId(resultSet.getInt("id"));
                chamado.setDataAbertura(resultSet.getTimestamp("data_abertura"));
                chamado.setDataFechamento(resultSet.getTimestamp("data_fechamento"));
                chamado.setStatus(resultSet.getString("status"));
                return chamado;
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar o chamado: " + e.getMessage());
        }
        return null;
    }

    public static List<Chamado> buscar_chamados_por_pessoa(int idPessoa) throws SQLException {
        List<Chamado> chamados = new ArrayList<>();
        String string_de_selecao = "SELECT * FROM tb_chamado WHERE id_pessoa = ?";

        try (PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_selecao)) {
            preparedStatement.setInt(1, idPessoa);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Chamado chamado = new Chamado(resultSet.getInt("id_pessoa"), resultSet.getString("descricao"), resultSet.getString("prioridade"));
                    chamado.setId(resultSet.getInt("id"));
                    chamado.setDataAbertura(resultSet.getTimestamp("data_abertura"));
                    chamado.setDataFechamento(resultSet.getTimestamp("data_fechamento"));
                    chamado.setStatus(resultSet.getString("status"));
                    chamados.add(chamado);
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar os chamados: " + e.getMessage());
        }
        return chamados;
    }


    public static void atualizar_chamado(Chamado chamado) throws SQLException {
        String string_de_atualizacao = "UPDATE tb_chamado SET descricao = ?, data_fechamento = ?, prioridade = ?, status = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_atualizacao);
            preparedStatement.setString(1, chamado.getDescricao());
            preparedStatement.setTimestamp(2, chamado.getDataFechamento());
            preparedStatement.setString(3, chamado.getPrioridade());
            preparedStatement.setString(4, chamado.getStatus());
            preparedStatement.setInt(5, chamado.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao atualizar o chamado: " + e.getMessage());
        }
    }

    public static void excluir_chamado(int id) throws SQLException {
        String string_de_delecao = "DELETE FROM tb_chamado WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_delecao);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao deletar o chamado: " + e.getMessage());
        }
    }

    public static void criar_tb_chamado() {
		String tb_chamado = "CREATE TABLE tb_chamado (id SERIAL PRIMARY KEY, id_pessoa INT NOT NULL, descricao TEXT NOT NULL, data_abertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP, data_fechamento TIMESTAMP, prioridade VARCHAR(50), status VARCHAR(50) DEFAULT 'Aberto', FOREIGN KEY (id_pessoa) REFERENCES tb_pessoa (id) );";

		try {
			Statement statement = Aps_2024.conexao_ao_banco.createStatement();
			statement.executeUpdate(tb_chamado);
			System.out.println("Tabela tb_chamado criada com sucesso.");
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao criar tb_chamado: " + e.getMessage());
		}
	}
}
