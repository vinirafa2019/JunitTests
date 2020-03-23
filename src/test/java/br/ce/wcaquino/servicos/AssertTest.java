//package br.ce.wcaquino.servicos;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//import br.ce.wcaquino.entidades.Usuario;
//
//
//
//public class AssertTest {
//
//	@Test
//	public void teste() {
//		Assert.assertTrue(true);
//		Assert.assertFalse(false);
//		
//		Assert.assertEquals("Erro de compraçao",1, 2);
//		Assert.assertEquals(0.5454, 0.545,0.01);
//		
//		
//		int i=5;
//		Integer i2=5;
//		Assert.assertEquals(Integer.valueOf(i),i2);
//		Assert.assertEquals(i,i2.intValue());
//		
//		Assert.assertNotEquals("bola","casa");
//		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
//		Assert.assertTrue("bola".startsWith("bo"));
//		
//		Usuario u1=new Usuario("Usuario 1");
//		Usuario u2=new Usuario("Usuario 2");
//		Usuario u3=null;
//		
//		//Assert.assertEquals(u1,u2);
//		Assert.assertNotSame(u1,u2);
//		
//		Assert.assertNull(u3);
//		
//	}
//		
//}
