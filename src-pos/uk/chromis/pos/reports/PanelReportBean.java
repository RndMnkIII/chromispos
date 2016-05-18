//    Chromis POS  - The New Face of Open Source POS
//    Copyright (c) 2015 
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

package uk.chromis.pos.reports;

import java.util.ArrayList;
import java.util.List;
import uk.chromis.basic.BasicException;
import uk.chromis.data.loader.BaseSentence;
import uk.chromis.data.loader.Datas;
import uk.chromis.data.loader.QBFBuilder;
import uk.chromis.data.loader.SerializerReadBasic;
import uk.chromis.data.loader.StaticSentence;
import uk.chromis.data.user.EditorCreator;
import uk.chromis.pos.forms.AppLocal;
import uk.chromis.pos.forms.AppView;
import uk.chromis.pos.forms.BeanFactoryException;

/**
 *
 * @author adrianromero
 */
public class PanelReportBean extends JPanelReport {
    
    private String title;
    private String report;
    
    private String resourcebundle = null;
    
    private String sentence;

    
// JG 16 May 12 use diamond inference
    private List<Datas> fielddatas = new ArrayList<>();
    private List<String> fieldnames = new ArrayList<>();
    
    private List<String> paramnames = new ArrayList<>();
    
    private JParamsComposed qbffilter = new JParamsComposed();
    
    /**
     *
     * @param app
     * @throws BeanFactoryException
     */
    @Override
    public void init(AppView app) throws BeanFactoryException {        
        
        qbffilter.init(app);       
        super.init(app);
    }
    
    /**
     *
     * @throws BasicException
     */
    @Override
    public void activate() throws BasicException {
        
        qbffilter.activate();
        super.activate();
        
        if (qbffilter.isEmpty()) {
            setVisibleFilter(false);
            setVisibleButtonFilter(false);
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    protected EditorCreator getEditorCreator() {
        
        return qbffilter;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     *
     * @param titlekey
     */
    public void setTitleKey(String titlekey) {
        title = AppLocal.getIntString(titlekey);
    }
   
    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param report
     */
    public void setReport(String report) {
        this.report = report;
    }
    
    /**
     *
     * @return
     */
    protected String getReport() {
        return report;
    }

    /**
     *
     * @param resourcebundle
     */
    public void setResourceBundle(String resourcebundle) {
        this.resourcebundle = resourcebundle;
        
                 /*    
        String locale = AppConfig.getInstance().getProperty("user.language").equals("")
                ? resourcebundle 
                : resourcebundle + "_" + AppConfig.getInstance().getProperty("user.language");
        
        this.resourcebundle = AppConfig.getInstance().getProperty("user.country").equals("")
                ? locale 
                : locale + "_" + AppConfig.getInstance().getProperty("user.country");    
       */
        
        
    }
    
    /**
     *
     * @return
     */
    protected String getResourceBundle() {
        return resourcebundle == null 
                ? report 
                : resourcebundle;
    }

    /**
     *
     * @param sentence
     */
    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
    
    /**
     *
     * @param name
     * @param data
     */
    public void addField(String name, Datas data) {
        fieldnames.add(name);
        fielddatas.add(data);
    }
    
    /**
     *
     * @param name
     */
    public void addParameter(String name) {
        paramnames.add(name);        
    }
    
    /**
     *
     * @return
     */
    protected BaseSentence getSentence() {
        return new StaticSentence(m_App.getSession()
            , new QBFBuilder(sentence, paramnames.toArray(new String[paramnames.size()]))
            , qbffilter.getSerializerWrite()
            , new SerializerReadBasic(fielddatas.toArray(new Datas[fielddatas.size()])));
    }

    /**
     *
     * @return
     */
    protected ReportFields getReportFields() {
        return new ReportFieldsArray(fieldnames.toArray(new String[fieldnames.size()]));
    }

    /**
     *
     * @param qbff
     */
    public void addQBFFilter(ReportEditorCreator qbff) {
        qbffilter.addEditor(qbff);
    }    
}
