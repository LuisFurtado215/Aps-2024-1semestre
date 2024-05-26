/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps_2024;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Patrimonio {
    private int id;
    private String nome;
    private String descricao;
    private String categoria;
    private Date dataAquisicao;
    private String estado;
    private int idPessoa; // ID da pessoa a quem o patrimônio está atribuído (se houver)

    public Patrimonio(String nome, String descricao, String categoria, Date dataAquisicao, String estado, int idPessoa) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.dataAquisicao = dataAquisicao;
        this.estado = estado;
        this.idPessoa = idPessoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(Date dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public static void inserir_patrimonio(Patrimonio patrimonio) throws SQLException {
        String sql = "INSERT INTO tb_patrimonio (nome, descricao, categoria, data_aquisicao, estado, id_pessoa) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patrimonio.getNome());
            preparedStatement.setString(2, patrimonio.getDescricao());
            preparedStatement.setString(3, patrimonio.getCategoria());
            preparedStatement.setDate(4, patrimonio.getDataAquisicao());
            preparedStatement.setString(5, patrimonio.getEstado());
            preparedStatement.setInt(6, patrimonio.getIdPessoa());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    patrimonio.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao inserir o patrimônio: " + e.getMessage());
        }
    }

    public static List<Patrimonio> buscar_todos_patrimonios() throws SQLException {
        List<Patrimonio> patrimonios = new ArrayList<>();
        String string_de_selecao = "SELECT * FROM tb_patrimonio";

        try {
                Statement statement = Aps_2024.conexao_ao_banco.createStatement();
                ResultSet resultSet = statement.executeQuery(string_de_selecao);

            while (resultSet.next()) {
                Patrimonio patrimonio = new Patrimonio(
                    resultSet.getString("nome"),
                    resultSet.getString("descricao"),
                    resultSet.getString("categoria"),
                    resultSet.getDate("data_aquisicao"),
                    resultSet.getString("estado"),
                    resultSet.getInt("id_pessoa")
                );
                patrimonio.setId(resultSet.getInt("id"));
                patrimonios.add(patrimonio);
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar os patrimônios: " + e.getMessage());
        }
        return patrimonios;
    }

    public static List<Patrimonio> buscar_patrimonios_por_pessoa(int idPessoa) throws SQLException {
        List<Patrimonio> patrimonios = new ArrayList<>();
        String sql = "SELECT * FROM tb_patrimonio WHERE id_pessoa = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(sql);
            preparedStatement.setInt(1, idPessoa);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Patrimonio patrimonio = new Patrimonio(
                    resultSet.getString("nome"),
                    resultSet.getString("descricao"),
                    resultSet.getString("categoria"),
                    resultSet.getDate("data_aquisicao"),
                    resultSet.getString("estado"),
                    resultSet.getInt("id_pessoa")
                );
                patrimonio.setId(resultSet.getInt("id"));
                patrimonios.add(patrimonio);
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar os patrimônios: " + e.getMessage());
        }

        return patrimonios;
    }

    public static void criar_tb_patrimonio() {
		String tb_patrimonio = "CREATE TABLE tb_patrimonio (id SERIAL PRIMARY KEY, nome VARCHAR(100) NOT NULL, descricao VARCHAR(255), categoria VARCHAR(50), data_aquisicao DATE, estado VARCHAR(50), id_pessoa INT, FOREIGN KEY (id_pessoa) REFERENCES tb_pessoa(id) );";

		try {
			Statement statement = Aps_2024.conexao_ao_banco.createStatement();
			statement.executeUpdate(tb_patrimonio);
			System.out.println("Tabela tb_patrimonio criada com sucesso.");
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao criar tb_patrimonio: " + e.getMessage());
		}
	}
}

