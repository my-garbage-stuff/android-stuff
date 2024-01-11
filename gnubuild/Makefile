SDK=~/Android/Sdk
TARGET=$(shell ls $(SDK)/platforms | grep "[2-3]." |  sort | tail -n 1)
TOOL=$(shell ls $(SDK)/build-tools | grep "[2-3]." | sort | tail -n 1)
JAVADIR=$(JAVA_HOME)/bin
BUILDTOOLS=$(SDK)/build-tools/$(TOOL)
AJAR=$(SDK)/platforms/$(TARGET)/android.jar
ADX=$(BUILDTOOLS)/dx
AAPT=$(BUILDTOOLS)/aapt
JAVAC=$(JAVADIR)/javac -target 8
JFLAGS=-source 8 -encoding UTF-8
#JFLAGS=-source 8 -encoding UTF-8 -g:none
JARSIGNER=$(JAVADIR)/jarsigner
APKSIGNER=$(BUILDTOOLS)/apksigner
ZIPALIGN=$(BUILDTOOLS)/zipalign
KEYTOOL=$(JAVADIR)/keytool
ADB=$(SDK)/platform-tools/adb

KEYFILE=keystore.jks

SRC=src/
NAME=app


all: clear build zipalign sign
build:
	mkdir -p bin
	mkdir -p gen
	mkdir -p assets src res || true
	$(AAPT) package -v -f -I $(AJAR) -M AndroidManifest.xml -A assets -S res -m -J gen -F bin/resources.ap_
	$(JAVAC) $(JFLAGS) -classpath $(AJAR) -sourcepath $(SRC) -sourcepath gen -d bin $(shell find $(SRC) -name *.java)
	$(ADX) --dex --output=bin/classes.dex bin
	mv bin/resources.ap_ bin/$(NAME).ap_
	cd bin ; $(AAPT) add $(NAME).ap_ classes.dex
zipalign:
	$(ZIPALIGN) -v -p 4 bin/$(NAME).ap_ bin/$(NAME)-aligned.ap_
	mv bin/$(NAME)-aligned.ap_ bin/$(NAME).ap_
optimize:
	optipng -o7 $(shell find res -name *.png)
sign:
	$(APKSIGNER) sign --ks $(KEYFILE) --out bin/$(NAME).apk bin/$(NAME).ap_
	rm -f bin/$(NAME).ap_
clear:
	rm -rf bin gen
install:
	$(ADB) install -r bin/$(NAME).apk
update-git:
	git add .
	git commit -m "İsimsiz Commit :D"
	git push -u origin master
