DESFOLDER = classes
SRCFOLDER = src
DOCFOLDER = docs

MANIFEST = META-INF/MANIFEST.MF
JARNAME = peer2peer.jar

DES = -d $(DESFOLDER)
DESCHE = -classpath $(DESFOLDER)
SRCCHE = -classpath $(SRCFOLDER)
CC = javac
RUNCMD = java
RUN = $(RUNCMD) -cp $(DESFOLDER)
DOC = javadoc -classpath $(SRCFOLDER)
JAR = jar cvfm

RMIC = rmic
RMICREG = rmiregistry

all: clean doc runClient1

doc:
	@echo "__GENERATION OF THE DOCUMENTATION __________________"
	mkdir -p $(DOCFOLDER)
	$(DOC) -subpackages peer2peer -d $(DOCFOLDER)
	@echo "__DOC GENERATED ____________________________________"
	@echo " "

compPeer:
	@echo "__COMPILATION OF THE PEER2PEER PROJECT _____________"
	mkdir -p $(DESFOLDER)
	$(CC) $(DES) $(SRCCHE) $(SRCFOLDER)/**/*.java
	$(RMIC) $(DESCHE) $(DES) peer2peer.model.PeerImpl
	@echo "__COMPILATION DONE _________________________________"
	@echo " "

jar:
	@echo "__CREATION OF THE JAR ______________________________"
	make compPeer
	$(JAR) $(JARNAME) $(MANIFEST) -C $(DESFOLDER) .
	@echo "__JAR CREATED ______________________________________"
	@echo " "

runClient1:
	@echo "__RUN THE PEER2PEER PROJECT WITH FIRST FOLDER ______"
	make jar
	$(RMICREG) -J-cp -J$(JARNAME) &
	$(RUNCMD) -jar $(JARNAME) localhost/peer2peer /Users/Cyril/rmiFolder

runClient2:
	@echo "__RUN THE PEER2PEER PROJECT WITH SECOND FOLDER _____"
	$(RUNCMD) -jar $(JARNAME) localhost/peer2peer /Users/Cyril/rmiFolder2

help:
	@echo "__HELP ______________________________________________"
	@echo "You can use :"
	@echo "all : This command will clean everything then create a jar and execute it with the first setup of the make file."
	@echo "doc : product the doc of the project."
	@echo "compPeer : This command will compile the sources of the Peer project."
	@echo "jar : Create a runnable jar for the project."
	@echo "runClient1 : Run the jar with a first folder (that is hard coded for now :( If you want to use this makefile please change the folder in the runClient1 and runClient2 to use a real folder of your computer)."
	@echo "runClient2 : Run the jar with a second folder."
	@echo "help : Help with the Make file."
	@echo "clean : Clean all the folders. (.class, jar, temp files, doc...)"

.PHONY: clean

clean:
	@echo "__CLEANING _________________________________________"
	rm -rf **/*.class classes/* docs/* test/test/*.class *~ **/*~ $(JARNAME)
	-killall -9 rmiregistry
	@echo "__CLEANED __________________________________________"
	@echo " "