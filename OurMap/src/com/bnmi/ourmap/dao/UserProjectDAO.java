/*******************************************************************************

com.bnmi.ourmap.dao.UserProjectDAO.java
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

package com.bnmi.ourmap.dao;

import com.inga.exception.BDException;
import com.inga.exception.NoConnectionException;
import com.inga.exception.RegistroNoExisteException;
import com.bnmi.ourmap.model.UserProject;
import com.bnmi.ourmap.model.CriteriosUserProject;
import com.inga.dao.ConnectionUser;
import java.util.Vector;

/*
 * UserProjectDAO.java
 *
 * Created on Tue Dec 08 19:37:36 COT 2009
 * by DaoGen2
 * @author Manuel Camilo Cuesta
 *
 */
public interface UserProjectDAO extends ConnectionUser {

    /** Busca por criterio de bu'squeda */
    public Vector<UserProject> find(CriteriosUserProject criteria) throws NoConnectionException, BDException;
    
    /** Obtiene un registro por llave primaria */
    public UserProject get(java.lang.String userid, java.lang.Integer project) throws NoConnectionException, BDException, RegistroNoExisteException;
    
    /** Crea una nuevo registro */
    public Integer create(UserProject registro) throws NoConnectionException, BDException;
    
    /** Modifica un registro */
    public int update(UserProject registro) throws NoConnectionException, BDException;
    
    /** Borra un registro */
    public int delete(java.lang.String userid, java.lang.Integer project) throws NoConnectionException, BDException;
    
}