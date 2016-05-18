//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) (c) 2015-2016
//    http://www.chromis.co.uk
//
//    This file is part of Chromis POS
//
//     Chromis POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Chromis POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Chromis POS.  If not, see <http://www.gnu.org/licenses/>.

package uk.chromis.pos.epm;

/**
 *
 * @author Ali Safdar & Aneeqa Baber
 */
public class Break {

    private String m_sId;
    private String m_sName;
    private String m_sNotes;
    private boolean m_sVisible;

    /**
     *
     * @param id
     * @param name
     * @param notes
     * @param visible
     */
    public Break(String id, String name, String notes,  boolean visible) {
        m_sId = id;
        m_sName = name;
        m_sNotes = notes;
        m_sVisible = visible;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return m_sId;
    }

    /**
     *
     * @param Id
     */
    public void setId(String Id) {
        this.m_sId = Id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return m_sName;
    }

    /**
     *
     * @param Name
     */
    public void setName(String Name) {
        this.m_sName = Name;
    }

    /**
     *
     * @return
     */
    public String getNotes() {
        return m_sNotes;
    }

    /**
     *
     * @param Notes
     */
    public void setNotes(String Notes) {
        this.m_sNotes = Notes;
    }

    /**
     *
     * @return
     */
    public boolean isVisible() {
        return m_sVisible;
    }

    /**
     *
     * @param Visible
     */
    public void setVisible(boolean Visible) {
        this.m_sVisible = Visible;
    }
}
