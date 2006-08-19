/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.jtrac.lucene;

import info.jtrac.JtracDao;
import info.jtrac.domain.Item;
import java.util.List;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.springmodules.lucene.search.core.HitExtractor;
import org.springmodules.lucene.search.core.LuceneSearchTemplate;
import org.springmodules.lucene.search.support.LuceneSearchSupport;

/**
 * Uses Spring Modules Lucene support, provides Lucene Index Searching support
 * in classic Spring Template style
 */
public class IndexSearcher extends LuceneSearchSupport {    
    
    private JtracDao dao;
    
    public void setDao(JtracDao dao) {
        this.dao = dao;
    }
    
    public List<Item> findItemsContainingText(String text) throws ParseException {       
        LuceneSearchTemplate template = getTemplate();
        QueryParser parser = new QueryParser("detail", getAnalyzer());
        Query query = parser.parse(text);
        HitExtractor hitExtractor = new ItemHitExtractor(dao);
        return template.search(query, hitExtractor);        
    }

}
