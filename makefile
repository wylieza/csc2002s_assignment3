BINDIR=./bin
SRCDIR=./src
DOCDIR=./doc


.SUFFIXES: .java .class

${BINDIR}/%.class: ${SRCDIR}/%.java
	javac $< -cp ${BINDIR} -d ${BINDIR} 

all: ${BINDIR}/CloudDataApp.class

${BINDIR}/CloudDataApp.class: ${BINDIR}/CloudData.class ${BINDIR}/Vector.class ${BINDIR}/ParCompute.class ${BINDIR}/SeqCompute.class

${BINDIR}/CloudData.class: ${BINDIR}/Vector.class

${BINDIR}/ParCompute.class: ${BINDIR}/CloudData.class ${BINDIR}/SeqCompute.class

${BINDIR}/SeqCompute.class: ${BINDIR}/CloudData.class

clean:
	rm -f ${BINDIR}/*.class

run:
	@java -cp ./bin CloudDataApp ${args}

docs:
	javadoc  -classpath ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -rf ${DOCDIR}/*