package br.ce.wcaquino.matcher;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiasSemanaMatcher caiEm(Integer diaSemana) {
		return new DiasSemanaMatcher(diaSemana);
	}
	
	public static DiasSemanaMatcher caiNumaSegunda() {
		return new DiasSemanaMatcher(Calendar.MONDAY);
		
	}
	
	public static DataDiferencaDias eHojeComDiferencaDias(Integer qtdDias) {
		return new DataDiferencaDias(qtdDias);
		
	}
	
	public static DataDiferencaDias eHoje() {
		return new DataDiferencaDias(0);
		
	}
}
