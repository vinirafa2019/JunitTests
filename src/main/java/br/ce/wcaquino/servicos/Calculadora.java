package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.naodivideporzero;

public class Calculadora {

	public int somar(int a, int b) {
		System.out.println("Estou executando o metodo somar");
		return a+b;
	}

	public int sub(int a, int b) {

		return a-b;
	}

	public int div(int a, int b) throws naodivideporzero {
		if(b==0) {
			throw new naodivideporzero("impossivel dividir por zero");
		}
		return a/b;
	}
	public void imprimir() {
		System.out.println("Passei aqui");
	}

}
