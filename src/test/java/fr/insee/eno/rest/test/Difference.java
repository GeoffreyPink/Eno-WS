package fr.insee.eno.rest.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fr.insee.eno.utils.Constants;

/**
 * Difference class calculating the differences between a reference file and a
 * file to compare Used to test the created form with a reference one in order
 * to validate the non regression of the application
 * 
 * @author gerose
 *
 */
public class Difference {

	public boolean nonRegressionTest() {

		String fileToComparePath = Constants.TEST_FILE_TO_COMPARE;
		String referenceFilePath = Constants.TEST_REFERENCE_FILE;
		String diffFilePath = Constants.TEMP_TEST_FOLDER + "/diff.txt";
		boolean result = false;

		BufferedReader referenceFile = null;
		BufferedReader fileToCompare = null;

		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(Constants.TEST_REFERENCE_FILE);
			referenceFile = new BufferedReader(new FileReader(url.getPath()));
		} catch (FileNotFoundException e) {
			System.out.println("File : " + referenceFilePath + " not Found");
			e.printStackTrace();
		}
		try {
			fileToCompare = new BufferedReader(new FileReader(fileToComparePath));
		} catch (FileNotFoundException e) {
			System.out.println("File : " + fileToComparePath + " not Found");
		}

		if (referenceFile != null & fileToCompare != null) {
			String referenceString = null;
			String referenceLine;
			try {
				while ((referenceLine = referenceFile.readLine()) != null) {
					referenceString = referenceString + referenceLine.trim();

				}
			} catch (IOException e1) {
				System.out.println("Impossible to read file : " + referenceFilePath);
				e1.printStackTrace();
			}

			String stringToCompare = null;
			String lineToCompare;
			try {
				while ((lineToCompare = fileToCompare.readLine()) != null) {
					stringToCompare = stringToCompare + lineToCompare.trim();
				}
			} catch (IOException e1) {
				System.out.println("Impossible to read file : " + fileToComparePath);
				e1.printStackTrace();
			}
			String diff = null;
			int indexOfDiff = 0;
			if (stringToCompare != null & referenceString != null) {
				diff = StringUtils.difference(referenceString, stringToCompare);
				indexOfDiff = StringUtils.indexOfDifference(referenceString, stringToCompare);
				System.out.println("Comparison done");
			}
			if (indexOfDiff > 0) {
				System.out.println("Test failed");
				System.out.println("Index at which the file begins to differ : " + indexOfDiff);
				result = false;

			} else {
				System.out.println("Test success");
				result = true;
			}
			FileWriter fw;
			try {
				FileUtils.forceMkdir(new File(Constants.TEMP_TEST_FOLDER));
				fw = new FileWriter(diffFilePath, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw);
				if (indexOfDiff > 0) {
					out.println("Index at which the file begins to differ :" + indexOfDiff);
				}
				out.println(diff);
				System.out.println("Difference written in file : " + diffFilePath);
				out.close();

			} catch (IOException e) {
				System.out.println("Impossible to write file : " + diffFilePath);
				e.printStackTrace();
			}
		}
		return result;
	}
}
