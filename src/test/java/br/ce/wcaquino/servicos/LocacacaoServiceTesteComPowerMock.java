package br.ce.wcaquino.servicos;
import static br.ce.wcaquino.builder.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matcher.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(PowerMockRunner.class)
@PrepareForTest(LocacaoService.class)
public class LocacacaoServiceTesteComPowerMock {
	@InjectMocks
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
		service = PowerMockito.spy(service);
	}	
	@Test
	public void DeveAlugarFilme() throws Exception {
		//Assume.assumeFalse(verificarDiaSemana(new Date(), Calendar.SATURDAY));

		//cenario
		
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes =Arrays.asList(umFilme().comValor(5.0).agora());
		
		//whenNew(Date.class).withNoArguments().thenReturn(obterData(27, 03, 2020));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,27);
		calendar.set(Calendar.MONTH,Calendar.MARCH);
		calendar.set(Calendar.YEAR,2020);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		//acao 
		Locacao locacao= service.alugarFilme(usuario, filmes);
			
		//verificaçao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
//		error.checkThat(locacao.getDataLocacao(),eHoje());
	//	error.checkThat(locacao.getDataRetorno(),eHojeComDiferencaDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(27,3, 2020)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(28,3, 2020)),is(true));
	}

	@Test
	public void DeveDevolvernasegundaaoalugarnosabado() throws Exception {
		//Assume.assumeTrue(verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario =  umUsuario().agora(); 
		List<Filme> filmes =Arrays.asList(umFilme().agora());
		
		//whenNew(Date.class).withNoArguments().thenReturn(obterData(28, 03, 2020));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,28);
		calendar.set(Calendar.MONTH,Calendar.MARCH);
		calendar.set(Calendar.YEAR,2020);
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
		//acao
		Locacao retorno= service.alugarFilme(usuario, filmes);
		
		
		//verificacao
		//4+4+3+2+1+0=14
		assertThat(retorno.getDataRetorno(),caiNumaSegunda());
		//PowerMockito.verifyNew(Date.class,Mockito.times(2)).withNoArguments();
		
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}


	@Test
	public void DeveAlugarFilmeSemCalcularValor() throws Exception {
		//cenario
		Usuario usuario= umUsuario().agora();
		List<Filme> filmes=Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(service,"CalcularValorLocacao",filmes);
		
		//acao
		Locacao locacao= service.alugarFilme(usuario, filmes);
		
		
		//verificacao
		assertThat(locacao.getValor(),is(1.0));
		PowerMockito.verifyPrivate(service).invoke("CalcularValorLocacao",filmes);
	}
	
	@Test
	public void deveCalcalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes=Arrays.asList(umFilme().agora());
		//acao
		Double valor  = (Double) Whitebox.invokeMethod(service,"CalcularValorLocacao",filmes);
		//verificacao
		Assert.assertThat(valor,is(4.0));
	}
	
}



















