package br.ce.wcaquino.matcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.ce.wcaquino.utils.DataUtils;


public class DataDiferencaDias extends TypeSafeMatcher<Date>{

	private Integer qtdDias;
	
	public DataDiferencaDias(Integer qtdDias) {
		this.qtdDias=qtdDias;
		// TODO Auto-generated constructor stub
	}
	
	public void describeTo(Description desc) {
		Date dtEsperada = DataUtils.obterDataComDiferencaDias(qtdDias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		desc.appendText(format.format(dtEsperada));
	}


	@Override
	protected boolean matchesSafely(Date data) {
		// TODO Auto-generated method stub
		return DataUtils.isMesmaData(data,DataUtils.obterDataComDiferencaDias(qtdDias));
	}
	

}
