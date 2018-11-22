package app;
/*
 * SerialPortTest.java
 *
 *       Created on:  Feb 27, 2015
 *  Last Updated on:  Jan 10, 2018
 *           Author:  Will Hedgecock
 *
 * Copyright (C) 2012-2018 Fazecast, Inc.
 *
 * This file is part of jSerialComm.
 *
 * jSerialComm is free software: you can redistribute it and/or modify
 * it under the terms of either the Apache Software License, version 2, or
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation, version 3 or above.
 *
 * jSerialComm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of both the GNU Lesser General Public
 * License and the Apache Software License along with jSerialComm. If not,
 * see <http://www.gnu.org/licenses/> and <http://www.apache.org/licenses/>.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestSerial
{
	
	static public void main(String[] args) throws IOException
	{
		
		File f = new File("/dev/ttyUSB0");
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String str = new String();
		
		while((str = br.readLine()) != null)
		{
			System.out.println(str);
		}
	}
		
}
