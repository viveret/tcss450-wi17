#!/bin/bash

nice -20 java -Xmx2648m -Xms1024m -cp "$(echo lib/*.jar | tr ' ' ':'):bin:." com.viveret.pilexa.pi.Main
