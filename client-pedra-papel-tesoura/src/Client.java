
/**
 * A classe Client cria uma conex�o com o servidor Server na porta padr�o 1337.
 * Espera que o usu�rio insira uma string com o teclado referente a sua escolha:
 * Pedra, Papel ou Tesoura!
 * 
 * Envia a string para o servidor por meio de um protocolo TCP. 
 * Espera por uma resposta do Servidor. 
 * Imprime a mensagem recebida do servidor. 
 * Fecha a conex�o.
 *
 * @author Gustavo Oleg�rio, Bruno Costa, Matheus Soares e Eduardo Gomes <https://github.com/olegario-gu/pedra-papel-tesoura>
 * @version 1.0
 * 
 */

import java.io.*;
import java.net.*;

class Client {

	/**
	 * O numero padr�o(default) do host
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
	 * A vers�o da classe Client
	 * 
	 * @var double
	 */
	private static Double numVersao = 1.0;

	/**
	 * Mensagem de boas vindas do Jogo
	 * 
	 * @var string
	 */
	private static String msgBoasVindas = "--- Bem-Vindo ao Jokenpo V. " + numVersao + " --- \n";

	/**
	 * Mensagem de regras do jogo
	 * 
	 * @var string
	 */
	private static String msgRegras = "\nRegras: \n-> Pedra ganha da Tesoura \n-> Tesoura ganha do Papel \n-> Papel ganha da Pedra\n";

	public static void main(String args[]) throws Exception {

		String entrada = "";
		String resposta;

		System.out.println(Client.msgBoasVindas);

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket(Client.host, Client.port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		do {
			do {

				if (entrada.equals("REGRAS")) {
					System.out.println(Client.msgRegras);
				}

				// Solicita ao usu�rio a sele��o de pedra, papel ou tesoura ...
				System.out.println("Escreva qual sua escolha: Pedra || Papel || Tessoura");
				System.out.print("-> digite \"regras\" para ver as regras do jogo:\n");
				System.out.print("-> digite \"0\" para sair do jogo: ");
				entrada = inFromUser.readLine();
				entrada = entrada.toUpperCase();

			} while (!entrada.equals("PEDRA") && !entrada.equals("PAPEL") && !entrada.equals("TESOURA")
					&& !entrada.equals("0"));

			outToServer.writeBytes(entrada + "\n");
			if (entrada.equals("0")) {
				System.out.println("\nVoc� optou por (" + entrada + ") e saiu do jogo, volte mais vezes! o/");
			} else {
				// Transmite a entrada para o servidor e fornece o feedback adequado do jogador
				System.out.println(
						"\nSua jogada (" + entrada + ") foi salva com sucesso no servidor. \nAguarde o resultado ...");
			}
			
			// Pega a resposta do servidor e imprime ao jogador
			do {
				resposta = inFromServer.readLine();
			} while(!inFromServer.readLine().equals("VOC� GANHOU!") || !inFromServer.readLine().equals("VOC� PERDEU!")
					|| !inFromServer.readLine().equals("EMPATE!") || !inFromServer.readLine().equals("VOC� SAIU DO JOGO.") 
					|| !inFromServer.readLine().equals("O OUTRO JOGADOR DECIDIU PARAR, FIM DE JOGO."));

			System.out.println("Resultado: " + resposta);				

			if (resposta.equals("VOC� SAIU DO JOGO.")
					|| resposta.equals("O OUTRO JOGADOR DECIDIU PARAR, FIM DE JOGO.")) {
				// Fecha o Socket
				clientSocket.close();
			}
		} while (!resposta.equals("VOC� SAIU DO JOGO.")
				|| !resposta.equals("O OUTRO JOGADOR DECIDIU PARAR, FIM DE JOGO."));
		
		
	}
}