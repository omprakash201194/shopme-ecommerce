package com.omprakashgautam.shopme.web.backend.category;

import com.omprakashgautam.shopme.commons.entity.Category;
import com.omprakashgautam.shopme.web.backend.reports.AbstractExporter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author gautam
 * Created on 27-Jul-21 at 9:31 PM.
 */
public class CategoryCSVExporter extends AbstractExporter<Category> {
    @Override
    public void export(List<Category> entities, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "categories_", "text/csv", ".csv");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"Category Id", "Category Name"};
        String[] fieldMapping = {"id", "name"};


        csvBeanWriter.writeHeader(csvHeader);
        for (Category category : entities) {
            category.setName(category.getName().replace("--", "  "));
            csvBeanWriter.write(category, fieldMapping);
        }
        csvBeanWriter.close();
    }
}
