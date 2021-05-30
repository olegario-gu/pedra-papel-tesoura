package Server;

/**
 * A classe do servidor aguarda a conex�o dos dois clientes client_1 
 * e client_2 (em qualquer ordem) na porta 1337. Recebe um caractere
 * de client_1 e um caractere de client_2 (em qualquer ordem) e
 * calcula o vencedor do jogo com base em um conjunto de regras.
 * Depois de enviar uma mensagem correspondente a cada cliente o 
 * servidor espera novamente por dois clientes para se conectar.
 * 
 * @author Gustavo Oleg�rio, Bruno Costa, Matheus Soares e Eduardo Gomes <https://github.com/olegario-gu/pedra-papel-tesoura>
 * @version 1.0
 * 
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

	/**
	 * O numero padr�o(default) da porta.
	 * 
	 * @var integer
	 * 
	 */
	private static Integer port = 1337;

	/**
	 * O numero da vers�o do jogo
	 * 
	 * @var integer
	 * 
	 */
	private static Double numVersao = 1.0;

	/**
	 * Mensagem de boas vindas do Servidor
	 * 
	 * @var string
	 * 
	 */
	private static String msgBoasVindas = "--- Bem-vindo ao Servidor do Jokenpo V. " + numVersao + " --- \n";

	/**
	 * A fun��o recebe um inteiro x como entrada e retorna o valor booleano
	 * verdadeiro se a entrada for estritamente maior que 0 e menor ou igual a
	 * 65535.
	 * 
	 * @param integer x
	 * @return boolean
	 */
	private static boolean portaValida(Integer x) {
		return x >= 1 && x <= 65535 ? true : false;
	}

	/**
	 * A fun��o solicita que o usu�rio escolha um n�mero de porta espec�fico ou
	 * pressione "0" para continuar com a configura��o padr�o (Server.port).
	 * 
	 * O n�mero inteiro retornado estritamente maior que 0 e menor ou igual a 65535.
	 * 
	 * @return integer
	 */
	private static int getPort() {

		Integer input;

		Scanner entrada = new Scanner(System.in);

		do {
			System.out.print("Selecione uma porta inserindo um valor inteiro entre 1 e 65535 ou\n");
			System.out.print(
					"A porta padr�o � a 1337, caso escolha outra, ser� necess�rio trocar manualmente a porta do Client.java\n");
			System.out.print("insira \"0\" para continuar com a configura��o padr�o (" + Server.port + "): ");
			input = entrada.nextInt();

		} while (input != 0 && !Server.portaValida(input));

		entrada.close();

		return input == 0 ? Server.port : input;
	}

	public static void main(String args[]) throws Exception {

		String resClient_1 = "";
		String resClient_2 = "";
		String inputClient_1;
		String inputClient_2;

		// Imprime a mensagem de boas vindas do Server
		System.out.println(Server.msgBoasVindas);

		// Setta a porta escolhida
		Server.port = Server.getPort();

		// Cria um novo server socket & imprime a mensagem de status de funcionamento
		ServerSocket welcomeSocket = new ServerSocket(Server.port);
		System.out.println("\nOk, estamos funcionando na porta " + welcomeSocket.getLocalPort() + " ...");

		while (!welcomeSocket.isClosed()) {

			do {
				// Jogador 1
				Socket client_1 = welcomeSocket.accept();
				if (client_1.isConnected()) {
					System.out.println("\nJogador 1 (" + (client_1.getLocalAddress().toString()).substring(1) + ":"
							+ client_1.getLocalPort() + ") entrou no servidor ... esperando o segundo jogador ...");
				}
				DataOutputStream outClient_1 = new DataOutputStream(client_1.getOutputStream());
				BufferedReader inClient_1 = new BufferedReader(new InputStreamReader(client_1.getInputStream()));

				// Jogador 2
				Socket client_2 = welcomeSocket.accept();
				if (client_2.isConnected()) {
					System.out.println("Jogador 2 (" + (client_2.getLocalAddress().toString()).substring(1) + ":"
							+ client_1.getLocalPort() + ") entrou no servidor ... Vamos come�ar!");
				}
				DataOutputStream outClient_2 = new DataOutputStream(client_2.getOutputStream());
				BufferedReader inClient_2 = new BufferedReader(new InputStreamReader(client_2.getInputStream()));

				// Recebe o input dos dois jogadores
				inputClient_1 = inClient_1.readLine();
				inputClient_2 = inClient_2.readLine();

				/**
				 * Se o input de C1 for 0, o servidor envia as respostas de fim de jogo para
				 * cada Client.
				 */
				if (inputClient_1.equals("0")) {
					resClient_1 = "Voc� saiu do Jogo.";
					resClient_2 = "O outro jogador decidiu parar, fim de jogo.";
				}
				/**
				 * Se o input de C2 for 0, o servidor envia as respostas de fim de jogo para
				 * cada Client.
				 */
				else if (inputClient_2.equals("0")) {
					resClient_1 = "O outro jogador decidiu parar, fim de jogo.";
					resClient_2 = "Voc� saiu do Jogo.";
				}
				/**
				 * Se os inputs de C1 e C2 forem iguais, o servidor envia de volta para ambos os
				 * clientes a string "Empate!".
				 */
				else if (inputClient_1.equals(inputClient_2)) {
					resClient_1 = "Empate!";
					resClient_2 = "Empate!";
				}
				/**
				 * Se o servidor receber 'PEDRA' do C1 e 'TESOURA' do C2, enviamos a string
				 * "Voc� Ganhou!" ao C1 e "Voc� Perdeu!" ao C2.
				 */
				else if (inputClient_1.equals("PEDRA") && inputClient_2.equals("TESOURA")) {
					resClient_1 = "Voc� Ganhou!";
					resClient_2 = "Voc� Perdeu!";

				}
				/**
				 * Se o servidor receber 'TESOURA' do C1 e 'PEDRA' do C2, enviamos a string
				 * "Voc� Ganhou!" ao C2 e "Voc� Perdeu!" ao C1.
				 */
				else if (inputClient_1.equals("TESOURA") && inputClient_2.equals("PEDRA")) {
					resClient_1 = "Voc� Perdeu!";
					resClient_2 = "Voc� Ganhou!";
				}
				/**
				 * Se o servidor receber 'PEDRA' do C1 e 'PAPEL' do C2, enviamos a string "Voc�
				 * Ganhou!" ao C2 e "Voc� Perdeu!" ao C1.
				 */
				else if (inputClient_1.equals("PEDRA") && inputClient_2.equals("PAPEL")) {
					resClient_1 = "Voc� Perdeu!";
					resClient_2 = "Voc� Ganhou!";
				}
				/**
				 * Se o servidor receber 'PAPEL' do C1 e 'PEDRA' do C2, enviamos a string "Voc�
				 * Ganhou!" ao C1 e "Voc� Perdeu!" ao C2.
				 */
				else if (inputClient_1.equals("PAPEL") && inputClient_2.equals("PEDRA")) {
					resClient_1 = "Voc� Ganhou!";
					resClient_2 = "Voc� Perdeu!";
				}
				/**
				 * Se o servidor receber 'TESOURA' do C1 e 'PAPEL' do C2, enviamos a string
				 * "Voc� Ganhou!" ao C1 e "Voc� Perdeu!" ao C2.
				 */
				else if (inputClient_1.equals("TESOURA") && inputClient_2.equals("PAPEL")) {
					resClient_1 = "Voc� Ganhou!";
					resClient_2 = "Voc� Perdeu!";
				}
				/**
				 * Se o servidor receber 'PAPEL' do C1 e 'TESOURA' do C2, enviamos a string
				 * "Voc� Ganhou!" ao C2 e "Voc� Perdeu!" ao C1.
				 */
				else if (inputClient_1.equals("PAPEL") && inputClient_2.equals("TESOURA")) {
					resClient_1 = "Voc� Perdeu!";
					resClient_2 = "Voc� Ganhou!";
				}

				System.out.println(resClient_1.toUpperCase());
				System.out.println(resClient_2.toUpperCase());
				// Envia as respostas em maiusculo e fecha os sockets
				outClient_1.writeBytes(resClient_1.toUpperCase());
				outClient_2.writeBytes(resClient_2.toUpperCase());

				client_2.close();
				client_1.close();

				// Avalia se algum jogador saiu do jogo
			} while (!inputClient_1.equals("0") || !inputClient_2.equals("0"));

			System.out.println("\nEsperando por novos jogadores ...\n");

		}
	}
}