#!/bin/bash

nice -20 java -Xmx2648m -Xms1024m -cp bin:lib/*:. com.viret.pilexa.pi.Main
