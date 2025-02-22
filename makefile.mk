JFLAGS = -g
COMP = javac

.SUFFIXES: .java .class

.java.class:
	$(COMP) $(JFLAGS) $*.java

CLASSES = \
	Vector.java \
	CloudData.java \
	CloudDataApp.java \
	SeqCompute.java \
	ParCompute.java

default: classes

classes: $(CLASSES:.java=.class)

run:
	java CloudDataApp

clean:
	$(RM) *.class