package io.ffreedom.redstone.example;

import io.ffreedom.common.env.SystemPropertys;

public class TestPath {
	
	public static void main(String[] args) {

		System.out.println("=========================================");

		System.out.println(System.getenv("JAVA_HOME"));

		System.out.println(SystemPropertys.JAVA_IO_TMPDIR);

		//System.out.println(new File("").getAbsolutePath() + "/lib");

		//File file = new File(new File("").getAbsolutePath() + "/lib/linux64");

		///=System.out.println(Arrays.asList(file.list()));

	}

}