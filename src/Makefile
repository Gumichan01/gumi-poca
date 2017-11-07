# Makefile scala

CC=fsc
DIR=../bin/
FLAG=-d .
SRC=$(shell ls *.scala)

compile: $(SRC:.scala=.class)

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(CC) $(FLAG) $*.scala

mrproper:
	rm *.class
