/*******************************************************************************

com.bnmi.ourmap.web.actions.AddMapOwner.java
Version: 1.0

********************************************************************************
Original Authors:
Manuel Cuesta, lead programmer <camilocuesta@hotmail.com>
Angus Leech, lead designer <alpinefabulist@yahoo.com>
Full credits at: <http://www.ourmapmaker.ca/content/about-ourmap/credits>  

For questions or comments please contact us at: [ourmap@ourmapmaker.ca]
********************************************************************************

OurMap is Copyright (c) 2010, The Banff Centre <ourmap@ourmapmaker.ca>
All rights reserved.

Published under the terms of the new BSD license.

See  <www.ourmapmaker.ca/content/about-ourmap>  for more information about the 
OurMap software and the license.

Full sourcecode, documentation and license info is also available here:
http://github.com/OurMap/OurMap

LICENSE:

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of The Banff Centre nor the names of its contributors may be
used to endorse or promote products derived from this software without specific
prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


********************************************************************************
Revision History / Change Log:

Version 1.0 released Oct.2010

********************************************************************************
Notes:

*******************************************************************************/

package com.bnmi.ourmap.web.actions;

import com.bnmi.ourmap.control.EasyDelegate;
import com.bnmi.ourmap.exceptions.SecurityIssue;
import com.bnmi.ourmap.model.User;
import com.bnmi.ourmap.web.Constantes;
import com.inga.utils.ReturnMessage;
import com.inga.utils.SigarUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Manuel Camilo Cuesta
 */
public class AddMapOwner extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        EasyDelegate del = (EasyDelegate) session.getAttribute(Constantes.DELEGATE);
        AddMapOwnerForm forma = (AddMapOwnerForm) form;

        Integer mapid = SigarUtils.parseInt( forma.getMapid() );
        String action = forma.getOperation();
        request.setAttribute("mapid", mapid );

        User principal = (User) session.getAttribute(Constantes.USER);
        ReturnMessage r = del.hasPermission(principal, "map-creator-owner", mapid, com.bnmi.ourmap.Constantes.MAP );
        if ( ! r.isSuccess() )
            throw new SecurityIssue( r.getMessage() );


        if ( action.equalsIgnoreCase("add") )
        {

            List<String> users = forma.getSelectedUsersList();

            for( String userId : users )
            {
                userId = userId.trim();
                try
                {
                    del.grantOwnership(userId.trim(), mapid, com.bnmi.ourmap.Constantes.MAP);
                }
                catch ( Exception ex )
                {
                    request.setAttribute("estado","FAIL");
                    request.setAttribute("mensaje", ex.getMessage() );
                }

            }

        }
        else if ( action.equalsIgnoreCase("remove") )
        {

            List<String> users = forma.getSelectedUsersList();


            for( String userId : users )
            {
                userId = userId.trim();

                try
                {
                    del.revokeOwnership(userId.trim(), mapid, com.bnmi.ourmap.Constantes.MAP);
                }
                catch ( Exception ex )
                {
                    request.setAttribute("estado","FAIL");
                    request.setAttribute("mensaje", ex.getMessage() );
                }
            }

        }

        request.setAttribute("estado","OK");
        request.setAttribute("mensaje", "OK");

        return mapping.findForward("results_jsp");

        
    }


}
