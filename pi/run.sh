#!/bin/bash

nice -20 java -Xmx2648m -Xms1024m -cp "$(echo lib/*.jar | tr ' ' ':'):bin:.:build/classes/main:$(echo build/dep/*.jar | tr ' ' ':')" com.viveret.pilexa.pi.Main
