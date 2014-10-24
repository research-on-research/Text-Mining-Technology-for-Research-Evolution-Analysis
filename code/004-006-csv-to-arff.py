import sys

def main(input1, output):
	fin1 = open(input1)
	fout = open(output, 'w')
	
	fout.write('@relation corpus\n')
	fout.write('\n')
	fout.write('@attribute class_attr {class1, class2, class3}\n')
	fout.write('@attribute abstract string\n')
	fout.write('\n')
	fout.write('@data\n')
	
	for line1 in fin1:
		list1 = line1.split(';', 1)
		classification1 = list1[0].replace('"', '')
		
		if classification1 == '':
			classification1 = '?'
		else:
			classification1 = 'class' + classification1
		
		abstract1 = list1[1].strip().replace('""', '\\"')
		output_line = '{0 ' + classification1 + ', 1 ' + abstract1 + '}\n'
		output_line = output_line.replace('1 \\""', '1 "\\"')
		fout.write(output_line)
	
	fin1.close()
	fout.close()

if __name__ == '__main__':
	main(sys.argv[1], sys.argv[2])