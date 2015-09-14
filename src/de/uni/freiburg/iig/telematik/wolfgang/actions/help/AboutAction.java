/*
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.wolfgang.actions.help;

import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.graphic.dialog.StringDialog;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractWolfgangAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;

public class AboutAction extends AbstractWolfgangAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4904945549599021867L;
	private static final String copyright = "WOLFGANG - Petri Net Editor\n"
			+ "Version: 1.0.1"
			+ "\nCopyright (c) 2015, IIG Telematics, Uni Freiburg\n"
            + "All rights reserved (see detailed licence header below).\n\n"			
			+ "WOLFGANG is licensed under the BSD 3-Clause license.\n"
            + "\n"
            + "It is based on software from the Department of Telematics of the\n"
            + "Institute of Computer Science and Social Studies, University of\n"
            + "Freiburg, namely TOVAL (http://sourceforge.net/p/toval), JAGAL\n"
            + "(http://sourceforge.net/p/jagal), SEWOL\n"
            + "(https://sourceforge.net/projects/jawl/), and SEPIA\n"
            + "(https://sourceforge.net/projects/sepiaframework/) and encloses the\n"
            + "following libraries:\n"
            + "- JGraphX (https://github.com/jgraph/jgraphx)\n"
            + "- OpenXES, (http://www.xes-standard.org/openxes/)\n"
            + "- Spex (http://code.deckfour.org/Spex/)\n"
            + "- Google Guava (https://github.com/google/guava)\n"
            + "- XStream (http://xstream.codehaus.org/)\n"
            + "- Jung 2 (http://jung.sourceforge.net/)\n"
            + "- XML Schema Object Model (https://xsom.java.net/)\n"
            + "- XML Datatypes Library (xsdlib)\n"
            + "- isorelax (http://iso-relax.sourceforge.net/)\n"
            + "- hamcrest (https://github.com/hamcrest)\n"
            + "- Multi Schema Validator (https://msv.java.net/)\n"
            + "- relaxng-Datatype (https://sourceforge.net/projects/relaxng/)\n"
            + "- iText® (https://sourceforge.net/projects/itext/)\n"
            + "\n"
            + "\n"
            + "\n"
            + "\n"
            + "License header:\n"
            + "\n"
            + "Copyright (c) 2015, IIG Telematics, Uni Freiburg\n"
            + "All rights reserved.\n"
            + "\n"
            + "Redistribution and use in source and binary forms, with or without\n"
            + "modification, are permitted (subject to the limitations in the disclaimer\n"
            + "below) provided that the following conditions are met:\n"
            + "\n"
            + "* Redistributions of source code must retain the above copyright notice,\n"
            + "this\n"
            + "  list of conditions and the following disclaimer.\n"
            + "* Redistributions in binary form must reproduce the above copyright notice,\n"
            + "  this list of conditions and the following disclaimer in the documentation\n"
            + "  and/or other materials provided with the distribution.\n"
            + "* Neither the name of IIG Telematics, Uni Freiburg nor the names of its\n"
            + "  contributors may be used to endorse or promote products derived from\n"
            + "  this software without specific prior written permission.\n"
            + "\n"
            + "NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED\n"
            + "BY THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND\n"
            + "CONTRIBUTORS \"AS IS\" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,\n"
            + "BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n"
            + "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE\n"
            + "COPYRIGHT HOLDER OR CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT,\n"
            + "INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\n"
            + "NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF\n"
            + "USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n"
            + "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n"
            + "(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF\n"
            + "THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";


   
    public AboutAction(AbstractWolfgang wolfgang) throws PropertyException, IOException {
        super(wolfgang, "About");

    }

    @Override
    protected void doFancyStuff(ActionEvent e) throws Exception {
        StringDialog.showDialog(wolfgang, "About", copyright);     
    }

}
