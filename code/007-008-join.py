import sys

def main(input1, input2, input3, output):
	fin1 = open(input1)
	fin2 = open(input2)
	fin3 = open(input3)
	fout = open(output, 'w')
	
	for line1 in fin1:
		if line1 == '"";"null"\n':
			fout.write(line1)
			continue
		
		list1 = line1.split(';', 1)
		classification1 = list1[0]
		abstract1 = list1[1].strip()
		
		fin2.seek(0)
		
		for line2 in fin2:
			list2 = line2.split(';', 1)
			classification2 = list2[0]
			abstract2 = list2[1].strip()
			
			if abstract1 == abstract2:
				fout.write(classification2 + ';' + abstract1 + '\n')
				break
		else:
			#fin3.seek(0)
			
			for line3 in fin3:
				list3 = line3.split(';', 1)
				classification3 = list3[0]
				abstract3 = list3[1].strip()
				
				if abstract1 == abstract3:
					fout.write(classification3 + ';' + abstract1 + '\n')
					break
			else:
				print 'ops:\n' + abstract1
				break
	
	fin1.close()
	fin2.close()
	fin3.close()
	fout.close()

if __name__ == '__main__':
	main(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])