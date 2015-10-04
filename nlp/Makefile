FILE = nlp-cs224n

#TARGET = 

PRINTER = lp

all: $(FILE).tex
	pdflatex $(FILE)

clean:
	rm -f $(FILE).ps $(FILE).pdf $(FILE).log $(FILE).dvi $(FILE).aux

#install:
#	scp $(FILE).ps  $(TARGET)
#	scp $(FILE).pdf  $(TARGET)

#pdf:
#	gs -q -dNOPAUSE -sDEVICE=pdfwrite -sOutputFile=$(FILE).pdf $(FILE).ps -c quit

p:
	lpr -P$(PRINTER) $(FILE).ps; 
	lpq -P$(PRINTER)
