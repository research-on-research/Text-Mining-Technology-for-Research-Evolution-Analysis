import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.AlphabeticTokenizer;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class MyClassifier {

	ArrayList<String> texts = new ArrayList<String>();
	
	Instances trainData;
	Instances instances;
	
	NaiveBayes classifier;

	public void loadDataset(String fileName, String fileName2) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			System.out.println("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		} catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
			System.out.println(e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(fileName2));
			ArffReader arff = new ArffReader(reader);
			instances = arff.getData();
			System.out.println("===== Loaded dataset: " + fileName2 + " =====");
			reader.close();
		} catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName2);
			System.out.println(e.toString());
		}
	}
	
	public void load(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split(";", 2);
				texts.add(split[1]);
            }
			System.out.println("===== Loaded text data: " + fileName + " =====");
			reader.close();
			
			//if(texts.size() > 0)
			//	System.out.println(texts.get(0));
		}
		catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
			System.out.println(e.toString());
		}
	}

	public void evaluate() {
		try {
			trainData.setClassIndex(0);
			instances.setClassIndex(0);

			StringToWordVector stringToWordVectorFilter = new StringToWordVector();
			stringToWordVectorFilter.setAttributeIndices("last");
			stringToWordVectorFilter.setLowerCaseTokens(true);
			stringToWordVectorFilter.setWordsToKeep(1000000);
			stringToWordVectorFilter.setTokenizer(new AlphabeticTokenizer());
			
			AttributeSelection attributeSelectionFilter = new AttributeSelection();
			attributeSelectionFilter.setEvaluator(new InfoGainAttributeEval());
			Ranker rankerSearch = new Ranker();
			rankerSearch.setThreshold(0);
			attributeSelectionFilter.setSearch(rankerSearch);
			
			Filter[] filters = new Filter[2];
			filters[0] = stringToWordVectorFilter;
			filters[1] = attributeSelectionFilter;
			
			MultiFilter multiFilter = new MultiFilter();
			multiFilter.setFilters(filters);
			multiFilter.setInputFormat(trainData);
			trainData = Filter.useFilter(trainData, multiFilter);
			
//			multiFilter.setInputFormat(trainData); //DON'T UNCOMMENT! We are using batch filtering.
			instances = Filter.useFilter(instances, multiFilter);
			
			classifier = new NaiveBayes();

			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, 10, new Random(1));
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString());
			System.out
					.println("===== Evaluating on filtered (training) dataset done =====");
		} catch (Exception e) {
			System.out.println("Problem found when evaluating");
			System.out.println(e.toString());
		}
	}

	public void learn() {
		try {
			classifier.buildClassifier(trainData);

			System.out
					.println("===== Training on filtered (training) dataset done =====");
		} catch (Exception e) {
			System.out.println("Problem found when training");
			System.out.println(e.toString());
		}
	}

	public void classify(String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fileWriter);

			for (int i = 0; i < instances.numInstances(); i++) {
				Instance instance = instances.instance(i);
				double pred = classifier.classifyInstance(instance);
				out.write("\"" + (((int) pred) + 1) + "\";" + texts.get(i) + "\n");
			}

			out.close();
		} catch (Exception e) {
			System.out.println("Problem found when classifying the text");
			System.out.println(e.toString());
		}
	}

	public static void main(String[] args) {
		MyClassifier classifier = new MyClassifier();
		classifier.loadDataset(args[0], args[1]);
		classifier.load(args[2]);
		classifier.evaluate();
		classifier.learn();
		classifier.classify(args[3]);
	}
}