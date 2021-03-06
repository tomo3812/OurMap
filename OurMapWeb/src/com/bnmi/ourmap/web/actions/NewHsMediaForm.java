/*******************************************************************************

com.bnmi.ourmap.web.actions.NewHsMediaForm.java
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

import com.bnmi.ourmap.model.EasyObject;
import com.bnmi.ourmap.web.Utils;
import com.inga.utils.SigarUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author Manuel Camilo Cuesta
 */
public class NewHsMediaForm  extends ActionForm {

    private FormFile mediaFile;
    private String mediaComments;
    private EasyObject obj;
    private String textContent;
    private String textTitle;
    private List<Integer> itemList;
    private Integer hsId;


    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        setMediaFile(null);
        setMediaComments(null);
        obj = new EasyObject();
        textContent = null;
        textTitle = null;
        itemList = null;
        hsId = null;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();

        try
        {
            setHsId(SigarUtils.parseInt(request.getParameter("hsId")));
            Integer type = SigarUtils.parseInt( request.getParameter("mediaType"));
            textContent = SigarUtils.validarCadena( request.getParameter("textContent"));
            textTitle = SigarUtils.validarCadena( request.getParameter("textTitle"));
            String itemListStr = SigarUtils.validarCadena( request.getParameter("itemList"));
            String mediaTitle = SigarUtils.validarCadena( request.getParameter("mediaTitle"));

            if ( itemListStr != null )
            {
                String items[] = itemListStr.split(",");
                if ( items.length > 0 )
                {
                    itemList = new ArrayList<Integer>();
                    for ( int i = 0; i < items.length; i++ )
                        getItemList().add( Integer.parseInt( items[i]) );
                }
                System.out.println( itemList );

            }
            else
                itemList = null;

            String extension = null;
            try
            {
                extension = mediaFile.getFileName();
                int dot = extension.lastIndexOf('.');
                if ( dot > 0 )
                {
                    try
                    {
                        extension = extension.substring(dot+1);
                    }
                    catch ( Exception ex )
                    {
                        extension = null;
                    }
                }
                else
                    extension = null;
                
            }
            catch ( Exception ex )
            {
                // niente
            }

            if ( mediaComments != null )
                mediaComments = Utils.escapeHtml(mediaComments);


            switch ( type )
            {
                case com.bnmi.ourmap.Constantes.IMAGE:
                case com.bnmi.ourmap.Constantes.AUDIO:
                case com.bnmi.ourmap.Constantes.VIDEO:
                    obj.setObjName( mediaTitle );
                    if ( mediaFile != null )
                        obj.setObjData( mediaFile.getFileData() );
                    if( mediaComments != null )
                        obj.setObjDescription( mediaComments );
                    if ( mediaFile != null )
                        obj.setObjSize( mediaFile.getFileSize() );
                    break;
                case com.bnmi.ourmap.Constantes.TEXT:
                    obj.setObjDescription( textContent );
                    obj.setObjName( textTitle );
                    break;
            }


            obj.setExtension(extension);
            obj.setObjType( type );

        }
        catch ( Exception ex )
        {
            errors.add("badData", new ActionMessage("bad.data"));
            ex.printStackTrace();
        }

        return errors;
    }


    /**
     * @return the mediaFile
     */
    public FormFile getMediaFile() {
        return mediaFile;
    }

    /**
     * @param mediaFile the mediaFile to set
     */
    public void setMediaFile(FormFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    /**
     * @return the mediaComments
     */
    public String getMediaComments() {
        return mediaComments;
    }

    /**
     * @param mediaComments the mediaComments to set
     */
    public void setMediaComments(String mediaComments) {
        this.mediaComments = mediaComments;
    }

    /**
     * @return the obj
     */
    public EasyObject getObj() {
        return obj;
    }

    /**
     * @param obj the obj to set
     */
    public void setObj(EasyObject obj) {
        this.obj = obj;
    }

    /**
     * @return the itemList
     */
    public List<Integer> getItemList() {
        return itemList;
    }

    /**
     * @return the hsId
     */
    public Integer getHsId() {
        return hsId;
    }

    /**
     * @param hsId the hsId to set
     */
    public void setHsId(Integer hsId) {
        this.hsId = hsId;
    }

}
