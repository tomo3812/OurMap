/*******************************************************************************

com.bnmi.ourmap.web.actions.ViewHsContent.java
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
import com.bnmi.ourmap.model.CriteriosHotspotObject;
import com.bnmi.ourmap.model.EasyObject;
import com.bnmi.ourmap.model.Hotspot;
import com.bnmi.ourmap.model.HotspotObject;
import com.bnmi.ourmap.model.Layer;
import com.bnmi.ourmap.model.Map;
import com.bnmi.ourmap.model.User;
import com.bnmi.ourmap.web.Constantes;
import com.inga.utils.ReturnMessage;
import com.inga.utils.SigarUtils;
import java.util.ArrayList;
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
public class ViewHsContent extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        EasyDelegate del = (EasyDelegate) session.getAttribute(Constantes.DELEGATE);


        Integer hsId = SigarUtils.parseInt( request.getParameter("hsId"));
        Integer block = SigarUtils.parseInt( request.getParameter("block"));

        if ( block == null )
            block = new Integer( 0 );

        Hotspot hs = del.getHotspot(hsId);
        Layer la = del.getLayer(hs.getHsLayer());
        Map m = del.getMap(la.getMapId());

        // Hotspot objects
        CriteriosHotspotObject criteria = new CriteriosHotspotObject();
        criteria.setHotspot( hs.getHsId() );
        criteria.setBlock(block);
        List<HotspotObject> hos = del.findHotspotObjects(criteria);

        // Get the display mode
        Integer displayMode = m.getDisplayMode();
        if ( displayMode == null )
            displayMode = 0;
        if ( displayMode.intValue() == 0 )
            displayMode = hs.getDisplayMode();
        if ( displayMode == null )
            displayMode = com.bnmi.ourmap.Constantes.MAP_HS_CONTENT_DEFAULT ;

        if ( displayMode.intValue() == 3 )
        {
            if ( hos.isEmpty() )
            {

            }
            else
            {
                HotspotObject firstObject = hos.get(0);
                request.setAttribute("objectId", firstObject.getObjectId() );
                return mapping.findForward("viewhsobject_do");
            }
        }


        ////
        User user = (User) session.getAttribute(Constantes.USER);
        int offset = 0;
        // Checks if the user can edit the hotspot, to show the edit tab, otherwise
        // move tabs to the right in order to indent them to the right
        ReturnMessage r = del.hasPermission(user, "edit-media", hsId, com.bnmi.ourmap.Constantes.HOTSPOT );
        boolean canEditHs = r.isSuccess();
        boolean hasDescription =  ( hs.getHsDescription() != null && hs.getHsDescription().length() > 0 ) ? true : false;

        if ( ! canEditHs )
            offset += Constantes.EDIT_OFFSET ;
        if ( ! hasDescription )
            offset += Constantes.DESCRIPTION_OFFSET ;

        Integer baseOffset = (Integer) session.getAttribute("baseOffset");
        request.setAttribute("offset", baseOffset.intValue() + offset );

        System.out.println( "Show edit tab? " + canEditHs + " Show Summary tab? " + hasDescription );
        System.out.println( "Offset:" + offset + " Base Offset:" + baseOffset + " Total offset:" + (baseOffset+offset) );

        ///

        List<EasyObject> objects = new ArrayList<EasyObject>();

        for ( HotspotObject ho : hos )
        {
            EasyObject eo = del.getObject(ho.getObjectId());
            objects.add(eo);
        }


        request.setAttribute("objects", objects );
        request.setAttribute("hs", hs );
        request.setAttribute("displayMode", displayMode );

        return mapping.findForward("hs_content");

    }

}