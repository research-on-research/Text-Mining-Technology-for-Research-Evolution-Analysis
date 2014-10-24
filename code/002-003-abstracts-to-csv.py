import sys
import csv

def main(input, output):
	fin = open(input)
	fout = open(output, 'w')
	csvwriter = csv.writer(fout, delimiter=';', quotechar='"', doublequote=True, lineterminator="\n", quoting=csv.QUOTE_ALL)
	count = 0
	
	#csvwriter.writerow(['class', 'abstract'])
	
	for line in fin:
		count = count + 1
		csvwriter.writerow(['', line.strip()])
	
	fin.close()
	fout.close()
	print str(count) + " abstracts read!\n"

if __name__ == '__main__':
	main(sys.argv[1], sys.argv[2])