package com.team13.datamining.fileIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.team13.datamining.datamodels.DataSet;
import com.team13.datamining.datamodels.Feature;
import com.team13.datamining.datamodels.Values;

public class FileIO {
	
	public DataSet readFile(String fileName) throws IOException	{
		
		File file = new File(fileName);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);
		
		//Skip the first two lines
		if(!scanner.hasNextLine())	{
			throw new IOException();
		}
		scanner.nextLine();
		if(!scanner.hasNextLine())	{
			throw new IOException();
		}
		scanner.nextLine();
		
		//Read all lines starting with @attribute
		String line = scanner.nextLine();
		List<Feature> featureList = new ArrayList<>();
		while(line != null && line.length() > 0)	{
			
			if(!line.startsWith("@attribute"))	{
				throw new IOException("Input File not in proper Format");
			}
			
			String[] lineParts = line.split("\\s+");
			if(lineParts.length < 3)	{
				throw new IOException("Input File not in proper Format");
			}
			Feature feature = new Feature(lineParts[1], lineParts[2]);
			featureList.add(feature);
			line = scanner.nextLine();
		}
		
		//Skip next two lines
		if(!scanner.hasNextLine())	{
			throw new IOException();
		}
		line = scanner.nextLine();
		if(!scanner.hasNextLine())	{
			throw new IOException();
		}
		line = scanner.nextLine();
		
		List<Values> valuesList = new ArrayList<>();
		int rowIndex = 0;
		while(scanner.hasNextLine() && line.length() > 0)	{
			String[] lineParts = line.split(",");
			if(lineParts.length < 3)	{
				throw new IOException("Input File not in proper Format");
			}
			Values value = new Values(rowIndex, lineParts, featureList);
			valuesList.add(value);
			line = scanner.nextLine();
			rowIndex++;
		}
		
		scanner.close();
		//Create the dataset
		DataSet dataset = new DataSet(rowIndex, featureList, valuesList);
		return dataset;
	}
	
	public void printToFile(String fileName, String[] linesToPrint) throws IOException		{
		FileWriter fileWriter = new FileWriter(fileName);
		for (String line : linesToPrint)	{
			fileWriter.write(line);
		}
		fileWriter.close();
	}

}
