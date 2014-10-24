import sys

def main(input1, input2, output):
	fin1 = open(input1)
	fin2 = open(input2)
	fout = open(output, 'w')
	
	for line1 in fin1:
		if line1 == '"";"null"\n':
			continue
		
		list1 = line1.split(';', 1)
		abstract1 = list1[1].strip()
		
		fin2.seek(0)
		
		for line2 in fin2:
			list2 = line2.split(';', 1)
			abstract2 = list2[1].strip()
			
			if abstract1 == abstract2:
				break
		else:
			fout.write('"";' + abstract1 + "\n")
	
	fin1.close()
	fin2.close()
	fout.close()

if __name__ == '__main__':
	main(sys.argv[1], sys.argv[2], sys.argv[3])