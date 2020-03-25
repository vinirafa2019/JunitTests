package br.ce.wcaquino.servicos;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builder.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matcher.MatchersProprios.eHoje;
import static br.ce.wcaquino.matcher.MatchersProprios.eHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEestoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(ParallelRunner.class)
public class LocacacaoServiceTeste {
	@InjectMocks @Spy
	private LocacaoService service;
	
	@Mock
	private SPCService spc ;
	@Mock
	private LocacaoDao dao;
	@Mock
	private EmailService email;
	
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception=ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);	
		System.out.println("Inicializando 2");
		
	}
	@After
	public void tearDown() {
		System.out.println("finalizando 2");
	}
	@Test
	public void DeveAlugarFilme() throws Exception {
		//Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =Arrays.asList(umFilme().comValor(5.0).agora());
		
		Mockito.doReturn(DataUtils.obterData(27, 3, 2020)).when(service).ObterData();
		//acao 
		Locacao locacao= service.alugarFilme(usuario, filmes);
			
		//verificaçao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//		error.checkThat(locacao.getDataLocacao(),eHoje());
//		error.checkThat(locacao.getDataRetorno(),eHojeComDiferencaDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(27,3, 2020)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(28,3, 2020)),is(true));
	}
	@Test(expected = FilmesSemEestoqueException.class)
	public void NaoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		Usuario usuario =umUsuario().agora();
		List<Filme> filmes =Arrays.asList(umFilmeSemEstoque().agora());
		
		//açao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void NaoDeveAlugarFilmeSemUsuario() throws FilmesSemEestoqueException {
		//cenario

		List<Filme> filmes =Arrays.asList(umFilme().agora());
		
		//acao
		try {
			service.alugarFilme(null,filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			// TODO Auto-generated catch block
			Assert.assertThat(e.getMessage(),is("Usuario Vazio"));
		}
	}
	@Test
	public void NaoDeveAlugarSemFilmeDisponivel() throws FilmesSemEestoqueException, LocadoraException {

		Usuario usuario = umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		//acao
		service.alugarFilme(usuario,null);
	}

	@Test
	public void DeveDevolvernasegundaaoalugarnosabado() throws Exception {
		//Assume.assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario =  umUsuario().agora(); 
		List<Filme> filmes =Arrays.asList(umFilme().agora());
		
		Mockito.doReturn(DataUtils.obterData(28, 3, 2020)).when(service).ObterData();
		
		//acao
		Locacao retorno= service.alugarFilme(usuario, filmes);
		
		
		//verificacao
		//4+4+3+2+1+0=14
		assertThat(retorno.getDataRetorno(),caiNumaSegunda());
		//PowerMockito.verifyNew(Date.class,Mockito.times(2)).withNoArguments();
	}
	

	@Test
	public void naodevealugarfilmeparanegativadospc() throws Exception {
		//cenario
		Usuario usuario= umUsuario().agora();
		//verificou um possivel erro --> Usuario usuario2= umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes=Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true); 
		
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
		//verificacao
			Assert.fail();
		}
		 catch (LocadoraException e) {
		assertThat(e.getMessage(),is("Usuario Negativado"));
		}
		
		verify(spc).possuiNegativacao(usuario);
	} 
	@Test
	public void deveenviaremailparalocacoesatrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro Atrasado").agora();
		List<Locacao>locacoes =
				Arrays.asList(
					umLocacao().atrasado().comUsuario(usuario).agora(),
					umLocacao().comUsuario(usuario2).agora(),
					umLocacao().atrasado().comUsuario(usuario3).agora(),
					umLocacao().atrasado().comUsuario(usuario3).agora());
		 
		when(dao.obterLocacoesPendesntes()).thenReturn(locacoes); 
		
		//acao
		service.notificarAtrasos();
		//verificacao
		verify(email,times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email,atLeastOnce()).notificarAtraso(usuario3);
		verify(email, never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);		
	}
	
	@Test
	public void daveTratarErronoSpc() throws Exception {
		//cenario
		Usuario usuario= umUsuario().agora();
		List<Filme> filmes=Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));
		
		//verificacao
		exception.expect(LocadoraException.class);
		//exception.expectMessage("Falha catastrofica");
		exception.expectMessage("Problema com SPC,Tente novamente");

		//acao
		service.alugarFilme(usuario, filmes);
	} 
	@Test
	public void DeveProrrogarlocacao() {
		//cenario
		Locacao locacao = umLocacao().agora();
		
		//acao
		service.prorrogarlocacaao(locacao, 3);
		
		//verificacao
		ArgumentCaptor<Locacao>argCaptor=ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).Salvar(argCaptor.capture());
		Locacao locacaoRetornada = argCaptor.getValue();
		
		error.checkThat(locacaoRetornada.getValor(),is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(),is(eHoje()));
		error.checkThat(locacaoRetornada.getDataRetorno(),is(eHojeComDiferencaDias(3)));
		
	}
	 
	@Test
	public void deveCalcalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes=Arrays.asList(umFilme().agora());
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		java.lang.reflect.Method metodo =clazz.getDeclaredMethod("CalcularValorLocacao",List.class);
		metodo.setAccessible(true);
		Double valor  = (Double) metodo.invoke(service,filmes);
		
	
		//verificacao
		Assert.assertThat(valor,is(4.0));
	}
	
}



















