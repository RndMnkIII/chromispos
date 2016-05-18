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

package uk.chromis.data.loader;

/**
 *
 * @author adrianromero
 */
public class SessionDBOracle implements SessionDB {

    /**
     *
     * @return
     */
    public String TRUE() {
        return "1";
    }

    /**
     *
     * @return
     */
    public String FALSE() {
        return "0";
    }

    /**
     *
     * @return
     */
    public String INTEGER_NULL() {
        return "CAST(NULL AS INTEGER)";
    }

    /**
     *
     * @return
     */
    public String CHAR_NULL() {
        return "CAST(NULL AS CHAR)";
    }

    /**
     *
     * @return
     */
    public String getName() {
        return "Oracle";
    }

    /**
     *
     * @param s
     * @param sequence
     * @return
     */
    public SentenceFind getSequenceSentence(Session s, String sequence) {
        return new StaticSentence(s, "SELECT " + sequence + ".NEXTVAL FROM DUAL", null, SerializerReadInteger.INSTANCE);
    }

    /**
     *
     * @param s
     * @param sequence
     * @return
     */
    public SentenceFind resetSequenceSentence(Session s, String sequence) {
        return new StaticSentence(s, "SELECT " + sequence + ".NEXTVAL FROM DUAL", null, SerializerReadInteger.INSTANCE);
    }
}