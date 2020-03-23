package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEestoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTeste {
	@InjectMocks
	private LocacaoService service;
	@Mock
	private LocacaoDao dao;
	@Mock
	private SPCService spc;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	private static Filme filme1 = umFilme().agora();
	private static Filme filme2 = umFilme().agora();
	private static Filme filme3 = umFilme().agora();
	private static Filme filme4 = umFilme().agora(); 
	private static Filme filme5 = umFilme().agora();
	private static Filme filme6 = umFilme().agora();
	private static Filme filme7 = umFilme().agora();
	
	@Parameters(name = "{2}")
	public static Collection<Object[]>getParametros(){
		return Arrays.asList(new Object[][]{
			{Arrays.asList(filme1,filme2),8.00,"2 Filmes:Sem desconto"},
			{Arrays.asList(filme1,filme2,filme3),11.00,"3 Filmes:25%"},
			{Arrays.asList(filme1,filme2,filme3,filme4),13.00,"4 Filmes: 50%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5),14.00,"5 Filmes: 75%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5,filme6),14.00,"6 Filmes:100%"},
			{Arrays.asList(filme1,filme2,filme3,filme4,filme5,filme6,filme7),18.00,"7 Filmes:Sem desconto"},
		});
	}
	
	@Test
	public void DeveCalcularValorLoccacaoComDescontos() throws FilmesSemEestoqueException, LocadoraException {
		//cenario
		Usuario usuario = umUsuario().agora();
		
		
		//acao
		Locacao resultado= service.alugarFilme(usuario, filmes);
		
		
		//verificacao
		//4+4+3=11
		assertThat(resultado.getValor(),CoreMatchers.is(valorLocacao));
		
	}

}
