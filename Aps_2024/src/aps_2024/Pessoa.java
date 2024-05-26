/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps_2024;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String senha;

    public Pessoa(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public static void inserir_pessoa(Pessoa pessoa) throws SQLException {
        String string_de_insercao = "INSERT INTO tb_pessoa (nome, email, senha) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_insercao);
            preparedStatement.setString(1, pessoa.getNome());
            preparedStatement.setString(2, pessoa.getEmail());
            preparedStatement.setString(3, pessoa.getSenha());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pessoa.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao inserir a pessoa: " + e.getMessage());
        }
    }

    public static Pessoa buscar_pessoa_por_id(int id) throws SQLException {
        String string_de_selecao = "SELECT * FROM tb_pessoa WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_selecao);
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Pessoa(resultSet.getString("nome"), resultSet.getString("email"), resultSet.getString("senha"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar a pessoa: " + e.getMessage());
        }
        return null;
    }

    public static void atualizar_pessoa(Pessoa pessoa) throws SQLException {
        String string_de_atualizacao = "UPDATE Pessoa SET nome = ?, email = ?, senha = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_atualizacao);
            preparedStatement.setString(1, pessoa.getNome());
            preparedStatement.setString(2, pessoa.getEmail());
            preparedStatement.setString(3, pessoa.getSenha());
            preparedStatement.setInt(4, pessoa.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao atualizar a pessoa: " + e.getMessage());
        }
    }

    public static void excluir_pessoa(int id) throws SQLException {
        String string_de_delecao = "DELETE FROM tb_pessoa WHERE id = ?";

        try {
            PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_delecao);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao deletar a pessoa: " + e.getMessage());
        }
    }

    public static Pessoa buscar_pessoa_por_email_e_senha(String email, String senha) throws SQLException {
        String string_de_selecao = "SELECT * FROM tb_pessoa WHERE email = ? AND senha = ?";

        try (PreparedStatement preparedStatement = Aps_2024.conexao_ao_banco.prepareStatement(string_de_selecao)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, senha);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Pessoa pessoa = new Pessoa(resultSet.getString("nome"), resultSet.getString("email"), resultSet.getString("senha"));
                    pessoa.setId(resultSet.getInt("id"));
                    return pessoa;
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar a pessoa: " + e.getMessage());
        }
        return null;
    }

    public static void criar_tb_pessoa() {
		String tb_pessoa = "DROP TABLE IF EXISTS tb_pessoa; CREATE TABLE tb_pessoa (id SERIAL, nome VARCHAR(100) NOT NULL, email VARCHAR(100) NOT NULL, senha VARCHAR(100) NOT NULL, CONSTRAINT pk_pessoa PRIMARY KEY (id), CONSTRAINT un_pessoa_email UNIQUE (email) );";

		try {
			Statement statement = Aps_2024.conexao_ao_banco.createStatement();
			statement.executeUpdate(tb_pessoa);
			System.out.println("Tabela tb_pessoa criada com sucesso.");
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao criar tb_pessoa: " + e.getMessage());
		}
	}
}
