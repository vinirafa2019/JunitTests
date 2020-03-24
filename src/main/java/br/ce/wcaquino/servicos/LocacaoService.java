package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.dao.LocacaoDao;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmesSemEestoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	 
	private LocacaoDao dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmesSemEestoqueException, LocadoraException {
		

		if(usuario==null) {
			throw new LocadoraException("Usuario Vazio");
		}
		if(filmes == null|| filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		
		for(Filme filme:filmes) {
			if (filme.getEstoque()==0) {
				throw new FilmesSemEestoqueException();
			}
		}
		boolean negativado;
		
		try {
			 negativado =spcService.possuiNegativacao(usuario);
		}catch (Exception e) {
			throw new LocadoraException("Problema com SPC,Tente novamente");
		}
		if(negativado) {			
			throw new LocadoraException("Usuario Negativado");
		}
			
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(ObterData());
		locacao.setValor(CalcularValorLocacao(filmes));
		//Entrega no dia seguinte
		Date dataEntrega = ObterData();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega=adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a Locacao
		 dao.Salvar(locacao);
		return locacao;
	}

	protected Date ObterData() {
		return new Date();
	}

	private Double CalcularValorLocacao(List<Filme> filmes) {
		System.out.println("Estou calculando");
		Double valorTotal=0d; 
		
		for(int i=0;i<filmes.size();i++) {
			Filme filme=filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
			case 2: valorFilme=valorFilme*0.75; break;
			case 3: valorFilme=valorFilme*0.50; break;
			case 4: valorFilme=valorFilme*0.25; break;
			case 5: valorFilme= 0d; break;
			}	
			valorTotal+=valorFilme;
		}
		return valorTotal;
	}

	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendesntes();
		for(Locacao locacao:locacoes) {
			if(locacao.getDataRetorno().before(ObterData())) {
				emailService.notificarAtraso(locacao.getUsuario());
			}
		}
	}
	public void prorrogarlocacaao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(ObterData());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor()*dias);
		dao.Salvar(novaLocacao);
	}
	
}