 package br.ce.wcaquino.dao;

import java.util.List;

import br.ce.wcaquino.entidades.Locacao;

public interface LocacaoDao {

	public void Salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendesntes();	  
}
