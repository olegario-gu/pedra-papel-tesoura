package Client;

/**
 * A classe Client cria uma conexão com o servidor Server na porta padrão 1337.
 * Espera que o usuário insira uma string com o teclado referente a sua escolha:
 * Pedra, Papel ou Tesoura!
 * 
 * Envia a string para o servidor por meio de um protocolo TCP. 
 * Espera por uma resposta do Servidor. 
 * Imprime a mensagem recebida do servidor. 
 * Fecha a conexão.
 *
 * @author Gustavo Olegário, Bruno Costa, Matheus Soares e Eduardo Gomes <https://github.com/olegario-gu/pedra-papel-tesoura>
 * @version 1.0
 * 
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

class Client {

	/**
	 * O numero padrão(default) do host
	 * 
	 * @var string
	 */
	private static String host = "localhost";

	/**
	 * A porta
	 * 
	 * @var integer
	 */
	private static Integer port = 1337;

	/**
	 * A versão da classe Client
	 * 
	 * @var double
	 */
	private static Double numVersao = 1.0;

	/**
	 * Mensagem de boas vindas do Jogo
	 * 
	 * @var string
	 */
	private static String msgBoasVindas = "--- Bem-Vindo ao Jokenpo V. " + numVersao + " --- \n"
			+ "\nDigite a opção de jogo que deseja: " + "\n1-> Para Single Player vs PC "
			+ "\n2-> Para Multiplayer Online";

	/**
	 * Mensagem de regras do jogo
	 * 
	 * @var string
	 */
	private static String msgRegras = "\nRegras: \n-> Pedra ganha da Tesoura \n-> Tesoura ganha do Papel \n-> Papel ganha da Pedra\n";

	public static void main(String args[]) throws Exception {

		System.out.println(Client.msgBoasVindas);

		String entrada = "";
		String resposta;
		int option;
		int vits = 0, emps = 0, ders = 0;

		Scanner scan = new Scanner(System.in);
		Scanner scanS = new Scanner(System.in);

		do {
		option = scan.nextInt();

			switch (option) {
			case 1:
				String entradaCpu = "";

				do {
					do {

						if (entrada.equals("REGRAS")) {
							System.out.println(Client.msgRegras);
						}

						// Solicita ao usuário a seleção de pedra, papel ou tesoura ...
						System.out.println("Escreva qual sua escolha: Pedra || Papel || Tesoura");
						System.out.print("-> digite \"regras\" para ver as regras do jogo:\n");
						System.out.print("-> digite \"0\" para sair do jogo: ");
						entrada = scanS.nextLine();
						entrada = entrada.toUpperCase();

					} while (!entrada.equals("PEDRA") && !entrada.equals("PAPEL") && !entrada.equals("TESOURA")
							&& !entrada.equals("0"));
					
					Random r = new Random();

					int cpuPlay = r.nextInt(3) + 1;
					if (cpuPlay == 1) {
						entradaCpu = "PEDRA";
					} else if (cpuPlay == 2) {
						entradaCpu = "PAPEL";
					} else if (cpuPlay == 3) {
						entradaCpu = "TESOURA";
					}
					
					
					resposta = resultado(entrada, entradaCpu);
					System.out.println("A CPU escolheu:" + entradaCpu);
					System.out.println("Resultado:" + resposta);

					if (resposta.equals("VOCÊ PERDEU!")) {
						ders++;
					} else if (resposta.equals("VOCÊ GANHOU!")) {
						vits++;
					} else if (resposta.equals("EMPATE!")) {
						emps++;
					}
					System.out.println("-----------------------     |PLACAR|     -----------------------");
					System.out.println("Vitórias: " + vits + " || Empates: " + emps + " || Derrotas: " + ders);

				} while (!entrada.equals("0"));
				
				System.out.println("\nVocê optou por (" + entrada + ") e saiu do jogo, volte mais vezes! o/");
				option = 0;
				
				break;
			case 2:

				boolean loop = true;

				do {
					BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
					Socket clientSocket = new Socket(Client.host, Client.port);
					DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
					BufferedReader inFromServer = new BufferedReader(
							new InputStreamReader(clientSocket.getInputStream()));

					do {

						if (entrada.equals("REGRAS")) {
							System.out.println(Client.msgRegras);
						}

						// Solicita ao usuário a seleção de pedra, papel ou tesoura ...
						System.out.println("Escreva qual sua escolha: Pedra || Papel || Tesoura");
						System.out.print("-> digite \"regras\" para ver as regras do jogo:\n");
						System.out.print("-> digite \"0\" para sair do jogo: ");
						entrada = inFromUser.readLine();
						entrada = entrada.toUpperCase();

					} while (!entrada.equals("PEDRA") && !entrada.equals("PAPEL") && !entrada.equals("TESOURA")
							&& !entrada.equals("0"));

					outToServer.writeBytes(entrada + "\n");
					if (entrada.equals("0")) {
						System.out.println("\nVocê optou por (" + entrada + ") e saiu do jogo, volte mais vezes! o/");
					} else {
						// Transmite a entrada para o servidor e fornece o feedback adequado do jogador
						System.out.println("\nSua jogada (" + entrada
								+ ") foi salva com sucesso no servidor. \nAguarde o resultado ...");
					}

					// Pega a resposta do servidor e imprime ao jogador
					resposta = inFromServer.readLine();
					System.out.println("Resultado: " + resposta + "\n");

					if (resposta.equals("VOCÊ PERDEU!")) {
						ders++;
					} else if (resposta.equals("VOCÊ GANHOU!")) {
						vits++;
					} else if (resposta.equals("EMPATE!")) {
						emps++;
					}
					System.out.println("-----------------------     |PLACAR|     -----------------------");
					System.out.println("Vitórias: " + vits + " || Empates: " + emps + " || Derrotas: " + ders);

					if (resposta.equals("VOCÊ SAIU DO JOGO.")
							|| resposta.equals("O OUTRO JOGADOR DECIDIU PARAR, FIM DE JOGO.")) {
						loop = false;
					}

					clientSocket.close();

				} while (loop);
				option = 0;

				break;
			default:
				System.out.println("Opção Inválida");
				System.out.println("Escolha uma opção válida:");
				break;
			}
		} while (option != 0);

	}

	public static String resultado(String j1, String j2) {

		String inputClient_1 = j1;
		String inputClient_2 = j2;
		String resClient_1 = "";

		/**
		 * Se os inputs de C1 e C2 forem iguais, o servidor envia de volta para ambos os
		 * clientes a string "Empate!".
		 */
		if (inputClient_1.equals(inputClient_2)) {
			resClient_1 = "Empate!";
		}
		/**
		 * Se o servidor receber 'PEDRA' do C1 e 'TESOURA' do C2, enviamos a string
		 * "Você Ganhou!" ao C1 e "Você Perdeu!" ao C2.
		 */
		else if (inputClient_1.equals("PEDRA") && inputClient_2.equals("TESOURA")) {
			resClient_1 = "Você Ganhou!";

		}
		/**
		 * Se o servidor receber 'TESOURA' do C1 e 'PEDRA' do C2, enviamos a string
		 * "Você Ganhou!" ao C2 e "Você Perdeu!" ao C1.
		 */
		else if (inputClient_1.equals("TESOURA") && inputClient_2.equals("PEDRA")) {
			resClient_1 = "Você Perdeu!";
		}
		/**
		 * Se o servidor receber 'PEDRA' do C1 e 'PAPEL' do C2, enviamos a string "Você
		 * Ganhou!" ao C2 e "Você Perdeu!" ao C1.
		 */
		else if (inputClient_1.equals("PEDRA") && inputClient_2.equals("PAPEL")) {
			resClient_1 = "Você Perdeu!";
		}
		/**
		 * Se o servidor receber 'PAPEL' do C1 e 'PEDRA' do C2, enviamos a string "Você
		 * Ganhou!" ao C1 e "Você Perdeu!" ao C2.
		 */
		else if (inputClient_1.equals("PAPEL") && inputClient_2.equals("PEDRA")) {
			resClient_1 = "Você Ganhou!";
		}
		/**
		 * Se o servidor receber 'TESOURA' do C1 e 'PAPEL' do C2, enviamos a string
		 * "Você Ganhou!" ao C1 e "Você Perdeu!" ao C2.
		 */
		else if (inputClient_1.equals("TESOURA") && inputClient_2.equals("PAPEL")) {
			resClient_1 = "Você Ganhou!";
		}
		/**
		 * Se o servidor receber 'PAPEL' do C1 e 'TESOURA' do C2, enviamos a string
		 * "Você Ganhou!" ao C2 e "Você Perdeu!" ao C1.
		 */
		else if (inputClient_1.equals("PAPEL") && inputClient_2.equals("TESOURA")) {
			resClient_1 = "Você Perdeu!";
		}

		return resClient_1.toUpperCase();
	}
}