/*******************************************************************************

com.bnmi.ourmap.daoimpl.PostgresObjCategoryDAOImpl.java
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

package com.bnmi.ourmap.daoimpl;

import com.inga.dao.BasicDAO;
import com.inga.utils.SigarUtils;
import com.inga.exception.BDException;
import com.inga.exception.NoConnectionException;
import com.inga.exception.RegistroNoExisteException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.inga.utils.SqlClauseHelper;
import com.bnmi.ourmap.model.ObjCategory;
import com.bnmi.ourmap.model.CriteriosObjCategory;
import com.bnmi.ourmap.dao.ObjCategoryDAO;
import java.util.Vector;

/*
 * PostgresObjCategoryDAOImpl.java
 *
 * Created on Wed Mar 03 12:26:24 COT 2010
 * by DaoGen2
 * Author: Camilo Cuesta
 *
 */
public class PostgresObjCategoryDAOImpl extends BasicDAO implements ObjCategoryDAO {

    private static SimpleDateFormat _df = new SimpleDateFormat( SigarUtils.FECHA4 );

    @SuppressWarnings("unchecked")
    @Override
    public Vector<ObjCategory> find(CriteriosObjCategory criteria) throws NoConnectionException, BDException {
        Vector<ObjCategory> results = new Vector<ObjCategory>();
        
        String sql = "select * from obj_categories";
        
        SqlClauseHelper _sh = new SqlClauseHelper();
        
        if ( criteria.getId() != null )
            _sh.addAndClause("id = " + criteria.getId().intValue() );
        if ( criteria.getCatname() != null )
            _sh.addAndClause("catname = '" + SigarUtils.escape(criteria.getCatname()) + "'" );
        
        
        String clause = _sh.getClause();
        if ( clause.length() > 0 )
            sql = sql + " where " + clause;
        
        // Aqui' puede especificar el ordenamiento de los registros
        //sql = sql + " order by ...";
        results = executeQuery( sql );
        
        
        return results;
     }
        
    @Override
    public ObjCategory get( java.lang.Integer id ) throws NoConnectionException, BDException, RegistroNoExisteException {
        String sql = "select * from obj_categories where id = " + id;
        
        Vector results  = executeQuery( sql );
        
        if ( results.isEmpty() )
            throw new RegistroNoExisteException( "" );
        
        return (ObjCategory) results.firstElement();
    }

    @Override
    public Integer create(ObjCategory registro) throws NoConnectionException, BDException {

        int rows = executeUpdate( getCreateStr(registro) );
        
        return new Integer(rows);
    }

    public String getCreateStr(ObjCategory registro) {

        StringBuffer sql = new StringBuffer();

        sql.append( "insert into obj_categories (");
        sql.append("id, catname");
        sql.append(")");
        sql.append(" values (");
        
        if ( registro.getId() == null )
             sql.append( "null" + "," );
        else
            sql.append( registro.getId().intValue() + "," );
        if ( registro.getCatname() == null )
             sql.append( "null" );
        else
            sql.append( "'" + SigarUtils.escape(registro.getCatname()) + "'" );
        sql.append( ")" );
        
        return sql.toString();
        
    }
    @Override
    public int update(ObjCategory registro) throws NoConnectionException, BDException {
        
        int rows = executeUpdate( getUpdateStr(registro) );
        
        return rows;
    }
    
    public static String getUpdateStr(ObjCategory registro) {
        
        String sql = "update obj_categories";
        SqlClauseHelper sh = new SqlClauseHelper();
        
        if ( registro.getCatname() != null )
            sh.append(",", "catname = " + "'" + SigarUtils.escape(registro.getCatname()) + "'" );
        
        sql = sql + " set " + sh.getClause() + " where id = " + registro.getId();
        
        return sql.toString();
    }
    
    @Override
    public int delete( java.lang.Integer id ) throws NoConnectionException, BDException {
        
        int rows = executeUpdate( getDeleteStr(id) );
        
        return rows;        
    }

    public static String getDeleteStr(java.lang.Integer id) {
        
        String sql = "delete from obj_categories where id = " + id;
        
        return sql;
    }

    @Override
    protected Vector extract(ResultSet rs ) throws BDException {
        Vector<ObjCategory> results = new Vector<ObjCategory>();
        try
        {
            while ( rs.next() )
            {
                try
                {
                    ObjCategory o = new ObjCategory();
                    
                    o.setId( new Integer(rs.getInt("id")) );
                    o.setCatname( rs.getString("catname") );
                    
                    results.add( o );
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            rs.close();
        }
        catch ( SQLException e )
        {
            throw new BDException(e);
        }
        
        return results;
    }
}