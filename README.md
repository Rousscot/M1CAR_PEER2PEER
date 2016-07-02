# M1CAR_PEER2PEER

**Authors:**
- __Aurelien Rousseau__
- __Cyril Ferlicot__

## Description

To manage the TP we created a Makefile. 

By default the url is `localhost` and the folder to share is `/home`.
To change them you can custom them with the FOLDER and the URL variables.

Here is an example of use:

In the first terminal:

`make FOLDER=/Users/Cyril/rmiFolder`

or if you are not in localhost: `make FOLDER=/Users/Cyril/rmiFolder URL=otherURL`

In the second terminal:

`make FOLDER=/Users/Cyril/rmiFolder2 runClient`

If you are onf windows, I am sorry for you but I did not make any instruction.

The make command will generate the documentation of the project in the `docs` folder.