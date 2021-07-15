package com.omprakashgautam.shopme.web.backend.reports;

import com.omprakashgautam.shopme.commons.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author omprakash gautam
 * Created on 15-Jul-21 at 7:18 PM.
 */
public class UserCSVExporter extends AbstractExporter{

    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String [] csvHeader = {"User Id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
        String [] fieldMapping = {"id","email","firstName", "lastName", "roles","enabled"};


        csvBeanWriter.writeHeader(csvHeader);
        for (User user : listUsers) {
            csvBeanWriter.write(user,fieldMapping);
        }
        csvBeanWriter.close();
    }
}
