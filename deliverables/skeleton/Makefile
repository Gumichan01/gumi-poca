# Makefile scala

CC=fsc
DIR=../bin/
FLAG=-d .
SRC=$(shell ls *.scala)

# I put the classes in order of dependency
all: Answer.class Question.class Survey.class

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(CC) $(FLAG) $*.scala

mrproper:
	rm *.class
