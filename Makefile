DESFOLDER = classes
SRCFOLDER = src
DOCFOLDER = docs
FOLDER = /home
URL = localhost

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

all: clean doc jar launchRMI runClient

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
	$(RMIC) $(DESCHE) $(DES) peer2peer.model.peers.RootImpl
	@echo "__COMPILATION DONE _________________________________"
	@echo " "

jar:
	@echo "__CREATION OF THE JAR ______________________________"
	make compPeer
	$(JAR) $(JARNAME) $(MANIFEST) -C $(DESFOLDER) .
	@echo "__JAR CREATED ______________________________________"
	@echo " "

launchRMI:
	@echo "__LAUNCH RMI REGISTRY ______________________________"
	$(RMICREG) -J-cp -J$(JARNAME) &

runClient:
	@echo "__RUN THE PEER2PEER PROJECT ________________________"
	$(RUNCMD) -jar $(JARNAME) $(URL) $(FOLDER)

help:
	@echo "__HELP _____________________________________________"
	@echo "You can use :"
	@echo "all : This command will clean everything then create a jar and execute it with the first setup of the make file."
	@echo "doc : Product the doc of the project."
	@echo "compPeer : This command will compile the sources of the Peer project."
	@echo "jar : Create a runnable jar for the project."
	@echo "launchRMI : Launch the RMI Registry."
	@echo "runClient : I run the jar with a folder I take in parameter named FOLDER. Example: make FOLDER=/Users/Cyril/rmiFolder2 runClient"
	@echo "help : Help with the Make file."
	@echo "clean : Clean all the folders. (.class, jar, temp files, doc...)"

.PHONY: clean

clean:
	@echo "__CLEANING _________________________________________"
	rm -rf **/*.class classes/* docs/* test/test/*.class *~ **/*~ $(JARNAME)
	-killall -9 rmiregistry
	@echo "__CLEANED __________________________________________"
	@echo " "