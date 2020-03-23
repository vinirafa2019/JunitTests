package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.CalcularodoraTeste;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTeste;
import br.ce.wcaquino.servicos.LocacacaoServiceTeste;

//@RunWith(Suite.class)
@SuiteClasses({
	CalcularodoraTeste.class,
	CalculoValorLocacaoTeste.class,
	LocacacaoServiceTeste.class
})
public class SuiteExecucao {
	//remova se puder
	
	
	@BeforeClass
	public static void befora() {
		System.out.println("before");
	}
	
	@AfterClass
	public static void after() {
		System.out.println("after");
	}
}
