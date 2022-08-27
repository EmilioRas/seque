### seque

#An audio midi tool
#Describe your all Audio and Midi Device
#It is able to connect at now two midi Device . DEvice IN to Device OUT

#Configuration
Get this code with git or download the package.
Build your main folder (folder where is the pom.xml) . To build you must run this command in main folder of your branch (main).  
- mvn clean package

 
https://youtu.be/3Z_Yb8uY5Qo

 

#####Seque
#####Sequencer and high level MIDI interface

###Run

To run Seque you before have jvm 1.7 o +
The command to run Seque have some arguments

java -jar seque-1.0.0.jar [arg1] [arg2] [arg3]

[arg1] is the name of your midi files (without ending string '_1.mid','_2.mid',ecc.)

[arg2] is the path of your working dir (where you have your jar (seque) . It's root for your [arg1] folder for your midi files)

[arg3] (optional). use NO-SRV to no show gui interface. for example in ssh connection to other device

###Quit
type 'q' to exit, 's' to stop sequencer, 'r' to restart sequencer
#Sequencer Configuration
###seque.ini

In this file, seque.ini, Seque read how to play your midi track on sequencer:

#first line
- i.e:
-     PPQ,1,10,4#       (PPQ ,DivisionType, for example said to Seque the division type is Pulse Per Quarter Note. 
                          Seque read also other DivisionType, but their not are used at now)
-        ,1,10,4#			(1 , is the Resolution, but her, like DivisionType, not is used)
-          ,10,4#			(10, for example are the max number of your midi tracks file in path. They can be less also)
-             ,4#			(4, for example your device number)
#second line
- i.e: 
-     180,1,0,0,-1#			 (180, are you setted BPM to play Seque at that moment)
-        ,1,0,0,-1#			 (1, tick position)
-          ,0,0,-1#			 (0, number of loops)
-            ,0,-1#			 (0, start of your loop)
-              ,-1#           (-1, end of your loop ,(-1 to the end of sequences))

#other lines (max 12,min 1)
- i.e: 
-       ,5,keys#				(5, your track numbers on that device)
-         ,keys#				(keys. for example your rely "device name" by track)

The command line is not user easy experience, but you can take in mind that when you run seque the Sequencer 
is last phase. Your may be able to connects before your midi IN/OUT ports. And then, ever with sy number , choose tracks for 
sequencer.

Enjoy 
& have a good sound



