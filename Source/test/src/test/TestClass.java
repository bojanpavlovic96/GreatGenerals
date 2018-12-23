package test;

public class TestClass implements Interface1, Interface2 {

	@Override
	public void printFromInterface2() {
		System.out.println("interface 2");
	}

	@Override
	public void printFromInterface1() {
		System.out.println("interface 1");
	}

}
