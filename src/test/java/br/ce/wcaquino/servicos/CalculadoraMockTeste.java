package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;



public class CalculadoraMockTeste {

	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void devomostrardiferencaentremockespy() {
		Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
		//Mockito.when(calcSpy.somar(1, 2)).thenReturn(5);
		Mockito.doReturn(5).when(calcSpy).somar(1, 2);
		Mockito.doNothing().when(calcSpy).imprimir();
		
		System.out.println("calcMock "+calcMock.somar(1, 2));
		System.out.println("calcSpy "+calcSpy.somar(1, 2));
		
		System.out.println("Mock");
		calcMock.imprimir();
		System.out.println("Spy");
		calcSpy.imprimir();
	}
	
	
	@Test
	public void teste() {
		Calculadora calc = mock(Calculadora.class);
		ArgumentCaptor<Integer>argCaptor=ArgumentCaptor.forClass(Integer.class);
		when(calc.somar(argCaptor.capture(),argCaptor.capture())).thenReturn(5);
		assertEquals(5,calc.somar(1, 8));
		//System.out.println(argCaptor.getAllValues());
		
	}
}
