DOMAIN=surgery

all: create_directories extract_abstracts abstracts_to_csv remove_gold_standard generate_arff_gold_standard generate_arff_must_classify computer_classification fix_encoding join

clean:
	rm -rf output temp

create_directories:
	mkdir temp
	mkdir output

extract_abstracts:
	python code/001-002-extract-abstracts.py input/$(DOMAIN).xml temp/$(DOMAIN).txt

abstracts_to_csv:
	python code/002-003-abstracts-to-csv.py temp/$(DOMAIN).txt temp/$(DOMAIN).csv

remove_gold_standard:
	python code/003-005-remove-gold-standard.py temp/$(DOMAIN).csv gold-standard/$(DOMAIN).csv temp/$(DOMAIN)-must-classify.csv

generate_arff_gold_standard:
	python code/004-006-csv-to-arff.py gold-standard/$(DOMAIN).csv temp/$(DOMAIN)-gold-standard.arff

generate_arff_must_classify:
	python code/004-006-csv-to-arff.py temp/$(DOMAIN)-must-classify.csv temp/$(DOMAIN)-must-classify.arff

compile_java_code:
	javac code/MyClassifier.java -d temp

computer_classification: compile_java_code
	java -cp "temp;libs/weka.jar" MyClassifier temp/$(DOMAIN)-gold-standard.arff temp/$(DOMAIN)-must-classify.arff temp/$(DOMAIN)-must-classify.csv temp/$(DOMAIN)-computer-classified.csv

fix_encoding:
	python code/007-007-fix-java-encoding.py temp/$(DOMAIN)-must-classify.csv temp/$(DOMAIN)-computer-classified.csv temp/$(DOMAIN)-computer-classified-fixed-encoding.csv

join:
	python code/007-008-join.py temp/$(DOMAIN).csv gold-standard/$(DOMAIN).csv temp/$(DOMAIN)-computer-classified-fixed-encoding.csv output/$(DOMAIN).csv