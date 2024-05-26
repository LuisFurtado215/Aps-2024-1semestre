/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aps_2024;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class Aps_2024 {
	public static Connection conexao_ao_banco;

	public static void main(String[] args) {
		connectar_database();

//		 Pessoa.criar_tb_pessoa();
//                 Chamado.criar_tb_chamado();
//                 Patrimonio.criar_tb_patrimonio();

		Scanner scanner = new Scanner(System.in);
        System.out.println("Digite 1 para fazer login, 2 para criar uma conta nova:");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        if (escolha == 1) {
            Pessoa pessoa = fazer_login(scanner);
            if (pessoa != null) {
                gerenciar_chamados(scanner, pessoa);
                gerenciar_patrimonios(scanner, pessoa);
            }
        } else if (escolha == 2) {
            criar_nova_conta(scanner);
        } else {
            System.out.println("Opção inválida.");
        }

		fechar_conexao_com_a_database();
	}

	private static Pessoa fazer_login(Scanner scanner) {
        System.out.println("Digite seu email:");
        String email = scanner.nextLine();
        System.out.println("Digite sua senha:");
        String senha = scanner.nextLine();

        try {
            Pessoa pessoa = Pessoa.buscar_pessoa_por_email_e_senha(email, senha);
            if (pessoa != null) {
                System.out.println("Login bem-sucedido! Bem-vindo, " + pessoa.getNome());
                return pessoa;
            } else {
                System.out.println("Email ou senha incorretos.");
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao fazer login: " + e.getMessage());
        }
        return null;
    }

    private static void gerenciar_chamados(Scanner scanner, Pessoa pessoa) {
        System.out.println("Digite 1 para abrir um chamado, 2 para visualizar seus chamados:");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()

        if (escolha == 1) {
            abrir_chamado(scanner, pessoa);
        } else if (escolha == 2) {
            visualizar_chamados(pessoa);
        } else {
            System.out.println("Opção inválida.");
        }
    }

    private static void abrir_chamado(Scanner scanner, Pessoa pessoa) {
        System.out.println("Digite a descrição do problema:");
        String descricao = scanner.nextLine();
        System.out.println("Digite a prioridade (Baixa, Média, Alta):");
        String prioridade = scanner.nextLine();

        Chamado novoChamado = new Chamado(pessoa.getId(), descricao, prioridade);
        try {
            Chamado.inserir_chamado(novoChamado);
            System.out.println("Chamado aberto com sucesso!");
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao abrir o chamado: " + e.getMessage());
        }
    }

    private static void visualizar_chamados(Pessoa pessoa) {
        try {
            List<Chamado> chamados = Chamado.buscar_chamados_por_pessoa(pessoa.getId());
            if (chamados.isEmpty()) {
                System.out.println("Você não tem chamados.");
            } else {
                System.out.println("Seus chamados:");
                for (Chamado chamado : chamados) {
                    System.out.println("ID: " + chamado.getId() + 
                                       ", Descrição: " + chamado.getDescricao() + 
                                       ", Prioridade: " + chamado.getPrioridade() + 
                                       ", Status: " + chamado.getStatus() + 
                                       ", Data de Abertura: " + chamado.getDataAbertura() +
                                       ", Data de Fechamento: " + chamado.getDataFechamento());
                }
            }
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao buscar os chamados: " + e.getMessage());
        }
    }

	private static void criar_nova_conta(Scanner scanner) {
        System.out.println("Digite seu nome:");
        String nome = scanner.nextLine();
        System.out.println("Digite seu email:");
        String email = scanner.nextLine();
        System.out.println("Digite sua senha:");
        String senha = scanner.nextLine();

        try {
            Pessoa novaPessoa = new Pessoa(nome, email, senha);
            Pessoa.inserir_pessoa(novaPessoa);
            System.out.println("Conta criada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao criar a conta: " + e.getMessage());
        }
    }

	private static void connectar_database() {
		String url = "jdbc:postgresql://localhost:5432/aps";
		String user = "furtado";
		String password = "123";

		try {
			Connection connection = DriverManager.getConnection(url, user, password);
			System.out.println("Conexão com o banco de dados PostgreSQL estabelecida com sucesso.");
			Aps_2024.conexao_ao_banco = connection;
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao conectar-se ao banco de dados: " + e.getMessage());
		}
	}

	private static void fechar_conexao_com_a_database() {
		try {
			Aps_2024.conexao_ao_banco.close();
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro ao desconectar-se do banco de dados: " + e.getMessage());
		}
	}

    private static void gerenciar_patrimonios(Scanner scanner, Pessoa pessoa) {
        System.out.println("Digite 1 para adicionar um patrimônio, 2 para visualizar seus patrimônios:");
        int escolha = scanner.nextInt();
        scanner.nextLine(); // Consome a nova linha deixada pelo nextInt()
    
        if (escolha == 1) {
            System.out.println("Digite o nome do patrimônio:");
            String nome = scanner.nextLine();
            System.out.println("Digite a descrição do patrimônio:");
            String descricao = scanner.nextLine();
            System.out.println("Digite a categoria do patrimônio:");
            String categoria = scanner.nextLine();
            System.out.println("Digite a data de aquisição do patrimônio (yyyy-mm-dd):");
            Date dataAquisicao = Date.valueOf(scanner.nextLine());
            System.out.println("Digite o estado do patrimônio:");
            String estado = scanner.nextLine();
    
            Patrimonio novoPatrimonio = new Patrimonio(nome, descricao, categoria, dataAquisicao, estado, pessoa.getId());
            try {
                Patrimonio.inserir_patrimonio(novoPatrimonio);
                System.out.println("Patrimônio adicionado com sucesso.");
            } catch (SQLException e) {
                System.out.println("Ocorreu um erro ao adicionar o patrimônio: " + e.getMessage());
            }
        } else if (escolha == 2) {
            try {
                List<Patrimonio> patrimonios = Patrimonio.buscar_patrimonios_por_pessoa(pessoa.getId());
                if (patrimonios.isEmpty()) {
                    System.out.println("Você não possui nenhum patrimônio.");
                } else {
                    System.out.println("Seus patrimônios:");
                    for (Patrimonio patrimonio : patrimonios) {
                        System.out.println("ID: " + patrimonio.getId() + ", Nome: " + patrimonio.getNome() +
                                           ", Descrição: " + patrimonio.getDescricao() + ", Categoria: " + patrimonio.getCategoria() +
                                           ", Data de Aquisição: " + patrimonio.getDataAquisicao() + ", Estado: " + patrimonio.getEstado());
                    }
                }
            } catch (SQLException e) {
                System.out.println("Ocorreu um erro ao visualizar os patrimônios: " + e.getMessage());
            }
        } else {
            System.out.println("Opção inválida.");
        }
    }
    
}
