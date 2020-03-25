package br.ce.wcaquino.servicos;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ce.wcaquino.exceptions.naodivideporzero;
import br.ce.wcaquino.runners.ParallelRunner;




@RunWith(ParallelRunner.class)
public class CalcularodoraTeste {

	private Calculadora calc;
	
	@Before
	public void setup() {
		 calc =new Calculadora();
		 System.out.println("iniciando");
	}
	@After
	public void tearDown() {
		System.out.println("finalizando...");
	}
	
	@Test
	public void som2() {
		//cenario
		int a=5;
		int b=3;
		

		
		//acao
		int resultado=calc.somar(a,b);
	
		//verificacao
		Assert.assertEquals(8,resultado);
	}
	
	@Test
	public void sub() {
		//cenario
		int a=8;
		int b=5;
		Calculadora calc =new Calculadora();
		//acao
		int resultado=calc.sub(a,b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
	}
	@Test
	public void div() throws naodivideporzero {
		//cenario
		int a=6;
		int b=2;
		
		
		//acao
		int resultado=calc.div(a,b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
		
	
	}

	
	
	
}
