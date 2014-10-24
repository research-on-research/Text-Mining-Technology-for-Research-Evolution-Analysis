import sys
import xml.etree.cElementTree as cElementTree
import HTMLParser


def main(input, output):
    fout = open(output, 'w')
    h = HTMLParser.HTMLParser()
    readAbstract = False
    count = 0

    for event, elem in cElementTree.iterparse(open(input)):
        if elem.tag == 'AbstractText':
            if elem.text:
                if readAbstract:
                    fout.write(' ')

                readAbstract = True
                fout.write(h.unescape(elem.text).encode('utf-8'))
        elif elem.tag == 'PubmedArticle':
            count = count + 1

            if readAbstract:
                fout.write("\n")
            else:
                fout.write("null\n")

            readAbstract = False
        elem.clear()

    fout.close()
    print str(count) + " abstracts read!\n"

if __name__ == '__main__':
    main(sys.argv[1], sys.argv[2])
