# Makefile scala

CC=fsc
DIR=../bin/
FLAG=-d . -unchecked
SRC=$(shell ls *.scala)

# I put the classes in order of dependency
all: Answer.class AnswerSheet.class Question.class Survey.class Course.class \
User.class Test.class Evaluator.class

%.class: %.scala
	@echo "Compiling $*.scala.."
	@$(CC) $(FLAG) $*.scala

mrproper:
	rm *.class
